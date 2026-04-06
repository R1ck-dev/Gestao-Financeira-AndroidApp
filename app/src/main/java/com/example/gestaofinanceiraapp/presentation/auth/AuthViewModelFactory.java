package com.example.gestaofinanceiraapp.presentation.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestaofinanceiraapp.data.repository.GoogleAuthRepositoryImpl;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(new GoogleAuthRepositoryImpl());
        }
        throw new IllegalArgumentException("ViewModel desconhecido");
    }
}