package com.example.groupcalculate.init;

import com.example.groupcalculate.MetaHqData;
import com.example.groupcalculate.definition.CodeListDefinition;
import com.example.groupcalculate.definition.GroupDefinition;
import com.example.groupcalculate.task.MonitorTask;
import com.example.groupcalculate.task.TimeWheelService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitWheelTimerTask implements InitializingBean {
    @Autowired
    private TimeWheelService timeWheelService;
    @Autowired
    private MonitorTask monitorTask;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < 1000_000; i++) {
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

        monitorTask.init(groupDefinitionList);
        for (GroupDefinition groupDefinition : groupDefinitionList) {
            timeWheelService.addGroupTask(groupDefinition);
        }
    }
}
