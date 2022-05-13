package com.example.bagsmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.CustomerAdapter;
import com.example.bagsmanager.Adapter.CustomerLockAdapter;
import com.example.bagsmanager.Model.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerLockActivity extends AppCompatActivity {
    ListView lvlistCustomerlock;
    ArrayList<Customer> customerslock;
    CustomerLockAdapter customerLockAdapter;

    String urlgetCustomerLock="http://10.0.2.2:3000/api/customer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_customer_lock);
        setActionBar();
        setControl();
        setEvent();
        getCustomer(urlgetCustomerLock);

    }
    private void setEvent() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuaction,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case  R.id.mnHome:
                onBackPressed();
                return true;
            case  R.id.mnProduct:
                startActivity(new Intent(CustomerLockActivity.this, ProductActivity.class));
                break;
            case  R.id.mnCustomer:
                startActivity(new Intent(CustomerLockActivity.this, CustomerActivity.class));
                break;
            case  R.id.mnCustomerlock:
                startActivity(new Intent(CustomerLockActivity.this, CustomerLockActivity.class));
                break;
            case  R.id.mnBill:
                startActivity(new Intent(CustomerLockActivity.this, BillUnConfirmActivity.class));
                break;
            case  R.id.mnBilled:
                startActivity(new Intent(CustomerLockActivity.this, BillConfirmedActivity.class));
                break;
            case  R.id.mnStatictis:
                startActivity(new Intent(CustomerLockActivity.this, StatictisActivity.class));
                break;
            default:break;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void setActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("DANH SÁCH ĐEN");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE4E1")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setControl() {
        lvlistCustomerlock= findViewById(R.id.lvlistCustomerlock);
        customerslock= new ArrayList<>();
        customerLockAdapter= new CustomerLockAdapter(CustomerLockActivity.this,R.layout.customer_item_lock, customerslock);
        lvlistCustomerlock.setAdapter(customerLockAdapter);
    }
    private void getCustomer(String url){
        customerslock.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerLockActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/status/0", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                customerslock.add(new Customer(k.getInt("idUser"),k.getInt("idRole"),k.getString("username"),
                                        k.getString("password"), k.getString("addressCustomer"),k.getString("email"),
                                        k.getString("phone"),k.getInt("sex"), k.getString("name")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        customerLockAdapter.notifyDataSetChanged();
                        Toast.makeText(CustomerLockActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerLockActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    public void unlockCustomer(int idCustomer){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Cảnh báo!");
        b.setMessage("Bạn có chắc chắn muốn mở khóa tài khoản khách hàng?");
        b.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //getAPI unlock customer
                RequestQueue mRequestQueue = Volley.newRequestQueue(CustomerLockActivity.this);
                StringRequest stringRequest= new StringRequest(Request.Method.PUT, urlgetCustomerLock + "/unblock/" + idCustomer,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(CustomerLockActivity.this, "Mở khóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerLockActivity.this, "Mở khóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
                mRequestQueue.add(stringRequest);
                getCustomer(urlgetCustomerLock);
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
}
