package com.example.neighbours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    TextView tv_backlogin;
    EditText et_email;
    Button btn_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().hide();
        tv_backlogin = findViewById(R.id.tv_backlogin);
        et_email = findViewById(R.id.et_email);
        btn_enter = findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Field Required", BaseTransientBottomBar.LENGTH_LONG).show();
                } else
                    FirebaseAuth.getInstance().sendPasswordResetEmail(et_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = new String();
                            if (task.isSuccessful())
                                msg = "Reset email sent successfully";
                            else
                                msg = task.getException().getMessage();
                            Snackbar.make(findViewById(android.R.id.content), msg, BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    });
            }
        });
        tv_backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotActivity.this, LogInActivity.class));
            }
        });
    }
}