package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(

	@field:SerializedName("data")
	val data: Any,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
