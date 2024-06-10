package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class PredictData(

	@field:SerializedName("stress_level")
	val stressLevel: Int
)
