package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.model.User;
import com.example.frontend.retrofit.RetrofitService;
import com.example.frontend.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;
    private TextInputEditText inputConfirmPassword;
    private MaterialButton buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_form);

        inputUsername = findViewById(R.id.form_register_username);
        inputPassword = findViewById(R.id.form_register_password);
        inputConfirmPassword = findViewById(R.id.form_register_cpassword);
        buttonRegister = findViewById(R.id.form_buttonRegisterUser);

        findViewById(R.id.alreadyHaveAccount).setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        buttonRegister.setOnClickListener(view -> {
            String username = inputUsername.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String cpassword = inputConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.equals(cpassword)){
                registerUser(username, password);
            }else{
                Toast.makeText(this, "Password and Confirm Password must match!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void registerUser(String username, String password) {
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        User user = new User(username, password);

        userApi.registerUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}