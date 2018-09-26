package com.acmtc.redismq.examples.config;

import java.io.Serializable;
import java.net.UnknownHostException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

	/**
	 * 重写RedisAutoConfiguration当中生成RedisTemplate的方法，将泛型改为实际项目当中使用的类型，否则启动报错
	 * @param redisConnectionFactory
	 * @return
	 * @throws UnknownHostException
	 */
	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<?, ?> redisTemplate(
			RedisConnectionFactory redisConnectionFactory)
					throws UnknownHostException {
		RedisTemplate<?, ?> template = new RedisTemplate<Serializable, Serializable>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}
	
}
