package com.example.gestaofinanceiraapp.presentation.budget;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaofinanceiraapp.databinding.ItemBudgetProgressBinding;
import com.example.gestaofinanceiraapp.domain.model.BudgetProgress;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// O adapter herda de RecyclerView.Adapter e exige um ViewHolder (a classe que possui os componentes visuais)
public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
    private List<BudgetProgress> budgets = new ArrayList<>();

    // Méto-do que o ViewModel deve chamar para atualizar a lista na tela
    public void submitList(List<BudgetProgress> newBudgets) {
        this.budgets = newBudgets;
        notifyDataSetChanged(); // Avisa o RecyclerView para redesenhar a tela
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o XML do item_budget_progress para cada linha nova que precisar
        ItemBudgetProgressBinding binding = ItemBudgetProgressBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BudgetViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        // Pega o item da posição atual e manda o ViewHolder preencher os textos
        BudgetProgress item = budgets.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }

    static class BudgetViewHolder extends RecyclerView.ViewHolder {
        private final ItemBudgetProgressBinding binding;
        private final NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        public BudgetViewHolder(ItemBudgetProgressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BudgetProgress item) {
            binding.txtCategoryName.setText(item.getCategoryName());

            String spent = format.format(item.getSpentAmount());
            String target = format.format(item.getTargetAmount());
            binding.txtAmountProgress.setText(spent + " / " + target);

            int percentage = item.getPercentageSpent();
            binding.progressBar.setProgress(percentage);

            // Regra de UI Semântica: Se passou de 100%, a barra e o texto ficam vermelhos
            if (percentage >= 100) {
                binding.progressBar.setIndicatorColor(Color.parseColor("#C62828")); // Vermelho
                binding.txtAmountProgress.setTextColor(Color.parseColor("#C62828"));
            } else {
                binding.progressBar.setIndicatorColor(Color.parseColor(item.getCategoryColor()));
                // Reseta a cor do texto para o padrão
                binding.txtAmountProgress.setTextColor(Color.parseColor("#49454F"));
            }
        }
    }

}
