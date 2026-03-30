package com.example.gestaofinanceiraapp.presentation.budget;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.databinding.ActivityAddBudgetBinding;
import com.example.gestaofinanceiraapp.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

public class AddBudgetActivity extends AppCompatActivity {
    private ActivityAddBudgetBinding binding;
    private AddBudgetViewModel viewModel;

    // Guardando a lista completa para mapear posteriormente
    private List<Category> loadedCategories = new ArrayList<>();

    // Guardar o ID da categoria selecionada
    private String selectedCategoryId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AddBudgetViewModelFactory factory = new AddBudgetViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(AddBudgetViewModel.class);

        setupObservers();
        setupListeners();

        // Assim que a tela abrir, pedimos para o ViewModel carregar as categorias
        viewModel.loadCategories();
    }

    private void setupObservers() {
        viewModel.getCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                this.loadedCategories = categories;

                List<String> categoryNames = new ArrayList<>();
                for (Category cat : categories) {
                    categoryNames.add(cat.getName());
                }

                // ArrayAdapter é uma ferramenta do Android que serve como ponte que vincula conjuntos de dados a visualizações baseadas em itens
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        categoryNames
                );

                binding.autoCompleteCategory.setAdapter(adapter);
            }
        });

        // Aguarda o sinal de sucesso
        viewModel.getIsSaved().observe(this, isSaved -> {
            if (isSaved != null && isSaved) {
                Toast.makeText(this, "Meta guardada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupListeners() {
        // Quando o usuário toca em um item do Dropdown
        binding.autoCompleteCategory.setOnItemClickListener((parent, view, position, id) -> {
            // A position clicada corresponde à mesma posição na nossa lista
            Category selectedCategory = loadedCategories.get(position);

            // Guardamos o id da categoria escolhida
            selectedCategoryId = selectedCategory.getId();
        });

        binding.btnSaveBudget.setOnClickListener(v -> {
            String amountStr = binding.edtBudgetAmount.getText().toString();

            if (selectedCategoryId == null) {
                Toast.makeText(this, "Por favor, selecione uma categoria.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amountStr.trim().isEmpty()) {
                binding.inputLayoutBudgetAmount.setError("Por favor, introduza um valor.");
                return;
            }

            binding.inputLayoutBudgetAmount.setError(null);
            viewModel.saveBudget(selectedCategoryId, amountStr);
        });
    }
}
