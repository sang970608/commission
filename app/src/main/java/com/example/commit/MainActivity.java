package com.example.commit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.commit.databinding.ActivityMainBinding;
import com.example.commit.view.HomeFragment;
import com.example.commit.view.MypageFragment;
import com.example.commit.view.NoticeFragment;
import com.example.commit.view.OfferFragment;
import com.example.commit.view.SearchFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding Binding;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private NoticeFragment noticeFragment;
    private OfferFragment offerFragment;
    private MypageFragment mypageFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        noticeFragment = new NoticeFragment();
        offerFragment = new OfferFragment();
        mypageFragment = new MypageFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, homeFragment).commitAllowingStateLoss();

        Binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.home.setBackgroundColor(Color.parseColor("#3498DB"));
                Binding.search.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.notice.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.offer.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.mypage.setBackgroundColor(Color.parseColor("#ffffff"));
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, homeFragment).commitAllowingStateLoss();
            }
        });
        Binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.home.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.search.setBackgroundColor(Color.parseColor("#3498DB"));
                Binding.notice.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.offer.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.mypage.setBackgroundColor(Color.parseColor("#ffffff"));
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, searchFragment).commitAllowingStateLoss();
            }
        });
        Binding.notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.home.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.search.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.notice.setBackgroundColor(Color.parseColor("#3498DB"));
                Binding.offer.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.mypage.setBackgroundColor(Color.parseColor("#ffffff"));
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, noticeFragment).commitAllowingStateLoss();
            }
        });
        Binding.offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.home.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.search.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.notice.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.offer.setBackgroundColor(Color.parseColor("#3498DB"));
                Binding.mypage.setBackgroundColor(Color.parseColor("#ffffff"));
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, offerFragment).commitAllowingStateLoss();
            }
        });
        Binding.mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.home.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.search.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.notice.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.offer.setBackgroundColor(Color.parseColor("#ffffff"));
                Binding.mypage.setBackgroundColor(Color.parseColor("#3498DB"));
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, mypageFragment).commitAllowingStateLoss();
            }
        });
    }
}