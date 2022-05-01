package com.example.bagsmanager;

import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bagsmanager.Adapter.StatictisAdapter;
import com.example.bagsmanager.Model.Statictis;

import java.util.ArrayList;

public class StatictisActivity extends AppCompatActivity {
    Spinner spnYear;
    ListView lvStatictis;
    TabHost tabHost;
    StatictisAdapter statictisAdapter;
    ArrayList<Statictis> statictiss;
    String urlgetStatictis="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statictis);
        setControl();
        setEvent();


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

        statictiss= new ArrayList<>();
        statictisAdapter= new StatictisAdapter(this, R.layout.statictis_item, statictiss);
        lvStatictis.setAdapter(statictisAdapter);
    }
    private  void setEvent(){
    }

    private void getStatictis(int year){

    }
}
