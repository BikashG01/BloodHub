package com.bloodhubnp.bikas.project.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bloodhubnp.bikas.project.Adapters.Friendlist_Adapter;
import com.bloodhubnp.bikas.project.DataModels.Friendlist_Data;
import com.bloodhubnp.bikas.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friendlist extends AppCompatActivity {
    private String URL_DATA = "http://192.168.10.6/BloodHub/friendlist.php";
    private RecyclerView recyclerView_friendlist;
    private RecyclerView.Adapter adapter_friendlist;
    private List<Friendlist_Data> friendlist_data;
    private String name, number;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
        recyclerView_friendlist = (RecyclerView) findViewById(R.id.friend_recycle);
        recyclerView_friendlist.setHasFixedSize(true);
        name = this.getIntent().getExtras().getString("name");
        number = this.getIntent().getExtras().getString("number");
        recyclerView_friendlist.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_friendlist.setItemAnimator(new DefaultItemAnimator());
        friendlist_data = new ArrayList<>();
        load_friend_list();
    }

    private void load_friend_list() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("friendlist");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject contain = array.getJSONObject(i);
                        Friendlist_Data friendlistData = new Friendlist_Data(
                                contain.getString("name"),
                                contain.getString("address"),
                                contain.getString("distance"),
                                contain.getString("phone"),
                                contain.getString("image_path")
                        );
                        friendlist_data.add(friendlistData);
                    }
                    adapter_friendlist = new Friendlist_Adapter(friendlist_data, getApplicationContext());
                    recyclerView_friendlist.setAdapter(adapter_friendlist);
                } catch (JSONException e) {
                    Log.e("FriendList", e.getMessage());
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
                params.put("name", name);
                params.put("phone", number);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
