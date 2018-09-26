package com.acmtc.redisMQ.service;

import com.acmtc.redisMQ.bean.RedisMQBean;
import com.acmtc.redisMQ.config.RedisMQKey;
import com.acmtc.redisMQ.util.RedisMQJedisUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName RedisService
 * @Description TODO
 * @Author paul
 * @Date 2018/8/30 11:42
 * @Version 1.0
 */

@Service
public class RedisProducer {
    @Autowired
    private RedisMQJedisUtil jedisUtil;

    /**
     * @Author paul
     * @Description 向通道发送消息的方法
     * @Date 11:43 2018/8/30
     * @Param [redisKey, channel, message]
     * @Return void
     **/
    public void sendChannelMessage (String channel, JSONObject message) throws Exception {
        RedisMQBean redisMQBean = new RedisMQBean();
        redisMQBean.setBusiness(message);
        redisMQBean.setErrorCount(0);
        jedisUtil.pushAndPublish(RedisMQKey.REDIS_MQ_LIST_PREFIX_KEY + channel, JSONObject.toJSONString(redisMQBean), channel, JSONObject.toJSONString(redisMQBean));
    }

    public static void main (String[] args) {
        try {
            RedisProducer redisProducer = new RedisProducer();
            JSONObject json = new JSONObject();
            json.put("name", "gaoxiang");
            redisProducer.sendChannelMessage( "channel1", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
