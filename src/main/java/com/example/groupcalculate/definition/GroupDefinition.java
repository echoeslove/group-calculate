package com.example.groupcalculate.definition;

import lombok.Data;

import java.util.List;

@Data
public class GroupDefinition {
    private String key;
    private String sourceType;
    private List<String> calItem;
    private String calPeriod;
    private List<CodeListDefinition> codelist;
    private Long ptime;
}
