package com.example.bagsmanager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.bagsmanager.Model.Bill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BillActivity extends AppCompatActivity {
    ListView lvlistBill;
    ArrayList<Bill> bills;
    BillAdapter billAdapter;

    String urlgetBill= "http://10.0.2.2:3000/api/bill";

    public static int idBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_bill);
        setControl();
        getBill(urlgetBill);
        setEvent();

    }

    private void setControl() {
        lvlistBill= findViewById(R.id.lvlistBill);
        bills= new ArrayList<>();
        billAdapter= new BillAdapter(BillActivity.this,R.layout.bill_item, bills);
        lvlistBill.setAdapter(billAdapter);
    }

    private void setEvent() {
        lvlistBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idBill= bills.get(i).getIdBill();
                Intent intent= new Intent(BillActivity.this, DetailBillActivity.class);
                Bundle bundle= new Bundle();
                bundle.putInt("idBill", idBill);
                intent.putExtra("Bill",bundle);
                startActivity(intent);
            }
        });
    }

    private void getBill(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(BillActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        SimpleDateFormat formater= new SimpleDateFormat("yyyy-MM-dd");
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                java.util.Date date= formater.parse(k.getString("dateBill"));
                                bills.add(new Bill(k.getInt("idBill"),k.getInt("idUser"), date));
                            } catch (JSONException | ParseException e) {
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
