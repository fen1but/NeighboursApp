package com.example.neighbours;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends androidx.fragment.app.Fragment {
    ImageView imageView;
    TextView tv_name, tv_edit;
    DatabaseReference userRef;
    String uid;

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);

        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        userRef = FirebaseDatabase.getInstance().getReference("Users/");

        imageView = (ImageView) getView().findViewById(R.id.imageView);
        tv_name = (TextView) getView().findViewById(R.id.tv_name);
        getUserName(uid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.account_fragment, container, false);
        return rootView;
    }

    private void getUserName(String uid ) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.getKey().equals(uid))
                        tv_name.setText(data.getValue(User.class).getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //no interesting in our purpose in the lesson
            }
        });

    }


}
