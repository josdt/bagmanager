package com.example.bagsmanager;

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
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.DetailBillAdapter;
import com.example.bagsmanager.Model.Bill;
import com.example.bagsmanager.Model.DetailBill;
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

public class DetailBillActivity extends AppCompatActivity {
    ListView lvlistDetailBill;
    ArrayList<DetailBill> detailBills;
    ArrayList<DetailBill> detailBills1;
    DetailBillAdapter detailBillAdapter;

    String urlgetDetailBill="http://192.168.1.10:3000/api/bill_detail";
    public int idBill;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.list_detail_bill);
        setActionBar();
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
        detailBills1= new ArrayList<>();
        detailBillAdapter= new DetailBillAdapter(DetailBillActivity.this, R.layout.detail_bill_item, detailBills);
        lvlistDetailBill.setAdapter(detailBillAdapter);
    }
    private void setEvent(){

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
                startActivity(new Intent(DetailBillActivity.this, HomeActivity.class));
                break;
            case  R.id.mnProduct:
                startActivity(new Intent(DetailBillActivity.this, ProductActivity.class));
                break;
            case  R.id.mnCustomer:
                startActivity(new Intent(DetailBillActivity.this, CustomerActivity.class));
                break;
            case  R.id.mnCustomerlock:
                startActivity(new Intent(DetailBillActivity.this, CustomerLockActivity.class));
                break;
            case  R.id.mnBill:
                onBackPressed();
                return true;
            case  R.id.mnBilled:
                onBackPressed();
                return true;
            case  R.id.mnStatictis:
                startActivity(new Intent(DetailBillActivity.this, StatictisActivity.class));
                break;
            case  R.id.mnPdf:
                try {
                    createPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailBillActivity.this, "Tạo dpf thất bại: "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void setActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("CHI TIẾT HÓA ĐƠN");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE4E1")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void createPDF() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "chitiethoadom.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);


        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(12, 12, 12, 12);

        Paragraph Title = new Paragraph("chi tiet hoa don").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
        float[] width = {5,5,10};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addCell(new Cell().add(new Paragraph("Ma san pham")));
        table.addCell(new Cell().add(new Paragraph("So luong")));
        table.addCell(new Cell().add(new Paragraph("Don gia")));


        detailBills1.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(DetailBillActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlgetDetailBill+"/"+idBill, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                detailBills1.add(new DetailBill(k.getInt("idProduct"),k.getInt("quantity"),k.getInt("price")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for(DetailBill j: detailBills1){
                            table.addCell(new Cell().add(new Paragraph(j.getidProduct()+"")));
                            table.addCell(new Cell().add(new Paragraph(j.getQuantity()+"")));
                            table.addCell(new Cell().add(new Paragraph(j.getPrice()+"")));
                        }
                        document.add(Title);
                        document.add(table);
                        document.close();
                        Toast.makeText(DetailBillActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailBillActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
        Toast.makeText(this, "Đã tạo file pdf", Toast.LENGTH_SHORT).show();
    }
}
