package com.example.groupcalculate.definition;

import lombok.Data;

@Data
public class IndexResultDefinition {

    private String indexName;

    private String indexUnit;

    private String indexVal;

    public IndexResultDefinition(String indexName, String indexUnit, String indexVal) {
        this.indexName = indexName;
        this.indexUnit = indexUnit;
        this.indexVal = indexVal;
    }
}
