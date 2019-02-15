package com.example.android.showerrush.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.showerrush.R;
import com.example.android.showerrush.model.Shower;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowerRecycleAdapter
        extends RecyclerView.Adapter<ShowerRecycleAdapter.ShowerRecordHolder>{

    private Context context;
    private List<Shower> showers;

    public ShowerRecycleAdapter(Context context, List<Shower> showers){
        this.context = context;
        this.showers = showers;
    }

    @NonNull
    @Override
    public ShowerRecordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(
                R.layout.shower_item, viewGroup, false);
        final ShowerRecordHolder showerRecordHolder
                = new ShowerRecordHolder(view);
        return showerRecordHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowerRecordHolder showerRecordHolder, int i) {
        showerRecordHolder.length.setText(showers.get(i).getStrLength());
        showerRecordHolder.date.setText(showers.get(i).getStrDate());
    }

    @Override
    public int getItemCount() {
        return showers.size();
    }

    public static class ShowerRecordHolder extends RecyclerView.ViewHolder{
        TextView length, date;

        public ShowerRecordHolder(@NonNull View itemView){
            super(itemView);
            length = itemView.findViewById(R.id.shower_length);
            date = itemView.findViewById(R.id.shower_date);
        }
    }
}
