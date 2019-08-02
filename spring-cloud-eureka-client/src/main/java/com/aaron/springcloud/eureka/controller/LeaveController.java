package com.aaron.springcloud.eureka.controller;

import com.aaron.springcloud.eureka.statemachine.leave.LeaveEventEnums;
import com.aaron.springcloud.eureka.statemachine.leave.LeaveStatusEnums;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-06-26
 */
@RestController
public class LeaveController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveController.class);

    @Autowired
    private StateMachine<LeaveStatusEnums, LeaveEventEnums> stateMachine;

    @Autowired
    @Qualifier ("redisPersist")
    private StateMachinePersister<LeaveStatusEnums, LeaveEventEnums, String> stateMachinePersister;


    /**
     * 模拟提交请假申请
     *
     * @param id String：单据id
     *
     * @throws Exception
     */
    @RequestMapping ("submit/{id}")
    public void submit(@PathVariable ("id") String id) throws Exception
    {

        LOGGER.info("提交请假申请：{}", id);
        StateMachine<LeaveStatusEnums, LeaveEventEnums> machine = stateMachinePersister.restore(stateMachine, id);

        Message<LeaveEventEnums> event = MessageBuilder.withPayload(LeaveEventEnums.SUBMITTED)
                                                       .copyHeaders(ImmutableMap.of("orderId", Integer.valueOf(id)))
                                                       .build();

        sendEvent(id, event, machine);

    }


    /**
     * 模拟直接上级审批
     *
     * @param id String：单据id
     *
     * @throws Exception
     */
    @RequestMapping ("tmApprove/{id}")
    public void tmApprove(@PathVariable ("id") String id) throws Exception
    {

        LOGGER.info("team leader审批申请：{}", id);
        StateMachine<LeaveStatusEnums, LeaveEventEnums> machine = stateMachinePersister.restore(stateMachine, id);

        Message<LeaveEventEnums> event = MessageBuilder.withPayload(LeaveEventEnums.TEAM_LEADER_APPROVED)
                                                       .copyHeaders(ImmutableMap.of("orderId", Integer.valueOf(id)))
                                                       .build();

        sendEvent(id, event, machine);
    }


    /**
     * 模拟经理审批
     *
     * @param id String：单据id
     *
     * @throws Exception
     */
    @RequestMapping ("maApprove/{id}")
    public void maApprove(@PathVariable ("id") String id) throws Exception
    {

        LOGGER.info("manager审批申请：{}", id);
        StateMachine<LeaveStatusEnums, LeaveEventEnums> machine = stateMachinePersister.restore(stateMachine, id);

        Message<LeaveEventEnums> event = MessageBuilder.withPayload(LeaveEventEnums.MANAGER_APPROVED)
                                                       .copyHeaders(ImmutableMap.of("orderId", Integer.valueOf(id)))
                                                       .build();

        sendEvent(id, event, machine);
    }


    private void sendEvent(String id, Message<LeaveEventEnums> event, StateMachine<LeaveStatusEnums, LeaveEventEnums> machine)
            throws Exception
    {

        machine.sendEvent(event);

        stateMachinePersister.persist(machine, id);
    }
}
