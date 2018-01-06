package com.bloodhubnp.bikas.project.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloodhubnp.bikas.project.DataModels.All_Data;
import com.bloodhubnp.bikas.project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bikas on 8/4/2017.
 */

public class all_adapter extends RecyclerView.Adapter<all_adapter.ViewHolder> {
    private List<All_Data> all_datas;
    private Context context;

    public all_adapter(List<All_Data> all_datas, Context context) {
        this.all_datas = all_datas;
        this.context = context;
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(all_adapter.ViewHolder holder, int position) {
        All_Data all_data= all_datas.get(position);
        holder.person_name.setText(all_data.getName());
        holder.distance.setText(all_data.getDistance());
        holder.blood_group.setText(all_data.getBlood_group());
        holder.location.setText(all_data.getLocation());
        Picasso.with(context)
                .load(all_data.getImage_path())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(holder.user_image);

    }

    @Override
    public int getItemCount() {
        return all_datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView person_name,distance,location,blood_group;
        private CircleImageView user_image;

        public ViewHolder(View itemView) {
            super(itemView);
            user_image=(CircleImageView)itemView.findViewById(R.id.profile_img2);
            person_name=(TextView)itemView.findViewById(R.id.Name2);
            distance=(TextView)itemView.findViewById(R.id.Distance2);
            location=(TextView)itemView.findViewById(R.id.Address2);
            blood_group=(TextView)itemView.findViewById(R.id.blood_group);
        }
    }

}
