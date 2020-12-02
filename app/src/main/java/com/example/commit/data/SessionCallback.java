package com.example.commit.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.commit.MainActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

public class SessionCallback implements ISessionCallback {
    Context mcontext;
    String TAG = "KAKAO API";

    public SessionCallback(Context context) {
        mcontext = context;
    }
    // 로그인에 성공한 상태
    @Override
    public void onSessionOpened() {
        requestMe();
    }

    // 로그인에 실패한 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
    }

    // 사용자 정보 요청
    public void requestMe() {
        UserManagement.getInstance()
                .me(new MeV2ResponseCallback() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e(TAG, "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e(TAG, "사용자 정보 요청 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        long kakaoAccount = result.getId();
                        Log.d(TAG, "카카오 ID: " + kakaoAccount);
                        Intent intent = new Intent(mcontext, MainActivity.class);
                        intent.putExtra("user", kakaoAccount);
                        mcontext.startActivity(intent);
                    }
                });
    }
}