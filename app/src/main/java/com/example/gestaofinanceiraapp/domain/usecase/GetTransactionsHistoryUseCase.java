package com.example.gestaofinanceiraapp.domain.usecase;

import com.example.gestaofinanceiraapp.domain.model.Category;
import com.example.gestaofinanceiraapp.domain.model.Transaction;
import com.example.gestaofinanceiraapp.domain.model.TransactionHistoryItem;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTransactionsHistoryUseCase {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public GetTransactionsHistoryUseCase(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retorna o histórico pronto para exibição, cruzando dados de transações com categorias.
     */
    public List<TransactionHistoryItem> execute(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.getTransactionsByPeriod(startDate, endDate);
        List<Category> categories = categoryRepository.getAll();

        Map<String, Category> categoryById = new HashMap<>();
        for (Category category : categories) {
            categoryById.put(category.getId(), category);
        }

        // Ordena do mais recente para o mais antigo
        transactions.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        List<TransactionHistoryItem> items = new ArrayList<>();
        for (Transaction transaction : transactions) {
            Category category = categoryById.get(transaction.getCategoryId());
            String categoryName = category != null ? category.getName() : "Desconhecido";

            BigDecimal signedAmount = transaction.getType() == TransactionType.EXPENSE
                    ? transaction.getAmount().negate()
                    : transaction.getAmount();

            items.add(new TransactionHistoryItem(
                    transaction.getId(),
                    transaction.getType(),
                    signedAmount,
                    transaction.getDate(),
                    categoryName,
                    transaction.getDescription()
            ));
        }

        return items;
    }
}

