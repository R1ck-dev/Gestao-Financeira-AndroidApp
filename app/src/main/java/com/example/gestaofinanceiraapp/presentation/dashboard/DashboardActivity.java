package com.example.gestaofinanceiraapp.presentation.dashboard;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gestaofinanceiraapp.R;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
    private DashboardViewModel viewModel;

    /*
    A função onCreate é o equivalente ao public static void main() para uma tela especifica. Será a primeira coisa que o Android vai chamar
    quando a tela nascer. O Bundle é um dicionário chave-valor. Se o Android precisar matar o app por faltar de memória enquanto ele estava
    em segundo plano, será usado esse Bundle para salvar temporiariamente dados pequenos para serem restaurados
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        No Andorid, os visuais são feitos em arquivos XMLL. A classe R (Resources) é gerada automaticamente pelo sistema e mapeia todos os
        arquivos visuais. Aqui estamos pegando o arquivo XM activity_dashboard e desenhando ele na tela
         */
        setContentView(R.layout.activity_dashboard);

        setupObservers();
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
        /*
        TextView é o equivalente a tags <p> ou <span> no HTML. Sendo um componente nativo do Android para textos.
        O findViewById, semelhante, é o equivalente ao document.getElementById() do JS. Procurando no arquivo XML o elemento que tem o ID
        passado, para que possamos manipula-lo.
         */
        TextView txtBalance = findViewById(R.id.txtTotalBalance);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String balanceFormatted = currencyFormat.format(state.getTotalBalance());

        txtBalance.setText(balanceFormatted);

        if (state.getTotalBalance().compareTo(BigDecimal.ZERO) >= 0) {
            txtBalance.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            txtBalance.setContentDescription("Saldo positivo de " + balanceFormatted);
        } else {
            txtBalance.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            txtBalance.setContentDescription("Saldo negativo de " + balanceFormatted);
        }
    }
}
