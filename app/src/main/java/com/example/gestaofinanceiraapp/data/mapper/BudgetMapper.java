package com.example.gestaofinanceiraapp.data.mapper;

import com.example.gestaofinanceiraapp.data.local.room.entity.BudgetEntity;
import com.example.gestaofinanceiraapp.domain.model.Budget;

import java.math.BigDecimal;
import java.time.YearMonth;

public class BudgetMapper {
    public static BudgetEntity toEntity(Budget domain) {
        if (domain == null) return null;

        BudgetEntity entity = new BudgetEntity();
        entity.id = domain.getId();
        entity.categoryId = domain.getCategoryId();

        // Converte o YearMonth para String
        entity.monthRef = domain.getMonthRef().toString();
        entity.targetAmount = domain.getTargetAmount().toString();

        return entity;
    }

    public static Budget toDomain(BudgetEntity entity) {
        if (entity == null) return null;

        return new Budget(
                entity.id,
                entity.categoryId,
                YearMonth.parse(entity.monthRef), // Reconstrói o objeto de data
                new BigDecimal(entity.targetAmount) // Reconstrói o valor financeiro exato
        );
    }
}
