package com.tyd.common.config;

import com.tyd.common.constant.FwConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


/**
 * @author 谭越东
 * @date 2022-09-27
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig extends CachingConfigurerSupport {


    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setValueSerializer(stringRedisSerializer);
        template.setHashValueSerializer(stringRedisSerializer);

        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
    /**
     * 配置一个CacheManager才能使用@Cacheable等注解
     *
     * 公众号：MarkerHub
     */

//    public RedisCacheConfiguration getCacheTTl(RedisTemplate<Object, Object> template,long time){
//        // 基本配置
//        RedisCacheConfiguration defaultCacheConfiguration =
//                RedisCacheConfiguration
//                        .defaultCacheConfig()
//                        // 设置key为String
//                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getStringSerializer()))
//                        // 设置value 为自动转Json的Object
//                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getValueSerializer()))
//                        // 不缓存null
//                        .disableCachingNullValues()
//                        // 缓存数据保存1小时
//                        .entryTtl(Duration.ofSeconds(time));
//        return defaultCacheConfiguration;
//    }
//
//    @Bean
//    public CacheManager cacheManager(RedisTemplate<Object, Object> template) {
//        // 够着一个redis缓存管理器
//        RedisCacheManager redisCacheManager =
//                RedisCacheManager.RedisCacheManagerBuilder
//                        // Redis 连接工厂
//                        .fromConnectionFactory(template.getConnectionFactory())
//                        // 缓存配置
//                        .cacheDefaults(getCacheTTl(template,3600))
//                        .withCacheConfiguration("shortTimeCache",getCacheTTl(template, FwConstants.CacheTTL_Short))
//                        .withCacheConfiguration("longTimeCache",getCacheTTl(template,FwConstants.CacheTTL_Long))
//                        // 配置同步修改或删除 put/evict
//                        .transactionAware()
//                        .build();
//
//        return redisCacheManager;
//    }
}
