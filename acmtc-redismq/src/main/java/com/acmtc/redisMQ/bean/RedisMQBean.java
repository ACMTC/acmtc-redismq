package com.acmtc.redisMQ.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName RedisMQBean
 * @Description 消息队列的Bean格式
 * @Author paul
 * @Date 2018/9/6 14:07
 * @Version 1.0
 */
public class RedisMQBean {

    private JSONObject business;

    private long errorCount;

    public JSONObject getBusiness() {
        return business;
    }

    public void setBusiness(JSONObject business) {
        this.business = business;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(long errorCount) {
        this.errorCount = errorCount;
    }
}
