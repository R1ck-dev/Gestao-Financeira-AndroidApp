package com.example.gestaofinanceiraapp.presentation.budget;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gestaofinanceiraapp.databinding.ActivityBudgetsBinding;

public class BudgetsActivity extends AppCompatActivity {

    private ActivityBudgetsBinding binding;
    private BudgetViewModel viewModel;
    private BudgetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Infla o XML usando ViewBinding
        binding = ActivityBudgetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Injeta o ViewModel através da nossa Fábrica
        BudgetViewModelFactory factory = new BudgetViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(BudgetViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Toda vez que a tela for aberta ou reaberta, as metas são recalculadas
        viewModel.loadBudgetsForCurrentMonth();
    }

    private void setupRecyclerView() {
        adapter = new BudgetAdapter();

        // Ensina o RecyclerView a dispor os itens de cima para baixo
        binding.recyclerBudgets.setLayoutManager(new LinearLayoutManager(this));

        // Conecta o RecyclerView com o Adapter
        binding.recyclerBudgets.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getBudgetList().observe(this, budgetProgressList -> {
            if (budgetProgressList != null) {
                // Manda a lista nova para o Adapter atualizar a tela
                adapter.submitList(budgetProgressList);
            }
        });
    }

    private void setupListeners() {
        binding.fabAddBudget.setOnClickListener(v -> {
            // Em breve, abriremos uma tela para criar o Orçamento.
            // Por enquanto, exibimos um Toast.
            Toast.makeText(this, "Funcionalidade de Nova Meta em breve", Toast.LENGTH_SHORT).show();
        });
    }
}