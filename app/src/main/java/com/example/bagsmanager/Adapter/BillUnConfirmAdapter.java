package com.example.bagsmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bagsmanager.BillUnConfirmActivity;
import com.example.bagsmanager.Model.Bill;
import com.example.bagsmanager.R;

import java.util.ArrayList;

public class BillUnConfirmAdapter extends ArrayAdapter<Bill> {
    Context context;
    ArrayList<Bill> bills;
    int resource;

    public BillUnConfirmAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Bill> bill) {
        super(context, resource, bill);
        this.context= context;
        this.resource= resource;
        this.bills= bill;
    }

    @Override
    public int getCount(){return bills.size();}


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView tvidBill= convertView.findViewById(R.id.tvidBill);
        TextView tvidUser= convertView.findViewById(R.id.tvidUser);
        TextView tvNgay= convertView.findViewById(R.id.tvNgay);
        ImageView ivCheckBill= convertView.findViewById(R.id.ivCheckBill);


        Bill bill = bills.get(position);

        tvidBill.setText(String.valueOf(bill.getIdBill()));
        tvidUser.setText(String.valueOf(bill.getIdUser()));
        tvNgay.setText(String.valueOf(bill.getDateBill()));

        ivCheckBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BillUnConfirmActivity)context).confirmDialog(bill.getIdBill(), bill.getIdUser());
            }
        });

        return convertView;
    }
}
