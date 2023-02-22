package com.example.groupcalculate.controller;

import com.example.groupcalculate.CalculateService;
import com.example.groupcalculate.definition.CalculateResultDefinition;
import com.example.groupcalculate.definition.GroupDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RealTimeController {

    @Autowired
    private CalculateService calculateService;

    @PostMapping("/cal")
    public List<CalculateResultDefinition> calculate(@RequestBody GroupDefinition definition) {
        return calculateService.calculateSingle(definition);
    }
}
