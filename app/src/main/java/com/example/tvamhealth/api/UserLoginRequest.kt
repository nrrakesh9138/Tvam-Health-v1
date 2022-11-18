package com.example.tvamhealth.api

import com.google.gson.annotations.SerializedName

data class UserLoginRequest(
    @SerializedName("customerMobileNo") val customerMobileNo :String?,
    @SerializedName("password") val password :String?,

)
