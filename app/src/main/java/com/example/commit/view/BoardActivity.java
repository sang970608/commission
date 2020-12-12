package com.example.commit.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.commit.ProfileActivity;
import com.example.commit.R;
import com.example.commit.base.base;
import com.example.commit.data.Board;
import com.example.commit.data.UserModel;
import com.example.commit.databinding.ActivityBoardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.sdk.user.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardActivity extends base {
    ActivityBoardBinding Binding;
    String title, tag;
    Bitmap bitmap;
    boolean onlyme, capture;
    private Uri filepath;
    private static final int REQUEST_CODE = 0;
    String TAG = "Board";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String filename, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_board);

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        Binding.boardOnlymeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    onlyme = true;
                } else{
                    onlyme = false;
                }
            }
        });
        Binding.boardCaptureBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    capture = true;
                } else{
                    capture = false;
                }
            }
        });
        Binding.boardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        Binding.boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
                finish();
            }
        });
    }
    private void RegisterImg(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMHH_mmss");
        Date now = new Date();
        filename = format.format(now) + ".png";
        StorageReference storageReference = storage.getReferenceFromUrl("gs://commission-b4348.appspot.com").child("images/" + filename);
        storageReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast(BoardActivity.this, "성공했습니다.");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast(BoardActivity.this, "실패했습니다.");
                    }
                });
    }
    private void Register(){
        RegisterImg();
        Board board = new Board();
        board.title = Binding.boardEdit.getText().toString();
        board.onlyme = onlyme;
        board.capture = capture;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date();
        String getDate = format.format(date);
        String getTime = formatter.format(date);
        board.img = "images/" + filename;
        board.date = getDate;
        board.time = getTime;

        String[] emails = email.split("@");
        databaseReference.child(emails[0]).child("board").push().setValue(board);
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
                    filepath = data.getData();
                    Log.e(TAG, String.valueOf(filepath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                    Binding.boardImg.setImageBitmap(bitmap);
                } catch (Exception e) {
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast(this, "사진 선택 취소");
            }
        }
    }
}
