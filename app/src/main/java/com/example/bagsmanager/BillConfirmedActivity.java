package com.example.bagsmanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.BillConfirmedAdapter;
import com.example.bagsmanager.Adapter.BillUnConfirmAdapter;
import com.example.bagsmanager.Model.Bill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BillConfirmedActivity extends AppCompatActivity {
    ListView lvlistBilled;
    ArrayList<Bill> billsed;
    BillConfirmedAdapter billConfirmedAdapter;

    String urlgetBilled="http://10.0.2.2:3000/api/bill";

    public static  int idbilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_billed);
        setActionBar();
        setControl();
        getBill(urlgetBilled);
        setEvent();
    }
    private void setControl() {
        lvlistBilled= findViewById(R.id.lvlistBilled);
        billsed= new ArrayList<>();
        billConfirmedAdapter = new BillConfirmedAdapter(BillConfirmedActivity.this,R.layout.bill_item_confirmed, billsed);
        lvlistBilled.setAdapter(billConfirmedAdapter);
    }

    private void setEvent() {
        lvlistBilled.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idbilled= billsed.get(i).getIdBill();
                Intent intent= new Intent(BillConfirmedActivity.this, DetailBillActivity.class);
                Bundle bundle= new Bundle();
                bundle.putInt("idBill", idbilled);
                intent.putExtra("Bill",bundle);
                startActivity(intent);
            }
        });
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
                startActivity(new Intent(BillConfirmedActivity.this, ProductActivity.class));
                break;
            case  R.id.mnCustomer:
                startActivity(new Intent(BillConfirmedActivity.this, CustomerActivity.class));
                break;
            case  R.id.mnCustomerlock:
                startActivity(new Intent(BillConfirmedActivity.this, CustomerLockActivity.class));
                break;
            case  R.id.mnBill:
                startActivity(new Intent(BillConfirmedActivity.this, BillUnConfirmActivity.class));
                break;
            case  R.id.mnBilled:
                startActivity(new Intent(BillConfirmedActivity.this, BillConfirmedActivity.class));
                break;
            case  R.id.mnStatictis:
                startActivity(new Intent(BillConfirmedActivity.this, StatictisActivity.class));
                break;
            default:break;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void setActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("HÓA ĐƠN HOÀN THÀNH");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE4E1")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getBill(String url){
        billsed.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(BillConfirmedActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/status_bill/1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        SimpleDateFormat formater= new SimpleDateFormat("yyyy-MM-dd");
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                java.util.Date date= formater.parse(k.getString("dateBill"));
                                billsed.add(new Bill(k.getInt("idBill"),k.getInt("idUser"), date,k.getInt("status")));
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        billConfirmedAdapter.notifyDataSetChanged();
                        Toast.makeText(BillConfirmedActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BillConfirmedActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
