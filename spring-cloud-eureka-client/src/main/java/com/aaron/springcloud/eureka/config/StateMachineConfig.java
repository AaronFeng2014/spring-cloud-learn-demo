package com.aaron.springcloud.eureka.config;

import com.aaron.springcloud.eureka.statemachine.action.state.StateInitAction;
import com.aaron.springcloud.eureka.statemachine.action.state.TeamLeaderStateEntryAction;
import com.aaron.springcloud.eureka.statemachine.action.transitions.ManagerApproveAction;
import com.aaron.springcloud.eureka.statemachine.action.transitions.SubmitAction;
import com.aaron.springcloud.eureka.statemachine.action.transitions.TeamLeaderApproveAction;
import com.aaron.springcloud.eureka.statemachine.guard.SubmitGuard;
import com.aaron.springcloud.eureka.statemachine.guard.TeamLeaderGuard;
import com.aaron.springcloud.eureka.statemachine.leave.LeaveEventEnums;
import com.aaron.springcloud.eureka.statemachine.leave.LeaveStatusEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineContextRepository;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.EnumSet;

/**
 * 状态机的配置
 *
 * @author FengHaixin
 * @description 一句话描述该文件的用途
 * @date 2019-06-26
 */
@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<LeaveStatusEnums, LeaveEventEnums>
{

    @Autowired
    private StateMachinePersist<LeaveStatusEnums, LeaveEventEnums, String> persist;

    @Autowired
    private SubmitAction submitHandler;

    @Autowired
    private TeamLeaderApproveAction teamLeaderApproveHandler;

    @Autowired
    private ManagerApproveAction managerApproveHandler;

    @Autowired
    private SubmitGuard submitGuard;

    @Autowired
    private TeamLeaderGuard teamLeaderGuard;

    @Autowired
    private StateInitAction stateInitAction;

    @Autowired
    private TeamLeaderStateEntryAction teamLeaderStateEntryAction;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * spring statemachine 持久化到本地内存中，适用于单机
     *
     * @return StateMachinePersister
     */
    @Bean ("localPersist")
    public StateMachinePersister<LeaveStatusEnums, LeaveEventEnums, String> stateMachinePersister()
    {
        return new DefaultStateMachinePersister<>(persist);
    }


    /**
     * spring statemachine持久化到redis中，适用于分布式环境中
     *
     * @return StateMachinePersister
     */
    @Bean ("redisPersist")
    public StateMachinePersister<LeaveStatusEnums, LeaveEventEnums, String> newStateMachinePersister()
    {
        StateMachineContextRepository<LeaveStatusEnums, LeaveEventEnums, StateMachineContext<LeaveStatusEnums, LeaveEventEnums>> repository = new RedisStateMachineContextRepository<LeaveStatusEnums, LeaveEventEnums>(
                redisConnectionFactory);

        RepositoryStateMachinePersist<LeaveStatusEnums, LeaveEventEnums> persist = new RepositoryStateMachinePersist<>(repository);

        return new DefaultStateMachinePersister<>(persist);
    }


    @Override
    public void configure(StateMachineConfigBuilder<LeaveStatusEnums, LeaveEventEnums> config) throws Exception
    {
        super.configure(config);
    }


    @Override
    public void configure(StateMachineModelConfigurer<LeaveStatusEnums, LeaveEventEnums> model) throws Exception
    {
        super.configure(model);
    }


    @Override
    public void configure(StateMachineConfigurationConfigurer<LeaveStatusEnums, LeaveEventEnums> config) throws Exception
    {
        super.configure(config);
    }


    @Override
    public void configure(StateMachineStateConfigurer<LeaveStatusEnums, LeaveEventEnums> states) throws Exception
    {
        //定义状态机包含的状态
        states.withStates()
              //定义初始状态，常用于需要初始化外部数据的情况
              .initial(LeaveStatusEnums.WAITING_SUBMIT, stateInitAction)
              .stateEntry(LeaveStatusEnums.WAITING_MANAGER_APPROVED, teamLeaderStateEntryAction)
              .states(EnumSet.allOf(LeaveStatusEnums.class));
    }


    @Override
    public void configure(StateMachineTransitionConfigurer<LeaveStatusEnums, LeaveEventEnums> transitions) throws Exception
    {
        /**
         * 定义状态转移关系，以及触发事件
         */
        transitions.withExternal()
                   .source(LeaveStatusEnums.WAITING_SUBMIT)
                   .target(LeaveStatusEnums.WAITING_TEAM_LEADER_APPROVED)
                   .event(LeaveEventEnums.SUBMITTED)
                   .guard(submitGuard)
                   .action(submitHandler)

                   .and()

                   .withExternal()
                   .source(LeaveStatusEnums.WAITING_TEAM_LEADER_APPROVED)
                   .target(LeaveStatusEnums.WAITING_MANAGER_APPROVED)
                   .event(LeaveEventEnums.TEAM_LEADER_APPROVED)
                   //guard 语法，用于判断是否接受
                   .guard(teamLeaderGuard)
                   .action(teamLeaderApproveHandler)

                   .and()

                   .withExternal()
                   .source(LeaveStatusEnums.WAITING_MANAGER_APPROVED)
                   .target(LeaveStatusEnums.END)
                   .action(managerApproveHandler)
                   .event(LeaveEventEnums.MANAGER_APPROVED);
    }
}
