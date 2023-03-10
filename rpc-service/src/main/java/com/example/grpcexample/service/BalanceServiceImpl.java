package com.example.grpcexample.service;

import com.example.grpcexample.*;
import com.example.grpcexample.repository.BalanceRepository;
import com.example.grpcexample.statistics.CounterChangeBalance;
import com.example.grpcexample.statistics.CounterGetBalance;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j
@GrpcService
public class BalanceServiceImpl extends BalanceServiceGrpc.BalanceServiceImplBase {

    @Autowired
    private final BalanceRepository balanceRepository;

    @Autowired
    private final ConcurrentHashMap<Long, Long> rpcHandler;

    private final Lock lock = new ReentrantLock();

    public void handleRequest(Long key, Long value) {
        lock.lock();
        try {
            if (rpcHandler.containsKey(key)) {
                rpcHandler.put(key, rpcHandler.get(key) + value);
            } else {
                rpcHandler.put(key, value);
            }
        } finally {
            lock.unlock();
        }
//        log.info("Handler state: " + rpcHandler);
    }

    public void handleUpdate(Long key) {
        lock.lock();
        try {
            if (rpcHandler.containsKey(key)) {
                balanceRepository.updateAmount(rpcHandler.get(key), key);
                rpcHandler.remove(key);
            }
        } finally {
            lock.unlock();
        }
    }

    public BalanceServiceImpl(BalanceRepository balanceRepository, ConcurrentHashMap<Long, Long> rpcHandler) {
        this.balanceRepository = balanceRepository;
        this.rpcHandler = rpcHandler;
    }

    @Async
    @Override
    @CounterGetBalance
    public void getBalance(GetBalanceRequest request, StreamObserver<GetBalanceResponse> responseObserver) {
        handleUpdate(request.getUserId());
        Long balance = balanceRepository.getBalance(request.getUserId()).orElse(0L);

        GetBalanceResponse response = GetBalanceResponse.newBuilder()
                .setAmount(balance)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Async
    @Override
    @CounterChangeBalance
    public void changeBalance(ChangeBalanceRequest request, StreamObserver<ChangeBalanceResponse> responseObserver) {
        handleRequest(request.getUserId(), request.getAmount());

        ChangeBalanceResponse response = ChangeBalanceResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}