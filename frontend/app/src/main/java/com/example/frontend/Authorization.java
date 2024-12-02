package com.example.frontend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.frontend.LoginActivity;

public class Authorization {

    private static final String PREF_NAME = "user_prefs";
    private static final String TOKEN_KEY = "auth_token";
    private static SharedPreferences sharedPreferences;

    // Funkcja sprawdzająca, czy token istnieje i jest ważny
    public static String getAuthorizationToken(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, null);

        if (token == null || token.isEmpty()) {
            Log.e("AUTH", "No token found!");
            redirectToLogin(context); // Jeśli brak tokenu, przekierowanie do logowania
            return null;
        }

        Log.d("AUTH", "Token found: " + token);
        return token;
    }

    // Funkcja przekierowująca użytkownika do ekranu logowania
    public static void redirectToLogin(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class); // Przekierowanie do ekranu logowania
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Czyści stack aktywności
        context.startActivity(loginIntent);
    }

    // Funkcja do zapisania tokenu
    public static void saveToken(Context context, String token) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
        Log.d("AUTH", "Token saved: " + token);
    }

    // Funkcja do usunięcia tokenu (np. przy wylogowaniu)
    public static void clearToken(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
        Log.d("AUTH", "Token cleared");
    }
}
