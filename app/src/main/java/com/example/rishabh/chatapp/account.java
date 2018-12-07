package com.example.rishabh.chatapp;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class account extends AppCompatActivity {
    private CircleImageView profileImage;
    private String name;
    private EditText nameText;
    private ProgressBar profileProgressBar;
    private Button nextButton;
    private Uri mainImageuri = null;
    private  String userid = null;
    private Boolean ischanged = false;
    private FirebaseAuth mAuth;
    private StorageReference storage;
    private String status;
    Task<Void> reference;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);



        profileProgressBar = findViewById(R.id.progressBar3);
        profileImage = findViewById(R.id.setup_image);
        nameText = findViewById(R.id.nameText);
        nextButton = findViewById(R.id.setupbutton);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userid = mAuth.getCurrentUser().getUid();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(account.this, "asdasdxda", Toast.LENGTH_SHORT).show();

                name = nameText.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && mainImageuri != null) {
                    profileProgressBar.setVisibility(View.VISIBLE);
                    if (ischanged) {
                        final StorageReference imagepath = storage.child("Profile_image").child(userid + ".jpg");

                        imagepath.putFile(mainImageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeFirestore(task, name);
                                } else {
                                    profileProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(account.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        storeFirestore(null, name);
                    }
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(ContextCompat.checkSelfPermission(account.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(account.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                }
                else
                {
                    imageSelect();
                }
            }
        });

    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String name) {
        Uri downloaduri;

        if (task != null) {

            downloaduri = task.getResult().getDownloadUrl();

        } else {

            downloaduri = mainImageuri;

        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("USerId", userid);
        userMap.put("image", downloaduri.toString());


        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    profileProgressBar.setVisibility(View.GONE);
                    Toast.makeText(account.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(account.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(account.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                    profileProgressBar.setVisibility(View.GONE);

                }
            }
        });
    }
    private void imageSelect()
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(account.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                mainImageuri = result.getUri();
                profileImage.setImageURI(mainImageuri);
                ischanged = true;

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }
}
