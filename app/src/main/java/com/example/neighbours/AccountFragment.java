package com.example.neighbours;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AccountFragment extends androidx.fragment.app.Fragment {
    Repository repo;
    ImageView imageView;
    TextView tv_name;
    DatabaseReference userRef;
    StorageReference mStorageRef;
    String uid;
    EditText et_bio;
    Button btn_bio;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.account_fragment, container, false);

        repo = new Repository();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        userRef = FirebaseDatabase.getInstance().getReference("Users/");
        et_bio = rootView.findViewById(R.id.et_bio);
        btn_bio = rootView.findViewById(R.id.btn_bio);
        imageView = rootView.findViewById(R.id.imageView);

        ((MainActivity)requireActivity()).getProfileUri().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(requireContext()).load(s).centerCrop().into(imageView);

            }
        });


        tv_name = rootView.findViewById(R.id.tv_name);
        getUserName(uid);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });


        repo.getBio().observe(getViewLifecycleOwner(), s -> et_bio.setText(s));


        btn_bio.setOnClickListener(v -> {
            String bio = et_bio.getText().toString();
            repo.setBio(bio);
        });

        return rootView;
    }

    private void getUserName(String uid) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals(uid))
                        tv_name.setText("Hi, " + data.getValue(User.class).getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //no interesting in our purpose in the lesson
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            imageView.setImageURI(mImageUri);

            if (mImageUri != null) {
                String imgName = System.currentTimeMillis() + "." + getFileExtension(mImageUri);
                StorageReference fileReference = mStorageRef.child(imgName);
                fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String imgId = imgName;
                        userRef.child(uid).child("uImgId").setValue(imgId);

                    }
                });
            }
        }
    }


}
