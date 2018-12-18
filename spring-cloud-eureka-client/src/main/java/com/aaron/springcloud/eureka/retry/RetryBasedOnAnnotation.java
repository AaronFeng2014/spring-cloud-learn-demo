package com.aaron.springcloud.eureka.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * spring retry
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-12-17
 */
@Component
public class RetryBasedOnAnnotation
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryBasedOnAnnotation.class);


    @Retryable (value = {RuntimeException.class}, maxAttempts = 5, backoff = @Backoff (random = true, maxDelay = 500))
    public int testRetryAnnotation(int in)
    {
        if (in % 2 == 0)
        {
            throw new RuntimeException();
        }

        if (in % 3 == 0)
        {
            throw new IllegalArgumentException();
        }

        return -1;
    }


    @Recover
    public int recoveryForException(RuntimeException exception)
    {
        LOGGER.warn("目标方法重试执行失败，这是RuntimeException兜底方法");
        return -111;
    }


    @Recover
    public int recoveryForException(IllegalArgumentException exception)
    {
        LOGGER.warn("目标方法重试执行失败，这是IllegalArgumentException兜底方法");
        return -222;
    }
}