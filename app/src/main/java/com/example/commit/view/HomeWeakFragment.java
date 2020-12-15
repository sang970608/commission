package com.example.commit.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commit.R;
import com.example.commit.data.Board;
import com.example.commit.data.WeakBoard;
import com.example.commit.databinding.FragmentHomeWeakBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeWeakFragment extends Fragment {

    FragmentHomeWeakBinding Binding;
    boolean likeme;
    int like;
    String email;
    String[] emails;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> imgs = new ArrayList<>();
    ArrayList<String> key = new ArrayList<>();
    ArrayList<String> nicks = new ArrayList<>();
    ArrayList<String> ssum = new ArrayList<>();
    ArrayList<Integer> likes = new ArrayList<>();
    ArrayList<String> emailss = new ArrayList<String>();
    RecyclerView.Adapter adapter;
    int i = 0;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_weak, container, false);
        getBoard();
        Bundle bundle = getArguments();
        email = bundle.getString("email");
        emails = email.split("@");

        return Binding.getRoot();
    }

    private void getBoard(){
        myRef.child("BoardAll").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (key.contains(snapshot.getKey())) {
                    titles.clear();
                    imgs.clear();
                    nicks.clear();
                    ssum.clear();
                    likes.clear();
                    key.clear();
                    i = 0;
                }
                try {
                    myRef.child("UID").child(emails[0]).child("WeeakBoard").child(key.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            WeakBoard weakBoard = snapshot.getValue(WeakBoard.class);
                            likeme = weakBoard.like;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }catch (Exception e){
                    likeme = false;
                }

                Board board = snapshot.getValue(Board.class);
                WeakBoard weakBoard = new WeakBoard();
                weakBoard.capture = board.capture;
                weakBoard.date = board.date;
                weakBoard.img = board.img;
                weakBoard.nick = board.nick;
                weakBoard.onlyme = board.onlyme;
                weakBoard.ssum = board.ssum;
                weakBoard.time = board.time;
                weakBoard.title = board.title;
                weakBoard.like = likeme;
                titles.add(board.title);
                nicks.add(board.nick);
                ssum.add(board.ssum);
                imgs.add(board.img);
                emailss.add(emails[0]);
                key.add(snapshot.getKey());
                myRef.child("UID").child(emails[0]).child("WeakBoard").child(key.get(i)).setValue(weakBoard);
                likes.add(board.like);
                i++;
                boardRecycle();
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
    private void boardRecycle(){
        Binding.homeHighRecycle.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        Binding.homeHighRecycle.setLayoutManager(linearLayoutManager);
        adapter = new HomeWeakAdapter(titles, imgs, nicks, ssum, likeme, key, emailss, getActivity());
        Binding.homeHighRecycle.setAdapter(adapter);
    }
}
