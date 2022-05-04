package com.example.bagsmanager;

import android.content.Intent;
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
import com.example.bagsmanager.Adapter.DetailBillAdapter;
import com.example.bagsmanager.Model.DetailBill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailBillActivity extends AppCompatActivity {
    ListView lvlistDetailBill;
    ArrayList<DetailBill> detailBills;
    DetailBillAdapter detailBillAdapter;

    String urlgetDetailBill="http://10.0.2.2:3000/api/bill_detail";
    public int idBill;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.list_detail_bill);
        Intent intent= getIntent();
        Bundle bundle=intent.getBundleExtra("Bill");
        idBill=bundle.getInt("idBill");
        setControl();
        setEvent();
        getDetailBill(urlgetDetailBill);
    }

    private void setControl() {
        lvlistDetailBill= findViewById(R.id.lvlistBillDetail);
        detailBills= new ArrayList<>();
        detailBillAdapter= new DetailBillAdapter(DetailBillActivity.this, R.layout.detail_bill_item, detailBills);
        lvlistDetailBill.setAdapter(detailBillAdapter);
    }
    private void setEvent(){

    }
    private void getDetailBill(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(DetailBillActivity.this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url+"/"+idBill, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject k= response.getJSONObject(i);
                                detailBills.add(new DetailBill(k.getInt("idProduct"),k.getInt("quantity"),k.getInt("price")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        detailBillAdapter.notifyDataSetChanged();
                            Toast.makeText(DetailBillActivity.this, response.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailBillActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
