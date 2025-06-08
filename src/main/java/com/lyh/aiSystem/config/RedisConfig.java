package com.lyh.aiSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author BigHH
 *  redis配置类
 */
@Configuration
public class RedisConfig {

//    @Bean
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//
//        // 使用StringRedisSerializer来序列化和反序列化redis的key和value
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashValueSerializer(stringRedisSerializer);
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
    /**
     *  配置RedisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建RedisTemplate实例
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置键的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // 设置值的序列化方式
        template.setValueSerializer(new StringRedisSerializer());

        // 设置哈希键的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置哈希值的序列化方式
        template.setHashValueSerializer(new StringRedisSerializer());

        // 初始化RedisTemplate
        template.afterPropertiesSet();
        return template;
    }
}