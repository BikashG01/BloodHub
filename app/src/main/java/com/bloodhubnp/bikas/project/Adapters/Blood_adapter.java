package com.bloodhubnp.bikas.project.Adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloodhubnp.bikas.project.DataModels.Blood_Data;
import com.bloodhubnp.bikas.project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bikas on 8/24/2017.
 */

public class Blood_adapter extends RecyclerView.Adapter<Blood_adapter.ViewHolder> {
    private List<Blood_Data> blood_datas;
    private Context context;
    AlertDialog.Builder builder;

    public Blood_adapter(List<Blood_Data> blood_datas, Context context) {
        this.blood_datas = blood_datas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_blood, parent, false);
        return new Blood_adapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(Blood_adapter.ViewHolder holder, int position) {
        final Blood_Data blood_data = blood_datas.get(position);
        holder.blood_bank_name.setText(blood_data.getName());
        holder.blood_bank_location.setText(blood_data.getLocation());
        holder.blood_bank_distance.setText(blood_data.getDistance());
        Picasso.with(context)
                .load(blood_data.getImage_path())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(holder.blood_bank_image);
        holder.number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,blood_data.getNumber(),Toast.LENGTH_LONG).show();
                builder = new AlertDialog.Builder(context);
//                String lineseparator = System.getProperty("line.separator");
                builder.setTitle("Call" + " " + blood_data.getName()+"."  + "Standard Call rates may apply");
                builder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+blood_data.getNumber()));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            context.startActivity(callIntent);

                        }
                        else {
                            context.startActivity(callIntent);

                        }

                    }


                });
                builder.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


    }


    @Override
    public int getItemCount() {
        return blood_datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView blood_bank_name,blood_bank_distance,blood_bank_location,number;
        private CircleImageView blood_bank_image;
        private RelativeLayout all_relativelayout;

        public ViewHolder(View itemView) {
            super(itemView);
            blood_bank_image=(CircleImageView)itemView.findViewById(R.id.profile_bloodbank);
            blood_bank_name=(TextView)itemView.findViewById(R.id.blood_Name2);
            blood_bank_distance=(TextView)itemView.findViewById(R.id.blood_dis);
            blood_bank_location=(TextView)itemView.findViewById(R.id.blood_address);
            number=(TextView)itemView.findViewById(R.id.call_bank);


        }
    }
}
