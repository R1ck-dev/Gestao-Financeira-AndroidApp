package com.example.gestaofinanceiraapp.domain.repository;

import com.example.gestaofinanceiraapp.domain.model.Category;

import java.util.List;

public interface CategoryRepository {
    void save(Category category);
    List<Category> getAll();
    Category getById(String id);
}
