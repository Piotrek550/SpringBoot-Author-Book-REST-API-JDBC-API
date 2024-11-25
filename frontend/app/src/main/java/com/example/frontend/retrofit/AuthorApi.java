package com.example.frontend.retrofit;

import com.example.frontend.model.Author;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthorApi {

    @GET("/authors")
    Call<List<Author>> getAllAuthors();

    @POST("/authors")
    Call<Author> createAuthor(@Body Author author);
}
