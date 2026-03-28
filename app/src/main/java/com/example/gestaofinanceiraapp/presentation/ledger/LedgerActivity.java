package com.example.gestaofinanceiraapp.presentation.ledger;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.R;
import com.example.gestaofinanceiraapp.databinding.ActivityLedgerBinding;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;

public class LedgerActivity extends AppCompatActivity {
    private ActivityLedgerBinding binding;
    private LedgerViewModel viewModel;

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
    }

    private void setupListeners() {
        binding.btnSave.setOnClickListener(v -> {
            String amountStr = binding.edtAmount.getText().toString();
            String description = binding.edtDescription.getText().toString();

            TransactionType type = binding.toggleTransactionType.getCheckedButtonId() == R.id.btnIncome
                    ? TransactionType.INCOME
                    : TransactionType.EXPENSE;

            viewModel.saveTransaction(amountStr, description, type);
        });
    }

    private void setupObservers() {
        viewModel.getIsSaved().observe(this, saved -> {
            if (saved != null && saved) {
                Toast.makeText(this, "Lançamento salvo com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
