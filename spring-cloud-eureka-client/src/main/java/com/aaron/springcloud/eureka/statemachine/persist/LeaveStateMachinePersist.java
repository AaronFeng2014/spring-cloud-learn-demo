package com.aaron.springcloud.eureka.statemachine.persist;

import com.aaron.springcloud.eureka.statemachine.leave.LeaveEventEnums;
import com.aaron.springcloud.eureka.statemachine.leave.LeaveStatusEnums;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 状态机状态的持久化，应为状态机实例是共用了
 * <p>
 * 因此可以根据id来保存或者获取状态机的当前状态
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-06-26
 */
@Component
public class LeaveStateMachinePersist implements StateMachinePersist<LeaveStatusEnums, LeaveEventEnums, String>
{

    private HashMap<String, StateMachineContext<LeaveStatusEnums, LeaveEventEnums>> cache = new HashMap<>();


    @Override
    public void write(StateMachineContext<LeaveStatusEnums, LeaveEventEnums> context, String contextObj) throws Exception
    {
        cache.put(contextObj, context);
    }


    @Override
    public StateMachineContext<LeaveStatusEnums, LeaveEventEnums> read(String contextObj) throws Exception
    {
        return new DefaultStateMachineContext<>(LeaveStatusEnums.WAITING_TEAM_LEADER_APPROVED, LeaveEventEnums.SUBMITTED, null, null);
    }
}
