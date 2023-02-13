package com.example.groupcalculate;

import com.example.groupcalculate.definition.*;
import com.example.groupcalculate.task.MonitorTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Map;

@Service
@Slf4j
public class CalculateService {

    @Autowired
    private MetaHqData metaHqData;

    @Autowired
    private ExecutorService calculateThreadPool;

    @Autowired
    private HqService hqService;
    @Autowired
    private MonitorTask monitorTask;

    public List<CalculateResultDefinition> calculateSingle(GroupDefinition group) {
//        log.info(Thread.currentThread().getName() + " - key [{}]", group.getKey());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CalculateResultDefinition> resultList = new ArrayList<>();

        CalculateResultDefinition result = new CalculateResultDefinition();
        result.setKey(group.getKey());
        double total = group.getCodelist().stream()
                .mapToDouble(t -> metaHqData.getHqSnapshot()
                        .get(t.getMarket() + ":" + t.getCode())
                        .getQuote().get("10"))
                .sum();
        double price = BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(group.getCodelist().size()), 2, RoundingMode.HALF_UP)
                .doubleValue();
        result.setCalResult(
                Arrays.asList(new IndexResultDefinition("now_price", "num", Double.toString(price))));
        resultList.add(result);


//        log.info(Thread.currentThread().getName() + " key [{}] - cal time [{}]", group.getKey(),
//                stopWatch.getTime(TimeUnit.MILLISECONDS));
        stopWatch.stop();
//        monitorTask.add(group.getKey());
        return resultList;
    }

    public List<CalculateResultDefinition> calculate(List<GroupDefinition> groupDefinitionList) {
//        log.info(Thread.currentThread().getName() + " - size [{}]", groupDefinitionList.size());
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
        List<CalculateResultDefinition> resultList = new ArrayList<>();
        for (GroupDefinition group : groupDefinitionList) {
            CalculateResultDefinition result = new CalculateResultDefinition();
            result.setKey(group.getKey());
            double total = group.getCodelist().stream()
                    .mapToDouble(t -> metaHqData.getHqSnapshot()
                            .get(t.getMarket() + ":" + t.getCode())
                            .getQuote().get("10"))
                    .sum();
            double price = BigDecimal.valueOf(total)
                    .divide(BigDecimal.valueOf(group.getCodelist().size()), 2, RoundingMode.HALF_UP)
                    .doubleValue();
            result.setCalResult(
                    Arrays.asList(new IndexResultDefinition("now_price", "num", Double.toString(price))));
            resultList.add(result);
//            monitorTask.add(group.getKey());
        }

//        log.info(Thread.currentThread().getName() + " - cal time [{}]", stopWatch.getTime(TimeUnit.MILLISECONDS));
//        stopWatch.stop();
        return resultList;
    }

    public List<CalculateResultDefinition> calculatePre(GroupDefinition group) {
        List<CalculateResultDefinition> resultList = new ArrayList<>();

        Map<String, Map<String, ItemDefinition>> preHq = hqService.getPreHqLimitDate();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        BigDecimal indexWeightPrice = BigDecimal.valueOf(1000D);
        for (Map.Entry<String, Map<String, ItemDefinition>> entry : preHq.entrySet()) {
            CalculateResultDefinition result = new CalculateResultDefinition();
            result.setKey(group.getKey());

            BigDecimal weightPriceTotal = BigDecimal.ZERO;
            for (CodeListDefinition codeListDefinition : group.getCodelist()) {
                Map<String, Double> quoteMap = entry.getValue()
                        .get(codeListDefinition.getMarket() + ":" + codeListDefinition.getCode()).getQuote();
                weightPriceTotal = weightPriceTotal.add(
                        BigDecimal.valueOf(quoteMap.get("11"))
                                .divide(BigDecimal.valueOf(quoteMap.get("6")), 6, RoundingMode.HALF_UP));
            }
            indexWeightPrice = weightPriceTotal.divide(BigDecimal.valueOf(group.getCodelist().size()), 6, RoundingMode.HALF_UP)
                    .multiply(indexWeightPrice);

            result.setCalResult(
                    Arrays.asList(new IndexResultDefinition("now_price", "num", indexWeightPrice.toPlainString())));
            resultList.add(result);
        }
        log.info(Thread.currentThread().getName() + " - cal time [{}]", stopWatch.getTime(TimeUnit.MILLISECONDS));
        stopWatch.stop();

        return resultList;
    }

    public void calculateMulti2(List<GroupDefinition> groupDefinitionList) {
//        log.info(Thread.currentThread().getName() + " multi - size [{}]", groupDefinitionList.size());
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        List<CompletableFuture<CalculateResultDefinition>> futureList = new ArrayList<>();
        for (GroupDefinition group : groupDefinitionList) {
            CompletableFuture<CalculateResultDefinition> future = CompletableFuture.supplyAsync(() -> {
                CalculateResultDefinition result = new CalculateResultDefinition();
                result.setKey(group.getKey());
                double total = group.getCodelist().stream()
                        .mapToDouble(t -> metaHqData.getHqSnapshot()
                                .get(t.getMarket() + ":" + t.getCode())
                                .getQuote().get("10"))
                        .sum();
                double price = BigDecimal.valueOf(total)
                        .divide(BigDecimal.valueOf(group.getCodelist().size()), 2, RoundingMode.HALF_UP)
                        .doubleValue();
                result.setCalResult(
                        Arrays.asList(new IndexResultDefinition("now_price", "num", Double.toString(price))));
                monitorTask.add(group.getKey());
                return result;
            }, calculateThreadPool);
//            futureList.add(future);
        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));
//        log.info(Thread.currentThread().getName() + " multi - cal time [{}]", stopWatch.getTime(TimeUnit.MILLISECONDS));
    }

    public void calculateMulti(List<GroupDefinition> groupDefinitionList) {
        log.info(Thread.currentThread().getName() + " multi - size [{}]", groupDefinitionList.size());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (GroupDefinition group : groupDefinitionList) {
            calculateThreadPool.execute(() -> {
                CalculateResultDefinition result = new CalculateResultDefinition();
                result.setKey(group.getKey());
                double total = group.getCodelist().stream()
                        .mapToDouble(t -> metaHqData.getHqSnapshot()
                                .get(t.getMarket() + ":" + t.getCode())
                                .getQuote().get("10"))
                        .sum();
                double price = BigDecimal.valueOf(total)
                        .divide(BigDecimal.valueOf(group.getCodelist().size()), 2, RoundingMode.HALF_UP)
                        .doubleValue();
//                result.setCalResult(
//                        Arrays.asList(new IndexResultDefinition("now_price", "num", Double.toString(price))));
//                return result;

                monitorTask.add(group.getKey());
            });
        }

        log.info(Thread.currentThread().getName() + " multi - cal time [{}]", stopWatch.getTime(TimeUnit.MILLISECONDS));

    }

    public void submitTask(GroupDefinition group) {
        calculateThreadPool.execute(() -> {
            CalculateResultDefinition result = new CalculateResultDefinition();
            result.setKey(group.getKey());
            double total = group.getCodelist().stream()
                    .mapToDouble(t -> metaHqData.getHqSnapshot()
                            .get(t.getMarket() + ":" + t.getCode())
                            .getQuote().get("10"))
                    .sum();
            double price = BigDecimal.valueOf(total)
                    .divide(BigDecimal.valueOf(group.getCodelist().size()), 2, RoundingMode.HALF_UP)
                    .doubleValue();
//                result.setCalResult(
//                        Arrays.asList(new IndexResultDefinition("now_price", "num", Double.toString(price))));
//                return result;
            monitorTask.add(group.getKey());
        });
    }

}
