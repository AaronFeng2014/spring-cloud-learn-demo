package com.aaron.springcloud.eureka.retry;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.RetryState;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;
import org.springframework.retry.backoff.UniformRandomBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * spring retry
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-12-17
 */
@Component
public class RetrySample
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrySample.class);

    private RetryListener retryListener = new RetryListenerSupport()
    {
        @Override
        public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable)
        {
            LOGGER.info("close");
        }


        @Override
        public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable)
        {
            LOGGER.error("onError");
        }


        @Override
        public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback)
        {
            LOGGER.info("open");

            //返回 false，将终止
            return true;
        }
    };


    public int testRetry(int in)
    {
        RetryTemplate retryTemplate = new RetryTemplate();

        retryTemplate.registerListener(retryListener);

        retryTemplate.setRetryPolicy(this.getRetryPolicy());

        retryTemplate.setBackOffPolicy(this.getBackOffPolicy());

        RetryCallback<Integer, RuntimeException> callback = getRetryCallback(in);

        RecoveryCallback<Integer> recoveryCallback = getRecoveryCallback();

        return retryTemplate.execute(callback, recoveryCallback, this.getRetryState());
    }


    private RecoveryCallback<Integer> getRecoveryCallback()
    {
        return retryContext -> {

            LOGGER.info("recoveryCallback逻辑");
            return -1;
        };
    }


    /**
     * 业务逻辑
     *
     * @param parameter int：业务逻辑参数
     *
     * @return Integer：指定的返回
     */
    private RetryCallback<Integer, RuntimeException> getRetryCallback(int parameter)
    {
        return retryContext -> {
            LOGGER.info("RetryCallback逻辑");

            if (parameter % 2 == 0)
            {
                throw new IllegalArgumentException();
            }

            if (parameter % 3 == 0)
            {
                throw new NullPointerException();
            }

            return parameter;
        };
    }


    /**
     * 重试策略
     *
     * @return
     */
    private RetryPolicy getRetryPolicy()
    {
        TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(1000);

        return retryPolicy;
    }


    /**
     * 回退策略，指的是如何发起下一次重试，是直接重试还是休眠一段时间后发起重试
     *
     * @return BackOffPolicy
     */
    private BackOffPolicy getBackOffPolicy()
    {
        /**
         * 随机休眠
         */
        UniformRandomBackOffPolicy backOffPolicy = new UniformRandomBackOffPolicy();

        backOffPolicy.setMaxBackOffPeriod(1000);
        backOffPolicy.setMinBackOffPeriod(200);
        backOffPolicy.setSleeper(new ThreadWaitSleeper());

        /**
         * 指定固定休眠时间重试
         */
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(500);

        return backOffPolicy;
    }


    /**
     * 重试状态
     *
     * @return RetryState
     */
    private RetryState getRetryState()
    {
        /**
         *
         * 第二个boolean 类型参数表示的意思是：是否抛出第一个参数指定的异常信息
         */
        BinaryExceptionClassifier classifier = new BinaryExceptionClassifier(ImmutableList.of(NullPointerException.class), false);
        classifier.setTraverseCauses(true);

        return new DefaultRetryState("testRetry", true, classifier);
    }
}