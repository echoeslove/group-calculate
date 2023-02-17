package com.example.groupcalculate;

import org.apache.lucene.util.NamedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class MetaCalculateThreadPoolConfig {

    @Bean
    public ExecutorService calculateThreadPool() {
        return new ThreadPoolExecutor(4, 4, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("calculateThreedPool"),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
