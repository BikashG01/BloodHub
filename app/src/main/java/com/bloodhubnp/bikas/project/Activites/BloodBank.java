package com.bloodhubnp.bikas.project.Activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bloodhubnp.bikas.project.DataModels.Blood_Data;
import com.bloodhubnp.bikas.project.Adapters.Blood_adapter;
import com.bloodhubnp.bikas.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodBank extends Fragment {
    private String URL_DATA = "http://192.168.10.6/BloodHub/BloodBank_Search.php";
    private RecyclerView recyclerView_all;
    private RecyclerView.Adapter adapter_bloodbank;
    private List<Blood_Data> list_data;
    private String blood_value;
    private View view;
    private Double long_value;
    private Double long_lat;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView_all = (RecyclerView) view.findViewById(R.id.recycleview_all);
        recyclerView_all.setHasFixedSize(true);
        blood_value = getActivity().getIntent().getExtras().getString("blood_value");
        recyclerView_all.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_all.setItemAnimator(new DefaultItemAnimator());
        list_data = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.all, container, false);
        }
        load_donor();
        return view;
    }

    public void load_donor() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("bloodbank");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject contain = array.getJSONObject(i);
                        Blood_Data blood_data = new Blood_Data(
                                contain.getString("name"),
                                contain.getString("address"),
                                contain.getString("distance"),
                                contain.getString("phone"),
                                contain.getString("image_path")

                        );
                        list_data.add(blood_data);
                    }
                    adapter_bloodbank = new Blood_adapter(list_data, getContext());
                    recyclerView_all.setAdapter(adapter_bloodbank);
                } catch (JSONException e) {
                    Log.e("ERROR:", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("blood", blood_value);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}
