package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: PredictData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class PredictData(

	@field:SerializedName("stress_level")
	val stressLevel: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("description")
	val description: String
)
// =======
// 	val stressLevel: Int
// )
// >>>>>>> develop
