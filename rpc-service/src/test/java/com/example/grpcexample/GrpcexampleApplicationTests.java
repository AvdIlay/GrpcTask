//package com.example.grpcexample;
//
//import com.example.grpcexample.repository.BalanceRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.concurrent.LinkedBlockingDeque;
//
//@SpringBootTest
//class GrpcexampleApplicationTests {
//
//	@Autowired
//	private BalanceServiceGrpc balanceServiceGrpc;
//	@Autowired
//	private BalanceRepository balanceRepository;
//
//	@Test
//	void getBalance() throws ProjectException {
//		User user = new User();
//		userDao.save(user);
//
//		Assertions.assertEquals(0, balanceService.getBalance(user.getId()).get());
//	}
//
//	@Test
//	void changeBalanceOneThread() throws ProjectException {
//		User user = new User();
//		userDao.save(user);
//
//		long sum = 0;
//		for (int i = 0; i < 10; i++) {
//			balanceService.changeBalance(user.getId(), 10L);
//			sum += 10L;
//			Assertions.assertEquals(sum, balanceService.getBalance(user.getId()).get());
//		}
//	}
//
//	@Test
//	void changeBalanceMultiThread() throws ProjectException, InterruptedException {
//		User user = new User();
//		userDao.save(user);
//		Long id = user.getId();
//		int countThreads = 10;
//
//
//		LinkedBlockingDeque<Boolean> blockingQueue = new LinkedBlockingDeque<>();
//
//		for (int i = 0; i < countThreads; i++) {
//			Thread thread = new Thread(() -> {
//				changeBalance(id, 10L);
//				blockingQueue.addLast(true);
//			});
//			thread.start();
//		}
//
//		for (int i = 0; i < countThreads; i++) {
//			blockingQueue.takeFirst();
//		}
//
//		Assertions.assertEquals(100, balanceService.getBalance(id).get());
//	}
//
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	public void changeBalance(Long id, Long amount) {
//		try {
//			balanceService.changeBalance(id, amount);
//		} catch (ProjectException e) {
//			System.out.println(Thread.currentThread().getName());
//		}
//	}
//
//
//}
