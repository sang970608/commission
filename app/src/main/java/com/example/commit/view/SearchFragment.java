package com.example.commit.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.commit.R;
import com.example.commit.databinding.FragmentSearchBinding;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    FragmentSearchBinding Binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        SearchCategory();
        return Binding.getRoot();
    }
    private void SearchCategory() { //게임 종류 스피너
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("태그");
        arrayList.add("그림명");
        arrayList.add("모두");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Binding.searchCategory.setAdapter(arrayAdapter);
        arrayAdapter.addAll(arrayList);
    }
}
