package com.baimicro.central.websocket.standard;

import com.baimicro.central.websocket.exception.DeploymentException;
import com.baimicro.central.websocket.annotation.RpcService;
import com.baimicro.central.websocket.annotation.ServerEndpoint;
import com.baimicro.central.websocket.pojo.PojoEndpointServer;
import com.baimicro.central.websocket.pojo.PojoMethodMapping;
import com.baimicro.central.websocket.pojo.StoreRpcServiceInstance;
import io.netty.channel.ChannelHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * @program: hospital-cloud-platform
 * @description: 当 ServerEndpointExporter 类通过 Spring 配置进行声明并被使用，它将会去扫描带有@ServerEndpoint注解的类
 * 被注解的类将被注册成为一个WebSocket端点 所有的都在这个注解的属性中 ( 如:`@ServerEndpoint("/ws")` )
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@ChannelHandler.Sharable
@Component
public class ServerEndpointExporter extends ApplicationObjectSupport implements SmartInitializingSingleton, BeanFactoryAware {

    @Autowired
    Environment environment;

    @Autowired
    private ServerEndpointConfig serverEndpointConfig;

    private AbstractBeanFactory beanFactory;

    private final Map<InetSocketAddress, WebsocketServer> addressWebsocketServerMap = new HashMap<>();

    /**
     * @param beanFactory
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof AbstractBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a AbstractBeanFactory: " + beanFactory);
        }
        this.beanFactory = (AbstractBeanFactory) beanFactory;
    }

    /**
     * 执行顺序步骤 1: 初始化 spring 容器实例化入口
     */
    @Override
    public void afterSingletonsInstantiated() {
        registerEndpoints();
    }

    /**
     * 执行顺序步骤 2: 具体实例化逻辑代码体
     */
    protected void registerEndpoints() {
        // 1. webSocket 服务类 集合
        Set<Class<?>> endpointClasses = new LinkedHashSet<>();
        ApplicationContext context = getApplicationContext();
        // 3. webSocket 服务类 集合
        String[] beanNames = context.getBeanNamesForAnnotation(ServerEndpoint.class);
        for (String beanName : beanNames) {
            endpointClasses.add(context.getType(beanName));
        }
        // 4. 添加 RPC 调用控制器类
        beanNames = context.getBeanNamesForAnnotation(RpcService.class);
        for (String beanName : beanNames) {
            Object serviceInstance = context.getBean(beanName);
            StoreRpcServiceInstance.put(beanName, serviceInstance);
        }
        // 5. 注册 ws 端点服务
        for (Class<?> endpointClass : endpointClasses) {
            registerEndpoint(endpointClass);
        }
        init();
    }

    /**
     * 执行顺序步骤 7: 初始启动服务网络
     */
    private void init() {
        for (Map.Entry<InetSocketAddress, WebsocketServer> entry : addressWebsocketServerMap.entrySet()) {
            WebsocketServer websocketServer = entry.getValue();
            try {
                websocketServer.init();
                PojoEndpointServer pojoEndpointServer = websocketServer.getPojoEndpointServer();
                StringJoiner stringJoiner = new StringJoiner(",");
                pojoEndpointServer.getPathMatcherSet().forEach(pathMatcher -> stringJoiner.add("'" + pathMatcher.getPattern() + "'"));
                logger.info(String.format("\033[34mNetty WebSocket started on port: %s with context path(s): %s .\033[0m", pojoEndpointServer.getPort(), stringJoiner.toString()));
            } catch (InterruptedException e) {
                logger.error(String.format("websocket [%s] init fail", entry.getKey()), e);
            }
        }
    }

    /**
     * 执行顺序步骤 3: 注册网络服务端口
     *
     * @param endpointClass
     */
    private void registerEndpoint(Class<?> endpointClass) {
        ServerEndpoint annotation = AnnotatedElementUtils.findMergedAnnotation(endpointClass, ServerEndpoint.class);
        // 1. 构建关于服务端点的配置
        ServerEndpointConfig serverEndpointConfig = buildConfig(annotation);
        // 2. 取得 Spring 上下文应用容器
        ApplicationContext context = getApplicationContext();
        // 3. 持久化关于 com.cdhld.scada.socket.annotation 包下注解映射处理
        PojoMethodMapping pojoMethodMapping;
        try {
            pojoMethodMapping = new PojoMethodMapping(endpointClass, context, beanFactory);
        } catch (DeploymentException e) {
            throw new IllegalStateException("Failed to register serverEndpointConfig: " + serverEndpointConfig, e);
        }
        // 4. 实例出网络套接字地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress(serverEndpointConfig.getHost(), serverEndpointConfig.getPort());
        String path = resolveAnnotationValue(annotation.value(), String.class, "path");
        // 5. WebsocketServer 对象实例化或者格式化
        WebsocketServer websocketServer = addressWebsocketServerMap.get(inetSocketAddress);
        if (websocketServer == null) {
            // 5.1. PojoEndpointServer 对象实例化
            PojoEndpointServer pojoEndpointServer = new PojoEndpointServer(pojoMethodMapping, serverEndpointConfig, path);
            // 5.2. WebsocketServer 对象实例化
            websocketServer = new WebsocketServer(pojoEndpointServer, serverEndpointConfig);
            addressWebsocketServerMap.put(inetSocketAddress, websocketServer);
        } else {
            websocketServer.getPojoEndpointServer().addPathPojoMethodMapping(path, pojoMethodMapping);
        }
    }

    /**
     * 执行顺序步骤 4: 构建关于服务端点的配置
     *
     * @return
     */
    private ServerEndpointConfig buildConfig(ServerEndpoint annotation) {
        String path = resolveAnnotationValue(annotation.path(), String.class, "path");
        String[] subprotocols = resolveAnnotationValue(annotation.subprotocols(), String[].class, "subprotocols");
        serverEndpointConfig.setPath(path);
        serverEndpointConfig.setSubprotocols(subprotocols);
        if (serverEndpointConfig.getPort() == null) {
            String port = environment.getProperty("server.port");
            if (StringUtils.isNotEmpty(port)) {
                serverEndpointConfig.setPort(Integer.parseInt(port));
            } else {
                serverEndpointConfig.setPort(9326);
            }
        }
        if (StringUtils.isEmpty(serverEndpointConfig.getHost())) {
            String address = environment.getProperty("server.address");
            if (StringUtils.isNotEmpty(address))
                serverEndpointConfig.setHost(address);
            else
                serverEndpointConfig.setHost("0.0.0.0");
        }
        return serverEndpointConfig;
    }

    /**
     * 解析注解对象里的参数
     *
     * @param value
     * @param requiredType
     * @param paramName
     * @param <T>
     * @return
     */
    private <T> T resolveAnnotationValue(Object value, Class<T> requiredType, String paramName) {
        if (value == null) {
            return null;
        }
        TypeConverter typeConverter = beanFactory.getTypeConverter();
        if (value instanceof String) {
            String strVal = beanFactory.resolveEmbeddedValue((String) value);
            BeanExpressionResolver beanExpressionResolver = beanFactory.getBeanExpressionResolver();
            value = beanExpressionResolver.evaluate(strVal, new BeanExpressionContext(beanFactory, null));
        }
        try {
            return typeConverter.convertIfNecessary(value, requiredType);
        } catch (TypeMismatchException e) {
            throw new IllegalArgumentException("Failed to convert value of parameter '" + paramName + "' to required type '" + requiredType.getName() + "'");
        }
    }

}
