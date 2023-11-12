package com.tvam.health.api

import com.google.gson.annotations.SerializedName

data class PatientInformationDetailsrResponse (
    @SerializedName("Success") val success: Boolean,
    @SerializedName("EngData") val engData: String?,
    @SerializedName("HindData") val hindData: String?,
    @SerializedName("Message") val message: String?,
)