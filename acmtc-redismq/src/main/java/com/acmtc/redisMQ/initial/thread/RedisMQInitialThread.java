package com.acmtc.redisMQ.initial.thread;

import com.acmtc.redisMQ.config.RedisConsumer;
import com.acmtc.redisMQ.config.RedisMQKey;
import com.acmtc.redisMQ.util.RedisMQJedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName RedisMQInitialThread
 * @Description TODO
 * @Author paul
 * @Date 2018/9/29 18:54
 * @Version 1.0
 */
public class RedisMQInitialThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RedisMQInitialThread.class);
    private Object consumer;
    private RedisMQJedisUtil redisMQJedisUtil;
    public RedisMQInitialThread(Object consumer, RedisMQJedisUtil redisMQJedisUtil) {
        this.consumer = consumer;
        this.redisMQJedisUtil = redisMQJedisUtil;
    }

    @Override
    public void run() {
        if (null != consumer && consumer instanceof RedisConsumer) {
            List<String> list = redisMQJedisUtil.getList(RedisMQKey.REDIS_MQ_LIST_PREFIX_KEY + ((RedisConsumer) consumer).getTopic());
            for (String message : list) {
                try {
                    ((RedisConsumer) consumer).handleMessage(message);
                }
                catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }
    }
}
