package com.example.frontend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.model.Author;
import com.example.frontend.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookHolder> {
    private List<Book> bookList;
    private OnBookInteractionListener onBookInteractionListener;

    public BookAdapter(List<Book> bookList, OnBookInteractionListener onBookInteractionListener) {
        this.bookList = bookList;
        this.onBookInteractionListener = onBookInteractionListener;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_book_item, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = bookList.get(position);
        Author author = book.getAuthor();
        holder.isbn.setText(book.getIsbn());
        holder.title.setText(book.getTitle());
        holder.author.setText(author.getName());

        // Kliknięcie w element -> edycja
        holder.itemView.setOnClickListener(v -> {
            if (onBookInteractionListener != null) {
                onBookInteractionListener.onBookEdit(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public List<Book> getBooks() {
        return bookList;
    }

    public void removeItem(int position) {
        Book book = bookList.get(position);

        if (onBookInteractionListener != null) {
            onBookInteractionListener.onBookRemoved(book);
        }

        bookList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnBookInteractionListener {
        void onBookRemoved(Book book);
        void onBookEdit(Book book);
    }
}
