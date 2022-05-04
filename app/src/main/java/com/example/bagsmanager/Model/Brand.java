package com.example.bagsmanager.Model;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Brand {
    int idBrand;
    String nameBrand;

    public Brand() {
    }

    public Brand(int idBrand, String nameBrand) {
        this.idBrand = idBrand;
        this.nameBrand = nameBrand;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public String getNameBrand() {
        return nameBrand;
    }

    public void setIdBrand(int idBrand) {
        this.idBrand = idBrand;
    }

    public void setNameBrand(String nameBrand) {
        this.nameBrand = nameBrand;
    }


}
