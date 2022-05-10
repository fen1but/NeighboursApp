package com.example.neighbours;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.Locale;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ViewHolder> {
    List<Apartment> apartments;
    Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;

    public ApartmentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_list_apartment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Apartment tmp = apartments.get(position);

        FirebaseStorage.getInstance().getReference("/uploads").child(tmp.getImgId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).centerCrop().into(holder.iv_photo);
            }
        });
        holder.tv_price.setText("Price: " + String.valueOf(tmp.getPrice()));
        holder.tv_rooms.setText("Rooms: " + String.valueOf(tmp.getRooms()));
        holder.tv_address.setText(tmp.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, ApartmentPage.class);
                intent.putExtra("id", tmp.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (apartments != null)
            return apartments.size();
        return 0;
    }

    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_photo;
        TextView tv_price, tv_rooms, tv_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_photo = itemView.findViewById(R.id.iv_photo);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_rooms = itemView.findViewById(R.id.tv_rooms);
            tv_address = itemView.findViewById(R.id.tv_address);
        }
    }

}
