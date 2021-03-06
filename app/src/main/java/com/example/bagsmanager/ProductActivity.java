package com.example.bagsmanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.bagsmanager.Adapter.ProductAdapter;
import com.example.bagsmanager.Model.Brand;
import com.example.bagsmanager.Model.Color;
import com.example.bagsmanager.Model.Customer;
import com.example.bagsmanager.Model.Product;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;


public class ProductActivity extends AppCompatActivity {

    ListView lvlistProduct;
    Button btnThem;
    ArrayList<Product> products;
    ArrayList<Product> products1;
    ProductAdapter productAdapter;

    ArrayList<Brand> brr;
    ArrayList<String> brand= new ArrayList<>();

    ArrayList<Color> coo;
    ArrayList<String> color= new ArrayList<>();

    TextView tvTitleDialog,tvTitleDialog1;
    EditText edtTitle, edtPrice, edtQuantity, edtDescr,edtTitle1, edtPrice1, edtQuantity1, edtDescr1;
    Spinner spnColor, spnBrand;
    Button btnCancel, btnSave;

    ImageButton ibUploadImage;
    ImageView ivUploadImage;
    TextView tvNotification;
    String hinhanh;
    int REQUEST_CODE_IMAGE = 123;
    public static String urri;



    String urlProduct=LoginActivity.ip+":3000/api/product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_product);
        setActionBar();
        setControl();
        getBrands();
        getColors();
        setEvent();
        getProduct(urlProduct);
    }

    private void setControl() {
        btnThem= findViewById(R.id.btnThem);
        lvlistProduct= findViewById(R.id.lvlistProduct);
        products= new ArrayList<>();
        products1= new ArrayList<>();
        brr= new ArrayList<>();
        coo= new ArrayList<>();
        productAdapter= new ProductAdapter(ProductActivity.this, R.layout.product_item, products);
        lvlistProduct.setAdapter(productAdapter);

    }

    private void setEvent(){
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductdialog();
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
                startActivity(new Intent(ProductActivity.this, ProductActivity.class));
                break;
            case  R.id.mnCustomer:
                startActivity(new Intent(ProductActivity.this, CustomerActivity.class));
                break;
            case  R.id.mnCustomerlock:
                startActivity(new Intent(ProductActivity.this, CustomerLockActivity.class));
                break;
            case  R.id.mnBill:
                startActivity(new Intent(ProductActivity.this, BillUnConfirmActivity.class));
                break;
            case  R.id.mnBilled:
                startActivity(new Intent(ProductActivity.this, BillConfirmedActivity.class));
                break;
            case  R.id.mnStatictis:
                startActivity(new Intent(ProductActivity.this, StatictisActivity.class));
                break;
            case  R.id.mnPdf:
                try {
                    createPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(ProductActivity.this, "T???o dpf th???t b???i: "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void setActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("QU???N L?? S???N PH???M");
        actionBar.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#FFE4E1")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void getBrands(){
        brr.clear();
        String url=LoginActivity.ip+":3000/api/brand";
        RequestQueue requestQueue= Volley.newRequestQueue(ProductActivity.this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject k = response.getJSONObject(i);
                                brr.add(new Brand(k.getInt("idBrand"),k.getString("nameBrand")));
                                brand.add(k.getString("nameBrand"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void getColors(){
        coo.clear();
        String url=LoginActivity.ip+":3000/api/color";
        RequestQueue requestQueue= Volley.newRequestQueue(ProductActivity.this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject k = response.getJSONObject(i);
                                coo.add(new Color(k.getInt("idColor"),k.getString("nameColor")));
                                color.add(k.getString("nameColor"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void getProduct(String url){
        products.clear();
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject k= response.getJSONObject(i);
                                products.add(new Product(k.getInt("idProduct"), k.getInt("price"), k.getString("descr"),
                                        k.getString("title"), k.getInt("idColor"), k.getInt("idBrand"), k.getString("image"),
                                        k.getInt("quantity")));
//
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        productAdapter.notifyDataSetChanged();
//                        Toast.makeText(ProductActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductActivity.this, "L???i!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(jsonArrayRequest);
    }

    private void addProductdialog(){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductActivity.this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.add_dialog,(ViewGroup) findViewById(R.layout.list_product));

        Dialog dialog= new Dialog(ProductActivity.this);
        dialog.setContentView(R.layout.add_dialog);
        tvTitleDialog= dialog.findViewById(R.id.tvTitleDialog);
        tvTitleDialog.setText("TH??M S???N PH???M");
        edtTitle= dialog.findViewById(R.id.edtTitle);
        edtPrice= dialog.findViewById(R.id.edtPrice);
        spnColor= dialog.findViewById(R.id.spnColor);
        spnBrand= dialog.findViewById(R.id.spnBrand);
        edtQuantity= dialog.findViewById(R.id.edtQuantity);
        edtDescr= dialog.findViewById(R.id.edtDescr);

        ibUploadImage= dialog.findViewById(R.id.ibUploadImage);
        ivUploadImage= dialog.findViewById(R.id.ivUploadImage);
        tvNotification= dialog.findViewById(R.id.tvNotification);
        btnCancel= dialog.findViewById(R.id.btnCancel);
        btnSave= dialog.findViewById(R.id.btnSave);


        ArrayAdapter adapter1= new ArrayAdapter(this,android.R.layout.simple_spinner_item,color);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(adapter1);

        ArrayAdapter adapter2= new ArrayAdapter(this,android.R.layout.simple_spinner_item,brand);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBrand.setAdapter(adapter2);

        final int[] idColr = new int[1];
        final int[] idBrnd = new int[1];
        spnBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ProductActivity.this, brr.get(i).getIdBrand()+"", Toast.LENGTH_SHORT).show();
                idBrnd[0] =brr.get(i).getIdBrand();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ProductActivity.this, coo.get(i).getIdColor()+"", Toast.LENGTH_SHORT).show();
                idColr[0]= coo.get(i).getIdColor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ibUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title= edtTitle.getText().toString().trim();
                float Price= Float.parseFloat(edtPrice.getText().toString().trim());
                int idColor = idColr[0];
                int idBrand = idBrnd[0];
                int Quantity = Integer.parseInt(edtQuantity.getText().toString());
                String Descr = edtDescr.getText().toString();

                try {
                    addProduct(urlProduct,Price,Descr,Title,idColor,idBrand,hinhanh,Quantity);

                }
                catch (Exception e){
                    Toast.makeText(ProductActivity.this, "Th??m th???t b???i",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
//        dialogBuilder.setPositiveButton("L??u", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String Title= edtTitle.getText().toString().trim();
//                float Price= Float.parseFloat(edtPrice.getText().toString().trim());
//                int idColor = idColr[0];
//                int idBrand = idBrnd[0];
//                int Quantity = Integer.parseInt(edtQuantity.getText().toString());
//                String Descr = edtDescr.getText().toString();
//
//                validate(Title, Price, Quantity,Descr, hinhanh);
//                try {
//                    addProduct(urlProduct,Price,Descr,Title,idColor,idBrand,hinhanh,Quantity);
//                    getProduct(urlProduct);
//                    dialogInterface.cancel();
//                }
//                catch (Exception e){
//                    Toast.makeText(ProductActivity.this, "Th??m th???t b???i",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        dialogBuilder.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });

        dialog.show();
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setTitle("TH??M S???N PH???M");
//        AlertDialog a = dialogBuilder.create();
//        a.show();
    }

    private void addProduct(String url ,float Price,String Descr,String Title,int idColor,int idBrand,String Image,int Quantity ) throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JSONObject jsonbody= new JSONObject();
        jsonbody.put("price",String.valueOf(Price));
        jsonbody.put("descr",Descr);
        jsonbody.put("title",Title);
        jsonbody.put("idColor",String.valueOf(idColor));
        jsonbody.put("idBrand",String.valueOf(idBrand));
        jsonbody.put("image",Image);
        jsonbody.put("quantity",String.valueOf(Quantity));
        final  String requestbody= jsonbody.toString();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ProductActivity.this,response.toString(), Toast.LENGTH_SHORT).show();
                        getProduct(urlProduct);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductActivity.this,"l???i"+error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError{
                try {
                    return requestbody== null ? null : requestbody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody,"utf-8");
                    return null;
                }
            }
        };
        mRequestQueue.add(stringRequest);
    }


    public void modifyProductDialog(Product product){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductActivity.this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.add_dialog,(ViewGroup) findViewById(R.layout.list_product));

        Dialog dialog1= new Dialog(ProductActivity.this);
        dialog1.setContentView(R.layout.add_dialog);
        tvTitleDialog= dialog1.findViewById(R.id.tvTitleDialog);
        tvTitleDialog.setText("S???A S???N PH???M");
        edtTitle= dialog1.findViewById(R.id.edtTitle);
        edtPrice= dialog1.findViewById(R.id.edtPrice);
        spnColor= dialog1.findViewById(R.id.spnColor);
        spnBrand= dialog1.findViewById(R.id.spnBrand);
        edtQuantity= dialog1.findViewById(R.id.edtQuantity);
        edtDescr= dialog1.findViewById(R.id.edtDescr);

        edtTitle.setText(product.getTitle());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtQuantity.setText(String.valueOf(product.getQuantity()));
        edtDescr.setText(product.getDescr());

        ibUploadImage= dialog1.findViewById(R.id.ibUploadImage);
        ivUploadImage= dialog1.findViewById(R.id.ivUploadImage);
        tvNotification= dialog1.findViewById(R.id.tvNotification);
        btnCancel= dialog1.findViewById(R.id.btnCancel);
        btnSave= dialog1.findViewById(R.id.btnSave);

        ArrayAdapter adapter1= new ArrayAdapter(this,android.R.layout.simple_spinner_item,color);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColor.setAdapter(adapter1);

        ArrayAdapter adapter2= new ArrayAdapter(this,android.R.layout.simple_spinner_item,brand);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBrand.setAdapter(adapter2);

        final int[] idColr = new int[1];
        final int[] idBrnd = new int[1];
        spnBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ProductActivity.this, brr.get(i).getIdBrand()+"", Toast.LENGTH_SHORT).show();
                idBrnd[0] =brr.get(i).getIdBrand();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ProductActivity.this, coo.get(i).getIdColor()+"", Toast.LENGTH_SHORT).show();
                idColr[0]= coo.get(i).getIdColor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ibUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title= edtTitle.getText().toString().trim();
                float Price= Float.parseFloat(edtPrice.getText().toString().trim());
                int idColor = idColr[0];
                int idBrand = idBrnd[0];
                int Quantity = Integer.parseInt(edtQuantity.getText().toString());
                String Descr = edtDescr.getText().toString();


                try {
                    modifyProduct(urlProduct,product.getIdProduct(),Price,Descr,Title,idColor,idBrand,hinhanh,Quantity);
                    Toast.makeText(ProductActivity.this, "S???a th??nh c??ng",Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){
                    Toast.makeText(ProductActivity.this, "Th??m th???t b???i",Toast.LENGTH_SHORT).show();
                }

                getProduct(urlProduct);
                dialog1.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
//        dialogBuilder.setPositiveButton("L??u", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String Title= edtTitle.getText().toString().trim();
//                float Price= Float.parseFloat(edtPrice.getText().toString().trim());
//                int idColor = idColr[0];
//                int idBrand = idBrnd[0];
//                int Quantity = Integer.parseInt(edtQuantity.getText().toString());
//                String Descr = edtDescr.getText().toString();
//
//                try {
//                    modifyProduct(urlProduct,product.getIdProduct(),Price,Descr,Title,idColor,idBrand,hinhanh,Quantity);
//                    Toast.makeText(ProductActivity.this, "S???a th??nh c??ng",Toast.LENGTH_SHORT).show();
//                }
//                catch (Exception e){
//                    Toast.makeText(ProductActivity.this, "S???a th???t b???i",Toast.LENGTH_SHORT).show();
//                }
//                getProduct(urlProduct);
//                dialogInterface.cancel();
//
//            }
//        });
//        dialogBuilder.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setTitle("S???A S???N PH???M");
//        AlertDialog a = dialogBuilder.create();
//        a.show();
        dialog1.show();
    }

    private void modifyProduct(String url ,int idProduct,float Price,String Descr,String Title,int idColor,int idBrand,String Image,int Quantity) throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JSONObject jsonbody= new JSONObject();
        jsonbody.put("price",String.valueOf(Price));
        jsonbody.put("descr",Descr);
        jsonbody.put("title",Title);
        jsonbody.put("idColor",String.valueOf(idColor));
        jsonbody.put("idBrand",String.valueOf(idBrand));
        jsonbody.put("image",Image);
        jsonbody.put("quantity",String.valueOf(Quantity));
        jsonbody.put("id",String.valueOf(idProduct));
        final  String requestbody= jsonbody.toString();
//        Toast.makeText(ProductActivity.this, requestbody,Toast.LENGTH_SHORT).show();

        StringRequest stringRequest= new StringRequest(Request.Method.PUT, url+"/"+idProduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError{
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
        mRequestQueue.add(stringRequest);
    }


    public void deleteProductAlret(int idProduct){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("C???nh b??o!");
        b.setMessage("B???n c?? ch???c ch???n mu???n x??a?");
        b.setPositiveButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    deleteProduct(idProduct);
                    Toast.makeText(ProductActivity.this,"X??a th??nh c??ng",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(ProductActivity.this, "X??a kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                dialogInterface.cancel();
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

    private void deleteProduct(int idProduct) throws JSONException {
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JSONObject jsonbody= new JSONObject();
        jsonbody.put("idProduct",String.valueOf(idProduct));

        final  String requestbody= jsonbody.toString();

        StringRequest stringRequest= new StringRequest(Request.Method.DELETE, urlProduct+"/"+idProduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getProduct(urlProduct);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError{
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
        mRequestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data!= null){
            Uri uri = data.getData();
            try {
                InputStream inputStream =  getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                ivUploadImage.setImageBitmap(bitmap);
                hinhanh= ImageUtil.convert(bitmap);
//                Bitmap bitmap1= ImageUtil.convert(hinhanh);
//                ivUploadImage.setImageBitmap(bitmap1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void checkdeleteProduct(int idProduct){
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, LoginActivity.ip+":3000/api/bill_detail/id_product/"+idProduct, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length()>0){
                            Toast.makeText(ProductActivity.this, "Kh??ng ???????c x??a s???n ph???m n??y", Toast.LENGTH_SHORT).show();
                        }else{
                            deleteProductAlret(idProduct);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductActivity.this, "L???i!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(jsonArrayRequest);

    }

    private void createPDF() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "Sanpham.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);


        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(12, 12, 12, 12);

        Paragraph Title = new Paragraph("Danh sach san pham").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
        float[] width = {30,10,10};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addCell(new Cell().add(new Paragraph("Ten san pham")));
        table.addCell(new Cell().add(new Paragraph("Don gia")));
        table.addCell(new Cell().add(new Paragraph("So luong ton")));



        products1.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(ProductActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlProduct, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject k =response.getJSONObject(i);
                                products1.add(new Product(k.getInt("idProduct"), k.getInt("price"), k.getString("descr"),
                                        k.getString("title"), k.getInt("idColor"), k.getInt("idBrand"), k.getString("image"),
                                        k.getInt("quantity")));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for(Product j: products1){
                            table.addCell(new Cell().add(new Paragraph(j.getTitle()+"")));
                            table.addCell(new Cell().add(new Paragraph(j.getPrice()+"")));
                            table.addCell(new Cell().add(new Paragraph(j.getQuantity()+"")));
                        }
                        document.add(Title);
                        document.add(table);
                        document.close();
                        Toast.makeText(ProductActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductActivity.this, "L???i!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
        Toast.makeText(this, "???? t???o file pdf", Toast.LENGTH_SHORT).show();
    }

}
