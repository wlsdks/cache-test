package com.study.cache.config.cache;

import com.study.cache.dto.PostDto;
import com.study.cache.dto.ProductReviewDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    /**
     * @return RedisConnectionFactory
     * @apiNote Redis 연결 Factory 설정
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(host);
        redisConfiguration.setPort(port);
        redisConfiguration.setPassword(password);

        return new LettuceConnectionFactory(redisConfiguration);
    }

    /**
     * @param connectionFactory - Redis 연결 Factory
     * @return Redis CacheManager
     * @apiNote Redis CacheManager 설정
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    /**
     * @param connectionFactory - Redis 연결 Factory
     * @return RedisTemplate
     * @apiNote RedisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, List<PostDto>> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<PostDto>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key Serializer
        template.setKeySerializer(new StringRedisSerializer());

        // Value Serializer using GenericJackson2JsonRedisSerializer
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }


    /**
     * @param connectionFactory - Redis 연결 Factory
     * @return RedisTemplate
     * @apiNote RedisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, List<ProductReviewDto>> reviewRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<ProductReviewDto>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key Serializer
        template.setKeySerializer(new StringRedisSerializer());

        // Value Serializer using GenericJackson2JsonRedisSerializer
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

}