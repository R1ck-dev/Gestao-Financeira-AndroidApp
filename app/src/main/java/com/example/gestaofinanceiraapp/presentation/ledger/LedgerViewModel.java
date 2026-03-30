package com.example.gestaofinanceiraapp.presentation.ledger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaofinanceiraapp.domain.model.Category;
import com.example.gestaofinanceiraapp.domain.model.Transaction;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;
import com.example.gestaofinanceiraapp.domain.repository.CategoryRepository;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LedgerViewModel extends ViewModel {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /*
    Como aqui a única regra de negócio a ser observada é um simples evento binário para analisar se a tela pode ser fechada, por isso um boolean é o suficiente
     */
    private final MutableLiveData<Boolean> _isSaved = new MutableLiveData<>();
    public LiveData<Boolean> getIsSaved() {
        return _isSaved;
    }

    private final MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    public LiveData<List<Category>> getCategories() {
        return _categories;
    }

    public LedgerViewModel(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public void loadCategories() {
        executor.execute(() -> {
            List<Category> categories = categoryRepository.getAll();
            _categories.postValue(categories);
        });
    }

    public void saveTransaction(BigDecimal amount, String description, TransactionType type, String categoryId) {
        executor.execute(() -> {
            try {
                Transaction transaction = new Transaction(
                        UUID.randomUUID().toString(),
                        type,
                        amount,
                        LocalDateTime.now(),
                        categoryId,
                        description
                );

                transactionRepository.save(transaction);

                _isSaved.postValue(true);

            } catch (Exception e) {
                android.util.Log.e("LEDGER_TEST", "ERRO FATAL! Tentou salvar a categoria com ID: [" + categoryId + "]", e);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}