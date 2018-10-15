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
    @Qualifier("springSessionRedisMQTaskExecutor")
    public void setRedisTaskExecutor (Executor redisTaskExecutor) {
        this.redisTaskExecutor = redisTaskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor springSessionRedisMQTaskExecutor () {
        ThreadPoolTaskExecutor springSessionRedisMQTaskExecutor = new ThreadPoolTaskExecutor();
        RedisMQConfig.Config config = redisMQConfig.getConfig();
        if (config != null && config.getCorePoolSize() > 1) {
            springSessionRedisMQTaskExecutor.setCorePoolSize(config.getCorePoolSize());
            springSessionRedisMQTaskExecutor.setMaxPoolSize(config.getMaxPoolSize());
            springSessionRedisMQTaskExecutor.setKeepAliveSeconds(config.getKeepAliveSeconds());
            springSessionRedisMQTaskExecutor.setQueueCapacity(config.getQueueCapacity());
            springSessionRedisMQTaskExecutor.setAllowCoreThreadTimeOut(config.isAllowCoreThreadTimeOut());
            springSessionRedisMQTaskExecutor.setThreadNamePrefix("Spring session redisMQ executor thread: ");
            springSessionRedisMQTaskExecutor.initialize();
            return springSessionRedisMQTaskExecutor;
        }
        else {
            return null;
        }
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
