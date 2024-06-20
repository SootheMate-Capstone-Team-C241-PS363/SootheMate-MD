package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: LoginData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class LoginUser(

	@field:SerializedName("email_verified")
	val emailVerified: Boolean,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("birth_date")
	val birthDate: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String
)

data class LoginData(

	@field:SerializedName("access_token")
	val accessToken: String,

	@field:SerializedName("token_type")
	val tokenType: String,

	@field:SerializedName("expires_in")
	val expiresIn: Int,

	@field:SerializedName("user")
	val user: LoginUser
)

