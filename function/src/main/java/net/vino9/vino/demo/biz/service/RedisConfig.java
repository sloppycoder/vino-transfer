package net.vino9.vino.demo.biz.service;

import lombok.extern.slf4j.Slf4j;
import net.vino9.vino.demo.biz.model.Transfer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Transfer> transferRedisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Transfer> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use Jackson to serialize/deserialize the Transfer object
        Jackson2JsonRedisSerializer<Transfer> serializer =
                new Jackson2JsonRedisSerializer<>(Transfer.class);
        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();

        log.info("RedisTemplate initialized with Jackson serializer");

        return template;
    }
}
