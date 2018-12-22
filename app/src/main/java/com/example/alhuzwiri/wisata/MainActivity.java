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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edemail,edpassword;
    ProgressBar progres;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.daftar_page).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        progres = findViewById(R.id.prog);
        edemail=findViewById(R.id.email);
        edpassword=findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();


    }
    private void login() {

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
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();
                    progres.setVisibility(View.GONE);
                    Intent intent = new Intent(MainActivity.this,Main4Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progres.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.daftar_page:
                startActivity(new Intent(this,Main2Activity.class));
                break;
            case R.id.login:
                login();
                break;

        }

    }


}
