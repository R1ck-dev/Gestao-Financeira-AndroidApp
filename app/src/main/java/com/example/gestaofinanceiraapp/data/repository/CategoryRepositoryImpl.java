package com.example.gestaofinanceiraapp.data.repository;

import com.example.gestaofinanceiraapp.data.local.room.dao.CategoryDao;
import com.example.gestaofinanceiraapp.data.local.room.entity.CategoryEntity;
import com.example.gestaofinanceiraapp.data.mapper.CategoryMapper;
import com.example.gestaofinanceiraapp.domain.model.Category;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {
    private CategoryDao categoryDao;

    public CategoryRepositoryImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    @Override
    public void save(Category category) {
        categoryDao.insert(CategoryMapper.toEntity(category));
    }

    @Override
    public List<Category> getAll() {
        List<CategoryEntity> entities = categoryDao.getAllCategories();
        List<Category> categories = new ArrayList<>();

        for (CategoryEntity entity : entities) {
            categories.add(CategoryMapper.toDomain(entity));
        }

        return categories;
    }

    @Override
    public Category getById(String id) {
        return CategoryMapper.toDomain(categoryDao.getCategoryById(id));
    }
}
