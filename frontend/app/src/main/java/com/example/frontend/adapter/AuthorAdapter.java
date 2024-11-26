package com.example.frontend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.frontend.R;
import com.example.frontend.model.Author;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorHolder> {
    private List<Author> authorList;

    public AuthorAdapter(List<Author> authorList) {
        this.authorList = authorList;
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
    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }
}
