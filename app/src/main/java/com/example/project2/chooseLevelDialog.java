package com.example.project2;

import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;

public class chooseLevelDialog extends Dialog implements View.OnClickListener {

    String TAG= "chooseLevelDialog";
    String level;
    Button cancelBtn;
    Context context ;
    TextView beginner;
    TextView intermediate;
    TextView advanced;
    Skill currentSkill;
    public chooseLevelDialog(@NonNull Context context,Skill skill) {
        super(context);
        this.context =context;
        setContentView(R.layout.level_popup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelBtn = findViewById(R.id.popupCancelBtn);
        this.currentSkill =skill;
    }
    @Override
    public void show() {
        super.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cancelBtn = findViewById(R.id.popupCancelBtn);
        beginner = findViewById(R.id.chooseBeginnerLevel);
        advanced = findViewById(R.id.chooseAdvancedLevel);
        intermediate = findViewById(R.id.chooseIntermediateLevel);
        advanced.setOnClickListener(this);
        intermediate.setOnClickListener(this);
        beginner.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.popupCancelBtn){
            level="";
        }else {
            String lev = ((TextView)v).getText().toString();
            Intent intent = new Intent(context, Lessons.class);
            intent.putExtra("level",lev );
            intent.putExtra("skill", currentSkill.toString());
            context.startActivity(intent);
        }
        dismiss();
    }
}