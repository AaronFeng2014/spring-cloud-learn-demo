package com.aaron.springcloud.eureka.controller;

import com.aaron.springcloud.eureka.retry.RetrySample;
import com.aaron.springcloud.eureka.retry.Retryable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2017-07-07
 */
@RestController
public class HelloController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RetrySample retrySample;

    @Autowired
    private RedisLock redisLock;


    @RequestMapping ("/hello1")
    public String sayHello1()
    {
        return "This is my spring cloud app 1 !";
    }


    @RequestMapping ("/hello2")
    public String sayHello2()
    {
        redisLock.releaseLock("test");
        if (redisLock.lock("test"))
        {
            LOGGER.info("获取到了锁,线程id：{}", Thread.currentThread().getId());
            return "acquire lock success";
        }

        LOGGER.info("没有获取到锁");

        return "This is my spring cloud app 2 !";
    }


    @RequestMapping ("/hello3")
    public String sayHello3()
    {
        return "This is my spring cloud app 3 !";
    }


    @RequestMapping ("/hello4")
    public String sayHello4()
    {
        return "This is my spring cloud app 4 !";
    }


    @RequestMapping ("/retry/{id}")
    public Integer sayHello4(@PathVariable ("id") Integer id)
    {
        return Retryable.<Integer, Integer>retry(parameter -> {

            if (parameter % 2 == 0)
            {
                throw new IllegalArgumentException();
            }

            if (parameter % 3 == 0)
            {
                throw new NullPointerException();
            }
            return parameter;

        }, id, IOException.class);
    }

}
