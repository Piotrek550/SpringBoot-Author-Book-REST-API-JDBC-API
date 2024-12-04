package com.example.frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class AuthorFormActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText inputEditName;
    private TextInputEditText inputEditAge;
    private MaterialButton buttonSave;

    private RetrofitService retrofitService;
    private AuthorApi authorApi;

    private Author existingAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_author_form);

        toolbar = findViewById(R.id.authorForm_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        retrofitService = new RetrofitService();
        authorApi = retrofitService.getRetrofit().create(AuthorApi.class);

        inputEditName = findViewById(R.id.form_authorName);
        inputEditAge = findViewById(R.id.form_authorAge);
        buttonSave = findViewById(R.id.form_buttonCreateAuthor);

        this.initializeFrom();

        buttonSave.setOnClickListener(view -> saveAuthor());
    }

    private void initializeFrom(){
        Long id = getIntent().getLongExtra("AUTHOR_ID", -1);
        String name = getIntent().getStringExtra("AUTHOR_NAME");
        Integer age = getIntent().getIntExtra("AUTHOR_AGE", -1);

        if( name != null && age != -1){
            existingAuthor = new Author();
            existingAuthor.setId(id);
            existingAuthor.setName(name);
            existingAuthor.setAge(age);

            inputEditName.setText(name);
            inputEditAge.setText(String.valueOf(age));

            toolbar.setTitle("Edit Author");
            buttonSave.setText("Edit");
        }
    }

    private void saveAuthor() {
        String token = Authorization.getAuthorizationToken(this);

        String name = String.valueOf(inputEditName.getText());
        String ageText = String.valueOf(inputEditAge.getText());

        if (name.isEmpty()) {
            inputEditName.setError("Name is required!");
            Toast.makeText(this, "Name is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ageText.isEmpty()) {
            inputEditAge.setError("Age is required!");
            Toast.makeText(this, "Age is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            inputEditAge.setError("Age must be a valid number!");
            Toast.makeText(this, "Age must be a valid number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (existingAuthor != null) {
            existingAuthor.setName(name);
            existingAuthor.setAge(age);

            authorApi.updateAuthor("Bearer "+token, existingAuthor.getId(), existingAuthor).enqueue(new Callback<Author>() {
                @Override
                public void onResponse(Call<Author> call, Response<Author> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AuthorFormActivity.this, "Author updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AuthorFormActivity.this, "Failed to update author!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Author> call, Throwable t) {
                    Toast.makeText(AuthorFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Logger.getLogger(AuthorFormActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                }
            });
        }else{
            Author author = new Author();
            author.setName(name);
            author.setAge(age);

            authorApi.createAuthor("Bearer "+token, author)
                    .enqueue(new Callback<Author>() {
                        @Override
                        public void onResponse(Call<Author> call, Response<Author> response) {
                            Toast.makeText(AuthorFormActivity.this, "Save successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Author> call, Throwable t) {
                            Toast.makeText(AuthorFormActivity.this, "Save failed!!!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(AuthorFormActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });
        }
    }
}