package com.example.bagsmanager;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.BillAdapter;
import com.example.bagsmanager.Adapter.CustomerAdapter;
import com.example.bagsmanager.Model.Bill;
import com.example.bagsmanager.Model.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

public class BillActivity extends AppCompatActivity {
    ListView lvlistBill;
    ArrayList<Bill> bills;
    BillAdapter billAdapter;

    String urlgetBill= "http://10.0.2.2:3000/api/bill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_bill);
        setControl();
        setEvent();
        getBill(urlgetBill);

    }

    private void setControl() {
        lvlistBill= findViewById(R.id.lvlistBill);
        bills= new ArrayList<>();
        billAdapter= new BillAdapter(BillActivity.this,R.layout.bill_item, bills);
        lvlistBill.setAdapter(billAdapter);
    }

    private void setEvent() {
    }

    private void getBill(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(BillActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                bills.add(new Bill(k.getInt("idBill"),k.getInt("idUser"), Date.valueOf(k.getString("dateBill"))));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        billAdapter.notifyDataSetChanged();
                        Toast.makeText(BillActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BillActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
