package com.example.commit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.commit.base.base;
import com.example.commit.data.SessionCallback;
import com.example.commit.databinding.ActivityIntroBinding;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.auth.StringSet;
import com.kakao.usermgmt.LoginButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IntroActivity extends base {
    ActivityIntroBinding Binding;
    Animation Blink;
    Dialog dlg, logdlg;
    Session session;
    Button login;
    private SessionCallback sessionCallback = new SessionCallback(this);
    private static final int PERMISSION_CODE = 100;
    private String[] PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_intro);
        Blinking();

        Binding.intro.setOnClickListener(Intro);
    }
    private void autoLogin(){
        Map<String, String> extraParams = new HashMap<>();
        extraParams.put(StringSet.auto_login, "true");
        Session.getCurrentSession().open(AuthType.KAKAO_TALK_ONLY, this, extraParams); //자동 로그인
    }
    View.OnClickListener Intro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!hasPermissions(IntroActivity.this, PERMISSION)){
                ActivityCompat.requestPermissions(IntroActivity.this, PERMISSION, PERMISSION_CODE);
            }else {
                login();
            }
        }
    };
    private boolean hasPermissions(Context context, String... permissions){
        if (context != null && permissions != null){
            for (String permission : permissions){
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    login();
                } else {
                    perDia();
                }
        }
    }
    private void Blinking(){
        Blink = new AlphaAnimation(0.0f, 1.0f);
        Blink.setStartOffset(50);
        Blink.setDuration(2400);
        Blink.setRepeatCount(Animation.INFINITE);
        Blink.setRepeatMode(Animation.REVERSE);
        Binding.introBlink.startAnimation(Blink);

        Glide.with(this).load(R.drawable.grim).into(Binding.introImg);
    }
    private void login(){
        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        autoLogin();
        logDia();
    }
    private void logDia(){
        logdlg = new Dialog(this);
        logdlg.setContentView(R.layout.dialog_login);
        logdlg.setCancelable(false);
        logdlg.show();
        login = logdlg.findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.open(AuthType.KAKAO_TALK_ONLY, IntroActivity.this);
            }
        });
    }
    private void perDia(){
        dlg = new Dialog(this);
        dlg.setContentView(R.layout.dialog_permission);
        dlg.setCancelable(false);
        dlg.show();
        Button okButton = (Button) dlg.findViewById(R.id.permission_btn);
        okButton.setOnClickListener(grant);
    }
    Button.OnClickListener grant = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse(
                    "package:" + getApplicationContext().getPackageName()
            ));
            startActivity(intent);
            dlg.dismiss();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}