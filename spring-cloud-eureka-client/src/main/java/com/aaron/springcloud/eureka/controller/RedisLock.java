package com.aaron.springcloud.eureka.controller;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 * <p>
 * <p>
 * FIXME 改写成Lua脚本，原子执行多条语句
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2018-12-12
 */
@Component
public final class RedisLock implements InitializingBean
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    private static final long DEFAULT_ACQUIRE_TIMEOUT = 500L;

    private static final long LOCK_KEY_TIMEOUT = 10000L;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private DefaultRedisScript<Long> lockScript;

    private DefaultRedisScript<Long> releaseLockScript;


    private RedisLock()
    {

    }


    /**
     * redis分布式锁，支持重入特性
     *
     * @return 获取锁成功时返回true，否则返回false
     */
    public boolean lock(String lockKey)
    {
        return lock(lockKey, DEFAULT_ACQUIRE_TIMEOUT, LOCK_KEY_TIMEOUT);
    }


    /**
     * redis分布式锁，支持重入特性，支持不同线程获取
     *
     * @param lockKey String：生成锁的资源id
     * @param acquireTimeout Long：获取锁这一动作的超时时间，单位是毫秒，超过时间还没有获取到锁，将返回false
     * @param timeout Long：锁的超时时间，单位是毫秒，超过这个时间自动释放锁
     *
     * @return 获取成功返回true，其他返回false
     */
    public boolean lock(String lockKey, long acquireTimeout, long timeout)
    {

        Assert.isTrue(acquireTimeout > 0, "获取所的超时时间需大于0");
        Assert.isTrue(timeout > 0, "锁的过期时间需大于0");
        long currentTimeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() < currentTimeMillis + acquireTimeout)
        {

            Long execute = redisTemplate.execute(lockScript,
                                                 ImmutableList.of(lockKey),
                                                 Thread.currentThread().getId(),
                                                 System.currentTimeMillis(),
                                                 System.currentTimeMillis() + timeout);

            if (execute != null && execute == 0)
            {
                return true;
            }

            try
            {
                TimeUnit.MILLISECONDS.sleep(50);
            }
            catch (InterruptedException e)
            {
                return false;
            }
        }

        return false;
    }


    /**
     * 释放锁
     *
     * @param lockKey String：生成锁的资源id
     */
    public void releaseLock(String lockKey)
    {
        try
        {
            Long execute = redisTemplate.execute(releaseLockScript,
                                                 ImmutableList.of(lockKey),
                                                 Thread.currentThread().getId(),
                                                 System.currentTimeMillis());

            if (execute == null || execute == 1)
            {
                throw new IllegalMonitorStateException("锁异常");
            }
        }
        catch (Exception e)
        {
            LOGGER.error("释放锁失败，lockKey：{}", lockKey);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception
    {

        redisTemplate.setValueSerializer(new RedisSerializer<Long>()
        {
            @Override
            public byte[] serialize(Long aLong) throws SerializationException
            {

                return String.valueOf(aLong).getBytes();

            }


            @Override
            public Long deserialize(byte[] bytes) throws SerializationException
            {
                return Long.valueOf(new String(bytes));
            }
        });
        lockScript = new DefaultRedisScript<>();
        lockScript.setResultType(Long.class);
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis.lua")));

        releaseLockScript = new DefaultRedisScript<>();
        releaseLockScript.setResultType(Long.class);
        releaseLockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("release_lock.lua")));
    }
}