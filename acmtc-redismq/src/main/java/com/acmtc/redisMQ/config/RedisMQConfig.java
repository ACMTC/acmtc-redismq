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

    private long maxErrorCount = 3;

    private Config config;

    private Consumer consumer;

    public long getMaxErrorCount() {
        return maxErrorCount;
    }

    public void setMaxErrorCount(long maxErrorCount) {
        this.maxErrorCount = maxErrorCount;
    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public static class Config {

        private int corePoolSize = 1;

        private int maxPoolSize = Integer.MAX_VALUE;

        private int keepAliveSeconds = 60;

        private int queueCapacity = Integer.MAX_VALUE;

        private boolean allowCoreThreadTimeOut = false;

        public int getCorePoolSize() {
            return this.corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return this.maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getKeepAliveSeconds() {
            return this.keepAliveSeconds;
        }

        public void setKeepAliveSeconds(int keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
        }

        public int getQueueCapacity() {
            return this.queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public boolean isAllowCoreThreadTimeOut() {
            return this.allowCoreThreadTimeOut;
        }

        public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        }
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
