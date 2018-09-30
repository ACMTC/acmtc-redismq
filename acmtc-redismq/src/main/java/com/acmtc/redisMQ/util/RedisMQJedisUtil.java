package com.acmtc.redisMQ.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * @author zhouhaitao
 * @date 2018-01-23
 */
@Component
public class RedisMQJedisUtil {
    private static RedisMQJedisUtil instance;

    private RedisMQJedisUtil(){
        instance = this;
    }

    public static RedisMQJedisUtil getInstance(){
        return instance;
    }

    @Autowired
    protected RedisTemplate<Serializable, Serializable> redisTemplate;

    /**
     * 在消息队列中pop指定key的数据，并在队列中移除该element
     * <p>
     * 如果key不存在或者队列已为空，则返回null

     * @param key
     * @return value
     * @throws Exception
     */
    public String pop(final String key) throws Exception {

        return redisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.rPop(serializer.serialize(key));

                return serializer.deserialize(value);
            }

        });
    }

    /**
     * 获取指定key的列表数据
     * @param key
     * @return
     */
    public List<String> getList(final String key){
        return redisTemplate.execute(new RedisCallback<List<String>>() {

            @Override
            public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                List<byte[]> list = connection.lRange(serializer.serialize(key), 0, -1);
                List<String> result = new ArrayList<String>();
                for (byte[] bytes : list) {
                    result.add(String.valueOf(serializer.deserialize(bytes)));
                }
                return result;
            }
        });
    }

    /**
     * @Author paul
     * @Description 推送消息队列并发布通知消费者消费该消息
     * @Date 11:24 2018/9/6
     * @Param [key, value, channel, message]
     * @Return void
     **/
    public void pushAndPublish (final String key, final String value, final String channel, final String message) throws Exception {
        redisTemplate.execute(new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis (RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                redisConnection.multi();
                redisConnection.lPush(serializer.serialize(key), serializer.serialize(value));
                redisConnection.publish(serializer.serialize(channel), serializer.serialize(message));
                return redisConnection.exec();
            }
        });
    }

}
