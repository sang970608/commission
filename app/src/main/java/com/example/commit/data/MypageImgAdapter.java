package com.example.commit.data;

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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commit.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class MypageImgAdapter extends RecyclerView.Adapter<MypageImgAdapter.MyViewHolder> {
    private ArrayList<String> img;
    Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public MypageImgAdapter(ArrayList<String> img, Context context){
        this.img = img;
        this.context = context;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        public ImageView imageView;

        public MyViewHolder(View view){
            super(view);
            this.imageView = view.findViewById(R.id.mypage_profile_content_items);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mypage_profile_content_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(holderView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        StorageReference imgRef = storageReference.child(this.img.get(i));
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
        return img.size();
    }
}
