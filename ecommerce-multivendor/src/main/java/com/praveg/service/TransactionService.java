package com.praveg.service;

import com.praveg.entity.Order;
import com.praveg.entity.Seller;
import com.praveg.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Order order);

    List<Transaction> getTransactionsBySellerId(Seller seller);

    List<Transaction> getAllTransactions();
}
