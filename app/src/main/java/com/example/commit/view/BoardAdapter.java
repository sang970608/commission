package com.example.commit.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commit.R;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.MyViewHolder> implements Filterable {
    private ArrayList<String> title = new ArrayList<>();
    private String[] material;
    private int[] image;
    ArrayList<String> unFilteredlist;
    ArrayList<String> filteredList;
    Context context;

    public BoardAdapter(ArrayList<String> title, Context context){
        this.title = title;
        this.image = image;
        this.material = material;
        this.unFilteredlist = title;
        this.filteredList = title;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;

        public MyViewHolder(View view){
            super(view);
            this.title= view.findViewById(R.id.board_item_title);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board_item, viewGroup, false);
        context = viewGroup.getContext();
        MyViewHolder myViewHolder = new MyViewHolder(holderView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(this.filteredList.get(i));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<String> filteringList = new ArrayList<>();
                    for(String name : unFilteredlist) {
                        if(name.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<String>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
