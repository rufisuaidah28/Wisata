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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main5Activity extends AppCompatActivity {
    private static final int MAX_LENGTH = 100;
    private ImageView Setupimage;
    private FloatingActionButton floatingActionButton;
    private Uri ImageUri = null;
    private EditText content,names;
    private String id;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private StorageReference Storage;

    private Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        Setupimage = findViewById(R.id.imageView2);
        post =findViewById(R.id.post);
        content = findViewById(R.id.content);
        names = findViewById(R.id.editText2);
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        Storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        floatingActionButton=findViewById(R.id.beranda);




    floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Main5Activity.this,Main6Activity.class));
        }
    });
    Setupimage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (ContextCompat.checkSelfPermission(Main5Activity.this , android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(Main5Activity.this,"TIDAK DIIZINKAN",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(Main5Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                else {
                    BringImagePicker();

                }


            }


        }

    });
    post.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String contents = content.getText().toString();
            final String nameplace = names.getText().toString();
            if (!TextUtils.isEmpty(contents) && !TextUtils.isEmpty(nameplace)&& ImageUri !=null){
                SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
                final String now = ISO_8601_FORMAT.format(new Date());
                String randomname = random();
                final String Rn =FieldValue.serverTimestamp().toString();
                final StorageReference image_path = Storage.child("C_image").child(randomname + ".jpg");
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
                            Map<String,String> userMap = new HashMap<>();
                            userMap.put("UserId",id);
                            userMap.put("namee",nameplace);
                            userMap.put("Image_tumb",downloadUri.toString());
                            userMap.put("desc",contents);
                            userMap.put("times",now);

//                            firestore.collection("Content").document(id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(Main5Activity.this,"Data Telah TerUpdate",Toast.LENGTH_SHORT).show();
//                                    }else {
//                                        String error = task.getException().getMessage();
//                                        Toast.makeText(Main5Activity.this,"FireStrore Error"+error,Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
                            firestore.collection("content").add(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(Main5Activity.this,"New Post Has been created",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Main5Activity.this,Main5Activity.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Main5Activity.this,"Can't Post",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Main5Activity.this,"Error"+error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    });
    }

    public void BringImagePicker(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(Main5Activity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ImageUri = result.getUri();
                Setupimage.setImageURI(ImageUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}
