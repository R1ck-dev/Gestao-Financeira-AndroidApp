package com.example.gestaofinanceiraapp.data.repository;

import com.example.gestaofinanceiraapp.data.local.room.dao.TransactionDao;
import com.example.gestaofinanceiraapp.data.local.room.entity.TransactionEntity;
import com.example.gestaofinanceiraapp.data.mapper.TransactionMapper;
import com.example.gestaofinanceiraapp.domain.model.Transaction;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
Classe concreta que implementa o contrato que definimos na camada domain.
 */
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionDao transactionDao;

    // Injeção de dependência via construtor
    public TransactionRepositoryImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public void save(Transaction transaction) {
        // Recebe o objeto de domínio
        // Mapeia para a entidade do banco
        TransactionEntity entity = TransactionMapper.toEntity(transaction);
        // Salva usando o Room
        transactionDao.insert(entity);
    }

    @Override
    public void delete(String transactionId) {
        transactionDao.delete(transactionId);
    }

    @Override
    public List<Transaction> getTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        String start = startDate.toString();
        String end = endDate.toString();

        // Busca as entidades no Room
        List<TransactionEntity> entities = transactionDao.getTransactionByPeriod(start, end);

        // Mapeia a lista de entidades de volta para a lista de domínio
        List<Transaction> transactions = new ArrayList<>();
        for (TransactionEntity entity : entities) {
            transactions.add(TransactionMapper.toDomain(entity));
        }

        return transactions;
    }
}
