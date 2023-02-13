package com.example.groupcalculate;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class AviatorScriptTest {

    @Test
    public void test() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AtomicInteger ai = new AtomicInteger(0);
        while (stopWatch.getTime(TimeUnit.MILLISECONDS) <= 1000) {
            Expression exp = AviatorEvaluator.getInstance()
                    .compile("(a+b-c)/d", true);
            Map<String, Object> param = new HashMap<>();
            param.put("a", BigDecimal.valueOf(10));
            param.put("b", BigDecimal.valueOf(2));
            param.put("c", BigDecimal.valueOf(5));
            param.put("d", BigDecimal.valueOf(3));
            BigDecimal result = (BigDecimal) exp.execute(param);
//            System.out.println(result.setScale(4, RoundingMode.HALF_UP));
            ai.getAndIncrement();
        }

        System.out.println(ai.get());
    }

    @Test
    public void test2(){
        String s = "123";
        String s2 = "12340000001230";
        System.out.println(s.hashCode());
        System.out.println(s2.hashCode());
    }
}
