package com.example.gestaofinanceiraapp.data.local.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gestaofinanceiraapp.data.local.room.entity.TransactionEntity;

import java.util.List;

@Dao
public interface TransactionDao {

    // OnConflictStrategy.REPLACE define que a query deve funcionar como um Update caso o item já exista, e como um Insert caso contrário
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TransactionEntity transaction);

    @Query("DELETE FROM transactions WHERE id = :transactionId") // O ":" indicam um param nomeado. Servindo para vincular uma variável na query
    void delete(String transactionId);

    @Query("SELECT * FROM transactions WHERE create_at BETWEEN :startDate AND :endDate")
    List<TransactionEntity> getTransactionByPeriod(String startDate, String endDate);
}
