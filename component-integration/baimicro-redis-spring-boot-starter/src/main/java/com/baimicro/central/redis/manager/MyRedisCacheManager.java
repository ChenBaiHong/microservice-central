package com.baimicro.central.redis.manager;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.baimicro.central.redis.util.RedisObjectSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @ClassName MyRedisCacheManager
 * @Description TODO 自定义 redis缓存管理器
 * @Author baiHoo.chen
 * @Date 2019/11/21 21:06
 */
@Slf4j
public class MyRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {


    private ApplicationContext applicationContext;

    private Map<String, RedisCacheConfiguration> initialCacheConfiguration = new LinkedHashMap<>();

    /**
     * key serializer
     */
    public static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
    /**
     * value serializer
     * <pre>
     *     使用 FastJsonRedisSerializer 会报错：java.lang.ClassCastException
     *     FastJsonRedisSerializer<Object> fastSerializer = new FastJsonRedisSerializer<>(Object.class);
     * </pre>
     */

    public static final GenericFastJsonRedisSerializer FASTJSON_SERIALIZER = new GenericFastJsonRedisSerializer();

    /**
     * value serializer
     * <pre>
     *     解决使用 GenericFastJsonRedisSerializer 会报错 第二方案
     * </pre>
     */
    public static final Jackson2JsonRedisSerializer<Object> JACKSON2_SERIALIZER;
    static {
        JACKSON2_SERIALIZER = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        JACKSON2_SERIALIZER.setObjectMapper(om);
    }
    /**
     * value serializer
     * <pre>
     *     解决使用 GenericFastJsonRedisSerializer 或  Jackson2JsonRedisSerializer 会报错: Caused by: com.alibaba.fastjson.JSONException: autoType is not support.
     *     第三方案
     * </pre>
     */
    public static final RedisObjectSerializer REDISOBJECT_SERIALIZER = new RedisObjectSerializer();


    /**
     * key serializer pair
     */
    public static final RedisSerializationContext.SerializationPair<String> STRING_PAIR = RedisSerializationContext
            .SerializationPair.fromSerializer(STRING_SERIALIZER);
    /**
     * value serializer pair
     */
    public static final RedisSerializationContext.SerializationPair<Object> FASTJSON_PAIR = RedisSerializationContext
            .SerializationPair.fromSerializer(FASTJSON_SERIALIZER);
    /**
     * value serializer pair
     */
    public static final RedisSerializationContext.SerializationPair JACKSON2_PAIR = RedisSerializationContext
            .SerializationPair.fromSerializer(JACKSON2_SERIALIZER);
    /**
     * value serializer pair
     */
    public static final RedisSerializationContext.SerializationPair REDISOBJECT_PAIR = RedisSerializationContext
            .SerializationPair.fromSerializer(REDISOBJECT_SERIALIZER);

    public MyRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        return new RedisCacheWrapper(cache);
    }


    @Override
    public void afterPropertiesSet() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class clazz = applicationContext.getType(beanName);
            add(clazz);
        }
        super.afterPropertiesSet();
    }

    @Override
    protected Collection<RedisCache> loadCaches() {
        List<RedisCache> caches = new LinkedList<>();
        for (Map.Entry<String, RedisCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
            caches.add(super.createRedisCache(entry.getKey(), entry.getValue()));
        }
        return caches;
    }

    private void add(final Class clazz) {
        ReflectionUtils.doWithMethods(clazz, method -> {
            ReflectionUtils.makeAccessible(method);
            CacheExpire cacheExpire = AnnotationUtils.findAnnotation(method, CacheExpire.class);
            if (cacheExpire == null) {
                return;
            }
            Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
            if (cacheable != null) {
                add(cacheable.cacheNames(), cacheExpire);
                return;
            }
            Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
            if (caching != null) {
                Cacheable[] cs = caching.cacheable();
                if (cs.length > 0) {
                    for (Cacheable c : cs) {
                        if (c != null) {
                            add(c.cacheNames(), cacheExpire);
                        }
                    }
                }
            } else {
                CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
                if (cacheConfig != null) {
                    add(cacheConfig.cacheNames(), cacheExpire);
                }
            }
        }, method -> null != AnnotationUtils.findAnnotation(method, CacheExpire.class));
    }

    private void add(String[] cacheNames, CacheExpire cacheExpire) {
        for (String cacheName : cacheNames) {
            if (cacheName == null || "".equals(cacheName.trim())) {
                continue;
            }
            long expire = cacheExpire.expire();
            log.info("cacheName: {}, expire: {}", cacheName, expire);
            if (expire >= 0) {
                // 缓存配置
                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(expire))
                        .disableCachingNullValues()
                        // .prefixKeysWith(cacheName)
                        .serializeKeysWith(STRING_PAIR)
                        .serializeValuesWith(REDISOBJECT_PAIR);
                initialCacheConfiguration.put(cacheName, config);
            } else {
                log.warn("{} use default expiration.", cacheName);
            }
        }
    }

    /***
     *
     * @Author baihoo.chen
     * @Description TODO 自定义缓存包装器
     * @Date 2019/11/21 21:18
     */
    protected static class RedisCacheWrapper implements Cache {
        private final Cache cache;

        RedisCacheWrapper(Cache cache) {
            this.cache = cache;
        }

        /**
         * Return the cache name.
         */
        @Override
        public String getName() {
            log.info("name: {}", cache.getName());
            try {
                return cache.getName();
            } catch (Exception e) {
                log.error("getName ---> errmsg: {}", e.getMessage(), e);
                return null;
            }
        }

        /**
         * Return the underlying native cache provider.
         */
        @Override
        public Object getNativeCache() {
            log.info("nativeCache: {}", cache.getNativeCache());
            try {
                return cache.getNativeCache();
            } catch (Exception e) {
                log.error("getNativeCache ---> errmsg: {}", e.getMessage(), e);
                return null;
            }
        }

        /**
         * Return the value to which this cache maps the specified key.
         * <p>Returns {@code null} if the cache contains no mapping for this key;
         * otherwise, the cached value (which may be {@code null} itself) will
         * be returned in a {@link ValueWrapper}.
         *
         * @param key the key whose associated value is to be returned
         * @return the value to which this cache maps the specified key,
         * contained within a {@link ValueWrapper} which may also hold
         * a cached {@code null} value. A straight {@code null} being
         * returned means that the cache contains no mapping for this key.
         * @see #get(Object, Class)
         */
        @Override
        public ValueWrapper get(Object key) {
            log.info("get ---> key: {}", key);
            try {
                return cache.get(key);
            } catch (Exception e) {
                log.error("get ---> key: {}, errmsg: {}", key, e.getMessage(), e);
                return null;
            }
        }

        /**
         * Return the value to which this cache maps the specified key,
         * generically specifying a type that return value will be cast to.
         * <p>Note: This variant of {@code get} does not allow for differentiating
         * between a cached {@code null} value and no cache entry found at all.
         * Use the standard {@link #get(Object)} variant for that purpose instead.
         *
         * @param key  the key whose associated value is to be returned
         * @param type the required type of the returned value (may be
         *             {@code null} to bypass a type check; in case of a {@code null}
         *             value found in the cache, the specified type is irrelevant)
         * @return the value to which this cache maps the specified key
         * (which may be {@code null} itself), or also {@code null} if
         * the cache contains no mapping for this key
         * @throws IllegalStateException if a cache entry has been found
         *                               but failed to match the specified type
         * @see #get(Object)
         * @since 4.0
         */
        @Override
        public <T> T get(Object key, Class<T> type) {
            log.info("get ---> key: {}, clazz: {}", key, type);
            try {
                return cache.get(key, type);
            } catch (Exception e) {
                log.error("get ---> key: {}, clazz: {}, errmsg: {}", key, type, e.getMessage(), e);
                return null;
            }
        }

        /**
         * Return the value to which this cache maps the specified key, obtaining
         * that value from {@code valueLoader} if necessary. This method provides
         * a simple substitute for the conventional "if cached, return; otherwise
         * create, cache and return" pattern.
         * <p>If possible, implementations should ensure that the loading operation
         * is synchronized so that the specified {@code valueLoader} is only called
         * once in case of concurrent access on the same key.
         * <p>If the {@code valueLoader} throws an exception, it is wrapped in
         * a {@link ValueRetrievalException}
         *
         * @param key         the key whose associated value is to be returned
         * @param valueLoader
         * @return the value to which this cache maps the specified key
         * @throws ValueRetrievalException if the {@code valueLoader} throws an exception
         * @since 4.3
         */
        @Override
        public <T> T get(Object key, Callable<T> valueLoader) {
            log.info("get ---> key: {}", key);
            try {
                return cache.get(key, valueLoader);
            } catch (Exception e) {
                log.error("get ---> key: {}, errmsg: {}", key, e.getMessage(), e);
                return null;
            }
        }

        /**
         * Associate the specified value with the specified key in this cache.
         * <p>If the cache previously contained a mapping for this key, the old
         * value is replaced by the specified value.
         *
         * @param key   the key with which the specified value is to be associated
         * @param value the value to be associated with the specified key
         */
        @Override
        public void put(Object key, Object value) {
            log.info("put ---> key: {}, value: {}", key, value);
            try {
                cache.put(key, value);
            } catch (Exception e) {
                log.error("put ---> key: {}, value: {}, errmsg: {}", key, value, e.getMessage(), e);
            }
        }

        /**
         * Atomically associate the specified value with the specified key in this cache
         * if it is not set already.
         * <p>This is equivalent to:
         * <pre><code>
         * Object existingValue = cache.get(key);
         * if (existingValue == null) {
         *     cache.put(key, value);
         *     return null;
         * } else {
         *     return existingValue;
         * }
         * </code></pre>
         * except that the action is performed atomically. While all out-of-the-box
         * {@link CacheManager} implementations are able to perform the put atomically,
         * the operation may also be implemented in two steps, e.g. with a check for
         * presence and a subsequent put, in a non-atomic way. Check the documentation
         * of the native cache implementation that you are using for more details.
         *
         * @param key   the key with which the specified value is to be associated
         * @param value the value to be associated with the specified key
         * @return the value to which this cache maps the specified key (which may be
         * {@code null} itself), or also {@code null} if the cache did not contain any
         * mapping for that key prior to this call. Returning {@code null} is therefore
         * an indicator that the given {@code value} has been associated with the key.
         * @since 4.1
         */
        @Override
        public ValueWrapper putIfAbsent(Object key, Object value) {
            log.info("putIfAbsent ---> key: {}, value: {}", key, value);
            try {
                return cache.putIfAbsent(key, value);
            } catch (Exception e) {
                log.error("putIfAbsent ---> key: {}, value: {}, errmsg: {}", key, value, e.getMessage(), e);
                return null;
            }
        }

        /**
         * Evict the mapping for this key from this cache if it is present.
         *
         * @param key the key whose mapping is to be removed from the cache
         */
        @Override
        public void evict(Object key) {
            log.info("evict ---> key: {}", key);
            try {
                cache.evict(key);
            } catch (Exception e) {
                log.error("evict ---> key: {}, errmsg: {}", key, e.getMessage(), e);
            }
        }

        /**
         * Remove all mappings from the cache.
         */
        @Override
        public void clear() {
            log.info("clear");
            try {
                cache.clear();
            } catch (Exception e) {
                log.error("clear ---> errmsg: {}", e.getMessage(), e);
            }
        }
    }
}
