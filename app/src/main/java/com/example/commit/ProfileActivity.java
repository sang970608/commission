package com.example.commit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.commit.base.base;
import com.example.commit.data.UserModel;
import com.example.commit.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends base {
    ActivityProfileBinding Binding;
    private static final int REQUEST_CODE = 0;
    FirebaseAuth firebaseAuth;
    String TAG = "FireBase";
    String user, email, uid;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    private Uri filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        Intent intent = getIntent();
        user = intent.getExtras().getString("user");
        email = intent.getExtras().getString("email");
        Binding.profilePicturePicture.setOnClickListener(Gallery);
        Binding.profileBtn.setOnClickListener(profileFinish);

    }
    ImageView.OnClickListener Gallery = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_CODE);
        }
    };
    Button.OnClickListener profileFinish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Register();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    private void Register(){
        final String pass = Binding.profilePassEdit.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast(ProfileActivity.this, "성공");
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            uid = firebaseUser.getUid();
                            UserModel userModel = new UserModel();
                            userModel.email = email;
                            userModel.kakao = user;
                            userModel.uid = uid;
                            userModel.nick = Binding.profileNickEdit.getText().toString();
                            userModel.info = Binding.profileInfoEdit.getText().toString();
                            userModel.pass = pass;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
                            Date date = new Date();
                            String getDate = format.format(date);
                            String getTime = formatter.format(date);
                            userModel.date = getDate;
                            userModel.time = getTime;
                            String filename = format.format(date) + ".png";
                            userModel.img = "images/" + filename;

                            databaseReference.child(uid).setValue(userModel);

                        } else if (task.isCanceled()){
                            Toast(ProfileActivity.this, "취소되었습니다.");
                        } else if (task.isComplete()){
                            Toast(ProfileActivity.this, "계정이 이미 있습니다.");
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    Binding.profilePicturePicture.setImageBitmap(img);
                    filepath = data.getData();
                    Log.e(TAG, String.valueOf(filepath));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    Binding.profilePicturePicture.setImageBitmap(bitmap);
                } catch (Exception e) {
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast(this, "사진 선택 취소");
            }
        }
    }
}
