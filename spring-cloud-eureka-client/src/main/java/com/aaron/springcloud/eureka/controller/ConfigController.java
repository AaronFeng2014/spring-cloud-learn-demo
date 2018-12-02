package com.aaron.springcloud.eureka.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-12-01
 */
@RestController
@RefreshScope
public class ConfigController
{
    @Value ("${test.config}")
    private String testConfig;

    @Value ("${redis.host}")
    private String redisHost;

    @Value ("${zk.host}")
    private String zkHost;


    @GetMapping (value = "config")
    public String getConfig()
    {
        return "远程配置中心实时配置信息 test.config 是：" + testConfig;
    }


    @GetMapping (value = "redis")
    public String getRedisHost()
    {
        return "远程配置中心实时配置信息 redis.host 是：" + redisHost;
    }


    @GetMapping (value = "zk")
    public String getZk()
    {
        return "远程配置中心实时配置信息 zk.host 是：" + zkHost;
    }
}