package com.example.bagsmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.CustomerAdapter;
import com.example.bagsmanager.Model.Customer;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity {
    ListView lvlistCustomer;
    ArrayList<Customer> customers;
    ArrayList<Customer> customers1;
    CustomerAdapter customerAdapter;

    String urlgetCustomer="http://10.0.2.2:3000/api/customer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_customer);
        setActionBar();
        setControl();
        setEvent();
//        customers.add(new Customer(1,1,"abc","123","kg","josductan@gmail.com","12345678",0,"Tấn"));
//        customerAdapter.notifyDataSetChanged();
        getCustomer(urlgetCustomer);

    }

    private void setEvent() {
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
                startActivity(new Intent(CustomerActivity.this, ProductActivity.class));
                break;
            case  R.id.mnCustomer:
                startActivity(new Intent(CustomerActivity.this, CustomerActivity.class));
                break;
            case  R.id.mnCustomerlock:
                startActivity(new Intent(CustomerActivity.this, CustomerLockActivity.class));
                break;
            case  R.id.mnBill:
                startActivity(new Intent(CustomerActivity.this, BillUnConfirmActivity.class));
                break;
            case  R.id.mnBilled:
                startActivity(new Intent(CustomerActivity.this, BillConfirmedActivity.class));
                break;
            case  R.id.mnStatictis:
                startActivity(new Intent(CustomerActivity.this, StatictisActivity.class));
                break;
            case  R.id.mnPdf:
                try {
                    createPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(CustomerActivity.this, "Tạo dpf thất bại: "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void setActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("QUẢN LÝ KHÁCH HÀNG");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE4E1")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setControl() {
        lvlistCustomer= findViewById(R.id.lvlistCustomer);
        customers= new ArrayList<>();
        customerAdapter= new CustomerAdapter(CustomerActivity.this,R.layout.customer_item, customers);
        lvlistCustomer.setAdapter(customerAdapter);
    }

    private void getCustomer(String url){
        customers.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/status/1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                customers.add(new Customer(k.getInt("idUser"),k.getInt("idRole"),k.getString("username"),
                                        k.getString("password"), k.getString("addressCustomer"),k.getString("email"),
                                        k.getString("phone"),k.getInt("sex"), k.getString("name")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        customerAdapter.notifyDataSetChanged();
                        Toast.makeText(CustomerActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    public void lockCustomer(int idCustomer){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Cảnh báo!");
        b.setMessage("Bạn có chắc chắn muốn khóa tài khoản khách hàng?");
        b.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //getAPI lock customer
                RequestQueue mRequestQueue = Volley.newRequestQueue(CustomerActivity.this);
                StringRequest stringRequest= new StringRequest(Request.Method.PUT, urlgetCustomer + "/block/" + idCustomer,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(CustomerActivity.this, "Khóa thành công", Toast.LENGTH_SHORT).show();
                                getCustomer(urlgetCustomer);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerActivity.this, "Khóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
                mRequestQueue.add(stringRequest);
                dialogInterface.cancel();
            }
        });
        b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();
    }


    private void createPDF() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "Khachhang.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);


        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(12, 12, 12, 12);

        Paragraph Title = new Paragraph("Danh sach khach hang").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
        float[] width = {10f, 30f, 10f};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addCell(new Cell().add(new Paragraph("Ho ten")));
        table.addCell(new Cell().add(new Paragraph("SDT")));
        table.addCell(new Cell().add(new Paragraph("Email")));
        table.addCell(new Cell().add(new Paragraph("Dia chi")));


        customers1.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlgetCustomer+"/status/1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                customers1.add(new Customer(k.getInt("idUser"),k.getInt("idRole"),k.getString("username"),
                                        k.getString("password"), k.getString("addressCustomer"),k.getString("email"),
                                        k.getString("phone"),k.getInt("sex"), k.getString("name")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for(Customer k: customers1){
                                table.addCell(new Cell().add(new Paragraph(k.getName()+"")));
                                table.addCell(new Cell().add(new Paragraph(k.getPhone()+"")));
                                table.addCell(new Cell().add(new Paragraph(k.getEmail()+"")));
                                table.addCell(new Cell().add(new Paragraph(k.getAddressCustommer()+"")));
                            }

                        }
                        customerAdapter.notifyDataSetChanged();
                        Toast.makeText(CustomerActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);

        document.add(Title);
        document.add(table);
        document.close();
        Toast.makeText(this, "Đã tạo file pdf", Toast.LENGTH_SHORT).show();
    }


}
