package com.example.commit.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.commit.R;
import com.example.commit.databinding.ActivityFollowBinding;

public class FollowActivity extends AppCompatActivity {
    ActivityFollowBinding Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_follow);

        Binding.followFollowerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.followFollowerBtn.setBackgroundColor(Color.parseColor("#34eeee"));
                Binding.followFollowingBtn.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });
        Binding.followFollowingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.followFollowerBtn.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.followFollowingBtn.setBackgroundColor(Color.parseColor("#34eeee"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
