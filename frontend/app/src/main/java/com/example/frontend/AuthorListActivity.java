package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.adapter.AuthorAdapter;
import com.example.frontend.model.Author;
import com.example.frontend.retrofit.AuthorApi;
import com.example.frontend.retrofit.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_author_list);

        recyclerView = findViewById(R.id.authorList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton floatingActionButton = findViewById(R.id.authorList_fab);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AuthorFormActivity.class);
            startActivity(intent);
        });
    }

    private void loadAuthors() {
        RetrofitService retrofitService = new RetrofitService();
        AuthorApi authorApi = retrofitService.getRetrofit().create(AuthorApi.class);
        authorApi.getAllAuthors()
                .enqueue(new Callback<List<Author>>() {
                    @Override
                    public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Author>> call, Throwable t) {
                        Toast.makeText(AuthorListActivity.this, "Failed to load authors", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Author> authorList) {
        AuthorAdapter authorAdapter = new AuthorAdapter(authorList);
        recyclerView.setAdapter(authorAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAuthors();
    }
}