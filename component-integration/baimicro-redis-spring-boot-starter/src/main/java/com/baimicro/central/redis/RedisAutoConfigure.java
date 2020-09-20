package com.baimicro.central.redis;

import com.baimicro.central.redis.manager.MyRedisCacheManager;
import com.baimicro.central.redis.properties.CacheManagerProperties;
import com.baimicro.central.redis.template.RedisRepository;
import com.baimicro.central.redis.util.RedisObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/15
 * @Description: TODO redis 配置类
 * version 0.1
 */
@EnableConfigurationProperties({RedisProperties.class, CacheManagerProperties.class})
@EnableCaching
public class RedisAutoConfigure extends CachingConfigurerSupport {

    @Autowired
    private CacheManagerProperties cacheManagerProperties;

    /**
     * RedisTemplate配置
     *
     * @param factory
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        /*
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        RedisSerializer stringSerializer = new StringRedisSerializer();
        RedisSerializer redisObjectSerializer = new RedisObjectSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(redisObjectSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
        */

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // set key serializer
        StringRedisSerializer serializer = MyRedisCacheManager.STRING_SERIALIZER;
        // 设置key序列化类，否则key前面会多了一些乱码
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        // redis serializer
        RedisSerializer redisSerializer = MyRedisCacheManager.REDISOBJECT_SERIALIZER;
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        // 如果 KeySerializer 或者 ValueSerializer 没有配置，则对应的 KeySerializer、ValueSerializer 才使用这个 Serializer
        template.setDefaultSerializer(redisSerializer);
        // factory
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis repository redis repository.
     *
     * @param redisTemplate the redis template
     * @return the redis repository
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisRepository redisRepository(RedisTemplate<String, Object> redisTemplate) {
        return new RedisRepository(redisTemplate);
    }

    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        /*
        RedisCacheConfiguration difConf = getDefConf().entryTtl(Duration.ofHours(1));
        //自定义的缓存过期时间配置
        int configSize = cacheManagerProperties.getConfigs() == null ? 0 : cacheManagerProperties.getConfigs().size();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(configSize);
        if (configSize > 0) {
            cacheManagerProperties.getConfigs().forEach(e -> {
                RedisCacheConfiguration conf = getDefConf().entryTtl(Duration.ofSeconds(e.getSecond()));
                redisCacheConfigurationMap.put(e.getKey(), conf);
            });
        }

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(difConf)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
        */
        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1));
        RedisCacheConfiguration redisCacheConfiguration = config
                .serializeKeysWith(MyRedisCacheManager.STRING_PAIR)
                .serializeValuesWith(MyRedisCacheManager.REDISOBJECT_PAIR);
        // 以锁写入的方式创建RedisCacheWriter对象
        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
        return new MyRedisCacheManager(cacheWriter, redisCacheConfiguration);
    }

    @Bean
    public KeyGenerator keyGenerator() {
        /*
        return (target, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":" + method.getName() + ":");
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
        */
        return (Object target, Method method, Object... params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getDeclaringClass().getName());
            Arrays.stream(params).map(Object::toString).forEach(sb::append);
            return sb.toString();
        };
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    private RedisCacheConfiguration getDefConf() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> "cache".concat(":").concat(cacheName).concat(":"))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisObjectSerializer()));
    }
}

