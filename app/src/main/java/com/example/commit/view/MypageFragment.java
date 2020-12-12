package com.example.commit.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commit.ProfileEditActivity;
import com.example.commit.R;
import com.example.commit.databinding.FragmentMypageBinding;

public class MypageFragment extends Fragment {
    FragmentMypageBinding Binding;
    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false);

        Bundle bundles = getArguments();
        email = bundles.getString("email");
        Binding.profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        return Binding.getRoot();
    }
}