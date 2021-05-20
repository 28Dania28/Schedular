package com.dania.scheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.SAViewHolder>{

    private Context context;
    private ArrayList<DataModel> dataModels;
    private ScheduleAdapterListener listener;

    public ScheduleAdapter(Context context, ArrayList<DataModel> dataModels, ScheduleAdapterListener listener) {
        this.context = context;
        this.dataModels = dataModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sa_item,parent,false);
        return new SAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SAViewHolder holder, int position) {
        DataModel dm = dataModels.get(position);
        holder.BindDataModel(dm,position);
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

    public class SAViewHolder extends RecyclerView.ViewHolder{

        private TextView title,content;
        private RelativeLayout rl;
        private CheckBox radioBtn;

        public SAViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content= itemView.findViewById(R.id.content);
            rl = itemView.findViewById(R.id.rl);
            radioBtn = itemView.findViewById(R.id.radioBtn);
        }

        public void BindDataModel(DataModel dm, int position) {
            title.setText(dm.getTitle());
            content.setText(dm.getContent());
            if (dm.getStatus().equals("Incomplete")){
                radioBtn.setChecked(false);
            }else {
                radioBtn.setChecked(true);
            }
            if (dm.isSelected()){
                rl.setBackgroundResource(R.drawable.c_r_selected_upper_10);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
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
                            rl.setBackgroundResource(R.drawable.c_r_selected_upper_10);
                        }else{
                            dm.setSelected(false);
                            rl.setBackgroundResource(R.drawable.c_r_upper_10);
                            if (getSelectedIds().size()==0){
                                listener.OnAction(false);
                            }
                        }
                    }

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!dm.isSelected()){
                        dm.setSelected(true);
                        rl.setBackgroundResource(R.drawable.c_r_selected_upper_10);
                        listener.OnAction(true);
                    }
                    return true;
                }
            });
            radioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        DatabaseScheduler ds = new DatabaseScheduler(context);
                        boolean result = ds.updateSchedule(dm,"Complete");
                        listener.OnStatusChange();
                    }else {
                        DatabaseScheduler ds = new DatabaseScheduler(context);
                        boolean result = ds.updateSchedule(dm,"Incomplete");
                        listener.OnStatusChange();
                    }
                }
            });

        }
    }
}
