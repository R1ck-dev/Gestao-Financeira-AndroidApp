package com.example.gestaofinanceiraapp.presentation.history;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestaofinanceiraapp.databinding.ItemTransactionHistoryBinding;
import com.example.gestaofinanceiraapp.domain.model.TransactionHistoryItem;
import com.example.gestaofinanceiraapp.domain.model.TransactionType;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder> {
    private List<TransactionHistoryItem> items = new ArrayList<>();

    public void submitList(List<TransactionHistoryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        ItemTransactionHistoryBinding binding = ItemTransactionHistoryBinding.inflate(
                android.view.LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new TransactionHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionHistoryBinding binding;
        private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        public TransactionHistoryViewHolder(ItemTransactionHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TransactionHistoryItem item) {
            binding.txtCategoryName.setText(item.getCategoryName());

            String description = item.getDescription();
            if (description != null && !description.trim().isEmpty()) {
                binding.txtDescription.setText(description);
                binding.txtDescription.setVisibility(View.VISIBLE);
            } else {
                binding.txtDescription.setText(null);
                binding.txtDescription.setVisibility(View.GONE);
            }

            binding.txtDate.setText(item.getDate().format(dateFormatter));

            binding.txtAmount.setText(currencyFormat.format(item.getSignedAmount()));

            int color;
            if (item.getType() == TransactionType.EXPENSE) {
                color = Color.parseColor("#C62828");
            } else {
                color = Color.parseColor("#2E7D32");
            }
            binding.txtAmount.setTextColor(color);
            binding.txtTypeDot.setBackgroundColor(color);
        }
    }
}

