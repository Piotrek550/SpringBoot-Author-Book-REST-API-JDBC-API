package com.example.frontend.retrofit;

import com.example.frontend.AuthorFormActivity;
import com.example.frontend.model.Author;
import com.example.frontend.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthorApi {

    @GET("/authors")
    Call<List<Author>> getAllAuthors(@Header("Authorization") String jwtToken);

    @POST("/authors")
    Call<Author> createAuthor(@Header("Authorization") String jwtToken, @Body Author author);

    @DELETE("/authors/{id}")
    Call<Author> deleteAuthor(@Header("Authorization") String jwtToken, @Path("id") Long id);

    @PATCH("/authors/{id}")
    Call<Author> updateAuthor(@Header("Authorization") String jwtToken, @Path("id") Long id, @Body Author author);
}
