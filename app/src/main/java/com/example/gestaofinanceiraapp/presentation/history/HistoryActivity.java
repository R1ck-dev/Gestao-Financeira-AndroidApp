package com.example.gestaofinanceiraapp.presentation.history;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gestaofinanceiraapp.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private HistoryViewModel viewModel;
    private TransactionHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HistoryViewModelFactory factory = new HistoryViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(HistoryViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();

        // Padrão: carregar o mês atual
        viewModel.loadCurrentMonth();
    }

    private void setupRecyclerView() {
        adapter = new TransactionHistoryAdapter();
        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerHistory.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnHistoryMonth.setOnClickListener(v -> viewModel.loadCurrentMonth());
        binding.btnHistoryLast30Days.setOnClickListener(v -> viewModel.loadLast30Days());
    }

    private void setupObservers() {
        viewModel.getTransactions().observe(this, transactions -> {
            if (transactions == null) return;

            adapter.submitList(transactions);
            binding.txtEmptyState.setVisibility(transactions.isEmpty() ? View.VISIBLE : View.GONE);
            binding.recyclerHistory.setVisibility(transactions.isEmpty() ? View.GONE : View.VISIBLE);
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading == null) return;
            binding.progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(this, message -> {
            if (message == null || message.trim().isEmpty()) return;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }
}

