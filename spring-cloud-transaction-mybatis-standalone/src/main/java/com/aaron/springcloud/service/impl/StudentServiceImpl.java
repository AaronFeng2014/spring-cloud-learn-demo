package com.aaron.springcloud.service.impl;

import com.aaron.springcloud.dao.StudentDao;
import com.aaron.springcloud.model.po.Classes;
import com.aaron.springcloud.model.po.Student;
import com.aaron.springcloud.service.ClassesService;
import com.aaron.springcloud.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019/12/18
 */
@Slf4j
@Service
public class StudentServiceImpl implements StudentService
{
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private ClassesService classesService;


    @Override
    @Transactional (rollbackFor = Exception.class)
    public Long update(Student student, Integer callbackType)
    {
        studentDao.updateByPrimaryKeySelective(student);

        registerCallback(student, callbackType);

        return student.getId();
    }


    @Override
    @Transactional (rollbackFor = Exception.class)
    public Long getUpdate(Student student, Integer callbackType)
    {
        studentDao.updateByPrimaryKeySelective(student);

        registerCallback2(student, callbackType);

        return null;
    }


    private void retrieve(long id)
    {
        Student student;
        while ((student = studentDao.selectByPrimaryKey(id)).getStatus() != 1)
        {
            try
            {
                log.info("查询状态，最新状态：{}", student.getStatus());
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException e)
            {
                log.error("sleep error", e);
            }
        }
        log.info("更新成功了");
    }


    private void registerCallback2(Student student, Integer callbackType)
    {
        if (callbackType == 1)
        {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
            {
                @Override
                public void afterCommit()
                {
                    log.info("afterCommit,当前事物：{}", TransactionSynchronizationManager.getCurrentTransactionName());
                    updateClass();
                }
            });

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
            {
                @Override
                public void afterCommit()
                {
                    log.info("第二个afterCommit,当前事物：{}", TransactionSynchronizationManager.getCurrentTransactionName());
                }
            });
        }
        else if (callbackType == 2)
        {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
            {

                @Override
                public void afterCompletion(int status)
                {
                    log.info("afterCompletion,当前事物：{}", TransactionSynchronizationManager.getCurrentTransactionName());
                    if (TransactionSynchronization.STATUS_COMMITTED == status)
                    {
                        updateClass();
                    }
                }
            });
        }
    }


    private void updateClass()
    {
        Classes classes = new Classes();
        classes.setId(1L);
        classes.setName("2");
        classes.setStatus(0);
        classesService.update(classes);
    }


    private void registerCallback(Student student, Integer callbackType)
    {
        if (callbackType == 1)
        {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
            {
                @Override
                public void afterCommit()
                {
                    log.info("afterCommit,当前事物：{}", TransactionSynchronizationManager.getCurrentTransactionName());
                    retrieve(student.getId());
                }
            });
        }
        else if (callbackType == 2)
        {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
            {

                @Override
                public void afterCompletion(int status)
                {
                    log.info("afterCompletion,当前事物：{}", TransactionSynchronizationManager.getCurrentTransactionName());

                    if (TransactionSynchronization.STATUS_COMMITTED == status)
                    {
                        retrieve(student.getId());
                    }
                }
            });
        }
    }
}
