package com.example.commit.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commit.R;
import com.example.commit.data.Commision;
import com.example.commit.data.CommissionPerchaseAdapter;
import com.example.commit.data.MypageImgAdapter;
import com.example.commit.databinding.DialogCommisionBinding;
import com.example.commit.databinding.DialogCommissionPerchaseBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.commit.R.id.commision_option_edit;

public class CommissionPerchaseDialog {

    private Context context;
    String[] emails;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();
    RecyclerView recyclerView;
    Button ok;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DialogCommissionPerchaseBinding Binding;
    RecyclerView.Adapter adapter;

    public CommissionPerchaseDialog(Context context) {
        this.context = context;
    }

    public void commisionPerchaseDia(String email) {
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.dialog_commission_perchase);
        recyclerView = dlg.findViewById(R.id.commission_perchase_content);
        ok = dlg.findViewById(R.id.commission_perchase_btn);
        dlg.setCancelable(false);
        dlg.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        emails = email.split("@");
        myRef.child("UID").child(emails[0]).child("commission").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Commision commision = snapshot.getValue(Commision.class);
                names.add(commision.name);
                options.add(commision.option);
                prices.add(commision.price);
                getContentRecycle();
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
    private void getContentRecycle(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CommissionPerchaseAdapter(names, options, prices, context);
        recyclerView.setAdapter(adapter);
    }
}
