package com.example.commit.base;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.commit.R;
import com.example.commit.databinding.ActivityIntroBinding;

public class base extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }
    protected void Toast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}