package com.example.grpcexample.client;

import com.example.grpcexample.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
@Log4j
public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello world!");
        ArrayList<Long> readIdList = new ArrayList<>();
        readIdList.add(1L);
        readIdList.add(2L);
        Long readQuota = 50L;
        Long writeQuota = 50L;
        ArrayList<Long> writeIdList = new ArrayList<>();
        writeIdList.add(1L);
        writeIdList.add(2L);

        int threadCount = 5;

        BalanceServiceGrpc.BalanceServiceBlockingStub stub = getRpcChannel();
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            new Thread(() -> {
                while (true) {
                    log.info("number 3td " + finalI);
                    double readProbability = (double) readQuota / (double) (readQuota + writeQuota);

                    if (ThreadLocalRandom.current().nextDouble() < readProbability) {
                        log.info("Take getBalance " + getBalance(stub, (randomFromList(readIdList))));
                    } else {
                        log.info("Take changeBalance " + changeBalance(stub, randomFromList(writeIdList), 1L));
                    }
                }
            }).start();
        }
    }

    private static Long randomFromList(ArrayList<Long> list) {
        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }

    private static GetBalanceResponse getBalance(BalanceServiceGrpc.BalanceServiceBlockingStub stub, Long id) {
        return stub.getBalance(GetBalanceRequest.newBuilder()
                .setUserId(id)
                .build());
    }

    private static ChangeBalanceResponse changeBalance(BalanceServiceGrpc.BalanceServiceBlockingStub stub, Long id, Long amount) {
        return stub.changeBalance(ChangeBalanceRequest.newBuilder()
                .setUserId(id)
                .setAmount(amount)
                .build());
    }

    private static BalanceServiceGrpc.BalanceServiceBlockingStub getRpcChannel() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        return BalanceServiceGrpc.newBlockingStub(channel);
    }
}


