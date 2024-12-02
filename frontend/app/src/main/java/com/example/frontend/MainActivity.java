package com.example.frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.frontend.model.Author;
import com.example.frontend.retrofit.AuthorApi;
import com.example.frontend.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.homePage_toolbar);
        setSupportActionBar(toolbar);

        Button buttonGoToAuthors = findViewById(R.id.toAuthors_button);
        Button buttonGoToBooks = findViewById(R.id.toBooks_button);

        FloatingActionButton fabLogout = findViewById(R.id.fab_logout);
        fabLogout.setOnClickListener(view -> logout());

        buttonGoToAuthors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AuthorListActivity.class);
                startActivity(intent);
            }
        });

        buttonGoToBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("auth_token");
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
