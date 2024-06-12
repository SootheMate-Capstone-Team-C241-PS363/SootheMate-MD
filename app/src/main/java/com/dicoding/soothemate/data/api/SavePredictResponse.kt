package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class SavePredictResponse(

	@field:SerializedName("data")
	val data: SavePredictDataResponse,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class SavePredictDataResponse(

	@field:SerializedName("min_working_hours")
	val minWorkingHours: Int,

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("max_working_hours")
	val maxWorkingHours: Int,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("physical_activity_level")
	val physicalActivityLevel: Int,

	@field:SerializedName("sleep_duration")
	val sleepDuration: Int,

	@field:SerializedName("update_at")
	val updateAt: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("quality_of_sleep")
	val qualityOfSleep: Int,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("email")
	val email: String
)

data class Result(

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("stress_level")
	val stressLevel: Int
)
