package com.example.bagsmanager.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bagsmanager.Model.Product;
import com.example.bagsmanager.ProductActivity;
import com.example.bagsmanager.R;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {

    Context context;
    ArrayList<Product> products;
    int resource;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> product) {
        super(context, resource, product);
        this.context= context;
        this.resource= resource;
        this.products= product;
    }

    @Override
    public int getCount(){return products.size();}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
       convertView= LayoutInflater.from(context).inflate(resource, null);

       TextView tvTitle= convertView.findViewById(R.id.tvTitle);
       TextView tvPrice= convertView.findViewById(R.id.tvPrice);
       TextView tvNumber= convertView.findViewById(R.id.tvNumber);
       ImageView ivSua= convertView.findViewById(R.id.ivSua);
       ImageView ivXoaProduct= convertView.findViewById(R.id.ivXoaProduct);

        Product product= products.get(position);

        tvTitle.setText(product.getTitle());
        tvPrice.setText(String.valueOf(product.getPrice()));
        tvNumber.setText(String.valueOf(product.getQuantity()));

        ivSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivXoaProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProductActivity)context).deleteProductAlret(product.getIdProduct());
            }
        });
        ivSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProductActivity)context).modifyProductDialog(product);
            }
        });
       return convertView;
    }
}
