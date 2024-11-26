package com.example.frontend.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;

public class AuthorHolder extends RecyclerView.ViewHolder {

    TextView name, age;

    public AuthorHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.authorListItem_name);
        age = itemView.findViewById(R.id.authorListItem_age);
    }

}
