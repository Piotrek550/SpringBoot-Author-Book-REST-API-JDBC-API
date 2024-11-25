package com.example.frontend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.frontend.model.Author;
import com.example.frontend.retrofit.AuthorApi;
import com.example.frontend.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;
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

        this.initializeComponents();
    }

    private void initializeComponents() {
        TextInputEditText inputEditName = findViewById(R.id.form_authorName);
        TextInputEditText inputEditAge = findViewById(R.id.form_authorAge);
        MaterialButton buttonSave = findViewById(R.id.form_buttonCreate);

        RetrofitService retrofitService = new RetrofitService();
        AuthorApi authorApi = retrofitService.getRetrofit().create(AuthorApi.class);

        buttonSave.setOnClickListener(view -> {
            String name = String.valueOf(inputEditName.getText());
            Integer age = Integer.valueOf(String.valueOf(inputEditAge.getText()));

            Author author = new Author();
            author.setName(name);
            author.setAge(age);

            authorApi.createAuthor(author)
                    .enqueue(new Callback<Author>() {
                        @Override
                        public void onResponse(Call<Author> call, Response<Author> response) {
                            Toast.makeText(MainActivity.this, "Save successful!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Author> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Save failed!!!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });
        });
    }
}