package com.example.groupcalculate.controller;

import com.example.groupcalculate.MetaHqData;
import com.example.groupcalculate.definition.CodeListDefinition;
import com.example.groupcalculate.definition.GroupDefinition;
import com.example.groupcalculate.task.TaskService;
import com.example.groupcalculate.task.TimeWheelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TimeWheelService timeWheelService;

    @PutMapping("/add/task")
    public void addTask(@RequestBody GroupDefinition definition) {
        taskService.addTask(definition, "0/2 * * * * ?");
    }

    @PutMapping("/add/task2")
    public void addTask2(@RequestBody GroupDefinition definition) {
        timeWheelService.addTask(definition.getKey());
    }

    @PutMapping("/cancel/task2")
    public void cancelTask2(@RequestBody GroupDefinition definition) {
        timeWheelService.cancelTask(definition.getKey());
    }

    @GetMapping("/get/v1")
    public void test() {
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            List<CodeListDefinition> codeList = new ArrayList<>();
            for (int j = 0; j < MetaHqData.CODE_LIST.size(); j++) {
                CodeListDefinition codeListDefinition = new CodeListDefinition();
                codeListDefinition.setMarket("33");
                codeListDefinition.setCode(MetaHqData.CODE_LIST.get(j));
                codeList.add(codeListDefinition);
            }
            GroupDefinition group = new GroupDefinition();
            group.setKey("key" + i);
            group.setCodelist(codeList);
            groupDefinitionList.add(group);

            taskService.addTask(group, "0/2 * * * * ?");
        }
    }
}
