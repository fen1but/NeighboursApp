package com.example.neighbours;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnw;
    EditText mSearchText;
    RelativeLayout relLayout_searchBar;
    Address address;
    boolean searchBar_visibility = false;
    MutableLiveData<String> profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchText = findViewById(R.id.input_search);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MapFragment()).commit();
        setTitle("Map");
        bnw = findViewById(R.id.bottomNavigation);
        bnw.setSelectedItemId(R.id.item2);
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId()){
                    case R.id.item2:
                        fragment = new MapFragment();
                        setTitle("Map");
                        break;
                    case R.id.item3:
                        fragment = new AccountFragment();
                        setTitle("Account");
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        profileUri = new MutableLiveData<>();
        FirebaseDatabase.getInstance().getReference("Users/").child(uid).child("uImgId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String path = snapshot.getValue(String.class);
                    FirebaseStorage.getInstance().getReference("/uploads").child(path).getDownloadUrl().addOnSuccessListener(uri -> profileUri.setValue(uri.toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LikedFragment()).commit();
                break;
            case R.id.item3:
                if(searchBar_visibility) {
                    relLayout_searchBar.setVisibility(View.VISIBLE);
                    searchBar_visibility = false;
                }
                else {
                    relLayout_searchBar.setVisibility(View.GONE);
                    searchBar_visibility = true;
                }
                break;
        }
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        return super.onOptionsItemSelected(item);
    }

    private void geoLocate() {
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            address = list.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    public Address getSearchedAddress(){
        return address;
    }

    public LiveData<String> getProfileUri() {
        return profileUri;
    }
}
