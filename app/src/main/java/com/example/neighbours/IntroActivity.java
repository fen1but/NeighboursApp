package com.example.neighbours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        WormDotsIndicator indicator = findViewById(R.id.indicator);

        int[] list = new int[3];
        list[0] = 1;
        list[1] = 2;
        list[2] = 3;

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, list);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(adapter);
        indicator.setViewPager2(viewPager2);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

    }
}