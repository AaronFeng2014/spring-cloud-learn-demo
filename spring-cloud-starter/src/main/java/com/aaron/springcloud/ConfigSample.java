package com.aaron.springcloud;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-01-25
 */
@ConfigurationProperties (prefix = "starter.config")
@Setter
@Getter
public class ConfigSample
{
    private boolean enable;

    private String name;

    private Integer age;

    private String address;
}
