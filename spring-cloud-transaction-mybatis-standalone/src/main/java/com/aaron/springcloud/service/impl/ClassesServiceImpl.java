package com.aaron.springcloud.service.impl;

import com.aaron.springcloud.dao.ClassesDao;
import com.aaron.springcloud.model.po.Classes;
import com.aaron.springcloud.service.ClassesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019/12/18
 */
@Slf4j
@Service
public class ClassesServiceImpl implements ClassesService
{
    @Autowired
    private ClassesDao classesDao;


    @Override
    @Transactional (rollbackFor = Exception.class)
    public void update(Classes classes)
    {
        classesDao.updateByPrimaryKeySelective(classes);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
        {
            @Override
            public void afterCommit()
            {
                log.info("当前事物：{}", TransactionSynchronizationManager.getCurrentTransactionName());
            }
        });
    }
}
