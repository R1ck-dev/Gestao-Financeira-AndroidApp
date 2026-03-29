package com.example.gestaofinanceiraapp.data.local.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gestaofinanceiraapp.data.local.room.entity.BudgetEntity;

import java.util.List;

@Dao
public interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BudgetEntity budget);

    @Query("SELECT * FROM budgets WHERE month_ref = :monthRef")
    List<BudgetEntity> getBudgetByMonth(String monthRef);

    @Query("SELECT * FROM budgets WHERE category_id = :categoryId AND month_ref = :monthRef LIMIT 1")
    BudgetEntity getBudgetByCategoryAndMonth(String categoryId, String monthRef);

    @Query("DELETE FROM budgets WHERE id = :budgetId")
    void delete(String budgetId);
}
