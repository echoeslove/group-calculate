package com.example.groupcalculate;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.NamedThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class MetaCalculateThreadPoolConfig {

    @Value("${thread.corepoolsize}")
    private Integer corePoolSize;

    @Value("${thread.maxpoolsize}")
    private Integer maxPoolSize;

    @Bean
    public ExecutorService calculateThreadPool() {
        log.info("thread pool config, corePoolSize = {}, maxPoolSize = {}", corePoolSize, maxPoolSize);
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("calculateThreedPool"),
                new ThreadPoolExecutor.AbortPolicy());
    }

}
