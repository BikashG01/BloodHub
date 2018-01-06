package com.bloodhubnp.bikas.project.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bloodhubnp.bikas.project.Activites.ProfileActivity;
import com.bloodhubnp.bikas.project.DataModels.DataProvider;
import com.bloodhubnp.bikas.project.Networks.Mysingleton;
import com.bloodhubnp.bikas.project.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bikas on 8/6/2017.
 */

public class DataProvider_Adapter extends RecyclerView.Adapter<DataProvider_Adapter.ViewHolder> {

    public String database_url = "http://192.168.10.6/BloodHub/sendNumber.php";
    public String ip = "http://192.168.10.6/BloodHub/";


    private List<DataProvider> dataProviders;
    private Context context;

    public DataProvider_Adapter(List<DataProvider> dataProviders, Context context) {
        this.dataProviders = dataProviders;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DataProvider provider = dataProviders.get(position);
        holder.r_name.setText(provider.getName());
        holder.r_address.setText(provider.getAddress());
        holder.r_distancce_.setText(provider.getDistance());
        holder.r_bloodgroup.setText(provider.getBloodgroup());
        Picasso.with(context).load(provider.getImage_url())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(holder.profile_image);

        holder.all_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, database_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("status");
                            if (status.equals("match")) {
                                Log.e("Status", status);
                                String image_link = jsonObject.getString("image_url");
                                Log.e("Image Link", image_link);
                                String image_path = ip + image_link;
                                Log.e("Image Path", image_path);
                                Intent intent = new Intent(context, ProfileActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("name", jsonObject.getString("name"));
                                bundle.putString("image_path", image_path);
                                /*bundle.putString("image_path",jsonObject.getString(image_path));*/
                                bundle.putString("phone_number", jsonObject.getString("number"));
                                bundle.putString("bloodgroup", jsonObject.getString("bloodgroup"));
                                bundle.putString("location", jsonObject.getString("location"));
                                bundle.putString("gender", jsonObject.getString("gender"));
                                bundle.putString("donation_date", jsonObject.getString("donation_date"));
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("WARNING", e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "No Internet. Please Try Again", Toast.LENGTH_LONG).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("phone", provider.getPh_number());
                        return params;
                    }
                };
                Mysingleton.getInstance(context).addToRequestque(stringRequest);

            }

        });

    }

    @Override
    public int getItemCount() {
        return dataProviders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView r_name, r_address, r_distancce_, r_bloodgroup;
        public CircleImageView profile_image;
        public RelativeLayout all_relativelayout;


        public ViewHolder(View itemView) {
            super(itemView);
            r_name = (TextView) itemView.findViewById(R.id.Name2);
            r_address = (TextView) itemView.findViewById(R.id.Address2);
            r_bloodgroup = (TextView) itemView.findViewById(R.id.blood_name);
            r_distancce_ = (TextView) itemView.findViewById(R.id.Distance2);
            profile_image = (CircleImageView) itemView.findViewById(R.id.profile_img2);
            all_relativelayout = (RelativeLayout) itemView.findViewById(R.id.all_layout);


        }
    }
}
