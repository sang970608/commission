package com.example.commit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commit.R;
import com.example.commit.data.Board;
import com.example.commit.data.WeakBoard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class HomeWeakAdapter extends RecyclerView.Adapter<HomeWeakAdapter.MyViewHolder> {
    private ArrayList<String> title;
    private ArrayList<String> img;
    private ArrayList<String> nick;
    private ArrayList<String> ssum;
    private ArrayList<String> key;
    private ArrayList<String> email;
    boolean like;
    Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public HomeWeakAdapter(ArrayList<String> title, ArrayList<String> img, ArrayList<String> nick, ArrayList<String> ssum, boolean like, ArrayList<String> key, ArrayList<String> email, Context context){
        this.title = title;
        this.img = img;
        this.nick = nick;
        this.like = like;
        this.ssum = ssum;
        this.email = email;
        this.key = key;
        this.context = context;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        public ImageView imageView;
        public ImageView ssumnail;
        public TextView TextTitle;
        public TextView nick;
        public ImageView like;

        public MyViewHolder(View view){
            super(view);
            this.imageView = view.findViewById(R.id.board_item_img);
            this.ssumnail = view.findViewById(R.id.board_item_ssum);
            this.TextTitle = view.findViewById(R.id.board_item_title);
            this.nick = view.findViewById(R.id.board_item_nick);
            this.like = view.findViewById(R.id.board_item_like);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board_weak_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(holderView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.TextTitle.setText(this.title.get(i));
        myViewHolder.nick.setText(this.nick.get(i));
        StorageReference imgRef = storageReference.child(this.img.get(i));
        StorageReference ssumRef = storageReference.child(this.ssum.get(i));
        myViewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!like){
                    like = true;
                    myViewHolder.like.setBackgroundResource(R.drawable.clicked);
                    Board board = new Board();
                    board.like ++;
                    WeakBoard weakBoard = new WeakBoard();
                    weakBoard.like = like;
                    databaseReference.child("BoardAll").child(key.get(i)).child("like").setValue(board.like);
                    databaseReference.child("UID").child(email.get(i)).child("WeakBoard").child(key.get(i)).child("like").setValue(weakBoard.like);
                } else {
                    like = false;
                    myViewHolder.like.setBackgroundResource(R.drawable.nonclicked);
                    Board board = new Board();
                    databaseReference.child("BoardAll").child(key.get(i)).child("like").setValue(board.like);
                    WeakBoard weakBoard = new WeakBoard();
                    weakBoard.like = like;
                    databaseReference.child("UID").child(email.get(i)).child("WeakBoard").child(key.get(i)).child("like").setValue(weakBoard.like);
                }
            }
        });
        getImg(ssumRef, myViewHolder);
        getImg(imgRef, myViewHolder);
    }
    private void getImg(StorageReference Ref, final MyViewHolder myViewHolder){
        long ONE_MEGA = 1024 * 1024;
        Ref.getBytes(ONE_MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                myViewHolder.imageView.setImageBitmap(bmp);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }
}
