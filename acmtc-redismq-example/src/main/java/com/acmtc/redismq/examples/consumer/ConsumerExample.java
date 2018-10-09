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
        log.info("测试Consumer，接收消息：" + json + "——开始");
        try {
            Thread.sleep(1000);
            log.info("第一秒：" + json);
            Thread.sleep(1000);
            log.info("第二秒：" + json);
            Thread.sleep(1000);
            log.info("第三秒：" + json);
            Thread.sleep(1000);
            log.info("第四秒：" + json);
            Thread.sleep(1000);
            log.info("第五秒：" + json);
            Thread.sleep(1000);
            log.info("第六秒：" + json);
            Thread.sleep(1000);
            log.info("第七秒：" + json);
            Thread.sleep(1000);
            log.info("第八秒：" + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("测试Consumer，接收消息：" + json + "——结束");
    }
}
