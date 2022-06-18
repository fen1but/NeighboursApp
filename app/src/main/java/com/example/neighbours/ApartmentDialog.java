package com.example.neighbours;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ApartmentDialog extends AppCompatDialogFragment {
    public View view;
    EditText floor, rooms, area, price, arnona, water, electricity, roomates, mSearchText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference postRef;
    StorageReference mStorageRef;
    Button btn_images;
    ImageView img;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    Address address;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.apartment_dialog, null);
        floor = view.findViewById(R.id.tv_floor);
        rooms = view.findViewById(R.id.tv_rooms);
        area = view.findViewById(R.id.tv_area);
        price = view.findViewById(R.id.tv_price);
        arnona = view.findViewById(R.id.arnona);
        water = view.findViewById(R.id.tv_water);
        btn_images = view.findViewById(R.id.btn_images);
        electricity = view.findViewById(R.id.tv_electricity);
        roomates = view.findViewById(R.id.tv_roomates);
        img = view.findViewById(R.id.img);
        mSearchText = view.findViewById(R.id.input_search);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
            firebaseDatabase = FirebaseDatabase.getInstance();
        btn_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        Spinner spinner_waterboiler = view.findViewById(R.id.spinner_waterboiler);
        ArrayList<String> list_waterboiler = new ArrayList<>();
        list_waterboiler.add("with waterboiler");
        list_waterboiler.add("no waterboiler");
        ArrayAdapter<String> adapter_waterboiler = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list_waterboiler);
        spinner_waterboiler.setAdapter(adapter_waterboiler);

        Spinner spinner_kosherkitchen = view.findViewById(R.id.spinner_kosherkitchen);
        ArrayList<String> list_kosherkitchen = new ArrayList<>();
        list_kosherkitchen.add("kosher kitchen");
        list_kosherkitchen.add("no kosher kitchen");
        ArrayAdapter<String> adapter_kosherkitchen = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list_kosherkitchen );
        spinner_kosherkitchen.setAdapter(adapter_kosherkitchen);

        Spinner spinner_ac = view.findViewById(R.id.spinner_ac);
        ArrayList<String> list_ac = new ArrayList<>();
        list_ac.add("with ac");
        list_ac.add("no ac");
        ArrayAdapter<String> adapter_ac = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list_ac );
        spinner_ac.setAdapter(adapter_ac);

        Spinner spinner_elevator = view.findViewById(R.id.spinner_elevator);
        ArrayList<String> list_elevator = new ArrayList<>();
        list_elevator.add("with elevator");
        list_elevator.add("no elevator");
        ArrayAdapter<String> adapter_elevator = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list_elevator );
        spinner_elevator.setAdapter(adapter_elevator);

        Spinner spinner_pets = view.findViewById(R.id.spinner_pets);
        ArrayList<String> list_pets = new ArrayList<>();
        list_pets.add("with pets");
        list_pets.add("no pets");
        ArrayAdapter<String> adapter_pets = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list_pets );
        spinner_pets.setAdapter(adapter_pets);


        builder.setView(view).setTitle("Add an apartment").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                boolean waterboiler = spinner_waterboiler.getSelectedItem().toString().equals("with waterboiler");
                boolean ac = spinner_ac.getSelectedItem().toString().equals("with ac");
                boolean kosherkitchen = spinner_waterboiler.getSelectedItem().toString().equals("kosher kitchen");
                boolean elevator = spinner_waterboiler.getSelectedItem().toString().equals("with elevator");
                boolean pets = spinner_waterboiler.getSelectedItem().toString().equals("with pets");
                geoLocate();
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
                            postRef = firebaseDatabase.getReference("Apartments").push();

                            Apartment apartment = new Apartment(imgId,postRef.getKey(), uid, String.valueOf(address.getLatitude()), String.valueOf(address.getLongitude()),
                                    Integer.parseInt(floor.getText().toString()), Integer.parseInt(rooms.getText().toString()), Double.parseDouble(area.getText().toString()),
                                    Double.parseDouble(price.getText().toString()), waterboiler, ac, kosherkitchen, Double.parseDouble(arnona.getText().toString()),
                                    Double.parseDouble(water.getText().toString()), Double.parseDouble(electricity.getText().toString()), elevator, pets,
                                    Integer.parseInt(roomates.getText().toString()), address.getAddressLine(0));
                            postRef.setValue(apartment);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            img.setImageURI(mImageUri);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void geoLocate(){

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e("TAG", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            address = list.get(0);

        }
    }
}
