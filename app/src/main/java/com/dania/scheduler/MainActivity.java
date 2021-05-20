package com.dania.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements DateChangeListener,ScheduleAdapterListener,AllAdapterListener{

    private RecyclerView rv, rv_top, rv_completed;
    private DateAdapter da;
    private Dialog dialog, addDateDialog, deleteDateDialog;
    private ImageView add, deleteBtn, allBtn;
    private EditText title_et, content_et;
    private ImageView cancel, save, addDateBtn;
    private DatabaseScheduler scheduleDatabase;
    private ArrayList<DataModel> dataModels = new ArrayList<>();
    private ArrayList<DataModel> completeDataModels = new ArrayList<>();
    private ArrayList<DataModel> allDataModels = new ArrayList<>();
    private ArrayList<TimeModel> timeModels = new ArrayList<>();
    private ArrayList<String> allDates = new ArrayList<>();
    private String date_db,title_db,content_db,timestamp_db,status_db, dateTimestamp_db;
    private String date_selected, dateTimestamp_selected;
    private RelativeLayout completed, btm_lay;
    private String dateLable_dd, dateTimestamp_dd, date_dd,month_dd,day_dd;
    private CalendarView calendarView;
    private TextView day_dialog, date_dialog, month_dialog;
    private Button deleteDateBtn;
    private String last_date = "na";
    private ScheduleAdapter sa;
    private ScheduleAdapter sa2;
    private AllDataAdapter allsa;
    private ImageView mode;
    boolean day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializing();
    }

    private void initializing() {
        mode = findViewById(R.id.mode);
        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
            day = false;
            mode.setImageDrawable(getDrawable(R.drawable.night));
        }
        else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            day = true;
            mode.setImageDrawable(getDrawable(R.drawable.sun));
        }
        rv = findViewById(R.id.rv);
        add = findViewById(R.id.add);
        completed = findViewById(R.id.completed);
        deleteBtn = findViewById(R.id.deleteBtn);
        btm_lay = findViewById(R.id.btm_lay);
        addDateBtn = findViewById(R.id.addDateBtn);
        allBtn = findViewById(R.id.allBtn);
        rv_top = findViewById(R.id.rv_top);
        rv_completed = findViewById(R.id.rv_completed);
        rv.setHasFixedSize(true);
        rv_top.setHasFixedSize(true);
        rv_completed.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(lm);
        LinearLayoutManager lm2 = new LinearLayoutManager(getApplicationContext());
        lm2.setOrientation(RecyclerView.VERTICAL);
        rv_top.setLayoutManager(lm2);
        LinearLayoutManager lm3 = new LinearLayoutManager(getApplicationContext());
        lm3.setOrientation(RecyclerView.VERTICAL);
        rv_completed.setLayoutManager(lm3);
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialogbox);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addDateDialog = new Dialog(this);
        addDateDialog.setContentView(R.layout.add_date_dialogbox);
        addDateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        deleteDateDialog = new Dialog(this);
        deleteDateDialog.setContentView(R.layout.delete_date_dialog);
        deleteDateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        calendarView = addDateDialog.findViewById(R.id.calendarView);
        title_et = dialog.findViewById(R.id.title_et);
        content_et = dialog.findViewById(R.id.content_et);
        save = dialog.findViewById(R.id.save);
        cancel = dialog.findViewById(R.id.cancel);
        day_dialog = deleteDateDialog.findViewById(R.id.day_dialog);
        month_dialog = deleteDateDialog.findViewById(R.id.month_dialog);
        date_dialog = deleteDateDialog.findViewById(R.id.date_dialog);
        deleteDateBtn = deleteDateDialog.findViewById(R.id.deleteDateBtn);
        scheduleDatabase = new DatabaseScheduler(getApplicationContext());
//        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
//            case Configuration.UI_MODE_NIGHT_YES:
//                day = false;
//                mode.setImageDrawable(getDrawable(R.drawable.night));
//                break;
//            case Configuration.UI_MODE_NIGHT_NO:
//                day = true;
//                mode.setImageDrawable(getDrawable(R.drawable.sun));
//                break;
//        }
        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (day){
                    day = false;
                    editor.putBoolean(
                            "isDarkModeOn", true);
                    editor.apply();
                    AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mode.setImageDrawable(getDrawable(R.drawable.night));
                }else {
                    day = true;
                    editor.putBoolean(
                            "isDarkModeOn", false);
                    editor.apply();
                    AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mode.setImageDrawable(getDrawable(R.drawable.sun));
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hyperspace_jump);
                allBtn.startAnimation(hyperspaceJumpAnimation);
                date_selected = "all";
                add.setEnabled(false);
                fillAllData();
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                String dateLabel = new SimpleDateFormat("d MMM, yyyy").format(c.getTime());
                TimeModel tm = new TimeModel(dateLabel, String.valueOf(c.getTimeInMillis()), String.valueOf(c.getTime().getDate()), getMonthFromInt(c.getTime().getMonth()), getDayFromInt(c.getTime().getDay()), true);
                addNewDate(tm);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!content_et.getText().toString().isEmpty()){
                    String date = date_selected;
                    String title = title_et.getText().toString().trim();
                    String content = content_et.getText().toString().trim();
                    Calendar c = Calendar.getInstance();
                    String timestamp = String.valueOf(c.getTimeInMillis());
                    DatabaseScheduler ds = new DatabaseScheduler(getApplicationContext());
                    boolean save = ds.addData(date,title,content,timestamp,"Incomplete",dateTimestamp_selected);
                    if (save){
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    title_et.setText(null);
                    content_et.setText(null);
                    title_et.requestFocus();
                    dialog.dismiss();
                    fillData(date);
                }else {
                    Toast.makeText(MainActivity.this, "Empty content cannot be saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fillDateRv();
        addDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDateDialog.show();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date_selected.equals("all")){
                    DatabaseScheduler ds = new DatabaseScheduler(getApplicationContext());
                    ArrayList<DataModel> dataModels = allsa.getSelectedIds();
                    for (int i=0;i<dataModels.size();i++){
                        boolean delete = ds.deleteSchedule(dataModels.get(i).getTimestamp());
                    }
                    fillAllData();
                    deleteBtn.setVisibility(View.GONE);
                    add.setEnabled(false);
                }else {
                    DatabaseScheduler ds = new DatabaseScheduler(getApplicationContext());
                    ArrayList<DataModel> dataModels = sa.getSelectedIds();
                    ArrayList<DataModel> selectedDataModel = sa2.getSelectedIds();
                    dataModels.addAll(selectedDataModel);
                    for (int i=0;i<dataModels.size();i++){
                        boolean delete = ds.deleteSchedule(dataModels.get(i).getTimestamp());
                    }
                    fillData(date_selected);
                    deleteBtn.setVisibility(View.GONE);
                    add.setEnabled(true);
                }


            }
        });
        fillData(date_selected);
    }

    private void fillDateRv() {
        timeModels = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        String dateLabel = new SimpleDateFormat("d MMM, yyyy").format(c.getTime());
        TimeModel tm = new TimeModel(dateLabel,String.valueOf(c.getTimeInMillis()),String.valueOf(c.getTime().getDate()),getMonthFromInt(c.getTime().getMonth()),getDayFromInt(c.getTime().getDay()),true);
        date_selected = dateLabel;
        dateTimestamp_selected = String.valueOf(c.getTimeInMillis());
        allDates.add(dateLabel);
        timeModels.add(tm);
        for (int i=1;i<=6;i++){
            c.add(Calendar.DAY_OF_MONTH,1);
            String dateLabel2 = new SimpleDateFormat("d MMM, yyyy").format(c.getTime());
            allDates.add(dateLabel2);
            TimeModel tm2 = new TimeModel(dateLabel2,String.valueOf(c.getTimeInMillis()),String.valueOf(c.getTime().getDate()),getMonthFromInt(c.getTime().getMonth()),getDayFromInt(c.getTime().getDay()),false);
            timeModels.add(tm2);
        }

        DateDatabase dd = new DateDatabase(getApplicationContext());
        Cursor c3 = dd.getAllDates();
        if (c3.getCount() > 0) {
            while (c3.moveToNext()){
                dateLable_dd = c3.getString(0);
                dateTimestamp_dd = c3.getString(1);
                date_dd = c3.getString(2);
                month_dd = c3.getString(3);
                day_dd = c3.getString(4);
                TimeModel tm_dd = new TimeModel(dateLable_dd,dateTimestamp_dd,date_dd,month_dd,day_dd,false);
                timeModels.add(tm_dd);
                allDates.add(dateLable_dd);
            }
        }
        Collections.sort(timeModels, new Comparator<TimeModel>() {
            @Override
            public int compare(TimeModel o1, TimeModel o2) {
                return o1.getDateTimestamp().compareTo(o2.getDateTimestamp());
            }
        });

        addToRv(timeModels);
    }


    private void addNewDate(TimeModel tm) {
        if (!allDates.contains(tm.getDateLable())){
            DateDatabase dd = new DateDatabase(getApplicationContext());
            boolean result = dd.addDate(tm.getDateLable(),tm.getDateTimestamp(),tm.getDate(),tm.getMonth(),tm.getDay());
            if (result){
                Toast.makeText(this, "Date Added", Toast.LENGTH_SHORT).show();
                addDateDialog.dismiss();
                fillDateRv();
            }
        }else {
            Toast.makeText(this, "Date Already in Dock", Toast.LENGTH_SHORT).show();
        }

    }

    private void fillDateRvWithSelection(String date_lable) {
        timeModels = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        String dateLabel = new SimpleDateFormat("d MMM, yyyy").format(c.getTime());
        TimeModel tm;
        if (dateLabel.equals(date_lable)){
            tm = new TimeModel(dateLabel,String.valueOf(c.getTimeInMillis()),String.valueOf(c.getTime().getDate()),getMonthFromInt(c.getTime().getMonth()),getDayFromInt(c.getTime().getDay()),true);
        }else {
            tm = new TimeModel(dateLabel,String.valueOf(c.getTimeInMillis()),String.valueOf(c.getTime().getDate()),getMonthFromInt(c.getTime().getMonth()),getDayFromInt(c.getTime().getDay()),false);
        }
        date_selected = dateLabel;
        dateTimestamp_selected = String.valueOf(c.getTimeInMillis());
        allDates.add(dateLabel);
        timeModels.add(tm);
        for (int i=1;i<=6;i++){
            c.add(Calendar.DAY_OF_MONTH,1);
            String dateLabel2 = new SimpleDateFormat("d MMM, yyyy").format(c.getTime());
            allDates.add(dateLabel2);
            if (dateLabel2.equals(date_lable)){
                TimeModel tm2 = new TimeModel(dateLabel2,String.valueOf(c.getTimeInMillis()),String.valueOf(c.getTime().getDate()),getMonthFromInt(c.getTime().getMonth()),getDayFromInt(c.getTime().getDay()),true);
                timeModels.add(tm2);
            }else {
                TimeModel tm2 = new TimeModel(dateLabel2,String.valueOf(c.getTimeInMillis()),String.valueOf(c.getTime().getDate()),getMonthFromInt(c.getTime().getMonth()),getDayFromInt(c.getTime().getDay()),false);
                timeModels.add(tm2);
            }

        }

        DateDatabase dd = new DateDatabase(getApplicationContext());
        Cursor c3 = dd.getAllDates();
        if (c3.getCount() > 0) {
            while (c3.moveToNext()){
                dateLable_dd = c3.getString(0);
                dateTimestamp_dd = c3.getString(1);
                date_dd = c3.getString(2);
                month_dd = c3.getString(3);
                day_dd = c3.getString(4);
                if (dateLable_dd.equals(date_lable)){
                    TimeModel tm_dd = new TimeModel(dateLable_dd,dateTimestamp_dd,date_dd,month_dd,day_dd,true);
                    timeModels.add(tm_dd);
                }else {

                }

                allDates.add(dateLable_dd);
            }
        }
        Collections.sort(timeModels, new Comparator<TimeModel>() {
            @Override
            public int compare(TimeModel o1, TimeModel o2) {
                return o1.getDateTimestamp().compareTo(o2.getDateTimestamp());
            }
        });

        addToRv(timeModels);
    }


    private void fillData(String dateLabel) {
        dataModels = new ArrayList<>();
        try {
            Cursor c = scheduleDatabase.getIncompleteDataFromDate(dateLabel);
            if (c.getCount() > 0) {
                while (c.moveToNext()){
                    date_db = c.getString(0);
                    title_db = c.getString(1);
                    content_db = c.getString(2);
                    timestamp_db = c.getString(3);
                    status_db = c.getString(4);
                    dateTimestamp_db = c.getString(5);
                    DataModel dm = new DataModel(date_db,title_db,content_db,timestamp_db,status_db,false,dateTimestamp_db);
                    dataModels.add(dm);
                }
                sa = new ScheduleAdapter(MainActivity.this, dataModels,this);
                rv_top.setAdapter(sa);
            }else {
                sa = new ScheduleAdapter(MainActivity.this, dataModels,this);
                rv_top.setAdapter(sa);
            }
            completeDataModels = new ArrayList<>();
            Cursor c1 = scheduleDatabase.getCompleteDataFromDate(dateLabel);
            if (c1.getCount() > 0) {
                completed.setVisibility(View.VISIBLE);
                rv_completed.setVisibility(View.VISIBLE);
                while (c1.moveToNext()){
                    date_db = c1.getString(0);
                    title_db = c1.getString(1);
                    content_db = c1.getString(2);
                    timestamp_db = c1.getString(3);
                    status_db = c1.getString(4);
                    dateTimestamp_db = c1.getString(5);
                    DataModel dm = new DataModel(date_db,title_db,content_db,timestamp_db,status_db,false,dateTimestamp_db);
                    completeDataModels.add(dm);
                }
                sa2 = new ScheduleAdapter(MainActivity.this, completeDataModels,this);
                rv_completed.setAdapter(sa2);
            }else {
                sa2 = new ScheduleAdapter(MainActivity.this, completeDataModels,this);
                rv_completed.setAdapter(sa2);
                rv_completed.setVisibility(View.GONE);
                completed.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("DataError","Error : "+e.getMessage());
        }

    }

    private void fillAllData(){
        completed.setVisibility(View.GONE);
        rv_completed.setVisibility(View.GONE);
        allDataModels = new ArrayList<>();
        try {
            Cursor c = scheduleDatabase.getAllData();
            if (c.getCount() > 0) {
                while (c.moveToNext()){
                    date_db = c.getString(0);
                    title_db = c.getString(1);
                    content_db = c.getString(2);
                    timestamp_db = c.getString(3);
                    status_db = c.getString(4);
                    dateTimestamp_db = c.getString(5);
                    if (!last_date.equals(date_db)){
                        DataModel dm = new DataModel(date_db,null,null,"na",null,false,null);
                        allDataModels.add(dm);
                        last_date = date_db;
                    }
                    DataModel dm = new DataModel(date_db,title_db,content_db,timestamp_db,status_db,false,dateTimestamp_db);
                    allDataModels.add(dm);
                }
                allsa = new AllDataAdapter(MainActivity.this, allDataModels,this);
                rv_top.setAdapter(allsa);
            }else {
                allsa = new AllDataAdapter(MainActivity.this, allDataModels,this);
                rv_top.setAdapter(allsa);
            }
        }catch (Exception e){
            Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("DataError","Error : "+e.getMessage());
        }


    }

    private void addToRv(ArrayList<TimeModel> timeModels) {
        da = new DateAdapter(MainActivity.this,timeModels,this);
        rv.setAdapter(da);
    }

    private String getMonthFromInt(int i) {
        if (i==0){
            return "Jan";
        }else if (i==1){
            return "Feb";
        }else if (i==2){
            return "Mar";
        }else if (i==3){
            return "Apr";
        }else if (i==4){
            return "May";
        }else if (i==5){
            return "Jun";
        }else if (i==6){
            return "Jul";
        }else if (i==7){
            return "Aug";
        }else if (i==8){
            return "Sep";
        }else if (i==9){
            return "Oct";
        }else if (i==10){
            return "Nov";
        }else if (i==11){
            return "Dec";
        }else {
            return "";
        }
    }

    private String getDayFromInt(int i) {
        if (i == 0) {
            return "Sun";
        } else if (i == 1) {
            return "Mon";
        } else if (i == 2) {
            return "Tue";
        } else if (i == 3) {
            return "Wed";
        } else if (i == 4) {
            return "Thu";
        } else if (i == 5) {
            return "Fri";
        } else if (i == 6) {
            return "Sat";
        } else {
            return "";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        deleteBtn.setVisibility(View.GONE);
        add.setEnabled(true);
        if (date_selected.equals("all")){
            fillAllData();
        }else {
            fillData(date_selected);
        }
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                btm_lay.getLayoutParams();
        HideBottomViewOnScrollBehavior behavior = (HideBottomViewOnScrollBehavior) params.getBehavior();
        behavior.slideUp(btm_lay);
    }



    @Override
    public void OnSelect(String dateLable, int position) {
        deleteBtn.setVisibility(View.GONE);
        add.setEnabled(true);
        ArrayList<TimeModel> tms = da.getAllModels();
        for (int i=0;i<tms.size();i++){
            if (tms.get(i).getDateLable()==dateLable){
                tms.get(i).setSelected(true);
                date_selected = dateLable;
                dateTimestamp_selected = tms.get(i).getDateTimestamp();
                fillData(date_selected);
            }else {
                tms.get(i).setSelected(false);
            }
        }
        da = new DateAdapter(MainActivity.this,tms,this);
        rv.setAdapter(da);
    }

    @Override
    public void OnLongPressed(TimeModel tm) {
        date_dialog.setText(tm.getDate());
        day_dialog.setText(tm.getDay());
        month_dialog.setText(tm.getMonth());
        deleteDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDatabase dd = new DateDatabase(getApplicationContext());
                boolean result = dd.deleteDate(tm.getDateLable());
                if (result){
                    deleteDateDialog.dismiss();
                    allDates.remove(tm.getDateLable());
                    fillDateRv();
                }else {
                    deleteDateDialog.dismiss();
                }
            }
        });
        deleteDateDialog.show();
    }

    @Override
    public void OnAction(Boolean isSelected) {
        if (!isSelected && sa.getSelectedIds().size()==0 && sa2.getSelectedIds().size()==0){
            deleteBtn.setVisibility(View.GONE);
            add.setEnabled(true);
        }else {
            deleteBtn.setVisibility(View.VISIBLE);
            add.setEnabled(false);
        }
    }

    @Override
    public void OnStatusChange() {
        deleteBtn.setVisibility(View.GONE);
        add.setEnabled(true);
        fillData(date_selected);
    }

    @Override
    public void OnAllAction(Boolean isSelected) {
        if (isSelected){
            deleteBtn.setVisibility(View.VISIBLE);
            add.setEnabled(false);
        }else {
            deleteBtn.setVisibility(View.GONE);
            add.setEnabled(true);
        }
    }

    @Override
    public void OnALLStatusChange() {
        fillAllData();
    }
}