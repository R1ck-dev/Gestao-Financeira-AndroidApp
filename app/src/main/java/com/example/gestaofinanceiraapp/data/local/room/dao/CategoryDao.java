package com.example.gestaofinanceiraapp.data.local.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gestaofinanceiraapp.data.local.room.entity.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryEntity category);

    @Query("SELECT * FROM categories ORDER BY name ASC")
    List<CategoryEntity> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :categoryId LIMIT 1")
    CategoryEntity getCategoryById(String categoryId);
}
