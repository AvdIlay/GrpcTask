package com.example.grpcexample.statistic;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Log4j2
@Component
@Profile("!test")
public class Statistic {

    @Value("5000")
    private int perMillisecond;

    private final AtomicInteger countGet = new AtomicInteger();
    private final AtomicInteger countPost = new AtomicInteger();

    @Around("@annotation(CounterGet)")
    public Object incrementGet(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } finally {
            countGet.incrementAndGet();
        }
    }

    @Around("@annotation(CounterPost)")
    public Object incrementPost(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } finally {
            countPost.incrementAndGet();
        }
    }

    @Scheduled(fixedRateString = "${statistic.per.millisecond}")
    public void statisticOutput() {
        log.info("Count get balance = {}. Count change balance = {}. Per {} seconds", countGet.get(), countPost.get(), perMillisecond / 1000);
        countGet.set(0);
        countPost.set(0);
    }
}

