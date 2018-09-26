package com.acmtc.redisMQ.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.Map;

/**
 * @ClassName RedisSubListenerConfiguration
 * @Description TODO
 * @Author paul
 * @Date 2018/8/30 11:33
 * @Version 1.0
 */
@Configuration
public class RedisSubListenerConfiguration {

    @Bean
    RedisMessageListenerContainer container (RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

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
