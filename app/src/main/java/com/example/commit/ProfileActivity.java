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
import com.example.commit.view.BoardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends base {
    ActivityProfileBinding Binding;
    private static final int REQUEST_CODE = 0;
    FirebaseAuth firebaseAuth;
    String TAG = "FireBase";
    String user, email, uid, filename;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    private Uri filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            Intent intents = getIntent();
            email = intents.getExtras().getString("email");
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        }
        Intent intent = getIntent();
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
            Check();
        }
    };
    private void Check(){
        if (Binding.profileNickEdit.getText().equals("")){
            Toast(this, "닉네임을 입력해주세요");
        } else if (Binding.profileInfoEdit.getText().equals("")){
            Toast(this, "한줄소개를 입력해주세요");
        } else if (Binding.profilePassEdit.getText().equals("")) {
            Toast(this, "사용하실 비밀번호를 입력해주세요");
        } else if (Binding.profilePassEdit.getText().length() < 10){
            Toast(this, "비밀번호가 짧습니다, 10자리 이상으로 해주세요");
        } else if (filepath.equals("")){
            Toast(this, "프로필 사진이 비어있습니다.");
        } else {
            Register();
            Intent intents = new Intent(ProfileActivity.this, MainActivity.class);
            intents.putExtra("email", email);
            startActivity(intents);
            finish();
        }
    }
    private void profileRegister(FirebaseAuth firebaseAuth, String pass){
        RegisterImg();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        uid = firebaseUser.getUid();
        UserModel userModel = new UserModel();
        userModel.email = email;
//        userModel.kakao = user;
//        userModel.uid = uid;
        userModel.nick = Binding.profileNickEdit.getText().toString();
        userModel.info = Binding.profileInfoEdit.getText().toString();
        userModel.pass = pass;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat formats = new SimpleDateFormat("yyyyMMdd_mmss");
        Date date = new Date();
        String getDate = format.format(date);
        String getTime = formatter.format(date);
        userModel.date = getDate;
        userModel.time = getTime;
        String filename = formats.format(date) + ".png";
        userModel.img = "images/" + filename;

        String[] emails = email.split("@");
        databaseReference.child("UID").child(emails[0]).setValue(userModel);
    }
    private void Register(){
        final String pass = Binding.profilePassEdit.getText().toString();
        Log.e("tag", email);
        Log.e("tag", pass);
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.e("tag", "성공");
                        } else if (task.isCanceled()){
                            Toast(ProfileActivity.this, "취소되었습니다.");
                            Log.e("tag", "실패");
                        } else if (task.isComplete()){
                            Log.e("tag", "완료");
                        }
                    }
                });
        profileRegister(firebaseAuth, pass);
    }
    private void RegisterImg(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_mmss");
        Date now = new Date();
        filename = format.format(now) + ".png";
        StorageReference storageReference = storage.getReferenceFromUrl("gs://commission-b4348.appspot.com").child("images/" + filename);
        storageReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast(ProfileActivity.this, "성공했습니다.");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast(ProfileActivity.this, "실패했습니다.");
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
