package com.example.gestaofinanceiraapp.domain.auth;

import android.content.Context;

public interface AuthRepository {
    interface AuthCallback {
        void onSuccess(String email, String displayName, String idToken);
        void onError(String errorMessage);
    }

    void signInWithGoogle(Context context, AuthCallback callback);
}