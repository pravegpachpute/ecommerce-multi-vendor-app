package com.praveg.repository;

import com.praveg.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySellerId(Long sellerId);
}
