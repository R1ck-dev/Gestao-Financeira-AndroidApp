package com.example.gestaofinanceiraapp.presentation.ledger;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.R;
import com.example.gestaofinanceiraapp.databinding.ActivityLedgerBinding;
import com.example.gestaofinanceiraapp.domain.model.Category;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LedgerActivity extends AppCompatActivity {
    private ActivityLedgerBinding binding;
    private LedgerViewModel viewModel;
    private List<Category> loadedCategories = new ArrayList<>();
    private String selectedCategoryId = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        No android, um arquivo XML é apenas um texto morto no disco. "Inflar" é o processo computacional de ler esse texto e transformar cada tag
        <TextView>, <Button>, etc em um objeto real na memória RAM e desenha-los na tela. O getLayoutInflater() é a ferramenta do Android que faz esse
        trabalho
         */
        binding = ActivityLedgerBinding.inflate(getLayoutInflater());
        /*
        O méotodo getRoot() retorna a tag mais externa do XML
         */
        setContentView(binding.getRoot());

        LedgerViewModelFactory factory = new LedgerViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(LedgerViewModel.class);

        setupListeners(); // Escutam ativamente eventos
        setupObservers(); // Escutam passivamente as mudanças que vêm do ViewModel

        viewModel.loadCategories();
    }

    private void setupObservers() {
        viewModel.getIsSaved().observe(this, isSaved -> {
            if (isSaved != null && isSaved) {
                Toast.makeText(this, "Lançamento guardado!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                this.loadedCategories = categories;

                List<String> categoryNames = new ArrayList<>();
                for (Category cat : categories) {
                    categoryNames.add(cat.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        categoryNames
                );
                binding.autoCompleteCategory.setAdapter(adapter);
            }
        });
    }

    private void setupListeners() {
        binding.autoCompleteCategory.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = loadedCategories.get(position);
            selectedCategoryId = selectedCategory.getId();
        });

        binding.btnSave.setOnClickListener(v -> {
            String amountStr = binding.edtAmount.getText().toString();
            String description = binding.edtDescription.getText().toString();

            if (amountStr.trim().isEmpty()) {
                binding.inputLayoutAmount.setError("Introduza um valor válido");
                return;
            }

            if (selectedCategoryId == null) {
                Toast.makeText(this, "Por favor, selecione uma categoria.", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.inputLayoutAmount.setError(null);

            int checkedId = binding.toggleTransactionType.getCheckedButtonId();
            TransactionType type = (checkedId == binding.btnExpense.getId()) ?
                    TransactionType.EXPENSE : TransactionType.INCOME;

            try {
                BigDecimal amount = new BigDecimal(amountStr.replace(",", "."));

                viewModel.saveTransaction(amount, description, type, selectedCategoryId);
            } catch (NumberFormatException e) {
                binding.inputLayoutAmount.setError("Formato numérico inválido");
            }
        });
    }
}
