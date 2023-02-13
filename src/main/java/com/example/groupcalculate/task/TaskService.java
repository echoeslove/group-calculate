package com.example.groupcalculate.task;

import com.example.groupcalculate.CalculateService;
import com.example.groupcalculate.definition.GroupDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

@Component
public class TaskService {

//    @Autowired
    private TaskRegisterConfig taskRegisterConfig;
    @Autowired
    private CalculateService calculateService;

    public void addTask(GroupDefinition definition, String cron) {
        ScheduledTask scheduledTask = taskRegisterConfig.getTaskRegistrar()
                .scheduleCronTask(new CronTask( () -> {
                    calculateService.calculateSingle(definition);
                }, cron));
    }

}
