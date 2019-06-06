package com.aaron.springcloud.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

/**
 * spring cloud gateway
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-05-25
 */
@EnableAutoConfiguration
@EnableEurekaClient
@Configuration
@EnableAdminServer
public class SpringBootAdminServerApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(SpringBootAdminServerApplication.class, args);
    }
}
