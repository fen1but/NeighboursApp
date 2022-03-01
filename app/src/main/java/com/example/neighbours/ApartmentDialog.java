package com.example.neighbours;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ApartmentDialog extends AppCompatDialogFragment {
    public View view;
    EditText latitude, longitude, floor, rooms, area, price, arnona, water, electricity, roomates;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference postRef;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.apartment_dialog, null);
        latitude = view.findViewById(R.id.tv_latitude);
        longitude = view.findViewById(R.id.tv_longitude);
        floor = view.findViewById(R.id.tv_floor);
        rooms = view.findViewById(R.id.tv_rooms);
        area = view.findViewById(R.id.tv_area);
        price = view.findViewById(R.id.tv_price);
        arnona = view.findViewById(R.id.arnona);
        water = view.findViewById(R.id.tv_water);
        electricity = view.findViewById(R.id.tv_electricity);
        roomates = view.findViewById(R.id.tv_roomates);
        firebaseDatabase = FirebaseDatabase.getInstance();

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
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                boolean waterboiler = spinner_waterboiler.getSelectedItem().toString().equals("with waterboiler");
                boolean ac = spinner_ac.getSelectedItem().toString().equals("with ac");
                boolean kosherkitchen = spinner_waterboiler.getSelectedItem().toString().equals("kosher kitchen");
                boolean elevator = spinner_waterboiler.getSelectedItem().toString().equals("with elevator");
                boolean pets = spinner_waterboiler.getSelectedItem().toString().equals("with pets");

                Apartment apartment = new Apartment(uid, Integer.parseInt(latitude.getText().toString()), Integer.parseInt(longitude.getText().toString()),
                        Integer.parseInt(floor.getText().toString()), Integer.parseInt(rooms.getText().toString()), Double.parseDouble(area.getText().toString()),
                        Double.parseDouble(price.getText().toString()), waterboiler, ac, kosherkitchen, Double.parseDouble(arnona.getText().toString()),
                        Double.parseDouble(water.getText().toString()), Double.parseDouble(electricity.getText().toString()), elevator, pets,
                        Integer.parseInt(roomates.getText().toString()));

                postRef = firebaseDatabase.getReference("Apartments").push();
                postRef.setValue(apartment);
            }
        });
        return builder.create();
    }
}
