package com.example.gestaofinanceiraapp.data.mapper;

import com.example.gestaofinanceiraapp.data.local.room.entity.TransactionEntity;
import com.example.gestaofinanceiraapp.domain.model.Transaction;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionMapper {

    public static TransactionEntity toEntity(Transaction domain) {

        // Converte de domínio (Java puro) para entidade (BD)
        if (domain == null) return null;

        TransactionEntity entity = new TransactionEntity();
        entity.id = domain.getId();
        entity.type = domain.getType().name(); // Converte Enum para String
        entity.amount = domain.getAmount().toString(); // BigDecimal para String
        entity.date = domain.getDate().toString(); // LocalDateTime para String
        entity.categoryId = domain.getCategoryId();
        entity.description = domain.getDescription();

        return entity;
    }

    public static Transaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;

        return new Transaction(
                entity.id,
                TransactionType.valueOf(entity.type), // String para Enum
                new BigDecimal(entity.amount), // String para BigDecimal
                LocalDateTime.parse(entity.date), // String para LocalDateTime
                entity.categoryId,
                entity.description
        );
    }
}
