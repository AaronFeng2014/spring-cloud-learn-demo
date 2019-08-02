package com.aaron.springcloud.eureka.statemachine.guard;

import com.aaron.springcloud.eureka.statemachine.leave.LeaveEventEnums;
import com.aaron.springcloud.eureka.statemachine.leave.LeaveStatusEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

/**
 * 根据返回确定是否要执行相应的action
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-06-26
 */
@Component
public class TeamLeaderGuard implements Guard<LeaveStatusEnums, LeaveEventEnums>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamLeaderGuard.class);


    @Override
    public boolean evaluate(StateContext<LeaveStatusEnums, LeaveEventEnums> context)
    {
        LOGGER.info("team leader前置判断：{}", context);

        int id = (int)context.getMessageHeader("orderId");

        return id % 2 == 0;
    }
}
