package com.example.frontend.retrofit;

import com.example.frontend.AuthorFormActivity;
import com.example.frontend.model.Author;
import com.example.frontend.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthorApi {

    @GET("/authors")
    Call<List<Author>> getAllAuthors();

    @POST("/authors")
    Call<Author> createAuthor(@Body Author author);

    @DELETE("/authors/{id}")
    Call<Author> deleteAuthor(@Path("id") Long id);

    @PATCH("/authors/{id}")
    Call<Author> updateAuthor(@Path("id") Long id, @Body Author author);
}
