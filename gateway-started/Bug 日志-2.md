# Fixing Spring Boot error 'Unable to start ServletWebServerApplicationContext due to missing ServletWebServerFactory bean.' 



我最近基于 Gradle 依赖管理构建了一个新 SpringCloud +Spring WebFlux应用程序，从start.spring下载项目模板之后。io时，我添加了一些第三方依赖并尝试启动应用程序。然后我遇到了org.springframework.context 这个错误,`ApplicationContextException:由于缺少ServletWebServerFactory bean，无法启动ServletWebServerApplicationContext`。实际上，当我搜索解决方案时，这是一个常见的问题。所有的解决方案都与Spring引导启动器缺少的依赖项有关。

```java
org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.context.ApplicationContextException: Unable to start ServletWebServerApplicationContext due to missing ServletWebServerFactory bean.
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:156) ~[spring-boot-2.1.12.RELEASE.jar:2.1.12.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:543) ~[spring-context-5.1.13.RELEASE.jar:5.1.13.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:141) ~[spring-boot-2.1.12.RELEASE.jar:2.1.12.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:744) ~[spring-boot-2.1.12.RELEASE.jar:2.1.12.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:391) ~[spring-boot-2.1.12.RELEASE.jar:2.1.12.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:312) ~[spring-boot-2.1.12.RELEASE.jar:2.1.12.RELEASE]
	at com.baimicro.central.GatewayApp.main(GatewayApp.java:27) [main/:na]
Caused by: org.springframework.context.ApplicationContextException: Unable to start ServletWebServerApplicationContext due to missing ServletWebServerFactory bean.
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.getWebServerFactory(ServletWebServerApplicationContext.java:203) ~[spring-boot-2.1.12.RELEASE.jar:2.1.12.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer(ServletWebServerApplicationContext.java:179) ~[spring-boot-2.1.12.RELEASE.jar:2.1.12.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:153) ~[spring-boot-2.1.12.RELEASE.jar:2.1.12.RELEASE]
	... 6 common frames omitted
```

但这不是我项目的原因。我的项目是从 start.spring.io 创建的, 但在我添加了一些依赖项后，他就出现上述问题了。

错误消息是解决方案的线索，它说不能启动 ServletWebServerApplicationContext，但我的项目使用的是 WebFlux，它是一个反应性 web 项目，而不是一个基于servle t的Spring web MVC项目。

在 Spring 源代码中调试帮助我找到了原因。在 org.springframework.boot 的方法 deduceWebApplicationType 中，SpringApplication web 应用程序类型设置为WebApplicationType。仅当类路径中不存在Spring Web MVC的类时才响应，而在 Gradle 依赖项树 指出，新添加的一个库具有对 Spring -webmvc的过渡依赖，因此Spring -webmvc被添加到类路径中，Spring Boot 将该项目视为servlet应用程序。

```java
private WebApplicationType deduceWebApplicationType() {
    if (ClassUtils.isPresent(REACTIVE_WEB_ENVIRONMENT_CLASS, null)
        && !ClassUtils.isPresent(MVC_WEB_ENVIRONMENT_CLASS, null)) {
      return WebApplicationType.REACTIVE;
    }
    for (String className : WEB_ENVIRONMENT_CLASSES) {
      if (!ClassUtils.isPresent(className, null)) {
        return WebApplicationType.NONE;
      }
    }
    return WebApplicationType.SERVLET;
  }
```

一旦找到根本原因，解决方案就很容易，做法如下：

* 更新 Gradle 依赖项以排除spring-webmvc

* 或者将 web 应用程序类型设置为`WebApplicationType.REACTIVE`，具体如下所示

  ```java
  public class GatewayApp {
      public static void main(String[] args) {
          SpringApplication application = new SpringApplication(GatewayApp.class);
        	// 该设置方式 也可以解决我的前一个问题
          application.setWebApplicationType(WebApplicationType.REACTIVE);
          application.run(args);
      }
  }
  ```

  







