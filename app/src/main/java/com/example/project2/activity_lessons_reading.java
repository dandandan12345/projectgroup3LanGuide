package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.R;

public class activity_lessons_reading extends AppCompatActivity {
    TextView title, lessonText, readingDescription;
    String fullText, fullText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_reading);

        //Show write title for each lessons
        lessonText=(TextView) findViewById(R.id.lessonText);
        title=(TextView) findViewById(R.id.lesson_number);
        Intent intent=getIntent();
        title.setText(intent.getStringExtra("Lesson"));
        fullText=getResources().getString(R.string.readingText);
        lessonText=(TextView) findViewById(R.id.lessonText);
        lessonText.setText(fullText);

        //Show description text
        readingDescription=(TextView) findViewById(R.id.readingDescription);
        fullText2=getResources().getString(R.string.readingDes);
        readingDescription.setText(fullText2);


    }
}