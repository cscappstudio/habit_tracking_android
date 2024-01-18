package com.thanhlv.fw.helper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.thanhlv.fw.model.LogEventPurchaseModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.thanhlv.fw.constant.AppConfigs.Companion.EVENT_IN_APP_PURCHASE_CUSTOM


class FirebaseHelper(context_: Context) {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    init {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context_.applicationContext)
        }
    }

    fun logEventToFirebase(event: String) {
        if (mFirebaseAnalytics != null) {
            mFirebaseAnalytics!!.logEvent(event, Bundle())
            println("thanhlv logEvent logEventToFirebase $event")
        }
    }
    fun logEventToFirebase(event: String, args: Bundle) {
        if (mFirebaseAnalytics != null) {
            mFirebaseAnalytics!!.logEvent(event, args)
            println("thanhlv logEvent logEventToFirebase $event")
        }
    }
    fun logEventPurchaseToFirebase(params: LogEventPurchaseModel) {
        if (mFirebaseAnalytics != null) {
            val bundle = Bundle()
            bundle.putString("source", params.source)
            bundle.putString("demand", params.demand)
            bundle.putDouble("decimalvalue", params.decimalvalue)
            mFirebaseAnalytics!!.logEvent(EVENT_IN_APP_PURCHASE_CUSTOM, bundle)
            println("thanhlv logEvent logEventPurchaseToFirebase $params")
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: FirebaseHelper? = null
        fun getInstance(context_: Context): FirebaseHelper {
            if (INSTANCE == null) {
                INSTANCE = FirebaseHelper(context_)
            }
            return INSTANCE!!
        }
    }
}