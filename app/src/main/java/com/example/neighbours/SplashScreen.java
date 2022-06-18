package com.example.neighbours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    ImageView iv_icon;
    Animation anim_fadein;
    Handler handler;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View decorView = getWindow().getDecorView();
        Utils.hideBars(decorView);

        iv_icon = findViewById(R.id.iv_icon);
        handler = new Handler();
        FirebaseAuth.getInstance().signOut(); //////////////////
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getUid() != null){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
                else
                    startActivity(new Intent(SplashScreen.this, IntroActivity.class));
                finish();
            }
        },3000);

        anim_fadein = AnimationUtils.loadAnimation(SplashScreen.this,
                R.anim.fadein);
        iv_icon.startAnimation(anim_fadein);
    }
}