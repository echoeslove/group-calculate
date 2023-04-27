package com.example.groupcalculate.test;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class KLineMatcher {

    public static Map<String, KLineMatchResult> match2(Map<String, List<StockCandleData>> map, double[][] klineP, double limit) {
        Map<String, KLineMatchResult> result = new HashMap<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        for (Map.Entry<String, List<StockCandleData>> e : map.entrySet()) {
            String key = e.getKey();
            List<StockCandleData> value = e.getValue();

            int tail = value.size();
            int head = tail - klineP.length;
            while (head > 0) {
                List<StockCandleData> subList = value.subList(head, tail);
                double[] highArr = new double[klineP.length];
                double[] lowArr = new double[klineP.length];
                double[] curHigh = new double[klineP.length];
                double[] curLow = new double[klineP.length];
                for (int i = 0; i < klineP.length; i++) {
                    curHigh[i] = klineP[i][0];
                    curLow[i] = klineP[i][1];
                    highArr[i] = subList.get(i).high;
                    lowArr[i] = subList.get(i).low;
                }

                double cal = pearsonsCorrelation.correlation(curHigh, highArr);
                double cal2 = pearsonsCorrelation.correlation(curLow, lowArr);
                if (!Double.isNaN(cal)
                        && !Double.isNaN(cal2)
                        && Double.compare(cal, limit) > 0
                        && Double.compare(cal2, limit) > 0) {
                    KLineMatchResult kLineMatchResult = new KLineMatchResult();
                    kLineMatchResult.list = subList;
                    kLineMatchResult.calList = Arrays.asList(cal, cal2);
                    result.put(key, kLineMatchResult);
                    break;
                }

                // index operation
                tail--;
                head = tail - klineP.length;
            }
        }

        System.out.println(stopWatch.getTime());
        stopWatch.stop();
        System.out.println("map: " + map.size());
        System.out.println("match: " + result.size());

        return result;
    }
}
