package com.example.gestaofinanceiraapp.presentation.ledger;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.data.local.room.AppDatabase;
import com.example.gestaofinanceiraapp.data.repository.CategoryRepositoryImpl;
import com.example.gestaofinanceiraapp.data.repository.TransactionRepositoryImpl;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;

/*
No android, o ciclo de vida das telas (activity) são bem "caóticos". Se o usuário girar o celular, a tela vai morrer e nascer de novo, por isso se fizessemos
um novo LedgerViewModel(), estariamos perdendno os dados a cada giro de tela. Para superar esse problema, o responsável por criar e guardar o ViewModel é o
próprio SO, atráves do ViewModelProvider
 */
/*
Como o Android é quem constrói o ViewModel para manter ele vivo, ele só sabe fazer isso se o ViewModel tiver uma construtor vazio. Mas no nosso caso, precisamos
sim de param no construtor (no caso o nosso TransactionRepository), ao passar ViewModelProvider.Factory, o Factory é a nossa forma de explicitar ao Android para
usar o construtor que indicamos quando for construir o ViewModel
 */
public class LedgerViewModelFactory implements ViewModelProvider.Factory {
    /*
    No celular, o app roda em uma caixa restrita com segurança (ao invés de um servidor livre como em um backend comum). Por causa disso, precisamos do context
    que vai servir como uma ponte de comunicação com o SO. Ele será usado para pedir permissão ao Android para acessar o disco do celular (onde o nosso arquivo
    SQLite do Room é salvo)
     */
    private final Context context;

    public LedgerViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    /*
    <T extends ViewModel> serve como seguranaça, indicando que essa função pode apenas criar o que herda de ViewModel
    Class<T> modelClass é o param que o Android utiliza para passar a classe que ele quer criar
    isAssignableFrom é basicamente um instanceof, mas para classes, vericiando se o o param vindo do android é compatível com o LedgerViewModel
     */
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LedgerViewModel.class)) {
            AppDatabase db = AppDatabase.getDataBase(context);
            /*
            db.transactionDao() vai extrair a ferramente SQL de dentro do db
            new TransactionRepositoryImpl(...) vai criar a implementação real da regra de acesso a dados, passando o DAO para dentro dela
            TransactionRepository repository = ... vai tipar a variável com a nossa interface pura de domínio
             */
            TransactionRepository repository = new TransactionRepositoryImpl(db.transactionDao());
            CategoryRepository categoryRepo = new CategoryRepositoryImpl(db.categoryDao());

            return (T) new LedgerViewModel(repository, categoryRepo);
        }
        throw new IllegalArgumentException("Classe ViewModel desconhecida");
    }
}