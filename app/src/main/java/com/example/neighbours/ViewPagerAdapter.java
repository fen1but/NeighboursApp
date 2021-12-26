package com.example.neighbours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder> {

    private Context context;
    ArrayList<Integer> pictures;
    ArrayList<String> text;

    public ViewPagerAdapter(Context context) {
        this.context = context;
        pictures = new ArrayList<>();
        pictures.add(R.drawable.jumpingpeople);
        pictures.add(R.drawable.sunset1);
        pictures.add(R.drawable.beach);
        text = new ArrayList<>();
        text.add("Welcome!");
        text.add("Find engaging\nrent mates");
        text.add("Rent together - pay less");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.img.setImageResource(pictures.get(position));
        holder.txt.setText(text.get(position));
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            txt = itemView.findViewById(R.id.text);
        }
    }
}
