package com.example.grpcexample.statistics;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Log4j2
@Component
public class Statistic {

    @Value("10000")
    private int inMillisecond;

    private final AtomicInteger countGet = new AtomicInteger();
    private final AtomicInteger countPost = new AtomicInteger();

    @Around("@annotation(CounterGet)")
    public Object incrementGet(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            countGet.incrementAndGet();
        }
    }

    @Around("@annotation(CounterPost)")
    public Object incrementPost(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            countPost.incrementAndGet();
        }
    }

    @Scheduled(fixedRateString = "10000")
    public void statisticOutput() {
        log.info("Count getBalance = {}. Count changeBalance = {}. In {} seconds", countGet.get(), countPost.get(), inMillisecond / 1000);
        countGet.set(0);
        countPost.set(0);
    }
}
