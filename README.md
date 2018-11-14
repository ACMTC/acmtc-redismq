# acmtc-redismq

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.acmtc/acmtc-redismq/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.acmtc/acmtc-redismq)
[![GitHub release](https://img.shields.io/github/release/acmtc/acmtc-redismq.svg)](https://github.com/acmtc/acmtc-redismq/release)
[![ACMTC Author](https://img.shields.io/badge/ACMTC%20Author-Wencst-ff69b4.svg)](https://www.wencst.com)
[![ACMTC QQ](https://img.shields.io/badge/ACMTC-QQ群-ff69b4.svg)](https://jq.qq.com/?_wv=1027&k=5NBXtHb)

## Introduction

acmtc-redismq is an open source solution suite for easily using redis as MQ.

### Functions

* redis producer
* redis consumer listener
* distributed deployment
* errors handler

### Change Log

- 1.0.0 
  <br>implementation function of RedisMQ
- 1.0.1 
  <br>1、bug-fix：handling the redundant message when starting the service
  <br>2、import the ThreadPoolTaskExecutor to control the concurrency multithreading of RedisMQ consumer

## How to use

### Environment

* JDK 1.9+
* Spring Boot 1.5+
* Redis

### maven use
* pom.xml
```
	<dependency>
	    <groupId>com.acmtc</groupId>
	    <artifactId>acmtc-redismq</artifactId>
	    <version>1.0.1-RELEASE</version>
	</dependency>
```
and use spring boot redis dependency as default.

* application.yml
```
redis-mq:
  maxErrorCount: 3                                # redisMQ consumer error count, greater than it will be discarded.
  config:
      corePoolSize: 10                            # Set the redisMQ consumer ThreadPoolExecutor's core pool size.
      maxPoolSize: 100                            # Set the redisMQ consumer ThreadPoolExecutor's maximum pool size.
      keepAliveSeconds: 120                       # Set the redisMQ consumer ThreadPoolExecutor's keep-alive seconds.
      queueCapacity: 2                            # Set the capacity for the redisMQ consumer ThreadPoolExecutor's BlockingQueue.
      allowCoreThreadTimeOut: false               # Specify whether to allow core threads to time out.
  consumer:
    topicMainSwitch: false                        # redisMQ customize whether all consuming listener will be opened,false for all opened,true for custuming below,default false
    switchList:                                   # redisMQ specific consumers customize
      - topic: channels                           # specific consumer name, same topic as annotation used in @RedisConsumerAnnotation
        topicSwitch: true                         # true for open listener, default false
      - topic: channel2
        topicSwitch: false
      - topic: apsToolsChannels
        topicSwitch: true
```
and use spring boot redis as omission here.
* Configuration
```
@Configuration
public class RedisConfig {
	/**
	 * 重写RedisAutoConfiguration当中生成RedisTemplate的方法，将泛型改为实际项目当中使用的类型，否则启动报错
	 * @param redisConnectionFactory
	 * @return
	 * @throws UnknownHostException
	 */
	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<?, ?> redisTemplate(
			RedisConnectionFactory redisConnectionFactory)
					throws UnknownHostException {
		RedisTemplate<?, ?> template = new RedisTemplate<Serializable, Serializable>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}
	
}
```
generate redisTemplate.

* startup
```
@EntityScan("com.acmtc")
@SpringBootApplication(scanBasePackages = {"com.acmtc"})
```
ServerApplication will scan package "com.acmtc"

* producer use
```
redisProducer.sendChannelMessage("testchannel", message);
```
please use message as JSONObject
* consumer use
```
@RedisConsumerAnnotation(topic = "testchannel")
public class ConsumerExample  extends RedisConsumer {
    public void onMessage(JSONObject json) {
        log.info("测试Consumer，接收消息：" + json);
    }
}
```
### source code
Download code : https://github.com/ACMTC/acmtc-redismq.git


