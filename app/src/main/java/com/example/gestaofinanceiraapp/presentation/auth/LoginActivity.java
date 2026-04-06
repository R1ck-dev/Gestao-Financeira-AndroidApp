package com.example.gestaofinanceiraapp.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestaofinanceiraapp.databinding.ActivityLoginBinding;
import com.example.gestaofinanceiraapp.presentation.dashboard.DashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AuthViewModelFactory factory = new AuthViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        binding.btnGoogleSignIn.setOnClickListener(v -> viewModel.loginWithGoogle(this));

        viewModel.getLoginSuccess().observe(this, welcomeMessage -> {
            if (welcomeMessage != null) {
                Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }
        });

        viewModel.getLoginError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, "Erro: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}