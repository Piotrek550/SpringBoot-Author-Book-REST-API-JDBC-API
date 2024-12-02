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

import com.example.frontend.model.Book;
import com.example.frontend.adapter.BookAdapter;
import com.example.frontend.retrofit.BookApi;
import com.example.frontend.retrofit.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private BookApi bookApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = findViewById(R.id.bookList_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RetrofitService retrofitService = new RetrofitService();
        bookApi = retrofitService.getRetrofit().create(BookApi.class);

        recyclerView = findViewById(R.id.bookList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton floatingActionButton = findViewById(R.id.bookList_fab);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BookFormActivity.class);
            startActivity(intent);
        });

        FloatingActionButton exportBooksToJsonFab = findViewById(R.id.bookExport_fab);
        exportBooksToJsonFab.setOnClickListener(view -> exportBooks());
    }

    private void loadBooks() {
        bookApi.getAllBooks()
                .enqueue(new Callback<List<Book>>() {
                    @Override
                    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Book>> call, Throwable t) {
                        Toast.makeText(BookListActivity.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Book> bookList) {
        bookAdapter = new BookAdapter(bookList, new BookAdapter.OnBookInteractionListener() {
            @Override
            public void onBookRemoved(Book book) {
                deleteBookFromDatabase(book);
            }

            @Override
            public void onBookEdit(Book book) {
                editBook(book);
            }
        });
        recyclerView.setAdapter(bookAdapter);

        // Dodanie obsługi przesuwania
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                bookAdapter.removeItem(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void editBook(Book book) {
        Intent intent = new Intent(this, BookFormActivity.class);
        intent.putExtra("BOOK_ISBN", book.getIsbn());
        intent.putExtra("BOOK_TITLE", book.getTitle());
        intent.putExtra("BOOK_AUTHOR", book.getAuthor());
        startActivity(intent);
    }

    private void deleteBookFromDatabase(Book book) {
        bookApi.deleteBook(book.getIsbn())
                .enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(BookListActivity.this, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BookListActivity.this, "Failed to delete book", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        Toast.makeText(BookListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void exportBooks() {
        if (bookAdapter != null && !bookAdapter.getBooks().isEmpty()) {
            List<Book> books = bookAdapter.getBooks();
            JsonExporter.exportBooksToJson(this, books);
            Toast.makeText(this, "Export initiated. Check notification for status.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No books to export!", Toast.LENGTH_SHORT).show();
        }
        // Zapisny plik bedzie w: '/storage/emulated/0/Android/data/com.example.frontend/files/ExportedData/ExportedBooks/books.json'
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }
}