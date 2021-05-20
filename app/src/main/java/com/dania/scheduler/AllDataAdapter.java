package com.dania.scheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllDataAdapter extends RecyclerView.Adapter<AllDataAdapter.AllDataViewHolder> {

    public static final int ITEM_DATE = 0;
    public static final int ITEM_Schedule = 1;
    private Context context;
    private ArrayList<DataModel> dataModels;
    private AllAdapterListener listener;

    public AllDataAdapter(Context context, ArrayList<DataModel> dataModels, AllAdapterListener listener) {
        this.context = context;
        this.dataModels = dataModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AllDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_Schedule){
            View view = LayoutInflater.from(context).inflate(R.layout.sa_item, parent, false);
            return new AllDataViewHolder(view);
        }else if (viewType==ITEM_DATE){
            View view = LayoutInflater.from(context).inflate(R.layout.all_date_item, parent, false);
            return new AllDataViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.all_date_item, parent, false);
            return new AllDataViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AllDataViewHolder holder, int position) {
        DataModel dm = dataModels.get(position);
        if (!dm.getTimestamp().equals("na")){
            holder.title.setText(dm.getTitle());
            holder.content.setText(dm.getContent());
            if (dm.getStatus().equals("Incomplete")){
                holder.radioBtn.setChecked(false);
            }else {
                holder.radioBtn.setChecked(true);
            }
            if (dm.isSelected()){
                holder.rl.setBackgroundResource(R.drawable.c_r_selected_upper_10);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSelectedIds().size()==0){
                        Intent i1 = new Intent(context, OpenDocument.class);
                        i1.putExtra("date",dm.getDate());
                        i1.putExtra("title",dm.getTitle());
                        i1.putExtra("content",dm.getContent());
                        i1.putExtra("timestamp",dm.getTimestamp());
                        i1.putExtra("status",dm.getStatus());
                        i1.putExtra("dateTimestamp",dm.getDateTimestamp());
                        context.startActivity(i1);
                    }else {
                        if (!dm.isSelected()){
                            dm.setSelected(true);
                            holder.rl.setBackgroundResource(R.drawable.c_r_selected_upper_10);
                        }else{
                            dm.setSelected(false);
                            holder.rl.setBackgroundResource(R.drawable.c_r_upper_10);
                            if (getSelectedIds().size()==0){
                                listener.OnAllAction(false);
                            }
                        }
                    }

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!dm.isSelected()){
                        dm.setSelected(true);
                        holder.rl.setBackgroundResource(R.drawable.c_r_selected_upper_10);
                        listener.OnAllAction(true);
                    }
                    return true;
                }
            });
            holder.radioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        DatabaseScheduler ds = new DatabaseScheduler(context);
                        boolean result = ds.updateSchedule(dm,"Complete");
                        listener.OnALLStatusChange();
                    }else {
                        DatabaseScheduler ds = new DatabaseScheduler(context);
                        boolean result = ds.updateSchedule(dm,"Incomplete");
                        listener.OnALLStatusChange();
                    }
                }
            });

        }else {
            holder.date_tv.setText(dm.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public ArrayList<DataModel> getSelectedIds(){
        ArrayList<DataModel> ids = new ArrayList<>();
        for (DataModel dm : dataModels){
            if (dm.isSelected()){
                ids.add(dm);
            }
        }
        return ids;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataModels.get(position).getTimestamp().equals("na")){
            return ITEM_DATE;
        }else {
            return ITEM_Schedule;
        }
    }

    public class AllDataViewHolder extends RecyclerView.ViewHolder{

        private TextView title,content;
        private RelativeLayout rl;
        private CheckBox radioBtn;
        private TextView date_tv;

        public AllDataViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content= itemView.findViewById(R.id.content);
            rl = itemView.findViewById(R.id.rl);
            radioBtn = itemView.findViewById(R.id.radioBtn);
            date_tv = itemView.findViewById(R.id.date_tv);
        }
    }
}
