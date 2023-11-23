package com.tvam.health.models.request

import com.google.gson.annotations.SerializedName

class PatientInformationDetailsRequest (
    @SerializedName("customerName") val customerName :String?,
    @SerializedName("customerMobileNo") val customerMobile :String?,
    @SerializedName("isSmartPhone") val isSmartPhone :String?,

    @SerializedName("smartMobileNo")
    var smartPhoneMobile: String?,
    @SerializedName("age")
    var age: String?,
    @SerializedName("gender")
    var gender: String?,
    @SerializedName("testDate")
    var testDate: String?,
    @SerializedName("weight") val weight :String?,
    @SerializedName("temperature") val temperature :String?,
    @SerializedName("systolicBloodPressure") val systolicBloodPressure :String?,
    @SerializedName("diastolicBloodPressure") val diastolicBloodPressure :String?,
    @SerializedName("createdBy")
    var createdBy: String?,
    @SerializedName("pluseRate")
    var pluseRate: String?,
    @SerializedName("hemoglobin")
    var hemoglobin: String?,
    @SerializedName("fundalHeight")
    var fundalHeight: String?,
    @SerializedName("fetalHeartRate") val fetalHeartRate :String?,
    @SerializedName("randomBloodSugar")
    var randomBloodSugar: String?,
    @SerializedName("urineProtein")
    var urineTest: String?,

    @SerializedName("urineSugar")
    var urineSugar: String?,
    @SerializedName("SubscriptionsType")
    var SubscriptionsType: String?
)