package com.example.groupcalculate;

import com.example.groupcalculate.definition.*;
import com.example.groupcalculate.task.MonitorTask;
import lombok.extern.slf4j.Slf4j;
import org.HdrHistogram.Histogram;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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

        calculate(group);

        return resultList;
    }

    public List<CalculateResultDefinition> calculate(List<GroupDefinition> groupDefinitionList) {
//        log.info(Thread.currentThread().getName() + " - size [{}]", groupDefinitionList.size());
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
        List<CalculateResultDefinition> resultList = new ArrayList<>();
        for (GroupDefinition group : groupDefinitionList) {
            calculate(group);
            CalculateResultDefinition result = new CalculateResultDefinition();
//            result.setKey(group.getKey());
//            double total = group.getCodelist().stream()
//                    .mapToDouble(t -> metaHqData.getHqSnapshot()
//                            .get(t.getMarket() + ":" + t.getCode())
//                            .getQuote().get("10"))
//                    .sum();
//            double price = BigDecimal.valueOf(total)
//                    .divide(BigDecimal.valueOf(group.getCodelist().size()), 2, RoundingMode.HALF_UP)
//                    .doubleValue();
//            result.setCalResult(
//                    Arrays.asList(new IndexResultDefinition("now_price", "num", Double.toString(price))));
            resultList.add(result);
//            monitorTask.add(group.getKey());
        }

//        log.info(Thread.currentThread().getName() + " - cal time [{}]", stopWatch.getTime(TimeUnit.MILLISECONDS));
//        stopWatch.stop();
        return resultList;
    }

    public long calculateMulti2(List<GroupDefinition> groupDefinitionList) {
//        log.info(Thread.currentThread().getName() + " multi - size [{}]", groupDefinitionList.size());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CompletableFuture<CalculateResultDefinition>> futureList = new ArrayList<>();
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
//                monitorTask.add(group.getKey());
                return result;
            }, calculateThreadPool);
            futureList.add(future);
        }
        CompletableFuture<Void> all = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));

        long result;
        all.join();
        result = stopWatch.getTime(TimeUnit.MILLISECONDS);
        log.info(Thread.currentThread().getName() + " multi size [{}] - cal time [{}]", groupDefinitionList.size(), result);

        return result;
    }

    void calculate(GroupDefinition group) {
        CalculateResultDefinition result = new CalculateResultDefinition();
        result.setKey(group.getKey());

        double res = 0.0D;
        for (CodeListDefinition t : group.getCodelist()) {
            Map<String, Double> quote = metaHqData.getHqSnapshot()
                    .get(t.getMarket() + ":" + t.getCode()).getQuote();
            res += quote.get("11") / quote.get("12");
        }

//        group.getCodelist().stream().mapToDouble(t -> BigDecimal.valueOf(metaHqData.getHqSnapshot()
//                .get(t.getMarket() + ":" + t.getCode()).getQuote().get("11")).divide(BigDecimal.valueOf(metaHqData.getHqSnapshot()
//                .get(t.getMarket() + ":" + t.getCode()).getQuote().get("12")), 2, RoundingMode.HALF_UP).doubleValue()).sum();
    }

    public long calculateMulti(List<GroupDefinition> groupDefinitionList) {
        log.info(Thread.currentThread().getName() + " multi - size [{}]", groupDefinitionList.size());
        CountDownLatch countDownLatch = new CountDownLatch(groupDefinitionList.size());

        long startTime = System.currentTimeMillis();

        for (GroupDefinition group : groupDefinitionList) {
            calculateThreadPool.submit(() -> {
                calculate(group);
                countDownLatch.countDown();
            });
        }
        log.info(Thread.currentThread().getName() + " multi size [{}] - submiit time [{}]", groupDefinitionList.size(),
                System.currentTimeMillis() - startTime);


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long result = System.currentTimeMillis() - startTime;
        log.info(Thread.currentThread().getName() + " multi size [{}] - cal time [{}]", groupDefinitionList.size(), result);
        return result;
    }

    public long calculateMulti10(List<GroupDefinition> groupDefinitionList) {
        log.info(Thread.currentThread().getName() + " multi - size [{}]", groupDefinitionList.size());
        CountDownLatch countDownLatch = new CountDownLatch(groupDefinitionList.size());

        long startTime = System.currentTimeMillis();


        int split = 5;
        for (int i = 0; i < groupDefinitionList.size(); i = i + split) {

            int finalI = i;
            calculateThreadPool.submit(() -> {
                for (int j = 0; j < split; j++) {
                    calculate(groupDefinitionList.get(finalI));
                    countDownLatch.countDown();
                }
            });
        }

        log.info(Thread.currentThread().getName() + " multi size [{}] - submiit time [{}]", groupDefinitionList.size(),
                System.currentTimeMillis() - startTime);


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long result = System.currentTimeMillis() - startTime;
        log.info(Thread.currentThread().getName() + " multi size [{}] - cal time [{}]", groupDefinitionList.size(), result);
        return result;
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

    //    @Scheduled(fixedDelay = 1000)
    public void monitor() {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) calculateThreadPool;
        int queueSize = tpe.getQueue().size();
        log.info("当前排队线程数：" + queueSize);

        int activeCount = tpe.getActiveCount();
        log.info("当前活动线程数：" + activeCount);

        long completedTaskCount = tpe.getCompletedTaskCount();
        log.info("执行完成线程数：" + completedTaskCount);

        long taskCount = tpe.getTaskCount();
        log.info("总线程数：" + taskCount);

    }

}
