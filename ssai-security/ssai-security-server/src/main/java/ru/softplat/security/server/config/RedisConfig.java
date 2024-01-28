package ru.softplat.security.server.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.databases}")
    Integer redisDatabase;

    @Value("${spring.redis.password}")
    String password;

    @Value("${spring.redis.host}")
    String host;

    @Value("${spring.redis.port}")
    Integer port;

    @Bean
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        if (!password.trim().isEmpty()) {
            redisStandaloneConfiguration.setPassword(password);
        }

        if (redisDatabase > 0) {
            redisStandaloneConfiguration.setDatabase(redisDatabase);
        }
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);
        return template;
    }
}