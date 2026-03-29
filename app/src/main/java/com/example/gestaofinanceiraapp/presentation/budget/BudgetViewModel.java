package com.example.gestaofinanceiraapp.presentation.budget;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaofinanceiraapp.domain.model.BudgetProgress;
import com.example.gestaofinanceiraapp.domain.usecase.GetBudgetProgressUseCase;

import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetViewModel extends ViewModel {

    private final GetBudgetProgressUseCase getBudgetProgressUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<BudgetProgress>> _budgetList = new MutableLiveData<>();
    public LiveData<List<BudgetProgress>> getBudgetList() {
        return _budgetList;
    }

    public BudgetViewModel(GetBudgetProgressUseCase getBudgetProgressUseCase) {
        this.getBudgetProgressUseCase = getBudgetProgressUseCase;
    }

    public void loadBudgetsForCurrentMonth() {
        executor.execute(() -> {
            // Pega o mês atual via Java 8 Time API
            YearMonth currentMonth = YearMonth.now();

            // Executa a regra de negócio cruzando as tabelas
            List<BudgetProgress> progressList = getBudgetProgressUseCase.execute(currentMonth);

            // Joga o resultado de volta para a Main Thread
            _budgetList.postValue(progressList);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}