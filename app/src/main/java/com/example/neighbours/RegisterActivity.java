package com.example.neighbours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
//todo email verification
public class RegisterActivity extends AppCompatActivity {
    TextView tv_backlogin;
    Button btn_enter;
    EditText et_name, et_email, et_password, et_passwordconfirm, et_phone;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_passwordconfirm = findViewById(R.id.et_passwordconfirm);
        tv_backlogin = findViewById(R.id.tv_backlogin);
        tv_backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
            }
        });
        btn_enter = findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String passwordconfirm = et_passwordconfirm.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        if(name.isEmpty()){
            et_name.setError("Full name is required");
            et_name.requestFocus();
            return;
        }
        if(email.isEmpty()){
            et_email.setError("Email is required");
            et_email.requestFocus();
            return;
        }
        if(phone.isEmpty()){
            et_phone.setError("Phone number is required");
            et_phone.requestFocus();
            return;
        }
        if(password.isEmpty()){
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }
        if(passwordconfirm.isEmpty()){
            et_passwordconfirm.setError("Password confirmation is required");
            et_passwordconfirm.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Provide a valid email address");
            et_email.requestFocus();
            return;
        }
        if(password.length() < 6){
            et_password.setError("Password should be longer than 6 characters");
            et_password.requestFocus();
            return;
        }
        if(!password.equals(passwordconfirm)){
            et_passwordconfirm.setError("Passwords should match");
            et_passwordconfirm.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(name, email, phone);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //todo отладка регистрации
                                startActivity(new Intent(RegisterActivity.this, TestActivity.class));
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}