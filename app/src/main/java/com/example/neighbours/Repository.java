package com.example.neighbours;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.AbstractPreferences;

public class Repository {

    DatabaseReference database;
    MutableLiveData<List<Apartment>> apartments;

    public Repository() {
        database = FirebaseDatabase.getInstance().getReference();
        apartments = new MutableLiveData<>();
        fetchApartments();
    }

    private  void fetchApartments(){
        List<Apartment> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Apartments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(snapshot.getValue(Apartment.class));
                apartments.setValue(list);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Apartment apt = snapshot.getValue(Apartment.class);
                for (Apartment i : list) {
                    if (i.getId() == apt.getId()) {
                        list.set(list.indexOf(i), apt);
                        break;
                    }
                    apartments.setValue(list);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Apartment apt = snapshot.getValue(Apartment.class);
                for(Apartment i : list){
                    if(i.getId() == apt.getId()){
                        list.remove(list.indexOf(i));
                        break;
                    }
                }
                apartments.setValue(list);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<List<Apartment>> getApartments() {
        return apartments;
    }
}
