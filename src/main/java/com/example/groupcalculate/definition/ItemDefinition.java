package com.example.groupcalculate.definition;

import lombok.Data;

import java.util.Map;

@Data
public class ItemDefinition {
    private String key;
    private Map<String, Double> quote;
    private Long ptime;

    public ItemDefinition(String key, Map<String, Double> quote, Long ptime) {
        this.key = key;
        this.quote = quote;
        this.ptime = ptime;
    }
}
