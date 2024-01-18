package com.thanhlv.fw.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubscriptionsModel : Serializable {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("keyID")
    var keyID: String? = null
}
