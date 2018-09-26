package com.acmtc.redismq.examples.publish;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acmtc.redisMQ.service.RedisProducer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@RestController("/")
public class PublishExample {
	@Autowired
	RedisProducer redisProducer;
	
	@GetMapping("publish")
	public int publish (HttpServletRequest request) throws Exception {
		Map<String,String[]> keyvalue = request.getParameterMap();
		String jsonString = JSON.toJSONString(
				orEmptyListIfNull(keyvalue));
		JSONObject message = JSONObject.parseObject(jsonString);
		redisProducer.sendChannelMessage("testchannel", message);
		return 1;
	}

    private static <M,N> Map<M,N> orEmptyListIfNull(Map<M,N> map) {
        return Optional.ofNullable(map).orElse(new HashMap<M,N>());
    }
}
