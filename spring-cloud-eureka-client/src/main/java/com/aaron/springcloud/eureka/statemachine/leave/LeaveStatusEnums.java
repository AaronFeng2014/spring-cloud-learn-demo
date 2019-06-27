package com.aaron.springcloud.eureka.statemachine.leave;

import lombok.Getter;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-06-26
 */
@Getter
public enum LeaveStatusEnums
{
    /**
     * 等待提交
     */
    WAITING_SUBMIT,

    /**
     * 等待直接上级审批
     */
    WAITING_TEAM_LEADER_APPROVED,

    /**
     * 等待经理审批
     */
    WAITING_MANAGER_APPROVED,

    END

}
