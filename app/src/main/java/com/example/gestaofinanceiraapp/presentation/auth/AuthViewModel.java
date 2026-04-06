package com.example.gestaofinanceiraapp.presentation.auth;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestaofinanceiraapp.domain.auth.AuthRepository;

public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<String> _loginSuccess = new MutableLiveData<>();
    public LiveData<String> getLoginSuccess() { return _loginSuccess; }

    private final MutableLiveData<String> _loginError = new MutableLiveData<>();
    public LiveData<String> getLoginError() { return _loginError; }

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void loginWithGoogle(Context context) {
        authRepository.signInWithGoogle(context, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String email, String displayName, String idToken) {
                _loginSuccess.postValue("Bem-vindo, " + displayName + "!");
            }

            @Override
            public void onError(String errorMessage) {
                _loginError.postValue(errorMessage);
            }
        });
    }
}