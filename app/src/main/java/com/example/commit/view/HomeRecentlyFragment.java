package com.example.commit.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.commit.data.UserModel;
import com.example.commit.databinding.FragmentHomeRecentlyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.sdk.user.model.User;

import java.util.ArrayList;

public class HomeRecentlyFragment extends Fragment {

    FragmentHomeRecentlyBinding Binding;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> imgs = new ArrayList<>();
    ArrayList<String> key = new ArrayList<>();
    ArrayList<String> nicks = new ArrayList<>();
    ArrayList<String> ssum = new ArrayList<>();
    String email;
    String[] emails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_recently, container, false);
        Bundle bundle = getArguments();
        email = bundle.getString("email");
        emails = email.split("@");
        getBoard();

        return Binding.getRoot();
    }
    private void getBoard(){
        myRef.child("UID").child(emails[0]).child("board").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (key.contains(snapshot.getKey())){
                    titles.clear();
                    imgs.clear();
                    nicks.clear();
                    ssum.clear();
                    key.clear();
                }
                Board board = snapshot.getValue(Board.class);
                titles.add(board.title);
                imgs.add(board.img);
                nicks.add(board.nick);
                ssum.add(board.ssum);
                Log.e("ssum", ssum.get(0));
                key.add(snapshot.getKey());
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
        Binding.homeRecentlyContent.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        Binding.homeRecentlyContent.setLayoutManager(layoutManager);
        adapter = new BoardAdapter(titles, imgs, nicks, ssum, getActivity());
        Binding.homeRecentlyContent.setAdapter(adapter);
    }
}
