package com.acmtc.redismq.examples.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmtc.redisMQ.annotation.RedisConsumerAnnotation;
import com.acmtc.redisMQ.config.RedisConsumer;
import com.alibaba.fastjson.JSONObject;

@RedisConsumerAnnotation(topic = "testchannel")
public class ConsumerExample  extends RedisConsumer {
    private static final Logger log = LoggerFactory.getLogger(ConsumerExample.class);

    public void onMessage(JSONObject json) {
        log.info("测试Consumer，接收消息：" + json);
    }
}
