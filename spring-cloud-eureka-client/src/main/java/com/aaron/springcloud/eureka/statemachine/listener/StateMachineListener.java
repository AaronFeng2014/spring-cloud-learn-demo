package com.aaron.springcloud.eureka.statemachine.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * 基于注解的监听器
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-06-26
 */
@WithStateMachine
public class StateMachineListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineListener.class);


    @OnTransition (source = "WAITING_MANAGER_APPROVED", target = "END")
    public void submit()
    {
        LOGGER.info("to end");
    }
}