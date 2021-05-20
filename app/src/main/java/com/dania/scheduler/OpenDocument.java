package com.dania.scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OpenDocument extends AppCompatActivity {

    private String date,title,content,timestamp,status,dateTimestamp;
    private TextView title_tv;
    private EditText content_et;
    private ImageView deleteBtn;
    private RelativeLayout rl;
    private Button saveBtn;
    private double addedCharIndex = 0.0;
    private double editwriting_length = 0.0;
    private SoundPool soundPool;
    private int keySound,backspaceSound,spaceSound,returnSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_document);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b!=null){
            date = b.getString("date");
            title = b.getString("title");
            content = b.getString("content");
            timestamp = b.getString("timestamp");
            status = b.getString("status");
            dateTimestamp = b.getString("dateTimestamp");
        }
        initialize();
    }

    private void initialize() {
        title_tv = findViewById(R.id.title_tv);
        content_et = findViewById(R.id.content_et);
        deleteBtn = findViewById(R.id.deleteBtn);
        rl = findViewById(R.id.rl);
        saveBtn = findViewById(R.id.saveBtn);
        title_tv.setText(title);
        content_et.setText(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_et.requestFocus(0);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(content_et, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        keySound = soundPool.load(this,R.raw.typewriter_key,1);
        backspaceSound = soundPool.load(this,R.raw.typewriter_backspace,1);
        spaceSound = soundPool.load(this,R.raw.typewriter_spacebar,1);
        returnSound = soundPool.load(this,R.raw.typewriter_return,1);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseScheduler ds = new DatabaseScheduler(getApplicationContext());
                boolean result = ds.deleteSchedule(timestamp);
                if (result){
                    finish();
                }else {
                    Toast.makeText(OpenDocument.this, "Failed to delete schedule.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseScheduler ds = new DatabaseScheduler(getApplicationContext());
                title = title_tv.getText().toString();
                content = content_et.getText().toString();
                boolean result = ds.updateFullSchedule(date,title,content,timestamp,status,dateTimestamp);
                if (result){
                    Toast.makeText(OpenDocument.this, "Saved", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(OpenDocument.this, "Failed to save schedule.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound("key");
            }
        });

       /* content_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    playSound("backspace");
                }else if (keyCode == KeyEvent.KEYCODE_ENTER){
                    playSound("return");
                } else if (keyCode == KeyEvent.KEYCODE_SPACE) {
                    playSound("space");
                }else {
                    playSound("key");
                }
                return false;
            }
        });

        */

        content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int n4=content_et.getSelectionEnd();
                int n5 = content_et.getSelectionStart();
                Log.d("TraceET","n5="+n5);
                Log.d("TraceET","n4="+n4);
                if (n5 == n4){
                    Log.d("TraceET","n5==n4");
                    if (n5 < 1) {
                        access1(this,1.0);
                    }else {
                        access1(this,n5);
                    }
                    if ((double)s.toString().length() == 1.0 + content_et.length()){
                        Log.d("TraceET","first");
                        if (s.toString().substring((int)(addedCharIndex-1.0),(int)addedCharIndex).equals(" ")){
                            Log.d("TraceET","space");
                            playSound("space");
                        }else if (s.toString().substring((int)(addedCharIndex-1.0),(int)addedCharIndex).equals("\n")){
                            Log.d("TraceET","return");
                            playSound("return");
                        }else {
                            Log.d("TraceET","key");
                            playSound("key");
                        }
                    }else if ((double)s.toString().length() == content_et.length()-1.0){
                        playSound("backspace");
                    }
                }else {
                    Log.d("TraceET","second");
                    if ((double)s.toString().length() == 1.0 + content_et.length()){
                        Log.d("TraceET","n5!=n4");
                        if (s.toString().substring(n5,n4).equals(" ")){
                            Log.d("TraceET","space2");
                            playSound("space");
                        }else if (s.toString().substring(n5,n4).equals("\n")){
                            Log.d("TraceET","return2");
                        }else {
                            Log.d("TraceET","key2");
                            playSound("key");
                        }
                    }else if ((double)s.toString().length() == content_et.length()-1.0){
                        playSound("backspace");
                    }
                }
                access5(this,s.toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void access5(TextWatcher textWatcher, double d) {
        editwriting_length = d;
    }

    private void access1(TextWatcher textWatcher, double v) {
        addedCharIndex = v;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DatabaseScheduler ds = new DatabaseScheduler(getApplicationContext());
        title = title_tv.getText().toString();
        content = content_et.getText().toString();
        boolean result = ds.updateFullSchedule(date,title,content,timestamp,status,dateTimestamp);
        if (result){
            Toast.makeText(OpenDocument.this, "Saved", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(OpenDocument.this, "Failed to save schedule.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void playSound(String string){
        if (string.equals("key")){
            soundPool.play(keySound,1,1,0,0,1);
        }else if (string.equals("backspace")){
            soundPool.play(backspaceSound,1,1,0,0,1);
        }else if (string.equals("space")){
            soundPool.play(spaceSound,1,1,0,0,1);
        }else if (string.equals("return")){
            soundPool.play(returnSound,1,1,0,0,1);
        }
    }
}