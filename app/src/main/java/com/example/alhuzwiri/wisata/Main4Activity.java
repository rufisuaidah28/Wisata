package com.example.alhuzwiri.wisata;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.net.URL;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main4Activity extends AppCompatActivity {
    private CircleImageView SetupImage;
    private  Uri ImageUri = null;
    private EditText setupname;
    private Button saved;
    private String user_id;
    private FirebaseAuth mAuth;
    private FirebaseFirestore Firestore;
    private StorageReference mStorage;
    private ProgressBar progressbar;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        SetupImage =findViewById(R.id.setup_image);
        setupname = findViewById(R.id.name);
        saved=findViewById(R.id.simpan);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance().getReference();
        Firestore = FirebaseFirestore.getInstance();
        progressbar = findViewById(R.id.progressBar);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main4Activity.this, Main5Activity.class));
            }
        });


        Firestore.collection("user").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if (task.getResult().exists()){
                        String names = task.getResult().getString("name");
                        String image = task.getResult().getString("Image");

                        setupname.setText(names);
                        RequestOptions palceholderrequest = new RequestOptions();
                        palceholderrequest.placeholder(R.drawable.avatar);

                        Glide.with(Main4Activity.this).load(image).into(SetupImage);


                    }


                }   else{
                    String error = task.getException().getMessage();
                    Toast.makeText(Main4Activity.this,"Error"+error,Toast.LENGTH_SHORT).show();

                }
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name = setupname.getText().toString();
                if(!TextUtils.isEmpty(user_name)&& ImageUri !=null){
                    user_id = mAuth.getCurrentUser().getUid();
                    progressbar.setVisibility(View.VISIBLE);
                    final StorageReference image_path = mStorage.child("P_image").child(user_id + ".jpg");
                    UploadTask uploadTask = image_path.putFile(ImageUri);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return image_path.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Map<String,String> userMap = new  HashMap<>();
                                userMap.put("name",user_name);
                                userMap.put("Image",downloadUri.toString());

                                Firestore.collection("user").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Main4Activity.this,"Data Telah TerUpdate",Toast.LENGTH_SHORT).show();
                                        }else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(Main4Activity.this,"FireStrore Error"+error,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(Main4Activity.this,"Error"+error,Toast.LENGTH_SHORT).show();
                            }
                            progressbar.setVisibility(View.INVISIBLE);
                        }
                    });



//                image_path.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                       if (task.isSuccessful()){
//
//                           Uri downloadl = task.getResult().getUploadSessionUri();
//                           Map<String,String> userMap = new  HashMap<>();
//                           userMap.put("name",user_name);
//                           userMap.put("Id",downloadl.toString());
//
//                           Firestore.collection("user").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                               @Override
//                               public void onComplete(@NonNull Task<Void> task) {
//                                   if(task.isSuccessful()){
//                                       Toast.makeText(Main4Activity.this,"FireStrore Succes",Toast.LENGTH_SHORT).show();
//                                   }else {
//                                       String error = task.getException().getMessage();
//                                       Toast.makeText(Main4Activity.this,"FireStrore Error"+error,Toast.LENGTH_SHORT).show();
//                                   }
//                               }
//                           });
//                       }
//
//                       else {
//                           String error = task.getException().getMessage();
//                           Toast.makeText(Main4Activity.this,"Error"+error,Toast.LENGTH_SHORT).show();
//
//                       }
//
//                        progressbar.setVisibility(View.INVISIBLE);
//
//                    }
//                });
                }

            }
        });

        SetupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (ContextCompat.checkSelfPermission(Main4Activity.this , android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(Main4Activity.this,"TIDAK DIIZINKAN",Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(Main4Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else {
                        BringImagePicker();

                    }


                }


            }

        });
    }
    public void BringImagePicker(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(Main4Activity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ImageUri = result.getUri();
                SetupImage.setImageURI(ImageUri);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
