package com.dania.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder>{

    private Context context;
    private ArrayList<TimeModel> timeModels;
    DateChangeListener dateChangeListener;

    public DateAdapter(Context context, ArrayList<TimeModel> timeModels, DateChangeListener dateChangeListener) {
        this.context = context;
        this.timeModels = timeModels;
        this.dateChangeListener = dateChangeListener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.date_item,parent,false);
        return new DateAdapter.DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        TimeModel tm = timeModels.get(position);
        holder.BindDateModel(tm,position);
    }

    @Override
    public int getItemCount() {
        return timeModels.size();
    }

    public ArrayList<TimeModel> getAllModels(){
        ArrayList<TimeModel> models = new ArrayList<>();
        models.addAll(timeModels);
        return models;
    }
    public class DateViewHolder extends RecyclerView.ViewHolder{

        TextView date,month,day;
        LinearLayout ll;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            day = itemView.findViewById(R.id.day);
            ll = itemView.findViewById(R.id.ll);
        }

        public void BindDateModel(TimeModel tm, int position) {
            date.setText(String.valueOf(tm.getDate()));
            month.setText(tm.getMonth());
            day.setText(tm.getDay());
            if (tm.getSelected()){
                day.setTextColor(context.getResources().getColor(R.color.txt3));
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_jump);
                ll.startAnimation(hyperspaceJumpAnimation);
            }else {
                day.setTextColor(context.getResources().getColor(R.color.txt2));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tm.getSelected()){
                        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_jump);
                        ll.startAnimation(hyperspaceJumpAnimation);
                        dateChangeListener.OnSelect(tm.getDateLable(),position);
                    }else {
                        dateChangeListener.OnSelect(tm.getDateLable(),position);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dateChangeListener.OnLongPressed(tm);
                    return false;
                }
            });
        }
    }
}
