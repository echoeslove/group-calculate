package com.example.groupcalculate;

import com.example.groupcalculate.definition.ItemDefinition;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class HqService {

    public Map<String, ItemDefinition> getPreHq() {
        Map<String, ItemDefinition> resultMap = new HashMap<>();
        long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        for (String code : MetaHqData.CODE_LIST) {
            Map<String, Double> quote = new HashMap<>();
            quote.put("6", RandomUtils.nextDouble(99, 100));
            quote.put("11", RandomUtils.nextDouble(99, 100));
            ItemDefinition itemDefinition = new ItemDefinition("33:" + code, quote, timestamp);
            resultMap.put(itemDefinition.getKey(), itemDefinition);
        }
        return resultMap;
    }

    public Map<String, Map<String, ItemDefinition>> getPreHqLimitDate() {
        Map<String, Map<String, ItemDefinition>> resultMap = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            resultMap.put(String.valueOf(i), this.getPreHq());
        }

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resultMap;
    }
}
