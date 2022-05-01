package com.example.bagsmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bagsmanager.Model.Bill;
import com.example.bagsmanager.Model.Statictis;
import com.example.bagsmanager.R;

import java.util.ArrayList;

public class StatictisAdapter extends ArrayAdapter<Statictis> {
    Context context;
    ArrayList<Statictis> statictiss;
    int resource;

    public StatictisAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Statictis> statictis) {
        super(context, resource, statictis);
        this.context= context;
        this.statictiss= statictis;
        this.resource= resource;
    }

    public int getCount(){
        return statictiss.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent ){
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView tvMonth= convertView.findViewById(R.id.tvMonth);
        TextView tvTurnover= convertView.findViewById(R.id.tvTurnover);

        Statictis statictis= statictiss.get(position);

        tvMonth.setText(String.valueOf(statictis.getMonth()));
        tvTurnover.setText(String.valueOf(statictis.getTurnover()));

        return convertView;
    }
}
