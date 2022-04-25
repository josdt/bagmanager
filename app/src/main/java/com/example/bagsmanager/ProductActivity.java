package com.example.bagsmanager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.API.APIService;
import com.example.bagsmanager.API.RetrofitClient;
import com.example.bagsmanager.Adapter.ProductAdapter;
import com.example.bagsmanager.Model.Color;
import com.example.bagsmanager.Model.Customer;
import com.example.bagsmanager.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductActivity extends AppCompatActivity {

    ListView lvlistProduct;
    Button btnThem;
    ArrayList<Product> products;
    ProductAdapter productAdapter;

    EditText edtTitle, edtPrice, edtQuantity, edtDescr;
    Spinner spnColor, spnBrand;

    String urlProduct="http://10.0.2.2:3000/api/product";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_product);
        setControl();
        setEvent();
        getProduct(urlProduct);

    }

    private void setControl() {
        btnThem= findViewById(R.id.btnThem);
        lvlistProduct= findViewById(R.id.lvlistProduct);
        products= new ArrayList<>();
        productAdapter= new ProductAdapter(ProductActivity.this, R.layout.product_item, products);
        lvlistProduct.setAdapter(productAdapter);
    }

    private void setEvent(){
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductdialog();
            }
        });
    }

    private void getProduct(String url){
        products.clear();
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject k= response.getJSONObject(i);
                                products.add(new Product(k.getInt("idProduct"), k.getInt("price"), k.getString("descr"),
                                        k.getString("title"), k.getInt("idColor"), k.getInt("idBrand"), k.getString("image"),
                                        k.getInt("quantity")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        productAdapter.notifyDataSetChanged();
//                        Toast.makeText(ProductActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(jsonArrayRequest);
    }

    private void addProductdialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.add_dialog,(ViewGroup) findViewById(R.layout.list_product));
        edtTitle= dialogView.findViewById(R.id.edtTitle);
        edtPrice= dialogView.findViewById(R.id.edtPrice);
        spnColor= dialogView.findViewById(R.id.spnColor);
        spnBrand= dialogView.findViewById(R.id.spnBrand);
        edtQuantity= dialogView.findViewById(R.id.edtQuantity);
        edtDescr= dialogView.findViewById(R.id.edtDescr);


        ArrayAdapter<CharSequence> adapter1= ArrayAdapter.createFromResource(this, R.array.color, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2= ArrayAdapter.createFromResource(this, R.array.brand, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBrand.setAdapter(adapter2);

        dialogBuilder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String Title= edtTitle.getText().toString().trim();
                float Price= Float.parseFloat(edtPrice.getText().toString().trim());
                int idColor = Integer.parseInt(spnColor.getSelectedItem().toString());
                int idBrand = Integer.parseInt(spnBrand.getSelectedItem().toString());
                int Quantity = Integer.parseInt(edtQuantity.getText().toString());
                String Image = "abcdef";
                String Descr = edtDescr.getText().toString();

                try {
                    addProduct(urlProduct,Price,Descr,Title,idColor,idBrand,Image,Quantity);
                    Toast.makeText(ProductActivity.this, "Thêm thành công",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(ProductActivity.this, "Thêm thất bại",Toast.LENGTH_SHORT).show();
                }
                getProduct(urlProduct);
                dialogInterface.cancel();

            }
        });
        dialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("THÊM SẢN PHẨM");
        AlertDialog a = dialogBuilder.create();
        a.show();
    }

    private void addProduct(String url ,float Price,String Descr,String Title,int idColor,int idBrand,String Image,int Quantity ) throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JSONObject jsonbody= new JSONObject();
        jsonbody.put("price",String.valueOf(Price));
        jsonbody.put("descr",Descr);
        jsonbody.put("title",Title);
        jsonbody.put("idColor",String.valueOf(idColor));
        jsonbody.put("idBrand",String.valueOf(idBrand));
        jsonbody.put("image",Image);
        jsonbody.put("quantity",String.valueOf(Quantity));
        final  String requestbody= jsonbody.toString();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError{
                try {
                    return requestbody== null ? null : requestbody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody,"utf-8");
                    return null;
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response){
                String responseString="";
                if(response != null){
                    responseString= String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        mRequestQueue.add(stringRequest);
    }


    public void modifyProductDialog(Product product){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.add_dialog,(ViewGroup) findViewById(R.layout.list_product));
        edtTitle= dialogView.findViewById(R.id.edtTitle);
        edtPrice= dialogView.findViewById(R.id.edtPrice);
        spnColor= dialogView.findViewById(R.id.spnColor);
        spnBrand= dialogView.findViewById(R.id.spnBrand);
        edtQuantity= dialogView.findViewById(R.id.edtQuantity);
        edtDescr= dialogView.findViewById(R.id.edtDescr);

        edtTitle.setText(product.getTitle());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtQuantity.setText(String.valueOf(product.getQuantity()));
        edtDescr.setText(product.getDescr());

        ArrayAdapter<CharSequence> adapter1= ArrayAdapter.createFromResource(this, R.array.color, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2= ArrayAdapter.createFromResource(this, R.array.brand, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBrand.setAdapter(adapter2);

        dialogBuilder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String Title= edtTitle.getText().toString().trim();
                float Price= Float.parseFloat(edtPrice.getText().toString().trim());
                int idColor = Integer.parseInt(spnColor.getSelectedItem().toString());
                int idBrand = Integer.parseInt(spnBrand.getSelectedItem().toString());
                int Quantity = Integer.parseInt(edtQuantity.getText().toString());
                String Image = "abcdef";
                String Descr = edtDescr.getText().toString();

                try {
                    modifyProduct(urlProduct,product.getIdProduct(),Price,Descr,Title,idColor,idBrand,Image,Quantity);
                    Toast.makeText(ProductActivity.this, "Sửa thành công",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(ProductActivity.this, "Sửa thất bại",Toast.LENGTH_SHORT).show();
                }
                getProduct(urlProduct);
                dialogInterface.cancel();

            }
        });
        dialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("SỬA SẢN PHẨM");
        AlertDialog a = dialogBuilder.create();
        a.show();
    }

    private void modifyProduct(String url ,int idProduct,float Price,String Descr,String Title,int idColor,int idBrand,String Image,int Quantity) throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JSONObject jsonbody= new JSONObject();
        jsonbody.put("price",String.valueOf(Price));
        jsonbody.put("descr",Descr);
        jsonbody.put("title",Title);
        jsonbody.put("idColor",String.valueOf(idColor));
        jsonbody.put("idBrand",String.valueOf(idBrand));
        jsonbody.put("image",Image);
        jsonbody.put("quantity",String.valueOf(Quantity));
        jsonbody.put("id",String.valueOf(idProduct));
        final  String requestbody= jsonbody.toString();
//        Toast.makeText(ProductActivity.this, requestbody,Toast.LENGTH_SHORT).show();

        StringRequest stringRequest= new StringRequest(Request.Method.PUT, url+"/"+idProduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError{
                try {
                    return requestbody== null ? null : requestbody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody,"utf-8");
                    return null;
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response){
                String responseString="";
                if(response != null){
                    responseString= String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        mRequestQueue.add(stringRequest);
    }


    public void deleteProductAlret(int idProduct){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Cảnh báo!");
        b.setMessage("Bạn có chắc chắn muốn xóa?");
        b.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    deleteProduct(idProduct);
                    Toast.makeText(ProductActivity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(ProductActivity.this, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                getProduct(urlProduct);
                dialogInterface.cancel();
            }
        });
        b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();
    }

    private void deleteProduct(int idProduct) throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JSONObject jsonbody= new JSONObject();
        jsonbody.put("idProduct",String.valueOf(idProduct));

        final  String requestbody= jsonbody.toString();

        StringRequest stringRequest= new StringRequest(Request.Method.DELETE, urlProduct+"/"+idProduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError{
                try {
                    return requestbody== null ? null : requestbody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody,"utf-8");
                    return null;
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response){
                String responseString="";
                if(response != null){
                    responseString= String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        mRequestQueue.add(stringRequest);
    }
}
