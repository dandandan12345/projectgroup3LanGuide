package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Lessons extends AppCompatActivity implements View.OnClickListener {
    Button btnLesson1, btnLesson2, btnLesson3, btnLesson4, btnLesson5, btnLesson6, btnLesson7;
    TextView title ;
    String skill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);


        //Assign variables
        btnLesson1=(Button) findViewById(R.id.lesson1);
        btnLesson2=(Button) findViewById(R.id.lesson2);
        btnLesson3=(Button) findViewById(R.id.lesson3);
        btnLesson4=(Button) findViewById(R.id.lesson4);
        btnLesson5=(Button) findViewById(R.id.lesson5);
        btnLesson6=(Button) findViewById(R.id.lesson6);
        btnLesson7=(Button) findViewById(R.id.lesson7);
        title = findViewById(R.id.lessons_title);
        btnLesson1.setOnClickListener(this);
        btnLesson2.setOnClickListener(this);
        btnLesson3.setOnClickListener(this);
        btnLesson4.setOnClickListener(this);
        btnLesson5.setOnClickListener(this);
        btnLesson6.setOnClickListener(this);

        // intent have level and skill
        Intent intent=getIntent();
        String level= intent.getStringExtra("level");
        skill = intent.getStringExtra("skill");

        // set the title of the layout to the right skill
        title.setText(skill);
        // int u=i.getExtras().getInt("test");

        // if(u==1)
        if(level.equals(LevelEnum.Beginner.toString()) )
        {
            //Nothing for now
        }
        //else if(u==2)
        else if(level.equals(LevelEnum.Intermediate.toString()))
        {
            btnLesson1.setText("Lesson8");
            btnLesson2.setText("Lesson9");
            btnLesson3.setText("Lesson10");
            btnLesson4.setText("Lesson11");
            btnLesson5.setText("Lesson12");
            btnLesson6.setText("Lesson13");
            btnLesson7.setText("Lesson14");
        }
        // else if(u==3)
        else if(level.equals(LevelEnum.Advanced.toString()))
        {
            btnLesson1.setText("Lesson15");
            btnLesson2.setText("Lesson16");
            btnLesson3.setText("Lesson17");
            btnLesson4.setText("Lesson18");
            btnLesson5.setText("Lesson19");
            btnLesson6.setText("Lesson20");
            btnLesson7.setText("Lesson21");
        }
/*        else if()
        {
        }*/
    }
    @Override
    public void onClick(View v) {

        Button button = (Button)v;

        if(skill.equals(Skill.Writing.toString())){
            Intent intent= new Intent(this, WritingLesson.class);
            intent.putExtra("Lesson",button.getText().toString() );
            startActivity(intent);
        }

        if(skill.equals(Skill.Reading.toString())){
            Intent intent= new Intent(this, activity_lessons_reading.class);
            intent.putExtra("Lesson", button.getText().toString());
            startActivity(intent);
        }

        if(skill.equals(Skill.Listening.toString())){
            Intent intent= new Intent(this, activity_lessons_listening.class);
            intent.putExtra("Lesson", button.getText().toString());
            startActivity(intent);
        }
    }
}
