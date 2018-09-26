package com.acmtc.redisMQ.config;

import com.acmtc.redisMQ.annotation.RedisConsumerAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableConfigurationProperties(RedisMQConfig.class)
public class RedisAnnotationProcessor implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(RedisAnnotationProcessor.class);
    public static Map<String, Object> EVENTCODESERVICEBEANMAP = new HashMap<String, Object>();
    private Map<String, RedisConsumerAnnotation> REDIS_CONSUMER_ANNOTATION_TOPIC_MAP = new HashMap<>();
    @Autowired
    private RedisMQConfig redisMQConfig;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RedisConsumerAnnotation annotation = bean.getClass().getAnnotation(RedisConsumerAnnotation.class);
        if (null != annotation && bean instanceof RedisConsumer) {
            if (!redisMQConfig.getConsumer().isTopicMainSwitch() || (redisMQConfig.getConsumer().isTopicMainSwitch() && getTopicSwitchByTopic(annotation.topic()))) {
                String topic = annotation.topic();
                if (REDIS_CONSUMER_ANNOTATION_TOPIC_MAP.containsKey(topic)) {
                    log.error("redisMQ注册的业务队列当中，topic名称重复，目前重复的名称为：" + topic);
                    throw new RuntimeException("redisMQ注册的业务队列当中，topic名称重复，目前重复的名称为：" + topic);
                }
                REDIS_CONSUMER_ANNOTATION_TOPIC_MAP.put(topic, annotation);
                EVENTCODESERVICEBEANMAP.put(beanName, bean);
            }
        }
        return bean;
    }

    /**
     * @Author paul
     * @Description 通过beanName获取队列是否加入监听的开关
     * @Date 18:36 2018/9/10
     * @Param [topic]
     * @Return boolean
     **/
    private boolean getTopicSwitchByTopic (String topic) {
        for (RedisMQConfig.Consumer.ConsumerSwitch cs : redisMQConfig.getConsumer().getSwitchList()) {
            if (topic.equals(cs.getTopic())) {
                return cs.isTopicSwitch();
            }
        }
        return false;
    }

}
