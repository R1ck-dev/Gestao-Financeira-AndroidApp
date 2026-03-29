package com.example.gestaofinanceiraapp.presentation.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestaofinanceiraapp.R;
import com.example.gestaofinanceiraapp.databinding.ActivityDashboardBinding;
import com.example.gestaofinanceiraapp.presentation.budget.BudgetsActivity;
import com.example.gestaofinanceiraapp.presentation.ledger.LedgerActivity;
import com.example.gestaofinanceiraapp.presentation.ledger.LedgerViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private DashboardViewModel viewModel;

    /*
    A função onCreate é o equivalente ao public static void main() para uma tela especifica. Será a primeira coisa que o Android vai chamar
    quando a tela nascer. O Bundle é um dicionário chave-valor. Se o Android precisar matar o app por faltar de memória enquanto ele estava
    em segundo plano, será usado esse Bundle para salvar temporiariamente dados pequenos para serem restaurados
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
        No Andorid, os visuais são feitos em arquivos XMLL. A classe R (Resources) é gerada automaticamente pelo sistema e mapeia todos os
        arquivos visuais. Aqui estamos pegando o arquivo XM activity_dashboard e desenhando ele na tela
         */
        // setContentView(R.layout.activity_dashboard);

        DashboardViewModelFactory factory = new DashboardViewModelFactory(this);

        viewModel = new ViewModelProvider(this, factory).get(DashboardViewModel.class);

        setupObservers();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadDashboardData();
    }

    private void setupListeners() {
        // Acessando o botão diretamente pelo binding
        binding.fabAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LedgerActivity.class);
            startActivity(intent);
        });

        binding.btnViewBudgets.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, BudgetsActivity.class);
            startActivity(intent);
        });
    }

    private void setupObservers() {
        /*
        Aqui a função vai acessar aquela visualização imutável possível através do LiveData. O método observe se trata de um Listener.
        Ele recebe uma função lambda que basicamente permite que o LiveData seja observado, e sempre que for atualizado, o bloco de código
        será executado, até por fim chamar o objeto state. Que no caso é o nosso DashboardState com os valores a serem exibidos
         */
        viewModel.getUiState().observe(this, state -> {
            if (state.isLoading()) {
                return;
            }

            updateDashboardUi(state);
        });
    }

    private void updateDashboardUi(DashboardState state) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        String balanceStr = format.format(state.getTotalBalance());

        // Acessando os TextViews diretamente pelo binding
        binding.txtTotalBalance.setText(balanceStr);
        binding.txtTotalIncomes.setText(format.format(state.getTotalIncomes()));
        binding.txtTotalExpenses.setText(format.format(state.getTotalExpenses()));

        if (state.getTotalBalance().compareTo(BigDecimal.ZERO) >= 0) {
            binding.txtTotalBalance.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            binding.txtTotalBalance.setContentDescription("Saldo positivo de " + balanceStr);
        } else {
            binding.txtTotalBalance.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            binding.txtTotalBalance.setContentDescription("Saldo negativo de " + balanceStr);
        }
    }
}

// Esse aqui é o updateDashboardUi antigo que não utilizava binding, vou deixar ele aqui por enquanto só por curiosidade e comparativo de funcionamento

/*
    private void updateDashboardUi(DashboardState state) {

        TextView é o equivalente a tags <p> ou <span> no HTML. Sendo um componente nativo do Android para textos.
        O findViewById, semelhante, é o equivalente ao document.getElementById() do JS. Procurando no arquivo XML o elemento que tem o ID
        passado, para que possamos manipula-lo.

        TextView txtBalance = findViewById(R.id.txtTotalBalance);
        TextView txtIncomes = findViewById(R.id.txtTotalIncomes);
        TextView txtExpenses = findViewById(R.id.txtTotalExpenses);

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        String balanceStr = format.format(state.getTotalBalance());
                txtBalance.setText(balanceStr);
                txtIncomes.setText(format.format(state.getTotalIncomes()));
                txtExpenses.setText(format.format(state.getTotalExpenses()));

                if (state.getTotalBalance().compareTo(BigDecimal.ZERO) >= 0) {
                txtBalance.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
                txtBalance.setContentDescription("Saldo positivo de " + balanceStr);
                } else {
                        txtBalance.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                txtBalance.setContentDescription("Saldo negativo de " + balanceStr);
                }
                        }
 */
