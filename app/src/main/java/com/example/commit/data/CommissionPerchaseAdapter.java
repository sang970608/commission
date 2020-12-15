package com.example.commit.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commit.R;
import com.example.commit.view.TradePerchaseFirstActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class CommissionPerchaseAdapter extends RecyclerView.Adapter<CommissionPerchaseAdapter.MyViewHolder> {
    private ArrayList<String> names;
    private ArrayList<String> options;
    private ArrayList<String> prices;
    Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public CommissionPerchaseAdapter(ArrayList<String> names, ArrayList<String> options, ArrayList<String> prices, Context context){
        this.names = names;
        this.options = options;
        this.prices = prices;
        this.context = context;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        public TextView name;
        public TextView option;
        public TextView price;
        public TextView number;
        public ConstraintLayout commissionPerchase;

        public MyViewHolder(View view){
            super(view);
            this.name = view.findViewById(R.id.commission_perchase_name);
            this.option = view.findViewById(R.id.commission_perchase_option);
            this.price = view.findViewById(R.id.commission_perchase_price);
            this.number = view.findViewById(R.id.number);
            this.commissionPerchase = view.findViewById(R.id.commission_perchase);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.commission_perchase_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(holderView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.number.setText(String.valueOf(i+1));
        myViewHolder.name.setText(this.names.get(i));
        myViewHolder.option.setText(this.options.get(i));
        myViewHolder.price.setText(this.prices.get(i));
        myViewHolder.commissionPerchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TradePerchaseFirstActivity.class);
                intent.putExtra("name", names.get(i));
                intent.putExtra("option", options.get(i));
                intent.putExtra("price", prices.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
}
