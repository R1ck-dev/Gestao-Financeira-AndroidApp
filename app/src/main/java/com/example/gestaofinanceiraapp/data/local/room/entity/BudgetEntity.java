package com.example.gestaofinanceiraapp.data.local.room.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*
Declaramos a tabela -> definimos a chave estrangeira apontando para a nossa CatagoryEntity
 */
@Entity(
        tableName = "budgets",
        foreignKeys = @ForeignKey(
                entity = CategoryEntity.class,
                parentColumns = "id", // Qual coluna vai ser referenciada da tabela pai
                childColumns = "category_id", // Coluna da tabela filha que referencia a tabela pai
                onDelete = ForeignKey.CASCADE // Caso a categoria (tabela pai) seja deletada, deletamos também o orçamento (tabela filha)
        ),
        indices = {@Index("category_id")} // Crioamos um Index na category_id para otimizar as buscas (exigido pelo Room)
)
public class BudgetEntity {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "category_id")
    public String categoryId;

    @ColumnInfo(name = "month_ref")
    public String monthRef;

    @ColumnInfo(name = "target_amount")
    public String targetAmount;

    public BudgetEntity() {

    }
}
