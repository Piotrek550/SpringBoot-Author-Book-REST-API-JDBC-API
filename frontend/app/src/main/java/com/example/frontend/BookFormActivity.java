package com.example.frontend;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.frontend.model.Author;
import com.example.frontend.model.Book;
import com.example.frontend.retrofit.AuthorApi;
import com.example.frontend.retrofit.BookApi;
import com.example.frontend.retrofit.RetrofitService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFormActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private AutoCompleteTextView authorAutoComplete;
    private TextInputEditText inputEditIsbn;
    private TextView textViewExampleIsbn;
    private TextInputEditText inputEditTitle;
    private MaterialButton buttonSave;
    private BookApi bookApi;
    private AuthorApi authorApi;

    private Book existingBook; // Obiekt reprezentujący edytowaną książkę (jeśli jest)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_form);

        toolbar = findViewById(R.id.bookForm_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        authorAutoComplete = findViewById(R.id.form_bookAuthor);
        inputEditIsbn = findViewById(R.id.form_bookIsbn);
        textViewExampleIsbn = findViewById(R.id.example_bookIsbn);
        inputEditTitle = findViewById(R.id.form_bookTitle);
        buttonSave = findViewById(R.id.form_buttonCreateBook);

        RetrofitService retrofitService = new RetrofitService();
        bookApi = retrofitService.getRetrofit().create(BookApi.class);
        authorApi = retrofitService.getRetrofit().create(AuthorApi.class);

        // Pobierz autorów do AutoCompleteTextView
        loadAuthors();

        // Inicjalizacja formularza (wypełnienie danymi w przypadku edycji)
        initializeForm();

        // Obsługa zapisu książki
        buttonSave.setOnClickListener(view -> saveBook());
    }

    private void initializeForm() {
        // Pobierz dane książki z Intent (jeśli edytujemy książkę)
        String isbn = getIntent().getStringExtra("BOOK_ISBN");
        String title = getIntent().getStringExtra("BOOK_TITLE");
        Author author = (Author) getIntent().getSerializableExtra("BOOK_AUTHOR");

        if (isbn != null && title != null && author != null) {
            // Tryb edycji książki
            existingBook = new Book();
            existingBook.setIsbn(isbn);
            existingBook.setTitle(title);
            existingBook.setAuthor(author);

            // Wypełnij pola formularza
            inputEditIsbn.setText(isbn);
            inputEditTitle.setText(title);
            authorAutoComplete.setText(author.getName());
            authorAutoComplete.setTag(author);

            // ISBN nie można zmieniać (pole tylko do odczytu)
            inputEditIsbn.setEnabled(false);

            toolbar.setTitle("Edit Book");
            textViewExampleIsbn.setText("ISBN cannot be modified");
            buttonSave.setText("Edit");
        }
    }

    private void loadAuthors() {
        authorApi.getAllAuthors().enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Author> authors = response.body();

                    ArrayAdapter<Author> adapter = new ArrayAdapter<>(
                            BookFormActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            authors
                    );

                    authorAutoComplete.setAdapter(adapter);
                    authorAutoComplete.setThreshold(1);

                    authorAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
                        Author selectedAuthor = (Author) parent.getItemAtPosition(position);
                        authorAutoComplete.setTag(selectedAuthor);
                        Toast.makeText(BookFormActivity.this, "Selected: " + selectedAuthor, Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(BookFormActivity.this, "Failed to load authors!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                Toast.makeText(BookFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidIsbn(String isbn) {
        if (isbn.matches("[0-9-]+")) {
            String cleanedIsbn = isbn.replace("-", "");

            // ISBN to 10 lub 13 CYFR
            return (cleanedIsbn.length() == 10 || cleanedIsbn.length() == 13);
        }
        return false;
    }

    private void saveBook() {
        String isbn = String.valueOf(inputEditIsbn.getText());
        String title = String.valueOf(inputEditTitle.getText());
        Author selectedAuthor = (Author) authorAutoComplete.getTag();

        if (!isValidIsbn(isbn)) {
            inputEditIsbn.setError("Invalid ISBN");
            Toast.makeText(this, "Invalid ISBN", Toast.LENGTH_SHORT).show();
            return;
        } else { inputEditIsbn.setError(null); }

        if (selectedAuthor == null) {
            Toast.makeText(this, "Select an author!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (existingBook != null) {
            // Tryb edycji
            existingBook.setTitle(title);
            existingBook.setAuthor(selectedAuthor);

            bookApi.updateBook(isbn, existingBook).enqueue(new Callback<Book>() {
                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(BookFormActivity.this, "Book updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(BookFormActivity.this, "Failed to update book!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {
                    Toast.makeText(BookFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Logger.getLogger(BookFormActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                }
            });
        } else {
            // Tryb tworzenia nowej książki
            Book book = new Book();
            book.setIsbn(isbn);
            book.setTitle(title);
            book.setAuthor(selectedAuthor);

            bookApi.createBook(isbn, book).enqueue(new Callback<Book>() {
                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(BookFormActivity.this, "Book created successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(BookFormActivity.this, "Failed to create book!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {
                    Toast.makeText(BookFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Logger.getLogger(BookFormActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                }
            });
        }
    }
}