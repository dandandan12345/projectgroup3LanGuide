package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.lessonsPackage.LessonAdapter;
import com.example.project2.lessonsPackage.LessonItem;

import java.util.ArrayList;

public class NewLessons extends AppCompatActivity implements LessonAdapter.OnLessonListener {

    TextView lessonsSkill, lessonsLevel;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<LessonItem> lessonList;
    String skill;
    String level;
    boolean isTablet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lessons);

        lessonsLevel = findViewById(R.id.lessonsLevel);
        lessonsSkill = findViewById(R.id.lessonsTitle);
        Intent intent=getIntent();
        level= intent.getStringExtra("level");
        skill = intent.getStringExtra("skill");
        lessonsSkill.setText(skill);
        lessonsLevel.setText(level);
        lessonList = new ArrayList<>();
        lessonList.add(new LessonItem("Lesson", "1", 1, 2.4));
        lessonList.add(new LessonItem("Lesson", "2", 2,4));
        lessonList.add(new LessonItem("Lesson", "3", 3,2));
        lessonList.add(new LessonItem("Lesson", "4", 4,3.4));
        lessonList.add(new LessonItem("Lesson", "5", 5, 3.1));
        lessonList.add(new LessonItem("Lesson", "6", 6,0));
        lessonList.add(new LessonItem("Lesson", "7", 7,3));
        lessonList.add(new LessonItem("Lesson", "8", 8,5));
        lessonList.add(new LessonItem("Lesson", "9", 9,3));
        lessonList.add(new LessonItem("Lesson", "10", 10,0));
        isTablet= getResources().getBoolean(R.bool.isTablet);

        mRecyclerView = findViewById(R.id.lessonsRecyclerView);

        // this line is important for performance
        mRecyclerView.setHasFixedSize(true);
        initializeLayoutForRecyclerView();

        mAdapter = new LessonAdapter(lessonList, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /* this function is a implementation of the OnLessonListener interface in LessonAdapter class
     * @param position is position of the item that was clicked
     * can get the clicked item by using position in lessonList ArrayList
     * */
    @Override
    public void onLessonClick(int position) {
        LessonItem clickedLesson = lessonList.get(position);
        // we can use clickedLesson object to get all information about clicked item
        //
        int lessonNumber = clickedLesson.getLessonNumber();
        Toast.makeText(this, lessonNumber+" ", Toast.LENGTH_LONG).show();

        if(skill.equals(Skill.Writing.toString())){
            Intent intent= new Intent(this, WritingLesson.class);
            intent.putExtra("LessonNumber",lessonNumber);
            startActivity(intent);
        }
        if(skill.equals(Skill.Reading.toString())){
            Intent intent= new Intent(this, activity_lessons_reading.class);
            intent.putExtra("LessonNumber",lessonNumber);
            startActivity(intent);
        }
        if(skill.equals(Skill.Listening.toString())){
            Intent intent= new Intent(this, activity_lessons_listening.class);
            intent.putExtra("LessonNumber",lessonNumber);
            startActivity(intent);
        }
    }

    /*
    * this function check if the device is tablet or mobile and if is land or port
    * with mobile port recyclerview display in 1 column
    * mobile landscape 2 column
    * tablet port 2 column
    * tablet landscape in 3 column*/
    void initializeLayoutForRecyclerView(){

        if (!isTablet){
            if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            }else {
                mLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
            }
        }else {
            if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
                mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
            }else {
                mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            }
        }
    }
}