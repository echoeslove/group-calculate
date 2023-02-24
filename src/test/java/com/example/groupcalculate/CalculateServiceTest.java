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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SpringBootTest
class CalculateServiceTest {

    @Autowired
    private CalculateService calculateService;


    private void log(long[] res) {
        Arrays.sort(res);
        System.out.println(("50, " + res[(int) Math.round(res.length * 0.5) - 1]));
        System.out.println(("80, " + res[(int) Math.round(res.length * 0.8) - 1]));
        System.out.println(("90, " + res[(int) Math.round(res.length * 0.9) - 1]));
        System.out.println(("avg: " + Arrays.stream(res).average().orElse(Double.NaN)));
    }

    @Test
    void calculate() throws InterruptedException {
        List<GroupDefinition> groupDefinitionList = new ArrayList<>();
        for (int i = 0; i < 250_000; i++) {
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

        long[] res = new long[10];
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            calculateService.calculate(groupDefinitionList);
            res[i] = System.currentTimeMillis() - start;
        }

        log(res);
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

        TimeUnit.SECONDS.sleep(10);
        for (int i = 0; i < 10; i++) {
            calculateService.calculateMulti(groupDefinitionList);
        }
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