package com.aaron.springcloud.eureka.statemachine.leave;

import lombok.Getter;

/**
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-06-26
 */
@Getter
public enum LeaveEventEnums
{
    /**
     * 申请已提交
     */
    SUBMITTED,

    /**
     * 申请已被直接上级审批
     */
    TEAM_LEADER_APPROVED,

    /**
     * 申请已被经理审批
     */
    MANAGER_APPROVED
}
