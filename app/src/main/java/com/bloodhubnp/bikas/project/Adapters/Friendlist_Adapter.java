package com.bloodhubnp.bikas.project.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodhubnp.bikas.project.DataModels.Friendlist_Data;
import com.bloodhubnp.bikas.project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bikas on 8/26/2017.
 */

public class Friendlist_Adapter  extends RecyclerView.Adapter<Friendlist_Adapter.ViewHolder> {
    private List<Friendlist_Data> friendlist_datas;
    private Context context;

    public Friendlist_Adapter(List<Friendlist_Data> friendlist_datas,Context context) {
        this.friendlist_datas = friendlist_datas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_friendlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Friendlist_Adapter.ViewHolder holder, int position) {
        final Friendlist_Data friendlist_data= friendlist_datas.get(position);
        holder.person_name.setText(friendlist_data.getFname());
        holder.blood_group.setText(friendlist_data.getFbloodgroup());
        holder.location.setText(friendlist_data.getFaddress());
        Picasso.with(context)
                .load(friendlist_data.getFimage())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(holder.user_image);
        holder.fnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,friendlist_data.getFnumber(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendlist_datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView person_name,distance,location,blood_group,fnumber;
        private CircleImageView user_image;

        public ViewHolder(View itemView) {
            super(itemView);
            user_image=(CircleImageView)itemView.findViewById(R.id.friend_pic);
            person_name=(TextView)itemView.findViewById(R.id.friend_name);
            location=(TextView)itemView.findViewById(R.id.friend_address);
            blood_group=(TextView)itemView.findViewById(R.id.blood_name_friend);
            fnumber=(TextView)itemView.findViewById(R.id.cancel_friend);
        }
    }
}
