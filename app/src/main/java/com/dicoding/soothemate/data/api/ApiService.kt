package com.dicoding.soothemate.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: UserCredentials): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: UserCredentials): RegisterResponse

    @PUT("auth/update-password")
    fun updatePassword(@Body request: ResetUserCredentials): Call<ApiResponse>

    @GET("user/detail")
    fun getDetailUser(): Call<DetailProfile>

    @PUT("user/update")
    fun updateUserInfo(@Body request: UpdateUserInfo): Call<UpdateUserInfoResponse>

    @POST("auth/logout")
    fun logout(): Call<ApiResponse>
}