package com.tvam.health

import com.google.gson.annotations.SerializedName

data class SubscriptionsResponse (
    @SerializedName("data") val data: ArrayList<DataSubscription>?,
    @SerializedName("ResponseCode") val ResponseCode: String?,
    @SerializedName("Message") val message: String?,
        )


data class DataSubscription(
        @SerializedName("ID")
        val ID: Int?,
        @SerializedName("SubscriptionType")
        val SubscriptionType: String?,
        @SerializedName("Status")
        val Status: String?,
        @SerializedName("SubscriptionDate")
        val SubscriptionDate: String?,
@SerializedName("Subscription")
        val Subscription: String?)
//): Parcelable {
//        constructor(parcel: Parcel) : this(
//                parcel.readString(),
//                parcel.readString(),
//                parcel.readString(),
//                parcel.readString(),
//        )
//
//
//        companion object CREATOR : Parcelable.Creator<DataSubscription> {
//                override fun createFromParcel(parcel: Parcel): DataSubscription {
//                        return DataSubscription(parcel)
//                }
//
//                override fun newArray(p0: Int): Array<DataSubscription?> {
//                        return arrayOfNulls(p0)
//                }
//        }
//
//        override fun describeContents(): Int {
//                return 0
//        }
//
//        override fun writeToParcel(parcel: Parcel?, p1: Int) {
//                parcel?.writeString(ID)
//                parcel?.writeString(SubscriptionType)
//                parcel?.writeString(Status)
//                parcel?.writeString(SubscriptionDate)
//        }
//}
