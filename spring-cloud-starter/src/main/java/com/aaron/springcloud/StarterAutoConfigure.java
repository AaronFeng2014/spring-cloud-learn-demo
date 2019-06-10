package com.aaron.springcloud;

import com.aaron.springcloud.service.StarterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-01-25
 */
@Configuration
//表示在classpath下存在该类时，才会去加载
@ConditionalOnClass (StarterService.class)
//spring.factories 可以用这个替换 @ImportAutoConfiguration ({StarterAutoConfigure.class})
@EnableConfigurationProperties (ConfigSample.class)
public class StarterAutoConfigure
{

    @Autowired
    private ConfigSample configSample;


    /**
     * @ConditionalOnBean: 当容器中有指定的Bean的条件下
     * @ConditionalOnClass: 当类路径下有指定的类的条件下
     * @ConditionalOnExpression: 基于SpEL表达式作为判断条件
     * @ConditionalOnJava: 基于JVM版本作为判断条件
     * @ConditionalOnJndi: 在JNDI存在的条件下查找指定的位置
     * @ConditionalOnMissingBean: 当容器中没有指定Bean的情况下
     * @ConditionalOnMissingClass: 当类路径下没有指定的类的条件下
     * @ConditionalOnNotWebApplication: 当前项目不是Web项目的条件下
     * @ConditionalOnProperty: 指定的属性是否有指定的值
     * @ConditionalOnResource: 类路径下是否有指定的资源
     * @ConditionalOnSingleCandidate: 当指定的Bean在容器中只有一个，或者在有多个Bean的情况下，用来指定首选的Bean @ConditionalOnWebApplication:当前项目是Web项目的条件下
     */
    @Bean
    //表示 spring context容器中不存在该类型的bean的时候，才会去加载
    @ConditionalOnMissingBean
    //表示属性 starter.config.enable = true 时，才会去加载
    @ConditionalOnProperty (prefix = "starter.config", value = "enable", havingValue = "true")
    public StarterService starterService()
    {
        return new StarterService(configSample);
    }
}