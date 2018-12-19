package com.aaron.springcloud.eureka.retry;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.RetryState;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;
import org.springframework.retry.backoff.UniformRandomBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 重试封装
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-12-18
 */
public class Retryable
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Retryable.class);


    /**
     * 无返回值重试
     * <p>
     * 需要自己判断业务逻辑是否允许重试
     * 如：GET 请求可以重试，冥等的请求可以重试；POST 请求不能重试
     *
     * @param consumer Consumer<C>：具体业务逻辑
     * @param parameter C：参数
     * @param <C> 参数类型
     * @param exceptions Class<? extends Throwable>：可变参数列表，表示需要n
     *
     * @return 无返回值
     */
    public static <C> Void retry(Consumer<C> consumer, C parameter, Class<? extends Throwable>... exceptions)
    {
        RetryTemplate retryTemplate = getRetryTemplate();

        RetryCallback<Void, RuntimeException> callback = getRetryCallback(consumer, parameter);

        return retryTemplate.execute(callback, getRetryState(exceptions));
    }


    /**
     * 有返回值重试
     * <p>
     * 需要自己判断业务逻辑是否允许重试
     * 如：GET 请求可以重试，冥等的请求可以重试；POST 请求不能重试
     *
     * @param function Function<T, R>：具体业务逻辑
     * @param parameter T：参数
     * @param <T> 参数类型
     * @param <R> 返回值类型
     *
     * @return R类型
     */
    public static <T, R> R retry(Function<T, R> function, T parameter, Class<? extends Throwable>... exceptions)
    {
        RetryTemplate retryTemplate = getRetryTemplate();

        RetryCallback<R, RuntimeException> callback = getRetryCallback(function, parameter);

        return retryTemplate.execute(callback, getRetryState(exceptions));
    }


    private static RetryTemplate getRetryTemplate()
    {
        RetryTemplate retryTemplate = new RetryTemplate();

        retryTemplate.registerListener(new MyRetryListener());

        retryTemplate.setRetryPolicy(getRetryPolicy());

        retryTemplate.setBackOffPolicy(getBackOffPolicy());
        return retryTemplate;
    }


    /**
     * 重试业务逻辑
     *
     * @param consumer Consumer<C>：待重试的业务逻辑
     * @param parameter C：待重试的业务逻辑的参数
     *
     * @return Integer：指定的返回
     */
    private static <C> RetryCallback<Void, RuntimeException> getRetryCallback(Consumer<C> consumer, C parameter)
    {
        return retryContext -> {

            consumer.accept(parameter);

            return null;
        };
    }


    /**
     * 重试业务逻辑
     *
     * @param function Function<T, R>：待重试的业务逻辑
     * @param parameter T：待重试的业务逻辑的参数
     *
     * @return Integer：指定的返回
     */
    private static <T, R> RetryCallback<R, RuntimeException> getRetryCallback(Function<T, R> function, T parameter)
    {
        return retryContext -> function.apply(parameter);
    }


    /**
     * 重试策略
     *
     * @return RetryPolicy
     */
    private static RetryPolicy getRetryPolicy()
    {

        //可以指定哪些异常重试，哪些异常不重试
        return new SimpleRetryPolicy(5);
    }


    /**
     * 回退策略，指的是如何发起下一次重试，是直接重试还是休眠一段时间后发起重试
     *
     * @return BackOffPolicy
     */
    private static BackOffPolicy getBackOffPolicy()
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
     * @param exceptions Class<? extends Throwable>
     *
     * @return RetryState
     */
    private static RetryState getRetryState(Class<? extends Throwable>... exceptions)
    {
        /**
         *
         * 第二个boolean 类型参数表示的意思是：是否抛出第一个参数指定的异常信息
         */

        Map<Class<? extends Throwable>, Boolean> maps = new HashMap<>();

        //可重试的异常
        maps.put(IllegalAccessException.class, false);

        if (ArrayUtils.isNotEmpty(exceptions))
        {
            //不可重试的异常
            Stream.of(exceptions).forEach(exception -> maps.put(exception, true));
        }

        //为申明的异常，默认不可重试，直接抛出
        BinaryExceptionClassifier classifier = new BinaryExceptionClassifier(maps, true);
        classifier.setTraverseCauses(true);

        return new DefaultRetryState("testRetry", true, classifier);
    }


    private static class MyRetryListener extends RetryListenerSupport
    {
        @Override
        public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable)
        {
            if (context.getLastThrowable() == null)
            {
                LOGGER.info("在{}次重试后，正常结束", context.getRetryCount());
            }
            else
            {
                LOGGER.warn("在{}次重试失败后，异常结束", context.getRetryCount());
            }
        }


        @Override
        public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable)
        {
            LOGGER.error("第{}次重试失败", context.getRetryCount());
        }


        @Override
        public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback)
        {
            LOGGER.info("即将开始执行需重试的逻辑,getRetryCount：{}", context.getRetryCount());

            //返回 false，将终止
            return true;
        }
    }
}