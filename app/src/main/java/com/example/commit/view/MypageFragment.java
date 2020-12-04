package com.example.commit.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commit.R;
import com.example.commit.databinding.FragmentMypageBinding;

public class MypageFragment extends Fragment {
    FragmentMypageBinding Binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false);

        return Binding.getRoot();
    }
}