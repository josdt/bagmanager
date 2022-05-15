package com.example.bagsmanager;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.BillUnConfirmAdapter;
import com.example.bagsmanager.Model.Bill;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BillUnConfirmActivity extends AppCompatActivity {
    ListView lvlistBill;
    ArrayList<Bill> bills;
    ArrayList<Bill> bills1;
    BillUnConfirmAdapter billAdapter;

    String urlgetBill= "http://192.168.1.10:3000/api/bill";

    public static int idBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_bill);
        setActionBar();
        setControl();
        getBill(urlgetBill);
        setEvent();

    }

    private void setControl() {
        lvlistBill= findViewById(R.id.lvlistBill);
        bills= new ArrayList<>();
        bills1= new ArrayList<>();
        billAdapter= new BillUnConfirmAdapter(BillUnConfirmActivity.this,R.layout.bill_item_unconfirm, bills);
        lvlistBill.setAdapter(billAdapter);
    }

    private void setEvent() {
        lvlistBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idBill= bills.get(i).getIdBill();
                Intent intent= new Intent(BillUnConfirmActivity.this, DetailBillActivity.class);
                Bundle bundle= new Bundle();
                bundle.putInt("idBill", idBill);
                intent.putExtra("Bill",bundle);
                startActivity(intent);
            }
        });
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
                startActivity(new Intent(BillUnConfirmActivity.this, ProductActivity.class));
                break;
            case  R.id.mnCustomer:
                startActivity(new Intent(BillUnConfirmActivity.this, CustomerActivity.class));
                break;
            case  R.id.mnCustomerlock:
                startActivity(new Intent(BillUnConfirmActivity.this, CustomerLockActivity.class));
                break;
            case  R.id.mnBill:
                startActivity(new Intent(BillUnConfirmActivity.this, BillUnConfirmActivity.class));
                break;
            case  R.id.mnBilled:
                startActivity(new Intent(BillUnConfirmActivity.this, BillConfirmedActivity.class));
                break;
            case  R.id.mnStatictis:
                startActivity(new Intent(BillUnConfirmActivity.this, StatictisActivity.class));
                break;
            case  R.id.mnPdf:
                try {
                    createPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(BillUnConfirmActivity.this, "Tạo dpf thất bại: "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void setActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("HÓA ĐƠN ĐANG CHỜ");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE4E1")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getBill(String url){
        bills.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(BillUnConfirmActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/status_bill/0", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        SimpleDateFormat formater= new SimpleDateFormat("yyyy-MM-dd");
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                java.util.Date date= formater.parse(k.getString("dateBill"));
                                bills.add(new Bill(k.getInt("idBill"),k.getInt("idUser"), date,k.getInt("status")));
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        billAdapter.notifyDataSetChanged();
                        Toast.makeText(BillUnConfirmActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BillUnConfirmActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    public void checkBill(int id){
        RequestQueue mRequestQueue = Volley.newRequestQueue(BillUnConfirmActivity.this);
        StringRequest stringRequest= new StringRequest(Request.Method.PUT, urlgetBill+"/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(BillUnConfirmActivity.this,"Xác nhận thành công", Toast.LENGTH_SHORT).show();
                        getBill(urlgetBill);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BillUnConfirmActivity.this,"Xác nhận thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
        mRequestQueue.add(stringRequest);
    }


    private void createPDF() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "hoadoncho.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);


        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(12, 12, 12, 12);

        Paragraph Title = new Paragraph("Danh sach hoa don cho").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
        float[] width = {10,10,10};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addCell(new Cell().add(new Paragraph("Ma so hoa don")));
        table.addCell(new Cell().add(new Paragraph("Ma khach hang")));
        table.addCell(new Cell().add(new Paragraph("Ngay lap")));


        bills1.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(BillUnConfirmActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlgetBill+"/status_bill/1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        SimpleDateFormat formater= new SimpleDateFormat("yyyy-MM-dd");
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                java.util.Date date= formater.parse(k.getString("dateBill"));
                                bills.add(new Bill(k.getInt("idBill"),k.getInt("idUser"), date,k.getInt("status")));
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        for(Bill j: bills1){
                            table.addCell(new Cell().add(new Paragraph(j.getIdBill()+"")));
                            table.addCell(new Cell().add(new Paragraph(j.getIdUser()+"")));
                            table.addCell(new Cell().add(new Paragraph(j.getDateBill()+"")));
                        }
                        document.add(Title);
                        document.add(table);
                        document.close();
                        Toast.makeText(BillUnConfirmActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BillUnConfirmActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
        Toast.makeText(this, "Đã tạo file pdf", Toast.LENGTH_SHORT).show();
    }
}
