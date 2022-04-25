package com.example.bagsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActvity extends AppCompatActivity {
    EditText edtUser, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setControl();
        setEvent();
    }

    private void setControl(){
        edtUser= findViewById(R.id.edtUser);
        edtPassword= findViewById(R.id.edtPassword);
        btnLogin= findViewById(R.id.btnLogin);
    }

    private void setEvent(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user= edtUser.getText().toString().trim();
                String pass= edtPassword.getText().toString().trim();
                Intent intent= new Intent(LoginActvity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }


}