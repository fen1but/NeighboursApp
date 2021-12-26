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

        sp = getApplicationContext().getSharedPreferences("MySP",
                0);
        editor = sp.edit();

        iv_icon = findViewById(R.id.iv_icon);
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,
                        IntroActivity.class);
                Log.d("tag", ""+sp.getBoolean("intro_key", false));
                if(sp.getBoolean("intro_key", true)){
                    intent= new Intent(SplashScreen.this,
                            IntroActivity.class);
                }
                else{
                    intent=new Intent(SplashScreen.this,
                            LogInActivity.class);
                }
                editor.putBoolean("intro_key", false);
                editor.commit();
                Intent intent1 = new Intent(SplashScreen.this, IntroActivity.class);
                startActivity(intent1);
                finish();
            }
        },3000);

        anim_fadein = AnimationUtils.loadAnimation(SplashScreen.this,
                R.anim.fadein);
        iv_icon.startAnimation(anim_fadein);
    }
}