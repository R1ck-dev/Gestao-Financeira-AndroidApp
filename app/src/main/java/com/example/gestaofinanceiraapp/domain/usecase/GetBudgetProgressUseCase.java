package com.example.gestaofinanceiraapp.domain.usecase;

import com.example.gestaofinanceiraapp.domain.model.Budget;
import com.example.gestaofinanceiraapp.domain.model.BudgetProgress;
import com.example.gestaofinanceiraapp.domain.model.Category;
import com.example.gestaofinanceiraapp.domain.model.Transaction;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;
import com.example.gestaofinanceiraapp.domain.repository.BudgetRepository;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/*
O UseCase vai pedir dados de três repositórios, cruzar as informações na memória e devolver a lista pronta para ser desenhada na tela
 */
public class GetBudgetProgressUseCase {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public GetBudgetProgressUseCase(BudgetRepository budgetRepository, TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    // Método principal para executar a ação
    public List<BudgetProgress> execute(YearMonth currentMonth) {
        // Busca todos as metas do mês que foi passado como param
        List<Budget> budgets = budgetRepository.getBudgetsByMonth(currentMonth);

        // Já busca todas as transações do mês
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);
        List<Transaction> monthTransactions = transactionRepository.getTransactionsByPeriod(startOfMonth, endOfMonth);

        List<BudgetProgress> progressList = new ArrayList<>();

        // Cruzamento de dados
        for (Budget budget : budgets) {
            // Pega os dados da categoria atual
            Category category = categoryRepository.getById(budget.getCategoryId());
            String catName = category != null ? category.getName() : "Desconhecido";
            String catColor = category != null ? category.getColor() : "#757575";

            // Soma apenas as despesas da categoria atual
            BigDecimal spent = BigDecimal.ZERO;
            for (Transaction t : monthTransactions) {
                if (t.getType() == TransactionType.EXPENSE && t.getCategoryId().equals(budget.getCategoryId())) {
                    spent = spent.add(t.getAmount());
                }
            }

            // Cria o objeto final
            BudgetProgress progress = new BudgetProgress(
                    budget.getId(),
                    catName,
                    catColor,
                    budget.getTargetAmount(),
                    spent
            );

            progressList.add(progress);
        }

        return progressList;
    }
}
