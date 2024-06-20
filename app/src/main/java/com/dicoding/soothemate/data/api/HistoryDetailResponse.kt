package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class HistoryDetailResponse(

	@field:SerializedName("data")
	val data: HistoryDetailData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class HistoryDetailData(

	@field:SerializedName("min_working_hours")
	val minWorkingHours: Int,

	@field:SerializedName("max_working_hours")
	val maxWorkingHours: Int,

	@field:SerializedName("daily_steps")
	val dailySteps: Int,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("physical_activity_level")
	val physicalActivityLevel: Int,

	@field:SerializedName("bmi_category")
	val bmiCategory: String,

	@field:SerializedName("sleep_duration")
	val sleepDuration: Int,

	@field:SerializedName("heart_rate")
	val heartRate: Int,

	@field:SerializedName("quality_of_sleep")
	val qualityOfSleep: Int,

	@field:SerializedName("blood_pressure")
	val bloodPressure: String,

	@field:SerializedName("age")
	val age: Int
)