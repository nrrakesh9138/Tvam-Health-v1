package com.example.tvamhealth.api

import com.example.tvamhealth.SubscriptionsResponse
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {


    @POST("/PatientInformationDetails")
    @Headers("Content-Type: application/json")
    fun patientInformation(@Body patientInformationDetailsRequest: PatientInformationDetailsRequest): Call<PatientInformationDetailsrResponse>

    @POST("/UserLogin")
    @Headers("Content-Type: application/json")
    fun userLogin(@Body userLoginRequest: UserLoginRequest): Call<UserLoginResponse>

    @GET("/Subscriptions")
    @Headers("Content-Type: application/json")
    fun getSubscriptions(): Call<SubscriptionsResponse>


}
