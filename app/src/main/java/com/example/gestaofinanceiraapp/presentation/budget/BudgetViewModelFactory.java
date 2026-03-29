package com.example.gestaofinanceiraapp.presentation.budget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.data.local.room.AppDatabase;
import com.example.gestaofinanceiraapp.data.repository.BudgetRepositoryImpl;
import com.example.gestaofinanceiraapp.data.repository.CategoryRepositoryImpl;
import com.example.gestaofinanceiraapp.data.repository.TransactionRepositoryImpl;
import com.example.gestaofinanceiraapp.domain.repository.BudgetRepository;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;
import com.example.gestaofinanceiraapp.domain.usecase.GetBudgetProgressUseCase;

public class BudgetViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public BudgetViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BudgetViewModel.class)) {
            // Instancia o Banco de Dados
            AppDatabase db = AppDatabase.getDataBase(context);

            // Instancia os 3 Repositórios
            BudgetRepository budgetRepo = new BudgetRepositoryImpl(db.budgetDao());
            TransactionRepository transactionRepo = new TransactionRepositoryImpl(db.transactionDao());
            CategoryRepository categoryRepo = new CategoryRepositoryImpl(db.categoryDao());

            // Injeta os repositórios no Caso de Uso
            GetBudgetProgressUseCase useCase = new GetBudgetProgressUseCase(budgetRepo, transactionRepo, categoryRepo);

            // Injeta o Caso de Uso no ViewModel
            return (T) new BudgetViewModel(useCase);
        }
        throw new IllegalArgumentException("Classe ViewModel desconhecida");
    }
}