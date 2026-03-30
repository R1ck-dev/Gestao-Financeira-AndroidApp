package com.example.gestaofinanceiraapp.presentation.budget;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaofinanceiraapp.domain.model.Budget;
import com.example.gestaofinanceiraapp.domain.model.Category;
import com.example.gestaofinanceiraapp.domain.repository.BudgetRepository;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddBudgetViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Usaremos para enviar a lista de categorias do BD para o Dropdown da tela
    private final MutableLiveData<List<Category>> _categories = new MutableLiveData<>();

    public LiveData<List<Category>> getCategories() {
        return _categories;
    }

    private final MutableLiveData<Boolean> _isSaved = new MutableLiveData<>();

    public LiveData<Boolean> getIsSaved() {
        return _isSaved;
    }

    public AddBudgetViewModel(CategoryRepository categoryRepository, BudgetRepository budgetRepository) {
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
    }

    // A Activity vai solicitar o carregamento das categorias na tela quando abrir
    public void loadCategories() {
        executor.execute(() -> {
            List<Category> categoryList = categoryRepository.getAll();
            _categories.postValue(categoryList);
        });
    }

    // A Activity vai madnar salvar quando o usuário clicar no botão
    public void saveBudget(String categoryId, String amountStr) {
        if (categoryId == null || categoryId.isEmpty() || amountStr == null || amountStr.isEmpty()) {
            return;
        }

        executor.execute(() -> {
            try {
                // Convertendo o valor financeiro de String para BigDecimal
                BigDecimal amount = new BigDecimal(amountStr.replace(",", "."));

                // Monta o objeto completo
                Budget budget = new Budget(
                        UUID.randomUUID().toString(),
                        categoryId,
                        YearMonth.now(),
                        amount
                );

                // Grava no disco
                budgetRepository.save(budget);

                // Da o sinal que a tela pode ser fechada
                _isSaved.postValue(true);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
