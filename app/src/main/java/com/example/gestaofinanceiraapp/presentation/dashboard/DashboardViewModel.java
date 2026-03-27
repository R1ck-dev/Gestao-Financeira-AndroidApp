package com.example.gestaofinanceiraapp.presentation.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestaofinanceiraapp.domain.model.Transaction;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;
import com.example.gestaofinanceiraapp.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Se chamarmos repository.getTransactionsByPeriod() ou repository.save() diretamente de uma Activity ou Fragment que pertencem à linha principal
de execução o aplicativo irá crashar. Isso porque o Android não permite operações pesadas de I/O (Banco de Dados/Rede) na Main Thread para
que a tela não congele.
Para evitar isso, vamos introduzir o ViewModel como responsável por chamar o repositório rodando fora da Main Thread, servindo apenas para
avisar a tela quando atualizar.
 */
public class DashboardViewModel extends ViewModel {
    private final TransactionRepository repository;

    /*
    O MutableLiveData é a versão que podemos alterar internamente. É a entrada que nos permite a usar funções como o .setValue()
    para empurrar novos valores para o DashboardState. Fazenmos isso empurrando uma nova instância completa.
     */
    private final MutableLiveData<DashboardState> _uiState = new MutableLiveData<>();

    /*
    Expomos apenas a interface LiveData, que é imutável, para ser observada. A Activity vai olhar essa saída. Como ela é imutável, a
    Activity vai poder apenas ler o que sai, sem poder alterar nada dentro da DashboardState, garantindo que a UI nunca vai alterar
    os dados por acidente
     */
    public LiveData<DashboardState> getUiState() {
        return _uiState;
    }

    // Criando nova thread para executar operações I/O no background
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DashboardViewModel(TransactionRepository repository) {
        this.repository = repository;

    }

    public void loadDashboardData() {
        // Aqui no instante zero da função, empurramos um DashboardState com isLoading = true e valores zerados, apenas para iniciarmos.
        _uiState.setValue(new DashboardState(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, true));

        /*
        A thread do ExecutorService vai ser responsável por ir no BD, fazer as operações necessários e criar um novo objeto DashboardState,
        dessa vez com valores reais, e isLoading = false.
         */
        executor.execute(() -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0);
            LocalDateTime endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);

            // Busca no room que travaria a main thread
            List<Transaction> transactions = repository.getTransactionsByPeriod(startOfMonth, endOfMonth);

            BigDecimal incomes = BigDecimal.ZERO;
            BigDecimal expenses = BigDecimal.ZERO;

            for (Transaction t : transactions) {
                if (t.getType() == TransactionType.INCOME) {
                    incomes = incomes.add(t.getAmount());
                } else {
                    expenses = expenses.add(t.getAmount());
                }
            }

            BigDecimal balance = incomes.subtract(expenses);

            /*
            Então finalmente podemos usar o postValue() para empurrar esse novo estado para a Activity. A antiga (com valores zerados) é
            descartada.
             */
            _uiState.postValue(new DashboardState(balance, incomes, expenses, false));
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
