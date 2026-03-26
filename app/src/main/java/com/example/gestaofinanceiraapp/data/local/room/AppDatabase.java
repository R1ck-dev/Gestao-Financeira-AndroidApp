package com.example.gestaofinanceiraapp.data.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gestaofinanceiraapp.data.local.room.dao.TransactionDao;
import com.example.gestaofinanceiraapp.data.local.room.entity.TransactionEntity;

// Definindo as entidades e versão que irão se relacionar
// ExportSchema = true permite que o ROOM salve um JSON do esquema
@Database(entities = {TransactionEntity.class}, version = 1, exportSchema = true)
@TypeConverters({Converters.class}) // Registrando conversores globais
public abstract class AppDatabase extends RoomDatabase {

    /*
    A classe AppDatabase é a dona do BD, e o TransactionDAO é apenas a interface que sabe como ler/escrever na tabela. Para que o
    resto do aplicativo consiga acessar os dados, o DAO precisa fornecer esse conhecimento ao AppDatabase. Ele é abstrato porque
    não precisamos escrever o código desse método, o framework do ROOM usa um Annotation Processor para ler as anotações @Dao e @Database
    e gerar uma classe real chamada AppDatabase_Impl.java . Essa classe gerada vai implementar os métodos e instanciar o DAO de verdade
     */
    public abstract TransactionDao transactionDao();

    // Implementação Singleton
    /*
    Singleton é um padrão de projeto que garante que exista apenas uma única instância dessa classe viva na memória durante toda a
    execução do aplicativo. Isso porque criar uma conexão com o BD é uma operação extremamente pesada para a CPU e a memória do celular.
    Volatile é uma instrução direta para a JVM e para a forma como o processador deve gerenciar a memória. Ela indica à JVM para nunca
    guardar essa variável em cache de CPU, mas sim direto na memória ram principal, garantindo que as diferentes threads enxerguem essa
    criação instantaneamente
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
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
