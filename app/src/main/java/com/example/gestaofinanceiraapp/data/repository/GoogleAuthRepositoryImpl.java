package com.example.gestaofinanceiraapp.data.repository;

import android.content.Context;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.example.gestaofinanceiraapp.domain.auth.AuthRepository;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GoogleAuthRepositoryImpl implements AuthRepository {

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void signInWithGoogle(Context context, AuthCallback callback) {
        CredentialManager credentialManager = CredentialManager.create(context);

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("766472644831-ve9rd3la28oplvr9abf2a1addaq8nls5.apps.googleusercontent.com")
                .setAutoSelectEnabled(false)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(context, request, null, executor,
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse response) {
                        try {
                            CustomCredential credential = (CustomCredential) response.getCredential();
                            if (credential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                                GoogleIdTokenCredential googleToken = GoogleIdTokenCredential.createFrom(credential.getData());
                                callback.onSuccess(googleToken.getId(), googleToken.getDisplayName(), googleToken.getIdToken());
                            }
                        } catch (Exception e) {
                            callback.onError("Erro ao ler dados do Google.");
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        callback.onError("Falha no Login: " + e.getMessage());
                    }
                });
    }
}