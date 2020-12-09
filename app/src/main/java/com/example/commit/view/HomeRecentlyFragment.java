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
import com.example.commit.data.BoardAdapter;
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
    ArrayList<String> key = new ArrayList<>();
    int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_recently, container, false);
        getBoard();
        Log.e("out", String.valueOf(titles.size()));


        return Binding.getRoot();
    }
    private void getBoard(){
        myRef.child("board").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (key.contains(snapshot.getKey())){
                    titles.clear();
                    key.clear();
                }
                Board board = snapshot.getValue(Board.class);
                titles.add(board.title);
                Log.e("in", String.valueOf(titles.size()));
                key.add(snapshot.getKey());
                boardRecycle(titles);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void boardRecycle(ArrayList<String> titles){
        Binding.homeRecentlyContent.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        Binding.homeRecentlyContent.setLayoutManager(layoutManager);
        adapter = new BoardAdapter(titles, getActivity());
        Binding.homeRecentlyContent.setAdapter(adapter);
    }
}
