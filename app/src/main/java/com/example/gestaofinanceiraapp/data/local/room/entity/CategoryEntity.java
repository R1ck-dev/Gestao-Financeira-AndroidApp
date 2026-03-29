package com.example.gestaofinanceiraapp.data.local.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Categories")
public class CategoryEntity {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String color;
    public String icon;

    public CategoryEntity() {

    }
}
