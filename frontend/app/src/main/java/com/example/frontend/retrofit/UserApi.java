package com.example.frontend.retrofit;

import com.example.frontend.model.User;
import com.example.frontend.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("/register")
    Call<Void> registerUser(@Body User user);

    // Endpoint do logowania użytkownika
    @POST("/login")
    Call<UserResponse> loginUser(@Body User user);
}
