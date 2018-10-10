package com.acmtc.redisMQ.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @ClassName RedisSubListenerConfiguration
 * @Description TODO
 * @Author paul
 * @Date 2018/8/30 11:33
 * @Version 1.0
 */
@Configuration
public class RedisSubListenerConfiguration {
    private Executor redisTaskExecutor;
    private Executor redisSubscriptionExecutor;
    @Autowired
    private RedisMQConfig redisMQConfig;

    @Autowired
    @Qualifier("springSessionRedisTaskExecutor")
    public void setRedisTaskExecutor (Executor redisTaskExecutor) {
        this.redisTaskExecutor = redisTaskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor springSessionRedisTaskExecutor () {
        ThreadPoolTaskExecutor springSessionRedisTaskExecutor = new ThreadPoolTaskExecutor();
        RedisMQConfig.Config config = redisMQConfig.getConfig();
        if (config != null) {
            springSessionRedisTaskExecutor.setCorePoolSize(config.getCorePoolSize());
            springSessionRedisTaskExecutor.setMaxPoolSize(config.getMaxPoolSize());
            springSessionRedisTaskExecutor.setKeepAliveSeconds(config.getKeepAliveSeconds());
            springSessionRedisTaskExecutor.setQueueCapacity(config.getQueueCapacity());
            springSessionRedisTaskExecutor.setAllowCoreThreadTimeOut(config.isAllowCoreThreadTimeOut());
        }
        springSessionRedisTaskExecutor.setThreadNamePrefix("Spring session redisMQ executor thread: ");
        springSessionRedisTaskExecutor.initialize();
        return springSessionRedisTaskExecutor;
    }

    @Bean
    RedisMessageListenerContainer container (RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        if (redisTaskExecutor != null) {
            container.setTaskExecutor(redisTaskExecutor);
        }

        if (redisSubscriptionExecutor != null) {
            container.setSubscriptionExecutor(redisSubscriptionExecutor);
        }
        Map<String,Object> maps = RedisAnnotationProcessor.EVENTCODESERVICEBEANMAP;
        if(null != maps && maps.size()>0){
            for(Object receiver:maps.values()){
                if(null != receiver && receiver instanceof RedisConsumer){
                    String topic = ((RedisConsumer)receiver).getTopic();
                    MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(receiver);
                    listenerAdapter.afterPropertiesSet();
                    container.addMessageListener(listenerAdapter, new PatternTopic(topic));
                }
            }
        }

         return container;
    }

}
