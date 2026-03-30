package com.example.gestaofinanceiraapp.presentation.budget;

import android.util.Log;
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
    public LiveData<List<BudgetProgress>> getBudgetList() { return _budgetList; }

    public BudgetViewModel(GetBudgetProgressUseCase getBudgetProgressUseCase) {
        this.getBudgetProgressUseCase = getBudgetProgressUseCase;
    }

    public void loadBudgetsForCurrentMonth() {
        executor.execute(() -> {
            try {
                List<BudgetProgress> progressList = getBudgetProgressUseCase.execute(YearMonth.now());
                _budgetList.postValue(progressList);
            } catch (Exception e) {
                Log.e("BUDGET_LOG", "Erro ao carregar orçamentos: ", e);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}