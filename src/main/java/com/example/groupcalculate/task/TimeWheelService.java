package com.example.groupcalculate.task;

import com.example.groupcalculate.CalculateService;
import com.example.groupcalculate.definition.GroupDefinition;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TimeWheelService {

    @Autowired
    private CalculateService calculateService;

    private HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(1, TimeUnit.SECONDS, 60);

    private ConcurrentHashMap<String, Timeout> timeoutMap = new ConcurrentHashMap<>();

    public void addTask(String name) {
        Timeout timeout = hashedWheelTimer.newTimeout(t -> {
            log.info("current task : [{}], map size [{}]", name, timeoutMap.size());
            this.callback(name, t.task());
        }, 5, TimeUnit.SECONDS);
        timeoutMap.put(name, timeout);
        log.info("put name [{}], hashcode: [{}]", name, Integer.toHexString(timeout.hashCode()));

    }
    public void addGroupTask(GroupDefinition group) {
        Timeout timeout = hashedWheelTimer.newTimeout(t -> {
//            log.info("current task : [{}], map size [{}]", group.getKey(), timeoutMap.size());
//            log.info("map size [{}]",  timeoutMap.size());
//            calculateService.submitTask(group);
            calculateService.submitTask(group);
            this.callback(group.getKey(), t.task());
        }, 15, TimeUnit.SECONDS);
        timeoutMap.put(group.getKey(), timeout);
//        log.info("put name [{}], hashcode: [{}]", group.getKey(), Integer.toHexString(timeout.hashCode()));
    }

    private void callback(String name, TimerTask task) {
        Timeout timeout = hashedWheelTimer.newTimeout(task, 15, TimeUnit.SECONDS);
        timeoutMap.put(name, timeout);
//        log.info("put name [{}], hashcode: [{}]", name, Integer.toHexString(timeout.hashCode()));
    }

    public void cancelTask(String name) {
        Timeout timeout = timeoutMap.get(name);
        if (null == timeout) {
            log.warn("timeout not exist [{}]", name);
            return;
        }

        log.info("cancel name [{}], hashcode: [{}]", name, Integer.toHexString(timeout.hashCode()));
        timeout.cancel();
    }
}
