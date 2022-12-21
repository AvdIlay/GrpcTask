package com.example.grpcexample.repository;


import com.example.grpcexample.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Transactional
    @Query("update Account a set a.balance = a.balance + :amount where a.id = :id")
    void updateAmount(Long amount, Long id);
    @Transactional
    @Query("select a.balance from  Account a where a.id = :id")
    Optional<Long> getBalance(Long id);
}
