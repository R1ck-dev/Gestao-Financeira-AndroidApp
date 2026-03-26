package com.example.gestaofinanceiraapp.data.local.room.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
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

    @ColumnInfo(name = "create_at")
    public String date;

    @ColumnInfo(name = "category_id")
    public String categoryId;
    public String description;

    public TransactionEntity() {
    }
}
