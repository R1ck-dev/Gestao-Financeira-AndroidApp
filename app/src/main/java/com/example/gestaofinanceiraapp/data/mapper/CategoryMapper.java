package com.example.gestaofinanceiraapp.data.mapper;

import com.example.gestaofinanceiraapp.data.local.room.entity.CategoryEntity;
import com.example.gestaofinanceiraapp.domain.model.Category;

public class CategoryMapper {
    public static CategoryEntity toEntity(Category domain) {
        if (domain == null) return null;

        CategoryEntity entity = new CategoryEntity();
        entity.id = domain.getId();
        entity.name = domain.getName();
        entity.color = domain.getColor();
        entity.icon = domain.getIcon();

        return entity;
    }

    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;

        return new Category(
                entity.id,
                entity.name,
                entity.color,
                entity.icon
        );
    }
}
