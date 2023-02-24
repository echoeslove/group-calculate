package com.example.groupcalculate.controller;

import com.example.groupcalculate.CalculateService;
import com.example.groupcalculate.MetaHqData;
import com.example.groupcalculate.definition.CodeListDefinition;
import com.example.groupcalculate.definition.GroupDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private CalculateService calculateService;

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestParam("size") Integer size, @RequestParam("loop") Integer loop) {
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


        long[] res = new long[loop];
        for (int i = 0; i < loop; i++) {
            long result = calculateService.calculateMulti(groupDefinitionList);
            res[i] = result;
        }

        log(res);
        return ResponseEntity.ok(Arrays.toString(res));
    }

    @GetMapping("/test2")
    public ResponseEntity<String> test2(@RequestParam("size") Integer size, @RequestParam("loop") Integer loop) {
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


        long[] res = new long[loop];
        for (int i = 0; i < loop; i++) {
            long result = calculateService.calculateMulti10(groupDefinitionList);
            res[i] = result;
        }

        log(res);
        return ResponseEntity.ok(Arrays.toString(res));
    }

    private void log(long[] res) {
        Arrays.sort(res);
        log.info("50, {}", res[(int) Math.round(res.length * 0.5) - 1]);
        log.info("80, {}", res[(int) Math.round(res.length * 0.8) - 1]);
        log.info("90, {}", res[(int) Math.round(res.length * 0.9) - 1]);
        log.info("avg: {}", Arrays.stream(res).average().orElse(Double.NaN));
    }
}
