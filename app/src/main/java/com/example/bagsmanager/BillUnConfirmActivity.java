package com.example.bagsmanager;


import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bagsmanager.Adapter.BillUnConfirmAdapter;
import com.example.bagsmanager.Mail.JavaMailAPI;
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

    String email;

    String urlgetBill= LoginActivity.ip+":3000/api/bill";

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
                    Toast.makeText(BillUnConfirmActivity.this, "T???o dpf th???t b???i: "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void setActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("H??A ????N ??ANG CH???");
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
                        Toast.makeText(BillUnConfirmActivity.this, "L???i!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    public void confirmDialog(int id, int idUser){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("C???nh b??o!");
        b.setMessage("B???n c?? ch???c ch???n mu???n kh??a t??i kho???n kh??ch h??ng?");
        b.setPositiveButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RequestQueue mRequestQueue = Volley.newRequestQueue(BillUnConfirmActivity.this);
                StringRequest stringRequest= new StringRequest(Request.Method.PUT, urlgetBill+"/"+id,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(BillUnConfirmActivity.this,"X??c nh???n th??nh c??ng", Toast.LENGTH_SHORT).show();
                                getEmail(idUser);
                                getBill(urlgetBill);
                                dialogInterface.cancel();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(BillUnConfirmActivity.this,"X??c nh???n th???t b???i", Toast.LENGTH_SHORT).show();
                            }
                        });
                mRequestQueue.add(stringRequest);
            }
        });
        b.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();
    }

    public void getEmail(int idUser){
        RequestQueue mRequestQueue = Volley.newRequestQueue(BillUnConfirmActivity.this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, LoginActivity.ip + ":3000/api/customer/" + idUser, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject k= response.getJSONObject(0);
                    email= k.getString("email");
                    sendEmail(email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(jsonArrayRequest);
    }

    private void sendEmail(String email){
        String message="c???m ??n b???n ???? s??? d???ng d???ch v??? c???a ch??ng t??i " +
                "\n ????n h??ng c???a b???n s??? ???????c g???i ?????n trong th???i gian s???m nh???t";
        String subject="X??c nh???n ????n h??ng";

        JavaMailAPI javaMailAPI= new JavaMailAPI(BillUnConfirmActivity.this, email, subject, message);
        javaMailAPI.execute();
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
                        Toast.makeText(BillUnConfirmActivity.this, "L???i!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
        Toast.makeText(this, "???? t???o file pdf", Toast.LENGTH_SHORT).show();
    }
}
