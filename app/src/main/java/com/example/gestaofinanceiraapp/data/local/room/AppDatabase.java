package com.example.gestaofinanceiraapp.data.local.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gestaofinanceiraapp.data.local.room.dao.BudgetDao;
import com.example.gestaofinanceiraapp.data.local.room.dao.CategoryDao;
import com.example.gestaofinanceiraapp.data.local.room.dao.TransactionDao;
import com.example.gestaofinanceiraapp.data.local.room.entity.BudgetEntity;
import com.example.gestaofinanceiraapp.data.local.room.entity.CategoryEntity;
import com.example.gestaofinanceiraapp.data.local.room.entity.TransactionEntity;

import java.time.YearMonth;
import java.util.UUID;
import java.util.concurrent.Executors;

// Definindo as entidades e versão que irão se relacionar
// ExportSchema = true permite que o ROOM salve um JSON do esquema
@Database(
        entities = {
                TransactionEntity.class,
                CategoryEntity.class,
                BudgetEntity.class
        },
        version = 5, // Subindo a versão do BD devido a alteração do esquema.
        exportSchema = true
)
@TypeConverters({Converters.class}) // Registrando conversores globais
public abstract class AppDatabase extends RoomDatabase {

    /*
    A classe AppDatabase é a dona do BD, e o TransactionDAO é apenas a interface que sabe como ler/escrever na tabela. Para que o
    resto do aplicativo consiga acessar os dados, o DAO precisa fornecer esse conhecimento ao AppDatabase. Ele é abstrato porque
    não precisamos escrever o código desse método, o framework do ROOM usa um Annotation Processor para ler as anotações @Dao e @Database
    e gerar uma classe real chamada AppDatabase_Impl.java . Essa classe gerada vai implementar os métodos e instanciar o DAO de verdade
     */
    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();
    public abstract BudgetDao budgetDao();

    // Implementação Singleton
    /*
    Singleton é um padrão de projeto que garante que exista apenas uma única instância dessa classe viva na memória durante toda a
    execução do aplicativo. Isso porque criar uma conexão com o BD é uma operação extremamente pesada para a CPU e a memória do celular.
    Volatile é uma instrução direta para a JVM e para a forma como o processador deve gerenciar a memória. Ela indica à JVM para nunca
    guardar essa variável em cache de CPU, mas sim direto na memória ram principal, garantindo que as diferentes threads enxerguem essa
    criação instantaneamente
     */
    /*
    A varíavel INSTANCE carrega a conexão ativa e o mecanismo de gerenciamento do BD SQLite.
    Ela não guarda os dados diretamente na memória RAM, mas sim a lógica que permite acessá-los.
    Portanto essa instância carrega a conexão com o SQLite, Gerenciador de Cache, Implementação dos DAOs,
    Esquema do Banco e Estado de Migração.
     */
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDataBase(final Context context) {
        /*
        Primeira checagem: O synchronized custa caro para a perfomance. Só queremos usar ele na primeira vez que o BD for criado.
        Depois que INSTANCE passa a existir, ele passa direto pela primeira checagem e devolve o banco.
         */
        if (INSTANCE == null) {
            /*
            Synchronized serve para criar um "funil" por onde apenas uma thread pode entrar por vez no bloco de código.
             */
            synchronized (AppDatabase.class) {
                /*
                Segunda checagem: No pior cenário, as Threads A e Thread B passam pela primeira checagem juntas. A thread A chega no sync,
                bloqueia a entrada, cria a INSTANCE e sai. A thread B entra logo quando a thread A sai. Se não houver a segunda checagem
                a thread B pode acabar criando novamente o BD, sobreescrevendo a thread A e quebrando nosso padrão Singleton
                 */
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "pfm_database")
                            .fallbackToDestructiveMigration()
                            // --- Data Seed ---
                            .addCallback(roomCallback)
                            // -----------------
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase database = INSTANCE;
                if (database != null) {
                    CategoryDao categoryDao = database.categoryDao();

                    categoryDao.insert(createCategory("default_category", "Alimentação", "#2E7D32", "ic_food"));

                    categoryDao.insert(createCategory("cat_transport", "Transporte", "#1565C0", "ic_transport"));

                    categoryDao.insert(createCategory("cat_housing", "Moradia", "#6A1B9A", "ic_home"));

                    categoryDao.insert(createCategory("cat_health", "Saúde", "#C62828", "ic_health"));

                    categoryDao.insert(createCategory("cat_leisure", "Lazer", "#F9A825", "ic_leisure"));

                    categoryDao.insert(createCategory("cat_salary", "Salário/Renda", "#00695C", "ic_money"));
                }
            });
        }
    };

    private static CategoryEntity createCategory(String id, String name, String color, String icon) {
        CategoryEntity category = new CategoryEntity();
        category.id = id;
        category.name = name;
        category.color = color;
        category.icon = icon;
        return category;
    }
}
