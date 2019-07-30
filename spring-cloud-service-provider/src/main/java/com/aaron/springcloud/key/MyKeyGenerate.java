package com.aaron.springcloud.key;

import io.shardingsphere.core.keygen.KeyGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * 分布式id获取的接口
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-07-29
 */
@Slf4j
public class MyKeyGenerate implements KeyGenerator
{
    private long id = 0L;


    @Override
    public Number generateKey()
    {
        log.info("获取id：{}", id);
        return id++;
    }
}
