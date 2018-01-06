package com.bloodhubnp.bikas.project.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bloodhubnp.bikas.project.Activites.ProfileActivity;
import com.bloodhubnp.bikas.project.DataModels.DataReference;
import com.bloodhubnp.bikas.project.Extras.Details;
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
 * Created by bikas on 5/19/2017.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.Recycleviewadapter> {
    private List<DataReference> referencelayouts;
    private Context context;
    public String ip = "http://192.168.10.6/BloodHub/";
    public String database_url = "http://192.168.10.6/BloodHub/sendNumber.php";
    AlertDialog.Builder builder;


    public RecycleAdapter(List<DataReference> referencelayouts, Context context) {
        this.referencelayouts = referencelayouts;
        this.context = context;


    }

    @Override
    public Recycleviewadapter onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = null;
        if (viewType == Details.FIRST_ROW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reference_layout, parent, false);
        } else if (viewType == Details.OTHER_ROW) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.umatched_layout, parent, false);
        }
        return new Recycleviewadapter(view, viewType);
    }

    @Override
    public void onBindViewHolder(final Recycleviewadapter holder, int position) {
        final DataReference referencelayout = referencelayouts.get(position);
//        String url="http://192.168.7.97/ContactDemo/image/a.jpg";
        Picasso.with(context)
                .load(referencelayouts.get(position).getImage_url())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(holder.user_image);
        holder.text_number.setText(referencelayout.getU_number());

        holder.text_name.setText(referencelayout.getU_name());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
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
                                bundle.putString("phone_number", jsonObject.getString("number"));
                                bundle.putString("bloodgroup", jsonObject.getString("bloodgroup"));
                                bundle.putString("location", jsonObject.getString("location"));
                                bundle.putString("gender", jsonObject.getString("gender"));
                                bundle.putString("donation_date", jsonObject.getString("donation_date"));
                                intent.putExtras(bundle);
                                context.startActivity(intent);


                            } else {
                                builder = new AlertDialog.Builder(context);
                                String lineseparator = System.getProperty("line.separator");
                                builder.setTitle("Invite" + " " + referencelayout.getU_name() + " to BloodHub." + lineseparator + "Standard SMS rates may apply");
                                builder.setPositiveButton("INVITE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(referencelayout.getU_number(), null, "Lets find blood donor.Join BloodHub for saving life.", null, null);
                                        Toast.makeText(context.getApplicationContext(), "Sending", Toast.LENGTH_LONG).show();
                                    }


                                });
                                builder.setNegativeButton("CANCEL",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();


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
                        params.put("phone", referencelayout.getU_number());
                        return params;
                    }
                };
                Mysingleton.getInstance(context).addToRequestque(stringRequest);

            }


        });
        try {
            holder.btn_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder = new AlertDialog.Builder(v.getContext());
                    String lineseparator = System.getProperty("line.separator");
                    builder.setTitle("Invite" + " " + referencelayout.getU_name() + " to BloodHub." + lineseparator + "Standard SMS rates may apply");
                    builder.setPositiveButton("INVITE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(referencelayout.getU_number(), null, "Lets find blood donor.Join BloodHub for saving life.", null, null);
                            Toast.makeText(context.getApplicationContext(), "Sending", Toast.LENGTH_LONG).show();
                        }


                    });
                    builder.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public int getItemCount() {

        return referencelayouts.size();
    }

    @Override
    public int getItemViewType(int position) {

        DataReference multipleRowModel = referencelayouts.get(position);

        if (multipleRowModel != null)
            return multipleRowModel.type;

        return super.getItemViewType(position);
    }

    public class Recycleviewadapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView text_name, text_number;
        public CircleImageView user_image;
        public Button btn_invite;
        public RelativeLayout relativeLayout;

        public Recycleviewadapter(View itemView, int type) {

            super(itemView);
            user_image = (CircleImageView) itemView.findViewById(R.id.image_view);
            text_name = (TextView) itemView.findViewById(R.id.username);
            text_number = (TextView) itemView.findViewById(R.id.number);
            btn_invite = (Button) itemView.findViewById(R.id.btn_invite);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.show);

        }

        @Override
        public void onClick(View v) {


        }
    }
}
