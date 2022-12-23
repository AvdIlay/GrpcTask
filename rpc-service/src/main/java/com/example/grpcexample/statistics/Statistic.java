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

    private final AtomicInteger countGetBalance = new AtomicInteger();
    private final AtomicInteger countChangeBalance = new AtomicInteger();

    @Around("@annotation(CounterGetBalance)")
    public Object incrementGet(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            countGetBalance.incrementAndGet();
        }
    }

    @Around("@annotation(CounterChangeBalance)")
    public Object incrementPost(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            countChangeBalance.incrementAndGet();
        }
    }

    @Scheduled(fixedRateString = "10000")
    public void statisticOutput() {
        log.info("Count getBalance = {}. Count changeBalance = {}. In {} seconds", countGetBalance.get(), countChangeBalance.get(), inMillisecond / 1000);
        countGetBalance.set(0);
        countChangeBalance.set(0);
    }

    @Scheduled(fixedRateString = "5000")
    public void statisticOutAllPut(){
        log.info("Count 2methods  = {}.", countChangeBalance.get() + countGetBalance.get());
        countGetBalance.set(0);
        countChangeBalance.set(0);
    }
}
