package com.aaron.springcloud.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019/12/18
 */
public interface TransactionService
{
    @Transactional
    void update();


    void retrieve();
}
