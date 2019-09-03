package com.marco.socketadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SocketAdapter extends RecyclerView.Adapter<SocketAdapter.MyViewHolder>{

    private List<Model> socketList;
    private Integer onOffAID,onOffBID,imageID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView labelA, labelB, temp, applianceA, applianceB;
        public ImageView onOffA, onOffB, tamper, locked;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            labelA = itemView.findViewById(R.id.tv_labelA);
            labelB = itemView.findViewById(R.id.tv_labelB);
            temp = itemView.findViewById(R.id.tv_temperature);
            onOffA = itemView.findViewById(R.id.iv_onOffA);
            onOffB = itemView.findViewById(R.id.iv_onOffB);
            tamper = itemView.findViewById(R.id.iv_TR);
            locked = itemView.findViewById(R.id.iv_locked);
            applianceA = itemView.findViewById(R.id.tv_appliance_a);
            applianceB = itemView.findViewById(R.id.tv_appliance_b);

        }
    }


    public SocketAdapter(List<Model> socketList){
        this.socketList=socketList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.rv_adapter_layout,parent,false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        final Model model = socketList.get(position);
        myViewHolder.labelA.setText(model.getLabel_A());
        myViewHolder.labelB.setText(model.getLabel_B());
        myViewHolder.temp.setText(model.getTemperature());
        myViewHolder.onOffA.setImageResource(model.isSocketA_On());
        myViewHolder.onOffB.setImageResource(model.isSocketB_On());
        myViewHolder.tamper.setImageResource(model.isTamperResitant());
        myViewHolder.locked.setImageResource(model.isLockedTimeOut());
        myViewHolder.applianceA.setText(model.getApplianceA());
        myViewHolder.applianceB.setText(model.getApplianceB());

        myViewHolder.onOffA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer tagID = model.getTagA_On();
                switch(tagID) {
                    case R.mipmap.red_button:
                    default:
                        myViewHolder.onOffA.setImageResource(R.mipmap.green_button);
                        model.setTagA_On(R.mipmap.green_button);
                        break;
                    case R.mipmap.green_button:
                        myViewHolder.onOffA.setImageResource(R.mipmap.red_button);
                        model.setTagA_On(R.mipmap.red_button);
                        break;
                }

            }
        });

        myViewHolder.onOffB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer tagID = model.getTagtB_On();
                switch(tagID) {
                    case R.mipmap.red_button:
                    default:
                        myViewHolder.onOffB.setImageResource(R.mipmap.green_button);
                        model.setTagB_On(R.mipmap.green_button);
                        break;
                    case R.mipmap.green_button:
                        myViewHolder.onOffB.setImageResource(R.mipmap.red_button);
                        model.setTagB_On(R.mipmap.red_button);
                        break;
                }
            }
        });

        myViewHolder.tamper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer tagID = model.getTagTR_On();
                switch(tagID) {
                    case R.mipmap.red_button:
                    default:
                        myViewHolder.tamper.setImageResource(R.mipmap.green_button);
                        model.setTagTR_On(R.mipmap.green_button);
                        break;
                    case R.mipmap.green_button:
                        myViewHolder.tamper.setImageResource(R.mipmap.red_button);
                        model.setTagTR_On(R.mipmap.red_button);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i("RECYCLER ", String.valueOf(socketList.size()));
        return socketList.size();
    }


}
