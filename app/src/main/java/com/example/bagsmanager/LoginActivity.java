package com.example.bagsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Model.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {
    EditText edtUser, edtPassword;
    Button btnLogin;
    Customer customerlogin;


    String urllogin="http://192.168.1.10:3000/api/customer/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        getSupportActionBar().hide();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test);
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
                try {
                    login(urllogin, user, pass);
                } catch (JSONException  e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Lỗi"+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login(String url , String username, String password ) throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        JSONObject jsonbody = new JSONObject();
        jsonbody.put("username", username);
        jsonbody.put("password", password);

        final String requestbody = jsonbody.toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){
                            Toast.makeText(LoginActivity.this, "Sai thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                JSONObject k = response.getJSONObject(0);
                                if(k.getInt("idRole")==1){
                                    customerlogin= new Customer(k.getInt("idUser"),k.getInt("idRole"),k.getString("username"),
                                            k.getString("password"), k.getString("addressCustomer"),k.getString("email"),
                                            k.getString("phone"),k.getInt("sex"), k.getString("name"));
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("customerLogin", customerlogin);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
//                k.getJSONObject("sex").getJSONArray("data").getInt(0)
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Lỗi!" + error, Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestbody == null ? null : requestbody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody, "utf-8");
                    return null;
                }
            }
        };
        mRequestQueue.add(jsonArrayRequest);
    }

}