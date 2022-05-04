package com.example.bagsmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bagsmanager.Model.DetailBill;
import com.example.bagsmanager.R;

import java.util.ArrayList;

public class DetailBillAdapter extends ArrayAdapter<DetailBill> {

    Context context;
    ArrayList<DetailBill> bill_details;
    int resource;

    public DetailBillAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DetailBill> bill_detail) {
        super(context, resource, bill_detail);
        this.context=context;
        this.bill_details=bill_detail;
        this.resource=resource;
    }

    @Override
    public int getCount(){return bill_details.size();}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView tvidProduct= convertView.findViewById(R.id.tvidProduct);
        TextView tvQuantity= convertView.findViewById(R.id.tvQuantity);
        TextView tvPrice= convertView.findViewById(R.id.tvPrice);

        DetailBill bill_detail = bill_details.get(position);

        tvidProduct.setText(String.valueOf(bill_detail.getidProduct()));
        tvQuantity.setText(String.valueOf(bill_detail.getQuantity()));
        tvPrice.setText(String.valueOf(bill_detail.getPrice()));


        return convertView;
    }
}
