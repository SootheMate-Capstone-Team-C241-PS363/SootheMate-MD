package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: RegisterData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class RegisterUser(

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

data class RegisterData(

	@field:SerializedName("access_token")
	val accessToken: String,

	@field:SerializedName("token_type")
	val tokenType: String,

	@field:SerializedName("expires_in")
	val expiresIn: Int,

	@field:SerializedName("user")
	val user: RegisterUser
)
