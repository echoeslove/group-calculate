package com.example.groupcalculate.init;

import com.example.groupcalculate.MetaHqData;
import com.example.groupcalculate.definition.CodeListDefinition;
import com.example.groupcalculate.definition.GroupDefinition;
import com.example.groupcalculate.task.TaskService;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
public class InitScheduleExecutorTask implements InitializingBean {

    @Getter
    private List<GroupDefinition> groupDefinitionList;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < 5_000_000; i++) {
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
        }

//        for (GroupDefinition groupDefinition : groupDefinitionList) {
//            taskService.addTask(groupDefinition, "0/5 * * * * ?");
//        }
        this.groupDefinitionList = groupDefinitionList;
    }

}
