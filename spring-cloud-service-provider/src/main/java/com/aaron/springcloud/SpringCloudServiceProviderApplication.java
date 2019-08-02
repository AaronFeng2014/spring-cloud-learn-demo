package com.aaron.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * EnableDiscoveryClient注解实现服务发现
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2017-07-07
 */
@SpringBootApplication
@EnableEurekaClient
@Configuration
@MapperScan (basePackages = "com.aaron.springcloud.dao")
public class SpringCloudServiceProviderApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpringCloudServiceProviderApplication.class, args);
    }
}
