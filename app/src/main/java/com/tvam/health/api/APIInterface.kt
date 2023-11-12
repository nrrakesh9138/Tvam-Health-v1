package com.tvam.health.api

import com.tvam.health.SubscriptionsResponse
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {


    @POST("/Health/PatientInformationDetails")
    @Headers("Content-Type: application/json")
    fun patientInformation(@Body patientInformationDetailsRequest: PatientInformationDetailsRequest): Call<PatientInformationDetailsrResponse>

    @POST("/Health/UserLogin")
    @Headers("Content-Type: application/json")
    fun userLogin(@Body userLoginRequest: UserLoginRequest): Call<UserLoginResponse>

    @GET("/Health/Subscriptions")
    @Headers("Content-Type: application/json")
    fun getSubscriptions(): Call<SubscriptionsResponse>


}
