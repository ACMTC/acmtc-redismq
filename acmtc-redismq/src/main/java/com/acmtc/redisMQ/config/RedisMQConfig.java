package com.acmtc.redisMQ.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName RedisMQConfig
 * @Description TODO
 * @Author paul
 * @Date 2018/9/10 14:53
 * @Version 1.0
 */
@Component
@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "redis-mq")
public class RedisMQConfig {

    private boolean initial = false;

    private long maxErrorCount = 3;

    private Consumer consumer;

    public boolean isInitial() {
        return this.initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public long getMaxErrorCount() {
        return maxErrorCount;
    }

    public void setMaxErrorCount(long maxErrorCount) {
        this.maxErrorCount = maxErrorCount;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public static class Consumer {

        private boolean topicMainSwitch = false;

        private List<ConsumerSwitch> switchList;

        public boolean isTopicMainSwitch() {
            return topicMainSwitch;
        }

        public void setTopicMainSwitch(boolean topicMainSwitch) {
            this.topicMainSwitch = topicMainSwitch;
        }

        public List<ConsumerSwitch> getSwitchList() {
            return switchList;
        }

        public void setSwitchList(List<ConsumerSwitch> switchList) {
            this.switchList = switchList;
        }

        public static class ConsumerSwitch {

            private String topic;

            private boolean topicSwitch = false;

            public String getTopic() {
                return topic;
            }

            public void setTopic(String topic) {
                this.topic = topic;
            }

            public boolean isTopicSwitch() {
                return topicSwitch;
            }

            public void setTopicSwitch(boolean topicSwitch) {
                this.topicSwitch = topicSwitch;
            }

        }

    }

}
