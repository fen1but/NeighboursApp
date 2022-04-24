package com.example.neighbours;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ViewHolder> {
    List<Apartment> apartments;
    Context context;

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

        //iv_photo.setImageURI(tmp.getId());
        holder.tv_price.setText("Price: " + String.valueOf(tmp.getPrice()));
        holder.tv_rooms.setText("Rooms: " + String.valueOf(tmp.getRooms()));
        //tv_address.setText();
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
