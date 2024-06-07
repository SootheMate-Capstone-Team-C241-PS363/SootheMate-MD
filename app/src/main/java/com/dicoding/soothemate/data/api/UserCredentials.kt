package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class UserCredentials(
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String,
    @field:SerializedName("gender")
    val gender : String,
    @field:SerializedName("birth_date")
    val birthDate : String
)

data class ResetUserCredentials(
    @field:SerializedName("old_password")
    val oldPassword: String,
    @field:SerializedName("password")
    val newPassword: String,
    @field:SerializedName("password_confirmation")
    val passwordConfirmation: String
)

data class UpdateUserInfo(
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("gender")
    val gender : String,
    @field:SerializedName("birth_date")
    val birthDate : String
)

