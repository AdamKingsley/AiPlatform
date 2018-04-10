package cn.edu.nju.software.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by AA on 2017/8/10.
 */
@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean(name="redisConnectionFactory")
    JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory conn = new JedisConnectionFactory();
        conn.setDatabase(redisProperties.getDatabase());
        conn.setHostName(redisProperties.getHost());
        conn.setPassword(redisProperties.getPassword());
        conn.setPort(redisProperties.getPort());
        conn.setTimeout(redisProperties.getTimeout());
        return conn;
    }

    @Bean("stringRedisTemplate")
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(connectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
