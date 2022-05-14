package com.example.bagsmanager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.MenuAdapter;
import com.example.bagsmanager.Model.Customer;
import com.example.bagsmanager.Model.ItemMenu;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView lvMenu;
    ArrayList<ItemMenu> itemMenus;
    MenuAdapter menuAdapter;
    EditText edtName, edtAddress, edtEmail, edtPhone;
    ImageView ivProductManager, ivCustomerManager,ivBillManager, ivStatistics, ivExit;
    Button btnUpdateInfor, btnChangePass;
    String urlCus="http://10.0.2.2:3000/api/customer";
    int idcus;
    String username;

    EditText edtPass, edtNewPass, edtConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
//        getSupportActionBar().hide();
        Intent intent = getIntent();
        Customer customer = (Customer) intent.getSerializableExtra("customerLogin");
        idcus= customer.getIdUser();
        username=customer.getUsername();
        setControl();
        actionToolBar();
        actionMenu();
        getInfor(customer);
        setEvent();
    }

    private void setControl(){
        toolbar= findViewById(R.id.toolBar);
        drawerLayout= findViewById(R.id.drawerLayout);
        navigationView= findViewById(R.id.nvgView);
        lvMenu= findViewById(R.id.lvMenu);

        edtName= findViewById(R.id.edtName);
        edtAddress= findViewById(R.id.edtAddress);
        edtEmail= findViewById(R.id.edtEmail);
        edtPhone= findViewById(R.id.edtPhone);
//        ivProductManager = findViewById(R.id.ivProductManager);
//        ivCustomerManager= findViewById(R.id.ivCustomerManager);
//        ivBillManager= findViewById(R.id.ivBillManager);
//        ivStatistics= findViewById(R.id.ivStatistics);
//        ivExit= findViewById(R.id.ivExit);
        btnUpdateInfor=findViewById(R.id.btnUpdateInfor);
        btnChangePass= findViewById(R.id.btnChangePass);

    }

    private void setEvent(){
        btnUpdateInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= edtName.getText().toString().trim();
                String addr= edtAddress.getText().toString().trim();
                String email= edtEmail.getText().toString().trim();
                String phone= edtPhone.getText().toString().trim();
                updateInfor(urlCus,idcus,name,addr,email,phone);
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassDialog(username);
            }
        });
//        ivProductManager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(HomeActivity.this, ProductActivity.class);
//                startActivity(intent);
//            }
//        });
//        ivCustomerManager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(HomeActivity.this, CustomerActivity.class);
//                startActivity(intent);
//            }
//        });
//        ivBillManager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(HomeActivity.this, BillActivity.class);
//                startActivity(intent);
//            }
//        });
//        ivStatistics.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(HomeActivity.this, StatictisActivity.class);
//                startActivity(intent);
//            }
//        });
//        ivExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HomeActivity.this.finish();
//            }
//        });
    }

    private void getInfor(Customer customer){
        edtName.setText(customer.getName());
        edtAddress.setText(customer.getAddressCustommer());
        edtEmail.setText(customer.getEmail());
        edtPhone.setText(customer.getPhone());
    }

    private void updateInfor(String url,int id, String name, String address, String email, String phone){
        RequestQueue requestQueue= Volley.newRequestQueue(HomeActivity.this);
        JSONObject jsonbody= new JSONObject();
        try {
            jsonbody.put("name", name);
            jsonbody.put("addressCustomer",address);
            jsonbody.put("email",email);
            jsonbody.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestbody= jsonbody.toString();
        StringRequest stringRequest= new StringRequest(Request.Method.PUT, url + "/info/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HomeActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestbody== null ? null : requestbody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody,"utf-8");
                    return null;
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response){
                String responseString="";
                if(response != null){
                    responseString= String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        requestQueue.add(stringRequest);
    }

    private void changePassDialog(String user){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.changepass_dialog,(ViewGroup) findViewById(R.layout.homepage));
        edtPass= dialogView.findViewById(R.id.edtPass);
        edtNewPass= dialogView.findViewById(R.id.edtNewPass);
        edtConfirmPass= dialogView.findViewById(R.id.edtConfirmPass);

        dialogBuilder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String pass= edtPass.getText().toString().trim();
                String newPass= edtNewPass.getText().toString().trim();
                String confirmPass= edtConfirmPass.getText().toString().trim();
                checkPass(user,pass,newPass, confirmPass);
            }
        });
        dialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("DỔI MẬT KHẨU");
        AlertDialog a = dialogBuilder.create();
        a.show();
    }

    private void checkPass(String user, String pass, String newPass, String confirmPass){
        RequestQueue mRequestQueue = Volley.newRequestQueue(HomeActivity.this);
        JSONObject jsonbody = new JSONObject();
        try {
            jsonbody.put("username", user);
            jsonbody.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestbody = jsonbody.toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, "http://10.0.2.2:3000/api/customer/login", null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){
                            Toast.makeText(HomeActivity.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                        }else{
                            if(newPass.equals(confirmPass)){
                                changePass(user,newPass);
                            }
                            else{
                                Toast.makeText(HomeActivity.this, "Vui lòng xác nhận lại mật khẩu", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                },
//                k.getJSONObject("sex").getJSONArray("data").getInt(0)
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Lỗi!" + error, Toast.LENGTH_SHORT).show();
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
    private void changePass(String user, String newPass){
        RequestQueue requestQueue= Volley.newRequestQueue(HomeActivity.this);
        JSONObject jsonbody= new JSONObject();
        try {
            jsonbody.put("username", user);
            jsonbody.put("password",newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestbody= jsonbody.toString();
        StringRequest stringRequest= new StringRequest(Request.Method.PUT, "http://10.0.2.2:3000/api/customer/change_pass",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HomeActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestbody== null ? null : requestbody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody,"utf-8");
                    return null;
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response){
                String responseString="";
                if(response != null){
                    responseString= String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        requestQueue.add(stringRequest);
    }

    public void itemAction(int a){
        switch (a){
            case 1:
                Intent intent= new Intent(HomeActivity.this, ProductActivity.class);
                startActivity(intent);
                break;
            case 2:
                Intent intent1= new Intent(HomeActivity.this, CustomerActivity.class);
                startActivity(intent1);
                break;
            case 3:
                Intent intent2= new Intent(HomeActivity.this, CustomerLockActivity.class);
                startActivity(intent2);
                break;
            case 4:
                Intent intent3= new Intent(HomeActivity.this, BillUnConfirmActivity.class);
                startActivity(intent3);
                break;
            case 5:
                Intent intent4= new Intent(HomeActivity.this, BillConfirmedActivity.class);
                startActivity(intent4);
                break;
            case 6:
                Intent intent5= new Intent(HomeActivity.this, StatictisActivity.class);
                startActivity(intent5);
                break;
            case 7:
                Intent intent6= new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent6);
                break;
        }
    }

    private void actionToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("QUẢN LÝ BÁN HÀNG");
        toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

    }
    private void actionMenu(){
        itemMenus=  new ArrayList<>();
        itemMenus.add(new ItemMenu(1,"Quản lý sản phầm", R.drawable.product));
        itemMenus.add(new ItemMenu(2,"Quản lý khách hàng", R.drawable.guest));
        itemMenus.add(new ItemMenu(3,"Danh sách đen", R.drawable.lockcus));
        itemMenus.add(new ItemMenu(4,"Hóa đơn đang chờ", R.drawable.bill));
        itemMenus.add(new ItemMenu(5,"Hóa đơn hoàn thành", R.drawable.billed));
        itemMenus.add(new ItemMenu(6,"Thống kê", R.drawable.chart));
        itemMenus.add(new ItemMenu(7,"Thoát", R.drawable.logout));

        menuAdapter= new MenuAdapter(HomeActivity.this,R.layout.menu_item,itemMenus);
        lvMenu.setAdapter(menuAdapter);
    }

}
