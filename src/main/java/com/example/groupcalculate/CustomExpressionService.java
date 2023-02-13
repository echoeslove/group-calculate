package com.example.groupcalculate;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CustomExpressionService {

    public static void main(String[] args) {
        Expression exp = AviatorEvaluator.getInstance()
                .compile("(a+b-c)/d", true);
        Map<String, Object> param = new HashMap<>();
        param.put("a", BigDecimal.valueOf(10));
        param.put("b", BigDecimal.valueOf(2));
        param.put("c", BigDecimal.valueOf(5));
        param.put("d", BigDecimal.valueOf(3));
        BigDecimal result = (BigDecimal) exp.execute(param);
        System.out.println(result.setScale(4, RoundingMode.HALF_UP));
    }
}
