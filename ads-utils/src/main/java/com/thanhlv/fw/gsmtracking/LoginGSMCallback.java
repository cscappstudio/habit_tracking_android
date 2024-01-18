package com.thanhlv.fw.gsmtracking;

public interface LoginGSMCallback {
    void loginSuccess(String accessToken);
    void loginFail();
}
