package com.tvam.health.models.request

import com.google.gson.annotations.SerializedName

data class UserLoginRequest(
    @SerializedName("customerMobileNo") val customerMobileNo :String?,
    @SerializedName("password") val password :String?,

)
