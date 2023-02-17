package com.example.groupcalculate;

import com.example.groupcalculate.definition.CodeListDefinition;
import com.example.groupcalculate.definition.GroupDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class CalculateServiceTest {

    @Autowired
    private CalculateService calculateService;

    @Test
    void testJson() throws JsonProcessingException {
        List<CodeListDefinition> codeList = new ArrayList<>();
//            int s = RandomUtils.nextInt(80, MetaHqData.CODE_LIST.size());
        for (int j = 0; j < MetaHqData.CODE_LIST.size(); j++) {
            CodeListDefinition codeListDefinition = new CodeListDefinition();
            codeListDefinition.setMarket("33");
            codeListDefinition.setCode(MetaHqData.CODE_LIST.get(j));
            codeList.add(codeListDefinition);
        }
        GroupDefinition group = new GroupDefinition();
        group.setKey("key");
        group.setCodelist(codeList);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(group));
    }

    @Test
    void calculate() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            List<CodeListDefinition> codeList = new ArrayList<>();
//            int s = RandomUtils.nextInt(80, MetaHqData.CODE_LIST.size());
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

//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
        calculateService.calculate(groupDefinitionList);
        while (true) {
        }

//        System.out.println(stopWatch.getTime(TimeUnit.MILLISECONDS));

//        assertEquals(resultList.size(), groupDefinitionList.size());
    }

    @Test
    void testCalculatePre() {
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            List<CodeListDefinition> codeList = new ArrayList<>();
            int s = 50;
            for (int j = 0; j < s; j++) {
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

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (GroupDefinition groupDefinition : groupDefinitionList) {
            calculateService.calculatePre(groupDefinition);
            System.out.println(stopWatch.getTime(TimeUnit.MILLISECONDS));
        }
    }

    @Test
    void calculateMulti() throws InterruptedException {
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < 500_000; i++) {
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

//        calculateService.calculateMulti(groupDefinitionList.subList(0, 100));

//        TimeUnit.SECONDS.sleep(10);

        calculateService.calculateMulti(groupDefinitionList);
    }

    @Test
    public void testMulti() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(10);
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
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

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        for (int i = 0; i < 10; i++) {
            calculateService.calculateMulti2(groupDefinitionList);
//        }
        System.out.println(stopWatch.getTime(TimeUnit.MILLISECONDS));
    }

}