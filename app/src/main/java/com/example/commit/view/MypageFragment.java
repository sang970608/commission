package com.example.commit.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.commit.ProfileEditActivity;
import com.example.commit.R;
import com.example.commit.data.Board;
import com.example.commit.data.BoardAdapter;
import com.example.commit.data.MypageImgAdapter;
import com.example.commit.data.UserModel;
import com.example.commit.databinding.FragmentMypageBinding;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MypageFragment extends Fragment {
    FragmentMypageBinding Binding;
    String email;
    String[] emails;
    ArrayList<String> imgs = new ArrayList<>();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    GridLayoutManager gridLayoutManager;
    RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false);

        Bundle bundles = getArguments();
        email = bundles.getString("email");
        getProfile();
        Binding.profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        Binding.profileCommissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommissionPerchaseDialog commissionPerchaseDialog = new CommissionPerchaseDialog(getContext());
                commissionPerchaseDialog.commisionPerchaseDia(email);
            }
        });
        return Binding.getRoot();
    }

    private void getBoardCount(){
        emails = email.split("@");
        myRef.child("UID").child(emails[0]).child("board").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Binding.mypageProfileUploadCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getProfile(){
        emails = email.split("@");
        myRef.child("UID").child(emails[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user= snapshot.getValue(UserModel.class);
                Binding.mypageProfileNick.setText(user.nick);
                Binding.mypageProfileInfo.setText(user.info);
                getImg(user.img);
                getBoardCount();
                getContent();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getImg(String path){
        StorageReference ssumRef = storageReference.child(path);
        long ONE_MEGA = 1024 * 1024;
        ssumRef.getBytes(ONE_MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Binding.mypageProfileSsum.setImageBitmap(bmp);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getContent(){
        emails = email.split("@");
        myRef.child("UID").child(emails[0]).child("board").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Board board = snapshot.getValue(Board.class);
                imgs.add(board.img);
                getContentRecycle(imgs);
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
    private void getContentRecycle(ArrayList<String> imgs){
        Binding.mypageProfileContent.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(),3);
        gridLayoutManager.setReverseLayout(true);
        Binding.mypageProfileContent.setLayoutManager(gridLayoutManager);
        adapter = new MypageImgAdapter(imgs, getActivity());
        Binding.mypageProfileContent.setAdapter(adapter);
    }
}