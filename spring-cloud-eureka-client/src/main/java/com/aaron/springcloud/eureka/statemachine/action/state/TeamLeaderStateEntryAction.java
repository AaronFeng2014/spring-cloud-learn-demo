package com.aaron.springcloud.eureka.statemachine.action.state;

import com.aaron.springcloud.eureka.statemachine.leave.LeaveEventEnums;
import com.aaron.springcloud.eureka.statemachine.leave.LeaveStatusEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-06-27
 */
@Component
public class TeamLeaderStateEntryAction implements Action<LeaveStatusEnums, LeaveEventEnums>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamLeaderStateEntryAction.class);


    @Override
    public void execute(StateContext<LeaveStatusEnums, LeaveEventEnums> context)
    {
        LOGGER.info("流转到经理审批状态，state：{}", context);
    }
}
