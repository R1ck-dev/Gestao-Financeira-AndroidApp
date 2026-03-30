package com.example.gestaofinanceiraapp.presentation.budget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.data.local.room.AppDatabase;
import com.example.gestaofinanceiraapp.data.repository.BudgetRepositoryImpl;
import com.example.gestaofinanceiraapp.data.repository.CategoryRepositoryImpl;
import com.example.gestaofinanceiraapp.domain.repository.BudgetRepository;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;

public class AddBudgetViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public AddBudgetViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddBudgetViewModel.class)) {
            // Instancia o Banco de Dados local
            AppDatabase db = AppDatabase.getDataBase(context);

            // Instancia apenas os Repositórios que esta tela precisa
            CategoryRepository categoryRepo = new CategoryRepositoryImpl(db.categoryDao());
            BudgetRepository budgetRepo = new BudgetRepositoryImpl(db.budgetDao());

            return (T) new AddBudgetViewModel(categoryRepo, budgetRepo);
        }
        throw new IllegalArgumentException("Classe ViewModel desconhecida");
    }
}