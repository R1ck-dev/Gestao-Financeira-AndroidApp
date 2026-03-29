package com.example.gestaofinanceiraapp.data.local.room.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "transactions",
        foreignKeys = @ForeignKey(
                entity = CategoryEntity.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.RESTRICT // Usamos RESTRICT para impedir a exclusão de uma Categoria se houver transações nela, para evitar corromper o histórico do usuário
        ),
        indices = {@Index("category_id")}
)
public class TransactionEntity {

    @PrimaryKey
    @NonNull
    public String id;

    public String type; // INCOME ou EXPENSE

    /*
    SQLite não suporta BigDecimal nativamente, e se usassemos double/float vamos sofrer com a perda de precisão (é a mantissa pai)
    então vamos armazenar como TEXT (String) e converter posteriormente
     */
    public String amount;

    @ColumnInfo(name = "created_at")
    public String date;

    @ColumnInfo(name = "category_id")
    public String categoryId;
    public String description;

    public TransactionEntity() {
    }
}
