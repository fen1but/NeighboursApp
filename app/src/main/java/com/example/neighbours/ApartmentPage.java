package com.example.neighbours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ApartmentPage extends AppCompatActivity {
    private String id;
    private TextView tv_addressp;
    private TextView tv_roomatesp;
    private TextView tv_floorp;
    private TextView tv_areap;
    private TextView tv_waterp;
    private TextView tv_arnonap;
    private TextView tv_electricityp;
    private TextView tv_petsp;
    private TextView tv_roomsp;
    private ImageView iv_call, iv_photo;
    private String phoneNum;
    private ImageView iv_save;
    DatabaseReference userRef;
    DatabaseReference apRef;
    Apartment tmp;
    ArrayList<String> liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Apartment");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        iv_save = findViewById(R.id.iv_save);
        tv_addressp = findViewById(R.id.tv_addressp);
        tv_roomatesp = findViewById(R.id.tv_roomatesp);
        tv_floorp = findViewById(R.id.tv_floorp);
        tv_areap = findViewById(R.id.tv_areap);
        tv_waterp = findViewById(R.id.tv_waterp);
        tv_arnonap = findViewById(R.id.tv_arnonap);
        tv_electricityp = findViewById(R.id.tv_electricityp);
        tv_petsp = findViewById(R.id.tv_petsp);
        tv_roomsp = findViewById(R.id.tv_roomsp);
        iv_call = findViewById(R.id.iv_call);
        iv_photo = findViewById(R.id.iv_photo);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        apRef = FirebaseDatabase.getInstance().getReference("Apartments/");
        userRef = FirebaseDatabase.getInstance().getReference("Users/");
        DatabaseReference likedRef = userRef.child(FirebaseAuth.getInstance().getUid()).child("Liked");

        setData(id);

        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                likedRef.child(id).setValue(userRef.push().getKey());

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);

    }

    private void setData(String id ) {
        apRef.orderByKey().equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                tmp = data.child(id).getValue(Apartment.class);

                String uid = tmp.getUid();
                userRef.orderByKey().equalTo(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.child(uid).getValue(User.class);
                        phoneNum = user.getPhone();
                        if (ActivityCompat.checkSelfPermission(ApartmentPage.this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(ApartmentPage.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                iv_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:0"+Integer.valueOf(phoneNum)));
                        startActivity(callIntent);
                    }
                });
                tv_addressp.setText(tmp.getAddress());
                tv_roomatesp.setText(String.valueOf(tmp.getRoomates()));
                tv_floorp.setText(String.valueOf(tmp.getFloor()));
                tv_areap.setText(String.valueOf(tmp.getArea()));
                tv_waterp.setText(String.valueOf(tmp.getWater()));
                tv_arnonap.setText(String.valueOf(tmp.getArnona()));
                tv_electricityp.setText(String.valueOf(tmp.getElctricity()));
                tv_roomsp.setText(String.valueOf(tmp.getRooms()));
                if(tmp.getPets())
                    tv_petsp.setText("allowed");
                else
                    tv_petsp.setText("not allowed");

                FirebaseStorage.getInstance().getReference("/uploads").child(tmp.getImgId())
                        .getDownloadUrl().addOnSuccessListener(uri -> Glide.with(ApartmentPage.this).load(uri).centerCrop().into(iv_photo));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}