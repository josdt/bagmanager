package com.example.bagsmanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.StatictisAdapter;
import com.example.bagsmanager.Model.Statictis;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StatictisActivity extends AppCompatActivity {
    Spinner spnYear;
    ListView lvStatictis;
    TabHost tabHost;
    StatictisAdapter statictisAdapter;
    ArrayList<Statictis> statictiss;
    String urlgetStatictis=LoginActivity.ip+":3000/api/bill/statistic_revenue";
    ArrayList<Integer> years= new ArrayList<>();

    BarChart bchart;
    HashMap<String,Float> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statictis);
        setActionBar();
        setControl();
        getYear();
        setEvent();
//        setSpinerOption();


    }

    private void setControl() {
        spnYear= findViewById(R.id.spnYear);
        lvStatictis= findViewById(R.id.lvStatictis);
        tabHost=findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec1, tabSpec2;
        tabSpec1= tabHost.newTabSpec("t1");
        tabSpec1.setContent(R.id.tab1);
        tabSpec1.setIndicator("Số liệu");
        tabHost.addTab(tabSpec1);
        tabSpec2= tabHost.newTabSpec("t2");
        tabSpec2.setContent(R.id.tab2);
        tabSpec2.setIndicator("Biểu đồ");
        tabHost.addTab(tabSpec2);


        statictiss= new ArrayList<>(12);
        statictisAdapter= new StatictisAdapter(this, R.layout.statictis_item, statictiss);
        lvStatictis.setAdapter(statictisAdapter);


        bchart= findViewById(R.id.bchart);
        map= new HashMap<>();
    }
    private  void setEvent(){
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
                startActivity(new Intent(StatictisActivity.this, ProductActivity.class));
                break;
            case  R.id.mnCustomer:
                startActivity(new Intent(StatictisActivity.this, CustomerActivity.class));
                break;
            case  R.id.mnCustomerlock:
                startActivity(new Intent(StatictisActivity.this, CustomerLockActivity.class));
                break;
            case  R.id.mnBill:
                startActivity(new Intent(StatictisActivity.this, BillUnConfirmActivity.class));
                break;
            case  R.id.mnBilled:
                startActivity(new Intent(StatictisActivity.this, BillConfirmedActivity.class));
                break;
            case  R.id.mnStatictis:
                startActivity(new Intent(StatictisActivity.this, StatictisActivity.class));
                break;
            default:break;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void setActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("THỐNG KÊ");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE4E1")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void getYear(){
        years.clear();
        String url=LoginActivity.ip+":3000/api/bill/year_bill";
        RequestQueue requestQueue= Volley.newRequestQueue(StatictisActivity.this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject k = response.getJSONObject(i);
                                years.add(k.getInt("year"));
                                setSpinerOption();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StatictisActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void setSpinerOption(){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(adapter);

        final int[] year = new int[1];

        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year[0]= years.get(i);
                Toast.makeText(StatictisActivity.this,year[0]+"", Toast.LENGTH_SHORT).show();
                getStatictis(year[0]);
//                getDataBChart();
//                setBarChart();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setBarChart(){
        map.clear();
        for(Statictis k: statictiss){
            String x=String.valueOf(k.getMonth());
            float y= k.getTurnover();
            map.put(x,y);
        }
        bchart.clear();
        ArrayList<BarEntry> barEntries= new ArrayList<>();
        ArrayList<String> labels= new ArrayList<>();

//        int i=0;
        for(int i=0;i<12;i++){
            barEntries.add(new BarEntry(i,map.get(String.valueOf(i+1)),String.valueOf(i+1)));
            labels.add(String.valueOf(i+1));
        }
//        for(String k: map.keySet()){
//            barEntries.add(new BarEntry(i, map.get(k), k));
//            labels.add(k);
////            Toast.makeText(StatictisActivity.this, k,Toast.LENGTH_SHORT).show();
//            i++;
//        }
        Toast.makeText(StatictisActivity.this,map+"",Toast.LENGTH_SHORT).show();

        XAxis xAxis= bchart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        BarDataSet dataSet= new BarDataSet(barEntries, "Số lượng");
        BarData data= new BarData(dataSet);
        bchart.setData(data);
    }

    private void getDataBChart(){
        map.clear();
            for(Statictis k: statictiss){
                String x=String.valueOf(k.getMonth());
                float y= k.getTurnover();
                map.put(x,y);
        }
//        for(int i=0;i<12;i++){
//            String x=String.valueOf(statictiss.get(i).getMonth());
//            float y= statictiss.get(i).getTurnover();
//            map.put(x,y);
//        }
    }

    private void getStatictis(int year){
        statictiss.clear();
        setNullArraylist(statictiss);
        RequestQueue requestQueue= Volley.newRequestQueue(StatictisActivity.this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, urlgetStatictis+"/"+year, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject k= response.getJSONObject(i);
                                statictiss.set((k.getInt("month"))-1,new Statictis(k.getInt("month"),k.getInt("revenue")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        setBarChart();
                        statictisAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StatictisActivity.this, "Lỗi"+error,Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void setNullArraylist(ArrayList<Statictis> stts){
        for(int i=0; i<12;i++){
            stts.add(i,new Statictis(i+1,0));
        }
    }
}
