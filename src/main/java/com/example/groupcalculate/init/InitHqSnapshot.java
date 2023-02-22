package com.example.groupcalculate.init;

import com.example.groupcalculate.MetaHqData;
import com.example.groupcalculate.definition.ItemDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InitHqSnapshot implements InitializingBean {

    @Autowired
    private MetaHqData metaHqData;

    @Override
    public void afterPropertiesSet() throws Exception {
        long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        for (String code : MetaHqData.CODE_LIST) {
            Map<String, Double> quote = new HashMap<>();
            quote.put("10", RandomUtils.nextDouble(0, 100000));
            quote.put("11", RandomUtils.nextDouble(0, 100000));
            quote.put("12", RandomUtils.nextDouble(0, 100000));
            quote.put("13", RandomUtils.nextDouble(0, 100000));
            quote.put("14", RandomUtils.nextDouble(0, 100000));
            ItemDefinition itemDefinition = new ItemDefinition("33:" + code, quote, timestamp);
            metaHqData.getHqSnapshot().put(itemDefinition.getKey(), itemDefinition);
        }

        log.info("init finish");
    }
}
