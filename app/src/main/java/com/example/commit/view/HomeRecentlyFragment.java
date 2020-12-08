package com.example.commit.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commit.R;
import com.example.commit.data.Board;
import com.example.commit.databinding.FragmentHomeRecentlyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeRecentlyFragment extends Fragment {

    FragmentHomeRecentlyBinding Binding;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> titles = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_recently, container, false);
        getBoard();
        return Binding.getRoot();
    }
    private void getBoard(){

        Binding.homeRecentlyContent.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        Binding.homeRecentlyContent.setLayoutManager(layoutManager);
        myRef.child("board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("tag", String.valueOf(snapshot.getValue()));
                for (int i=0; i<snapshot.getChildrenCount();i++){
                    Board board = snapshot.getValue(Board.class);
                    titles.add(board.title);
                }
//                adapter = new BoardAdapter(titles, getActivity());
//                Binding.homeRecentlyContent.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
