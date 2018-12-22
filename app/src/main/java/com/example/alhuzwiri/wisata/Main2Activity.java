package com.example.alhuzwiri.wisata;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    EditText edemail,edpassword;
    private FirebaseAuth mAuth;
    ProgressBar progres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.daftar).setOnClickListener(this);
        findViewById(R.id.login_page).setOnClickListener(this);
        edemail = findViewById(R.id.email);
        edpassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progres = findViewById(R.id.prog);
    }
    private void register() {

        String email = edemail.getText().toString().trim();
        String password =edpassword.getText().toString().trim();
        if(email.isEmpty()){
            edemail.setError("Masukan email");
            edemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edemail.setError("Email tidal Valid");
            edemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edpassword.setError("Masukan Password");
            edpassword.requestFocus();
            return;
        }
        if(password.length()<8) {
            edpassword.setError("Password Minimal 8 Karacter");
            edpassword.requestFocus();
            return;
        }
        progres.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progres.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(Main2Activity.this,Main4Activity.class));

                }
                else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "email telah terdaftar", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.daftar:
                register();
                break;
            case R.id.login_page:
                startActivity(new Intent(this, MainActivity.class));

        }
    }


}
