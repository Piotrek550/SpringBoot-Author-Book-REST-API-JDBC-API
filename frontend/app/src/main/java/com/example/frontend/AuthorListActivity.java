package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.adapter.AuthorAdapter;
import com.example.frontend.model.Author;
import com.example.frontend.retrofit.AuthorApi;
import com.example.frontend.retrofit.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.frontend.JsonExporter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AuthorAdapter authorAdapter;
    private AuthorApi authorApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_author_list);

        Toolbar toolbar = findViewById(R.id.authorList_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RetrofitService retrofitService = new RetrofitService();
        authorApi = retrofitService.getRetrofit().create(AuthorApi.class);

        recyclerView = findViewById(R.id.authorList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton createAuthorFab = findViewById(R.id.authorList_fab);
        createAuthorFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AuthorFormActivity.class);
            startActivity(intent);
        });

        FloatingActionButton exportAuthorsToJsonFab = findViewById(R.id.authorExport_fab);
        exportAuthorsToJsonFab.setOnClickListener(view -> exportAuthors());

    }

    private void loadAuthors() {
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
        authorAdapter = new AuthorAdapter(authorList, new AuthorAdapter.OnAuthorInteractionListener(){
            @Override
            public void onAuthorRemoved(Author author) {
                deleteAuthorFromDatabase(author);
            }

            @Override
            public void onAuthorEdit(Author author) {
                editAuthor(author);
            }
        });
        recyclerView.setAdapter(authorAdapter);

        // Dodanie obsługi przesuwania
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // Nie obsługujemy przesuwania w górę/dół
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                authorAdapter.removeItem(position); // Usuń książkę z adaptera
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void editAuthor(Author author) {
        Intent intent = new Intent(this, AuthorFormActivity.class);
        intent.putExtra("AUTHOR_ID", author.getId());
        intent.putExtra("AUTHOR_NAME", author.getName());
        intent.putExtra("AUTHOR_AGE", author.getAge());
        startActivity(intent);
    }

    private void deleteAuthorFromDatabase(Author author) {
        authorApi.deleteAuthor(author.getId())
                .enqueue(new Callback<Author>() {
                    @Override
                    public void onResponse(Call<Author> call, Response<Author> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AuthorListActivity.this, "Author deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuthorListActivity.this, "Failed to delete author", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Author> call, Throwable t) {
                        Toast.makeText(AuthorListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void exportAuthors() {
        if (authorAdapter != null && !authorAdapter.getAuthors().isEmpty()) {
            List<Author> authors = authorAdapter.getAuthors();
            JsonExporter.exportAuthorsToJson(this, authors);
            Toast.makeText(this, "Export initiated. Check notification for status.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No authors to export!", Toast.LENGTH_SHORT).show();
        }
        // Zapisny plik bedzie w: '/storage/emulated/0/Android/data/com.example.frontend/files/ExportedData/ExportedAuthors/authors.json'
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAuthors();
    }
}