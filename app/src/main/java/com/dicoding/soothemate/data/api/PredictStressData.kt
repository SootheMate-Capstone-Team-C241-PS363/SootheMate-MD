package com.dicoding.soothemate.data.api

import com.google.gson.annotations.SerializedName

data class PredictStressData(

	@field:SerializedName("min_working_hours")
	val minWorkingHours: Int,

	@field:SerializedName("max_working_hours")
	val maxWorkingHours: Int,

	@field:SerializedName("daily_steps")
	val dailySteps: Int? = null,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("physical_activity_level")
	val physicalActivityLevel: Int,

	@field:SerializedName("bmi_category")
	val bmiCategory: String? = null,

	@field:SerializedName("sleep_duration")
	val sleepDuration: Int,

	@field:SerializedName("heart_rate")
	val heartRate: Int? = null,

	@field:SerializedName("quality_of_sleep")
	val qualityOfSleep: Int,

	@field:SerializedName("blood_pressure")
	val bloodPressure: String? = null,

	@field:SerializedName("age")
	val age: Int
)
