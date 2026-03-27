package com.example.gestaofinanceiraapp.domain.repository;

import com.example.gestaofinanceiraapp.domain.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);
    void delete(String transactionId);

    // Buscar transações de um período estipulado
    List<Transaction> getTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate);
}
