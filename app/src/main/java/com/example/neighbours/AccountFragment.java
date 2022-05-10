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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    ImageView imageView;
    TextView tv_name, tv_edit;
    DatabaseReference userRef;
    StorageReference mStorageRef;
    String uid;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);

        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        userRef = FirebaseDatabase.getInstance().getReference("Users/");

        imageView = (ImageView) getView().findViewById(R.id.imageView);
//        try{
//            FirebaseStorage.getInstance().getReference("/uploads").child(tmp.getImgId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(context).load(uri).centerCrop().into(holder.iv_photo);
//                }
//            });
//        } catch (Exception e) {
//        }
        tv_name = (TextView) getView().findViewById(R.id.tv_name);
        getUserName(uid);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                if(mImageUri != null) {
                    String imgName = System.currentTimeMillis() + "." + getFileExtension(mImageUri);
                    StorageReference fileReference = mStorageRef.child(imgName);
                    fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String imgId = imgName;
                            userRef.orderByKey().equalTo(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //не привязывает картинку
                                    User tmp = (User) snapshot.child(uid).getValue();
                                    tmp.setuImgId(imgId);
                                    userRef.child(uid).setValue(tmp);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }
        });
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
                        tv_name.setText("Hi, " + data.getValue(User.class).getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //no interesting in our purpose in the lesson
            }
        });

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            imageView.setImageURI(mImageUri);
        }
    }


}
