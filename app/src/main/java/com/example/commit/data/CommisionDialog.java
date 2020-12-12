package com.example.commit.data;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commit.R;
import com.example.commit.databinding.DialogCommisionBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.commit.R.id.commision_option_edit;

public class CommisionDialog {

    private Context context;

    public CommisionDialog(Context context) {
        this.context = context;
    }

    public void commisionDia(final String email) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.dialog_commision);
        dlg.show();

        final EditText name = (EditText) dlg.findViewById(R.id.commision_name_edit);
        final Spinner option = dlg.findViewById(commision_option_edit);
        final EditText price = (EditText) dlg.findViewById(R.id.commision_price_edit);
        Button okButton = (Button) dlg.findViewById(R.id.commision_btn);
        OptionSpinner(option);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Commision commision = new Commision();
                commision.name = name.getText().toString();
                commision.option = option.getSelectedItem().toString();
                commision.price = price.getText().toString();

                if (name.equals("") && price.equals("")){
                    Toast.makeText(context, "비어있는 란이 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    String[] emails = email.split("@");
                    databaseReference.child(emails[0]).child("commission").push().setValue(commision);
                    dlg.dismiss();
                }
            }
        });
    }
    private void OptionSpinner(Spinner option){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("상업용");
        arrayList.add("비상업용");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        option.setAdapter(arrayAdapter);
        arrayAdapter.addAll(arrayList);
    }
}
