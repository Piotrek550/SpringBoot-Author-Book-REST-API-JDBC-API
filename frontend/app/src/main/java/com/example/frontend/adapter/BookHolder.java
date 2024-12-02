package com.example.frontend.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;

public class BookHolder extends RecyclerView.ViewHolder {
    TextView isbn, title, author;

    public BookHolder(@NonNull View itemView) {
        super(itemView);
        isbn = itemView.findViewById(R.id.bookListItem_isbn);
        title = itemView.findViewById(R.id.bookListItem_title);
        author = itemView.findViewById(R.id.bookListItem_author);
    }
}
