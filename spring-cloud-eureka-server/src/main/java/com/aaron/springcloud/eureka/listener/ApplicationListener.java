package com.aaron.springcloud.eureka.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-07-17
 */
@Slf4j
public class ApplicationListener implements SpringApplicationRunListener
{

    public ApplicationListener(SpringApplication application, String[] args)
    {

    }


    @Override
    public void starting()
    {
        log.warn("app start to run");
    }


    @Override
    public void environmentPrepared(ConfigurableEnvironment environment)
    {
        log.warn("app environmentPrepared");
    }


    @Override
    public void contextPrepared(ConfigurableApplicationContext context)
    {
        log.warn("app contextPrepared");
    }


    @Override
    public void contextLoaded(ConfigurableApplicationContext context)
    {
        log.warn("app contextLoaded");
    }


    @Override
    public void started(ConfigurableApplicationContext context)
    {
        log.warn("app started");
    }


    @Override
    public void running(ConfigurableApplicationContext context)
    {
        log.warn("app running");
    }


    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception)
    {
        log.warn("app run failed");
    }
}
