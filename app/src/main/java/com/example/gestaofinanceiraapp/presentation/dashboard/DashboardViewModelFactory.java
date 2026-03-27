package com.example.gestaofinanceiraapp.presentation.dashboard;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.data.local.room.AppDatabase;
import com.example.gestaofinanceiraapp.data.repository.TransactionRepositoryImpl;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;

public class DashboardViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public DashboardViewModelFactory(Context context) {
        // Usamos getApplicationContext() para evitar vazamentos de memória
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            // Pega a instância Singleton do nosso banco de dados Room
            AppDatabase db = AppDatabase.getDataBase(context);

            // Instancia a implementação do Repositório passando o DAO
            TransactionRepository repository = new TransactionRepositoryImpl(db.transactionDao());

            // Retorna o ViewModel com o Repositório injetado
            return (T) new DashboardViewModel(repository);
        }
        throw new IllegalArgumentException("Classe ViewModel desconhecida");
    }
}
