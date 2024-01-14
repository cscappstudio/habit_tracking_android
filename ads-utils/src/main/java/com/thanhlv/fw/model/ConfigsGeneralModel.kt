package com.thanhlv.fw.model

import com.google.gson.annotations.SerializedName


class ConfigsGeneralModel {
    @SerializedName("emailSupport")
    var emailSupport = "support@cscmobi.com"

    @SerializedName("shareText")
    var shareText = "Dog Translator: Train & Sounds"

    @SerializedName("termsURL")
    var termsURL = "https://cscmobi.com/privacy-policy/"

    @SerializedName("privacyURL")
    var privacyURL = "https://cscmobi.com/privacy-policy/"

    @SerializedName("moreAppURL")
    var moreAppURL = "https://play.google.com/store/apps/dev?id=9128344770332001884"

    @SerializedName("adsPlatform")
    var adsPlatform = "admod"

    @SerializedName("lastVersion")
    var lastVersion = "1.0.0"

    @SerializedName("updateMessage")
    var updateMessage = "Please update to best performance"

    @SerializedName("updateURL")
    var updateURL = "https://play.google.com/store/apps/details?id=%s"

    @SerializedName("showUpdate")
    var isShowUpdate = false

    @SerializedName("forceUpdate")
    var isForceUpdate = false

    @SerializedName("subscription")
    var subscription = "[{\"name\": \"month\", \"keyID\": \"cscmobi.screencasttv.monthly\"}," +
            "{\"name\": \"year\",\"keyID\": \"cscmobi.screencasttv.yearly\"}," +
            "{\"name\": \"lifetime\",\"keyID\": \"cscmobi.casttv.lifetime\"}]"
}
