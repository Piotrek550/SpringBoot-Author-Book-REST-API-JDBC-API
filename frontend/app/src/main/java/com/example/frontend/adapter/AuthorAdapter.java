package com.example.frontend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.frontend.R;
import com.example.frontend.model.Author;
import com.example.frontend.model.Book;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorHolder> {
    private List<Author> authorList;
    private OnAuthorInteractionListener onAuthorInteractionListener;

    public AuthorAdapter(List<Author> authorList, OnAuthorInteractionListener onAuthorInteractionListener) {
        this.authorList = authorList;
        this.onAuthorInteractionListener = onAuthorInteractionListener;
    }

    @NonNull
    @Override
    public AuthorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_author_item, parent, false);
        return new AuthorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorHolder holder, int position) {
        Author author = authorList.get(position);
        holder.name.setText(author.getName());
        holder.age.setText(String.valueOf(author.getAge()));

        // Kliknięcie w element -> edycja
        holder.itemView.setOnClickListener(v -> {
            if (onAuthorInteractionListener != null) {
                onAuthorInteractionListener.onAuthorEdit(author);
            }
        });
    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    public List<Author> getAuthors() {
        return authorList;
    }

    public void removeItem(int position) {
        Author author = authorList.get(position);

        if (onAuthorInteractionListener != null) {
            onAuthorInteractionListener.onAuthorRemoved(author);
        }

        authorList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnAuthorInteractionListener {
        void onAuthorRemoved(Author author);
        void onAuthorEdit(Author author);
    }
}
