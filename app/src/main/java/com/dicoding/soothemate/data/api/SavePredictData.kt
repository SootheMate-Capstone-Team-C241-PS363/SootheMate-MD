package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class SavePredictData(

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("sleep_duration")
	val sleepDuration: Int,

	@field:SerializedName("quality_of_sleep")
	val qualityOfSleep: Int,

	@field:SerializedName("physical_activity_level")
	val physicalActivityLevel: Int,

	@field:SerializedName("min_working_hours")
	val minWorkingHours: Int,

	@field:SerializedName("max_working_hours")
	val maxWorkingHours: Int,

	@field:SerializedName("result")
	val result: SavePredictDataResult

)

data class SavePredictDataResult(

	@field:SerializedName("stress_level")
	val stressLevel: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("description")
	val description: String
)
