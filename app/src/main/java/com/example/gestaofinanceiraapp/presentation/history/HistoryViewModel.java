package com.example.gestaofinanceiraapp.presentation.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaofinanceiraapp.domain.model.TransactionHistoryItem;
import com.example.gestaofinanceiraapp.domain.usecase.GetTransactionsHistoryUseCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryViewModel extends ViewModel {
    private final GetTransactionsHistoryUseCase getTransactionsHistoryUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<TransactionHistoryItem>> _transactions = new MutableLiveData<>();
    public LiveData<List<TransactionHistoryItem>> getTransactions() {
        return _transactions;
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> getIsLoading() {
        return _isLoading;
    }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return _errorMessage;
    }

    public HistoryViewModel(GetTransactionsHistoryUseCase getTransactionsHistoryUseCase) {
        this.getTransactionsHistoryUseCase = getTransactionsHistoryUseCase;
    }

    public void loadCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);
        load(startOfMonth, endOfMonth);
    }

    public void loadLast30Days() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.minusDays(30).atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        load(start, end);
    }

    private void load(LocalDateTime start, LocalDateTime end) {
        _isLoading.postValue(true);
        _errorMessage.postValue(null);

        executor.execute(() -> {
            try {
                List<TransactionHistoryItem> items = getTransactionsHistoryUseCase.execute(start, end);
                _transactions.postValue(items);
            } catch (Exception e) {
                _errorMessage.postValue("Erro ao carregar histórico.");
            } finally {
                _isLoading.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}

