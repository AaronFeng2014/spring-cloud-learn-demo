package com.aaron.springcloud.service;

import com.aaron.springcloud.ConfigSample;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-01-25
 */
public class StarterService
{
    private ConfigSample configSample;


    public StarterService(ConfigSample configSample)
    {
        this.configSample = configSample;
    }


    public Object testStarter()
    {
        return configSample;
    }
}
