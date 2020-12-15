package com.example.commit;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.commit.base.base;
import com.example.commit.data.Commision;
import com.example.commit.data.CommisionDialog;
import com.example.commit.data.UserModel;
import com.example.commit.databinding.ActivityProfileEditBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.sdk.user.model.User;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileEditActivity extends base {
    ActivityProfileEditBinding Binding;
    private static final int REQUEST_CODE = 0;
    FirebaseAuth firebaseAuth;
    String TAG = "FireBase";
    String[] emails;
    String user, email, uid, filename;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    private Uri filepath;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit);

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        getValue();
        Binding.profilePicturePicture.setOnClickListener(Gallery);
        Binding.profileBtn.setOnClickListener(profileFinish);
        Binding.profileTrade.setOnClickListener(profileTrade);
        Binding.profileCommision.setOnClickListener(profileCommision);
    }
    TextView.OnClickListener profileTrade = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    TextView.OnClickListener profileCommision = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CommisionDialog commisionDialog = new CommisionDialog(ProfileEditActivity.this);
            commisionDialog.commisionDia(email);
        }
    };
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
            Intent intents = new Intent(ProfileEditActivity.this, MainActivity.class);
            intents.putExtra("email", email);
            startActivity(intents);
            finish();
        }
    }
    private void Register(){
        final String pass = Binding.profilePassEdit.getText().toString();
        RegisterImg();
        UserModel userModel = new UserModel();
        userModel.nick = Binding.profileNickEdit.getText().toString();
        userModel.info = Binding.profileInfoEdit.getText().toString();
        userModel.pass = pass;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat formats = new SimpleDateFormat("yyyyMMHH_mmss");
        Date date = new Date();
        String getDate = format.format(date);
        String getTime = formatter.format(date);
        userModel.date = getDate;
        userModel.time = getTime;
        String filename = formats.format(date) + ".png";
        userModel.img = "profile/" + filename;
        String[] emails = email.split("@");
        databaseReference.child(emails[0]).setValue(userModel);
    }
    private void RegisterImg(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMHH_mmss");
        Date now = new Date();
        filename = format.format(now) + ".png";
        StorageReference storageReference = storage.getReferenceFromUrl("gs://commission-b4348.appspot.com").child("images/" + filename);
        storageReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast(ProfileEditActivity.this, "성공했습니다.");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast(ProfileEditActivity.this, "실패했습니다.");
                    }
                });
    }
    private void getValue(){
        emails = email.split("@");
        myRef.child("UID").child(emails[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                Binding.profileNickEdit.setText(user.nick);
                Binding.profileInfoEdit.setText(user.info);
                StorageReference ssumRef = storageReference.child(user.img);
                long ONE_MEGA = 1024 * 1024;
                ssumRef.getBytes(ONE_MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Binding.profilePicturePicture.setImageBitmap(bmp);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileEditActivity.this, "실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
