package com.aaron.springcloud.service.impl;

import com.aaron.springcloud.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019/12/18
 */
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService
{

    private TransactionSynchronization afterCommitCallback = new TransactionSynchronizationAdapter()
    {
        @Override
        public void afterCommit()
        {
            log.info("afterCommit回调执行，当前事物：{}", TransactionSynchronizationManager.getCurrentTransactionName());
            retrieve();
        }
    };

    private TransactionSynchronization afterCompletionCallback = new TransactionSynchronizationAdapter()
    {
        @Override
        public void afterCompletion(int status)
        {
            log.info("afterCompletion回调执行，当前事物：{}", TransactionSynchronizationManager.getCurrentTransactionName());
            retrieve();
        }
    };


    @Override
    @Transactional (rollbackFor = Exception.class)
    public void update()
    {
        TransactionSynchronizationManager.registerSynchronization(afterCommitCallback);
    }


    @Override
    public void retrieve()
    {

    }
}
