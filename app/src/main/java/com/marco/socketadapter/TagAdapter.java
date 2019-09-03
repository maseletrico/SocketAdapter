package com.marco.socketadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {

    private List<Model> tagList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTagListID,tvTagListApplianceName,tvTagListLock,tvTagListTimeout;
        public ImageButton ibTagListButtonDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTagListID=itemView.findViewById(R.id.tv_tag_id);
            tvTagListApplianceName=itemView.findViewById(R.id.tv_appliance_name);
            tvTagListLock=itemView.findViewById(R.id.tv_lock);
            tvTagListTimeout=itemView.findViewById(R.id.tv_timeout);
        }
    }

    public TagAdapter(List<Model> tagList) {
        this.tagList=tagList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rv_tag_layout,viewGroup,false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        final Model model = tagList.get(position);
        myViewHolder.tvTagListID.setText(model.getTagID());
        myViewHolder.tvTagListApplianceName.setText(model.getTagApplianceName());
        myViewHolder.tvTagListLock.setText(model.getTagLock());
        myViewHolder.tvTagListTimeout.setText(model.getTagTimeout());
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }



}
