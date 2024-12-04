package com.example.frontend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.model.User;
import com.example.frontend.model.UserResponse;
import com.example.frontend.retrofit.RetrofitService;
import com.example.frontend.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputUsername, inputPassword;
    private MaterialButton buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_form);

        inputUsername = findViewById(R.id.form_login_username);
        inputPassword = findViewById(R.id.form_login_password);
        buttonLogin = findViewById(R.id.form_buttonLoginUser);

        findViewById(R.id.noAccountYet).setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        buttonLogin.setOnClickListener(view -> {
            String username = inputUsername.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (username.isEmpty()) {
                inputUsername.setError("Username section cannot be empty!");
                Toast.makeText(this, "Username section cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password section cannot be empty!");
                Toast.makeText(this, "Password section cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(this, username, password);
        });
    }

    private void loginUser(Context context, String username, String password) {
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        User user = new User(username, password);

        userApi.loginUser(user).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    Authorization.saveToken(context, token);

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

