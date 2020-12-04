package com.example.commit.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.commit.R;
import com.example.commit.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    FragmentHomeBinding Binding;
    private FragmentManager fragmentManager;
    private HomeWeakFragment homeWeakFragment;
    private HomeRecentlyFragment homeRecentlyFragment;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        fragmentManager = getFragmentManager();
        homeWeakFragment = new HomeWeakFragment();
        homeRecentlyFragment = new HomeRecentlyFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_content, homeWeakFragment).commitAllowingStateLoss();

        Binding.homeWeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.homeWeakLine.setVisibility(View.VISIBLE);
                Binding.homeRecentlyLine.setVisibility(View.INVISIBLE);
                Binding.homeMarkLine.setVisibility(View.INVISIBLE);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.home_content, homeWeakFragment).commitAllowingStateLoss();
            }
        });
        Binding.homeRecently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.homeWeakLine.setVisibility(View.INVISIBLE);
                Binding.homeRecentlyLine.setVisibility(View.VISIBLE);
                Binding.homeMarkLine.setVisibility(View.INVISIBLE);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.home_content, homeRecentlyFragment).commitAllowingStateLoss();
            }
        });
        Binding.homeMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Binding.homeWeakLine.setVisibility(View.INVISIBLE);
                Binding.homeRecentlyLine.setVisibility(View.INVISIBLE);
                Binding.homeMarkLine.setVisibility(View.VISIBLE);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.home_content, homeWeakFragment).commitAllowingStateLoss();
            }
        });

        return Binding.getRoot();
    }
}
