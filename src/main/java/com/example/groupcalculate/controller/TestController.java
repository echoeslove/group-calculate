package com.example.groupcalculate.controller;

import com.example.groupcalculate.CalculateService;
import com.example.groupcalculate.MetaHqData;
import com.example.groupcalculate.definition.CodeListDefinition;
import com.example.groupcalculate.definition.GroupDefinition;
import com.example.groupcalculate.task.MonitorTask;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    @Autowired
    private CalculateService calculateService;
    @Autowired
    private MonitorTask monitorTask;

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestParam("size") Integer size) {
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
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

        long result = calculateService.calculateMulti(groupDefinitionList);

        return ResponseEntity.ok(String.valueOf(result));
    }

    @GetMapping("/test2")
    public ResponseEntity<String> test2(@RequestParam("size") Integer size) {
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
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

        long result = calculateService.calculateMulti10(groupDefinitionList);

        return ResponseEntity.ok(String.valueOf(result));
    }
}
