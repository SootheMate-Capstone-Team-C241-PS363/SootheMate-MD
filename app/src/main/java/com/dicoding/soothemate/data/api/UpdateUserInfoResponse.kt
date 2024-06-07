package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class UpdateUserInfoResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email_verified")
	val emailVerified: Boolean,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("birth_date")
	val birthDate: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("avatar")
	val avatar: String,

	@field:SerializedName("email")
	val email: String
)
