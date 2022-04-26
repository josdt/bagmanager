package com.example.bagsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Model.Customer;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActvity extends AppCompatActivity {
    EditText edtUser, edtPassword;
    Button btnLogin;
    Customer cus=null;

    String urllogin="http://10.0.2.2:3000/api/customer/login";

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
//                String user= edtUser.getText().toString().trim();
//                String pass= edtPassword.getText().toString().trim();
//                try {
//                    login(urllogin,user,pass);
//                    String email = cus.getEmail();
//                    Log.d("Tag", email);
//                    if(cus==null){
//                        Toast.makeText(LoginActvity.this, "dang nhap sai mk hoac pass", Toast.LENGTH_SHORT).show();
//
//                    } else{
//                        Toast.makeText(LoginActvity.this, "dang nhap thanh cong", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Intent intent= new Intent(LoginActvity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String url , String username, String password ) throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(LoginActvity.this);

        JSONObject jsonbody = new JSONObject();
        jsonbody.put("username", username);
        jsonbody.put("password", password);

//        cus=null;
        final String requestbody = jsonbody.toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject k= response.getJSONObject(i);
                                cus= new Customer(k.getInt("idUser"), k.getInt("idRole"),k.getString("username"),k.getString("password"),k.getString("addressCustomer"),k.getString("email"),k.getString("phone"),k.getJSONObject("sex").getJSONArray("data").getInt(0),k.getString("name"));
                                return;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActvity.this, "Lỗi!" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ){
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestbody== null ? null : requestbody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody,"utf-8");
                    return null;
                }
            }};
        mRequestQueue.add(jsonArrayRequest);
    }

}