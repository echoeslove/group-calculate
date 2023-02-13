package com.example.groupcalculate.definition;

import lombok.Data;

import java.util.List;

@Data
public class CalculateResultDefinition {

    private String key;
    private String dataVersion;
    private List<IndexResultDefinition> calResult;
}
