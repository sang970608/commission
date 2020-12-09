package com.example.commit.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commit.R;

import java.util.ArrayList;


public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.MyViewHolder> {
    private ArrayList<String> title;
//    private int[] img;
    Context context;

    public BoardAdapter(ArrayList<String> title, Context context){
        this.title = title;
//        this.img = img;
        this.context = context;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
//        public ImageView imageView;
        public TextView TextTitle;

        public MyViewHolder(View view){
            super(view);
//            this.imageView = view.findViewById(R.id.board_item_img);
            this.TextTitle = view.findViewById(R.id.board_item_title);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(holderView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.TextTitle.setText(this.title.get(i));
//        myViewHolder.imageView.setBackgroundResource(this.img[i]);
    }

    @Override
    public int getItemCount() {
        return title.size();
    }
}
