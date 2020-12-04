package com.example.commit.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.commit.R;
import com.example.commit.databinding.FragmentOfferBinding;

import java.util.ArrayList;

public class OfferFragment extends Fragment {
    FragmentOfferBinding Binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer, container, false);
        OfferCategory();

        return Binding.getRoot();
    }
    private void OfferCategory() { //게임 종류 스피너
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("그림 작가 구인");
        arrayList.add("디자이너 구인");
        arrayList.add("모두");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Binding.offerCategory.setAdapter(arrayAdapter);
        arrayAdapter.addAll(arrayList);
    }
}