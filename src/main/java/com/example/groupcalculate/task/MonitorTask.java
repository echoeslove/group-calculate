package com.example.groupcalculate.task;

import com.example.groupcalculate.definition.GroupDefinition;
import com.google.common.util.concurrent.AtomicLongMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MonitorTask {

    private static AtomicLongMap<String> map = AtomicLongMap.create();

    private static LongAdder longAdder = new LongAdder();

    public void init(List<GroupDefinition> groupDefinitionList) {
//        for (GroupDefinition groupDefinition : groupDefinitionList) {
//            map.put(groupDefinition.getKey(), 0);
//        }
//        log.info("size {}", map.asMap().size());
    }

    public void add(String key) {
//        map.getAndIncrement(key);
        longAdder.increment();
    }


//    @Scheduled(fixedDelay = 1000L)
    public void print() throws InterruptedException {
//        Map<Long, Long> resultMap = map.asMap().entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));

        log.info("---------");
//        log.info("size {}", map.asMap().size());
//        resultMap.forEach(
//                (k, v) -> {
//                    log.info(String.format("cal times: %s, value: %s", k, v));
//
//                });

        log.info("size {}", longAdder.longValue());

    }
}
