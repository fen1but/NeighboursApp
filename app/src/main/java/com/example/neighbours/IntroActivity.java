package com.example.neighbours;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
//todo viewpager animation, delete bar
public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        View decorView = getWindow().getDecorView();
        Utils.hideBars(decorView);

        WormDotsIndicator indicator = findViewById(R.id.indicator);

        int[] list = new int[3];
        list[0] = 1;
        list[1] = 2;
        list[2] = 3;

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, list);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        //viewPager2.setPageTransformer(new DepthPageTransformer()); --viewpager2 animation
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