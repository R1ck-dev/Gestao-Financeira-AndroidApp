package com.example.gestaofinanceiraapp.presentation.ledger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaofinanceiraapp.domain.model.Transaction;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LedgerViewModel extends ViewModel {
    private final TransactionRepository transactionRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /*
    Como aqui a única regra de negócio a ser observada é um simples evento binário para analisar se a tela pode ser fechada, por isso um boolean é o suficiente
     */
    private final MutableLiveData<Boolean> _isSaved = new MutableLiveData<>();

    public LiveData<Boolean> getIsSaved() {
        return _isSaved;
    }

    public LedgerViewModel(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveTransaction(String amountStr, String description, TransactionType type) {
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        executor.execute(() -> {
            try {
                BigDecimal amount = new BigDecimal(amountStr.replace(",", "."));

                Transaction transaction = new Transaction(
                        UUID.randomUUID().toString(),
                        type,
                        amount,
                        LocalDateTime.now(),
                        "default_category",
                        description
                );
                transactionRepository.save(transaction);
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
