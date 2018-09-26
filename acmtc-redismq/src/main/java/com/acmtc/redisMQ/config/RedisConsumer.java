package com.acmtc.redisMQ.config;

import com.acmtc.redisMQ.annotation.RedisConsumerAnnotation;
import com.acmtc.redisMQ.bean.RedisMQBean;
import com.acmtc.redisMQ.util.RedisMQJedisUtil;
import com.acmtc.redisMQ.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @ClassName RedisReceiver
 * @Description TODO
 * @Author paul
 * @Date 2018/8/30 11:42
 * @Version 1.0
 */
@Service
@EnableConfigurationProperties(RedisMQConfig.class)
public abstract class RedisConsumer {
    private static final Logger log = LoggerFactory.getLogger(RedisConsumer.class);
    @Autowired
    private RedisMQConfig redisMQConfig;
    @Autowired
    private RedisMQJedisUtil jedisUtil;

    /**
     * @Author paul
     * @Description 默认处理订阅的发布消息的接口
     * @Date 12:35 2018/9/8
     * @Param [message]
     * @Return void
     **/
    public void handleMessage (String message) throws Exception {
        String topic = "";
        RedisMQBean redisMQBean = new RedisMQBean();
        try {
            topic = getTopic();
            message = jedisUtil.pop(RedisMQKey.REDIS_MQ_LIST_PREFIX_KEY + topic);
            log.info("接受到来自topic:" + topic + "的消息内容为：" + message);
            if (!StringUtil.isEmpty(message)) {
                redisMQBean = JSONObject.parseObject(message, RedisMQBean.class);
                onMessage(redisMQBean.getBusiness());
            }
        } catch (Exception e) {
            if (!StringUtil.isEmpty(message) && !StringUtil.isEmpty(topic)) {
                if (redisMQBean.getErrorCount() <= redisMQConfig.getMaxErrorCount()) {
                    resendChannelMessage(topic, message);
                }
                else {
                    log.info("来自topic：" + topic + "的消息内容为：" + message + "已经执行异常超过" + redisMQConfig.getMaxErrorCount() + "次，将不再被继续执行！");
                }
            }
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * @Author paul
     * @Description 从实现类的RedisConsumerAnnotation注解上获取topic
     * @Date 12:35 2018/9/8
     * @Param []
     * @Return java.lang.String
     **/
    public String getTopic(){
        return this.getClass().getAnnotation(RedisConsumerAnnotation.class).topic();
    }

    /**
     * @Author paul
     * @Description 重新向通道发送消息的方法
     * @Date 13:30 2018/9/6
     * @Param [channel, redisMQBeanStr]
     * @Return void
     **/
    private void resendChannelMessage (String channel, String redisMQBeanStr) throws Exception {
        RedisMQBean redisMQBean = JSONObject.parseObject(redisMQBeanStr, RedisMQBean.class);
        redisMQBean.setErrorCount(redisMQBean.getErrorCount() + 1);
        jedisUtil.pushAndPublish(RedisMQKey.REDIS_MQ_LIST_PREFIX_KEY + channel, JSONObject.toJSONString(redisMQBean), channel, JSONObject.toJSONString(redisMQBean));
    }

    /**
     * @Author paul
     * @Description 对订阅消息的业务处理的抽象接口
     * @Date 12:36 2018/9/8
     * @Param [json]
     * @Return void
     **/
    public abstract void onMessage(JSONObject json);

}
