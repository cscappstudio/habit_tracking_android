package com.cscmobi.habittrackingandroid.utils;

import static com.thanhlv.fw.constant.AppConfigs.accessTokenGSM;

import android.content.Context;

import androidx.annotation.NonNull;

import com.cscmobi.habittrackingandroid.BuildConfig;
import com.cscmobi.habittrackingandroid.R;
import com.google.gson.JsonObject;
import com.thanhlv.fw.gsmtracking.ApiGSM;
import com.thanhlv.fw.gsmtracking.GSMClient;
import com.thanhlv.fw.gsmtracking.LoginGSMCallback;
import com.thanhlv.fw.gsmtracking.VerifyIAPGSMCallback;
import com.thanhlv.fw.helper.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GSMUtil {

    final static String DOMAIN_GSM_PROD = "https://gsm.cscmobicorp.com";
    final static String DOMAIN_GSM_DEV = "https://gsmdev.cscmobicorp.com";
    public static int retryLoginGSM = 0;
    public static void login(Context context, LoginGSMCallback loginGSMCallback) {

        JsonObject params = new JsonObject();
        params.addProperty("appId", context.getString(R.string.gsm_app_id));
        params.addProperty("deviceId", MyUtils.Companion.getDeviceID(context.getApplicationContext()));
        params.addProperty("pkName", context.getPackageName());
        params.addProperty("os", 1); // 1 = android, 2 = ios
        params.addProperty("version", BuildConfig.VERSION_NAME);

        GSMClient.getClient(BuildConfig.DEBUG ? DOMAIN_GSM_DEV : DOMAIN_GSM_PROD)
                .create(ApiGSM.class)
                .login(params)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body() != null && new JSONObject(response.body().toString()).has("accessToken")) {
                                    JSONObject obj = new JSONObject(response.body().toString());
                                    accessTokenGSM = obj.getString("accessToken");
                                    System.out.println("thanhlv set access token GSM ======= " + accessTokenGSM);
                                    if (loginGSMCallback != null) {
                                        loginGSMCallback.loginSuccess(accessTokenGSM);
                                    }
                                }
                            } catch (JSONException e) {
                                if (retryLoginGSM++ < 4) {
                                    login(context, loginGSMCallback);
                                }
                            }
                        } else {
                            if (retryLoginGSM++ < 4) {
                                login(context, loginGSMCallback);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                        login(context, loginGSMCallback);
                    }
                });
    }

    public static void verifyIAP(Context context, JsonObject params, VerifyIAPGSMCallback verifyIAPGSMCallback) {

        if (!accessTokenGSM.equals("")) {
            GSMClient.getClient(BuildConfig.DEBUG ? DOMAIN_GSM_DEV : DOMAIN_GSM_PROD)
                    .create(ApiGSM.class)
                    .verifyIAP("Bearer "+accessTokenGSM, params)
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                System.out.println("thanhlv verifyIAP GSM ------ " + response.body());
                                if (verifyIAPGSMCallback != null)
                                    verifyIAPGSMCallback.verifySuccess();
                            } else {
                                if (verifyIAPGSMCallback != null)
                                    verifyIAPGSMCallback.verifyFail();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                            if (verifyIAPGSMCallback != null)
                                verifyIAPGSMCallback.verifyFail();
                        }
                    });
        } else {
            retryLoginGSM = 0;
            //khi chua co accessToken thi phai call login GSM de lay token truoc
            login(context, new LoginGSMCallback() {
                @Override
                public void loginSuccess(String accessToken) {
                    verifyIAP(context, params, verifyIAPGSMCallback);
                }

                @Override
                public void loginFail() {

                }
            });
        }

    }

    public static void verifySUBS(Context context, JsonObject params, VerifyIAPGSMCallback verifyIAPGSMCallback) {

        if (!accessTokenGSM.equals("")) {
            GSMClient.getClient(BuildConfig.DEBUG ? DOMAIN_GSM_DEV : DOMAIN_GSM_PROD)
                    .create(ApiGSM.class)
                    .verifySubs("Bearer "+accessTokenGSM, params)
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                System.out.println("thanhlv verifySubs GSM ------ " + response.body());
                                if (verifyIAPGSMCallback != null)
                                    verifyIAPGSMCallback.verifySuccess();
                            } else {
                                if (verifyIAPGSMCallback != null)
                                    verifyIAPGSMCallback.verifyFail();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                            if (verifyIAPGSMCallback != null)
                                verifyIAPGSMCallback.verifyFail();
                        }
                    });
        } else {
            retryLoginGSM = 0;
            //khi chua co accessToken thi phai call login GSM de lay token truoc
            login(context, new LoginGSMCallback() {
                @Override
                public void loginSuccess(String accessToken) {
                    verifySUBS(context, params, verifyIAPGSMCallback);
                }

                @Override
                public void loginFail() {

                }
            });
        }

    }
}
