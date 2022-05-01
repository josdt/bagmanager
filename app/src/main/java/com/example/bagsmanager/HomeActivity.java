package com.example.bagsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    ImageView ivProductManager, ivCustomerManager,ivBillManager, ivStatistics, ivExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        setControl();
        setEvent();
    }

    private void setControl(){
        ivProductManager = findViewById(R.id.ivProductManager);
        ivCustomerManager= findViewById(R.id.ivCustomerManager);
        ivBillManager= findViewById(R.id.ivBillManager);
        ivStatistics= findViewById(R.id.ivStatistics);
        ivExit= findViewById(R.id.ivExit);

    }

    private void setEvent(){
        ivProductManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
        ivCustomerManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, CustomerActivity.class);
                startActivity(intent);
            }
        });
        ivBillManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, BillActivity.class);
                startActivity(intent);
            }
        });
        ivStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, StatictisActivity.class);
                startActivity(intent);
            }
        });
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.finish();
            }
        });
    }

}
