package com.mbahgojol.chami.data.remote


import com.google.gson.annotations.SerializedName

data class ResponsePushNotif(
    @SerializedName("canonical_ids")
    var canonicalIds: Int,
    @SerializedName("failure")
    var failure: Int,
    @SerializedName("multicast_id")
    var multicastId: Long,
    @SerializedName("results")
    var results: List<Result>,
    @SerializedName("success")
    var success: Int
) {
    data class Result(
        @SerializedName("error")
        var error: String
    )
}