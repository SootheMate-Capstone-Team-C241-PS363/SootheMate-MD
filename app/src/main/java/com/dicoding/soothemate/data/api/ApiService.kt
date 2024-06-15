package com.dicoding.soothemate.data.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @Multipart
    @POST("user/avatar")
    fun updateAvatar(@Part avatar: MultipartBody.Part): Call<UpdateUserInfoResponse>

    @GET("/tracking/history")
    fun getHistory(@Query("filter") filter: String?): Call<HistoryResponse>

    @GET("/tracking/history/{id}")
    fun getDetailHistory(@Path("id") id: String): Call<HistoryDetailResponse>

    @POST("auth/logout")
    fun logout(): Call<ApiResponse>

    @POST("stress/predict")
    fun predictStress(@Body request: PredictStressData): Call<PredictResponse>

    @POST("stress/save")
    fun savePredictStress(@Body request: SavePredictData): Call<SavePredictResponse>
}