package com.example.tvamhealth.api

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @SerializedName("Success") val success: Boolean,
    @SerializedName("Message") val message: String?,
    @SerializedName("CustomerName") val customerName: String?,

)
