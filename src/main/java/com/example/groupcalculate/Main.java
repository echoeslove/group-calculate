package com.example.groupcalculate;

import com.example.groupcalculate.definition.CodeListDefinition;
import com.example.groupcalculate.definition.GroupDefinition;
import com.example.groupcalculate.definition.ItemDefinition;
import org.apache.commons.lang3.RandomUtils;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static void main(String[] args) {
        ConcurrentHashMap<String, ItemDefinition> map = new ConcurrentHashMap<>(100_000);
        for (int i = 0; i < 100_000; i++) {
            Map<String, Double> quote = new HashMap<>();
            quote.put("10", RandomUtils.nextDouble());
            quote.put("11", RandomUtils.nextDouble());
            quote.put("12", RandomUtils.nextDouble());
            quote.put("13", RandomUtils.nextDouble());
            quote.put("14", RandomUtils.nextDouble());
            quote.put("15", RandomUtils.nextDouble());
            quote.put("16", RandomUtils.nextDouble());
            quote.put("17", RandomUtils.nextDouble());
            quote.put("18", RandomUtils.nextDouble());
            quote.put("19", RandomUtils.nextDouble());
            ItemDefinition itemDefinition = new ItemDefinition("33:30003" + i, quote, new Date().getTime());
            map.put(itemDefinition.getKey(), itemDefinition);
        }
        System.out.println(RamUsageEstimator.humanSizeOf(map));

//        ConcurrentHashMap<String, GroupDefinition> groupDefinitionConcurrentHashMap = new ConcurrentHashMap<>();
//        for (int i = 0; i < 200_000; i++) {
//            List<CodeListDefinition> codeList = new ArrayList<>();
//            int s = RandomUtils.nextInt(80, MetaHqData.CODE_LIST.size());
//            for (int j = 0; j < s; j++) {
//                CodeListDefinition codeListDefinition = new CodeListDefinition();
//                codeListDefinition.setMarket("33");
//                codeListDefinition.setCode(MetaHqData.CODE_LIST.get(j));
//                codeList.add(codeListDefinition);
//            }
//            GroupDefinition group = new GroupDefinition();
//            group.setKey("key" + i);
//            group.setCodelist(codeList);
//            groupDefinitionConcurrentHashMap.put(group.getKey(), group);
//        }
//        System.out.println(RamUsageEstimator.humanSizeOf(groupDefinitionConcurrentHashMap));
    }
}
