package com.example.bagsmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bagsmanager.CustomerActivity;
import com.example.bagsmanager.Model.Customer;
import com.example.bagsmanager.Model.Product;
import com.example.bagsmanager.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomerAdapter extends ArrayAdapter<Customer> {

    Context context;
    ArrayList<Customer> customers;
    int resource;

    public CustomerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Customer> customer) {
        super(context, resource, customer);
        this.context= context;
        this.resource= resource;
        this.customers= customer;
    }

    @Override
    public int getCount(){return customers.size();}



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView tvName= convertView.findViewById(R.id.tvName);
        TextView tvPhone= convertView.findViewById(R.id.tvPhone);
        TextView tvEmail= convertView.findViewById(R.id.tvEmail);
        TextView tvAddress= convertView.findViewById(R.id.tvAddress);
        ImageView ivLock= convertView.findViewById(R.id.ivLock);

        Customer customer = customers.get(position);

        tvName.setText(customer.getName());
        tvPhone.setText(customer.getPhone());
        tvEmail.setText(customer.getEmail());
        tvAddress.setText(customer.getAddressCustommer());

        ivLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CustomerActivity)context).lockCustomer(customer.getIdUser());
            }
        });

        return convertView;
    }
}
