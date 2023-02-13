package com.example.groupcalculate.task;

import com.example.groupcalculate.CalculateService;
import com.example.groupcalculate.definition.GroupDefinition;
import com.example.groupcalculate.init.InitScheduleExecutorTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

//@Configuration("TaskRegisterConfig")
public class TaskRegisterConfig implements SchedulingConfigurer {
    private ScheduledTaskRegistrar taskRegistrar;

//    @Autowired
    private InitScheduleExecutorTask initScheduleExecutorTask;
//    @Autowired
    private CalculateService calculateService;
//    @Autowired
    private MonitorTask monitorTask;

    /**
     * 定时任务名称与定时任务回调钩子  的关联关系容器
     */
    private Map<String, ScheduledFuture> nameToScheduledFuture = new ConcurrentHashMap<>();

    public ScheduledFuture getScheduledFuture(String name) {
        return nameToScheduledFuture.get(name);
    }

    public void addScheduledFuture(String name, ScheduledFuture scheduledFuture) {
        this.nameToScheduledFuture.put(name, scheduledFuture);
    }

    public ScheduledTaskRegistrar getTaskRegistrar() {
        return taskRegistrar;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.taskRegistrar = scheduledTaskRegistrar;
        taskRegistrar.setScheduler(taskScheduler());
        monitorTask.init(initScheduleExecutorTask.getGroupDefinitionList());
        for (GroupDefinition groupDefinition : initScheduleExecutorTask.getGroupDefinitionList()) {
            taskRegistrar.scheduleCronTask(new CronTask(() -> {
                calculateService.calculateSingle(groupDefinition);
            }, "0/5 * * * * ?"));
        }
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.setThreadNamePrefix("task-thread-");
        taskScheduler.setWaitForTasksToCompleteOnShutdown(false);
        taskScheduler.setAwaitTerminationSeconds(0);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
