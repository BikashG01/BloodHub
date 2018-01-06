package com.bloodhubnp.bikas.project.Activites;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.bloodhubnp.bikas.project.DataModels.DataProvider;
import com.bloodhubnp.bikas.project.Adapters.DataProvider_Adapter;
import com.bloodhubnp.bikas.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friend extends Fragment {
    private String URL_DATA = "http://192.168.10.6/BloodHub/working_friend_search_part_two2.php";
    private RecyclerView recyclerView_all;
    private RecyclerView.Adapter adapter_all;
    private List<DataProvider> list_data;
    private String blood_value, u_name, u_phonenumber;
    private View view;
    private Double long_value;
    private Double long_lat;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView_all = (RecyclerView) view.findViewById(R.id.recycleview_all);
        recyclerView_all.setHasFixedSize(true);
        blood_value = getActivity().getIntent().getExtras().getString("blood_value");
        u_name = getActivity().getIntent().getExtras().getString("name");
        u_phonenumber = getActivity().getIntent().getExtras().getString("phone_number");


        recyclerView_all.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_all.setItemAnimator(new DefaultItemAnimator());
        list_data = new ArrayList<>();
       /* Log.i("JSON NAME",u_name);
        Log.i("JSON NUMBER",u_phonenumber);*/

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
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("result");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject contain = array.getJSONObject(i);
                        DataProvider item = new DataProvider(
                                contain.getString("name"),
                                contain.getString("address"),
                                contain.getString("bloodgroup"),
                                contain.getString("distance"),
                                contain.getString("image_path"),
                                contain.getString("phone")
                        );
                        list_data.add(item);
                    }
                    adapter_all = new DataProvider_Adapter(list_data, getContext());
                    recyclerView_all.setAdapter(adapter_all);
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage());
                    progressDialog.dismiss();
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
                params.put("name", u_name);
                params.put("phone_number", u_phonenumber);
//                params.put("long",long_value.toString());
//                params.put("lat",long_lat.toString());
                Log.e("Blood Group", blood_value);
                Log.e("NUMBER USER", u_phonenumber);
                Log.e("NAME OF USER", u_name);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
