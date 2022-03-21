package com.example.neighbours;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.ArrayList;
public class RoomateDialog extends AppCompatDialogFragment {
    public View view;
    EditText pay, bio, age;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference postRef;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
    Button btn_images;
    ImageView img;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/");
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    String name, email, phone;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_roomate_dialog, null);
        pay = view.findViewById(R.id.tv_pay);
        age = view.findViewById(R.id.tv_age);
        bio = view.findViewById(R.id.tv_bio);
        img = view.findViewById(R.id.img);

        Spinner spinner_sex = view.findViewById(R.id.spinner_sex);
        ArrayList<String> list_sex = new ArrayList<>();
        list_sex.add("male");
        list_sex.add("female");
        ArrayAdapter<String> adapter_sex = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list_sex);
        spinner_sex.setAdapter(adapter_sex);

        Spinner spinner_pet = view.findViewById(R.id.spinner_pet);
        ArrayList<String> list_pet = new ArrayList<>();
        list_pet.add("no pet");
        list_pet.add("with pet");
        ArrayAdapter<String> adapter_pet = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list_pet);
        spinner_pet.setAdapter(adapter_pet);
        btn_images = view.findViewById(R.id.btn_images);
        btn_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        builder.setView(view).setTitle("Add a roomate").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sex = spinner_sex.getSelectedItem().toString();
                boolean pet = spinner_pet.getSelectedItem().toString().equals("with pet");
                //image upload
                if(mImageUri != null){
                    String imgName = System.currentTimeMillis() + "." + getFileExtension(mImageUri);
                    StorageReference fileReference = mStorageRef.child(imgName);
                    fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(getActivity(),"Successfully added",Toast.LENGTH_SHORT).show();
                            //String imgId = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            String imgId = imgName;
                            getUserInfo(uid);

                            Roomate roomate = new Roomate(imgId, uid, name, email, phone, pay.getText().toString(), sex, bio.getText().toString(), pet,
                                    Integer.parseInt(age.getText().toString()));
                            postRef = firebaseDatabase.getReference("Roomates").push();
                            postRef.setValue(roomate);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getActivity(),"Upload failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    //Toast.makeText(getActivity(),"No image selected",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void getUserInfo(String uid ) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.getKey().equals(uid)){
                        name = (String) data.child("name").getValue();
                        email = data.getValue(User.class).getEmail();
                        phone = data.getValue(User.class).getPhone();
                        //todo retrieve name email and a phone from firebase
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //no interesting in our purpose in the lesson
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            img.setImageURI(mImageUri);
        }
    }
}