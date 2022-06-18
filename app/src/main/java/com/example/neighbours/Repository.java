package com.example.neighbours;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    DatabaseReference database;
    MutableLiveData<List<Apartment>> apartments;
    MutableLiveData<List<Apartment>> likedApartments;

    public Repository() {
        database = FirebaseDatabase.getInstance().getReference();
        apartments = new MutableLiveData<>();
        likedApartments = new MutableLiveData<>();
        fetchApartments();
    }

    private  void fetchApartments(){
        List<Apartment> list = new ArrayList<>();
        database.child("Apartments").addChildEventListener(new ChildEventListener() {
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

        List<Apartment> likedList = new ArrayList<>();
        database.child("Users").child(FirebaseAuth.getInstance().getUid()).child("Liked").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                database.child("Apartments").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            Apartment apt = task.getResult().getValue(Apartment.class);
                            likedList.add(apt);
                            likedApartments.setValue(likedList);
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getKey();
                for(Apartment i: likedList){
                    if(i.getId().equals(id)){
                        likedList.remove(i);
                        likedApartments.setValue(likedList);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public LiveData<Boolean> isSaved(String id){
        MutableLiveData<Boolean> saved = new MutableLiveData<>();
        database.child("Users").child(FirebaseAuth.getInstance().getUid()).child("Liked").orderByKey().equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", "changed " + snapshot.exists());
                saved.setValue(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return saved;
    }

    public LiveData<List<Apartment>> getApartments() {
        return apartments;
    }

    public LiveData<List<Apartment>> getLikedApartments() {
        return likedApartments;
    }

    public void setBio(String s){
        database.child("Users").child(FirebaseAuth.getInstance().getUid()).child("bio").setValue(s);
    }

    public LiveData<String> getBio(){
        MutableLiveData<String> data = new MutableLiveData<>();
        database.child("Users").child(FirebaseAuth.getInstance().getUid()).child("bio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.setValue(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }
}
