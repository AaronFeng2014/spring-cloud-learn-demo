package com.aaron.springcloud.eureka.controller;

import com.aaron.springcloud.eureka.retry.RetrySample;
import com.aaron.springcloud.eureka.retry.Retryable;
import java.io.IOException;
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

    @Autowired
    private RetrySample retrySample;


    @RequestMapping ("/hello1")
    public String sayHello1()
    {
        return "This is my spring cloud app 1 !";
    }


    @RequestMapping ("/hello2")
    public String sayHello2()
    {
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
