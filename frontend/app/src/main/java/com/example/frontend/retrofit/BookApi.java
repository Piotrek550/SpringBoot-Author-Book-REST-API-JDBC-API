package com.example.frontend.retrofit;

import com.example.frontend.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookApi {
    @GET("/books")
    Call<List<Book>> getAllBooks(@Header("Authorization") String jwtToken);

    @PUT("books/{isbn}")
    Call<Book> createBook(@Header("Authorization") String jwtToken, @Path("isbn") String isbn, @Body Book book);

    @DELETE("/books/{isbn}")
    Call<Book> deleteBook(@Header("Authorization") String jwtToken, @Path("isbn") String isbn);

    @PATCH("/books/{isbn}")
    Call<Book> updateBook(@Header("Authorization") String jwtToken, @Path("isbn") String isbn, @Body Book book);
}
