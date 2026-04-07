package com.example.gestaofinanceiraapp.presentation.history;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.data.local.room.AppDatabase;
import com.example.gestaofinanceiraapp.data.repository.CategoryRepositoryImpl;
import com.example.gestaofinanceiraapp.data.repository.TransactionRepositoryImpl;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;
import com.example.gestaofinanceiraapp.domain.usecase.GetTransactionsHistoryUseCase;

public class HistoryViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public HistoryViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HistoryViewModel.class)) {
            AppDatabase db = AppDatabase.getDataBase(context);

            TransactionRepository transactionRepo = new TransactionRepositoryImpl(db.transactionDao());
            CategoryRepository categoryRepo = new CategoryRepositoryImpl(db.categoryDao());

            GetTransactionsHistoryUseCase useCase = new GetTransactionsHistoryUseCase(transactionRepo, categoryRepo);

            return (T) new HistoryViewModel(useCase);
        }

        throw new IllegalArgumentException("Classe ViewModel desconhecida");
    }
}

