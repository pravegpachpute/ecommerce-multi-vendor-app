package com.praveg.service.impl;

import com.praveg.entity.Order;
import com.praveg.entity.Seller;
import com.praveg.entity.Transaction;
import com.praveg.repository.SellerRepo;
import com.praveg.repository.TransactionRepo;
import com.praveg.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final SellerRepo sellerRepo;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepo.findById(order.getSellerId()).get();

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);
        return transactionRepo.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return transactionRepo.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }
}
