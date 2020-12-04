package com.example.commit.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.commit.R;
import com.example.commit.databinding.FragmentHomeMarkBinding;
import com.example.commit.databinding.FragmentHomeRecentlyBinding;

public class HomeMarkFragment extends Fragment {

    FragmentHomeMarkBinding Binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_mark, container, false);


        return Binding.getRoot();
    }
}
