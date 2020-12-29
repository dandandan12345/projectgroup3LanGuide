package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.project2.Database.LessonItemForIntent;
import com.example.project2.lessonsPackage.LessonAdapter;
import com.example.project2.lessonsPackage.LessonItem;
import com.example.project2.utils.Skill;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewLessons extends AppCompatActivity implements LessonAdapter.OnLessonListener {

    TextView lessonsSkill, lessonsLevel;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<LessonItem> lessonList;
    String skill;
    String level;
    String userId ;
    boolean isTablet;
    Map<Integer, String> ratingsMap = new HashMap<>();
    DatabaseReference ratingsReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lessons);
        Intent intent=getIntent();
        level= intent.getStringExtra("level");
        skill = intent.getStringExtra("skill");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ratingsReference = FirebaseDatabase.getInstance().getReference().child("grades").child(userId).child(skill).child(level).child("ratings");
        lessonsLevel = findViewById(R.id.lessonsLevel);
        lessonsSkill = findViewById(R.id.lessonsTitle);
        readRatingsFromDatabase();
        lessonsSkill.setText(skill);
        lessonsLevel.setText(level);


        isTablet= getResources().getBoolean(R.bool.isTablet);
        mRecyclerView = findViewById(R.id.lessonsRecyclerView);

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
       // Toast.makeText(this, lessonNumber+" ", Toast.LENGTH_LONG).show();
        LessonItemForIntent lessonItemForIntent = new LessonItemForIntent(clickedLesson.getLesson(),
                clickedLesson.getLessonNumberString(), clickedLesson.getLessonNumber(), clickedLesson.getLessonRating(),
                skill, level);
        if(skill.equals(Skill.Writing.toString())){
            Intent intent= new Intent(this, WritingLesson.class);
            //intent.putExtra("LessonNumber",lessonNumber);

            intent.putExtra("lessonObject", lessonItemForIntent);
            startActivity(intent);
        }
        if(skill.equals(Skill.Reading.toString())){
            Intent intent= new Intent(this, activity_lessons_reading.class);
            //intent.putExtra("LessonNumber",lessonNumber);

            intent.putExtra("lessonObject", lessonItemForIntent);
            startActivity(intent);
/*            Intent intent= new Intent(this, activity_lessons_reading.class);
            intent.putExtra("lessonObject", lessonItemForIntent);
            startActivity(intent);*/
        }
        if(skill.equals(Skill.Listening.toString())){
            Intent intent= new Intent(this, activity_lessons_listening.class);
            intent.putExtra("lessonObject", lessonItemForIntent);
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
    void readRatingsFromDatabase(){
        ratingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()){
                    if(snap.getKey()!=null && snap.getValue() !=null)
                    ratingsMap.put(Integer.parseInt(snap.getKey()), snap.getValue().toString());
                    Log.d("GGG", "key: "+snap.getKey()+" value: "+snap.getValue());
                }
                displayLayout();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    void displayLayout(){
        lessonList = new ArrayList<>();
        lessonList.add(new LessonItem("Lesson", "1", 1, ratingsMap.get(1)));
        lessonList.add(new LessonItem("Lesson", "2", 2,ratingsMap.get(2)));
        lessonList.add(new LessonItem("Lesson", "3", 3,ratingsMap.get(3)));
        lessonList.add(new LessonItem("Lesson", "4", 4,ratingsMap.get(4)));
        lessonList.add(new LessonItem("Lesson", "5", 5, ratingsMap.get(5)));
        lessonList.add(new LessonItem("Lesson", "6", 6,ratingsMap.get(6)));
        lessonList.add(new LessonItem("Lesson", "7", 7,ratingsMap.get(7)));
        lessonList.add(new LessonItem("Lesson", "8", 8,ratingsMap.get(8)));
        lessonList.add(new LessonItem("Lesson", "9", 9,ratingsMap.get(9)));
        lessonList.add(new LessonItem("Lesson", "10", 10,ratingsMap.get(10)));

        // this line is important for performance
        mRecyclerView.setHasFixedSize(true);
        initializeLayoutForRecyclerView();
        mAdapter = new LessonAdapter(lessonList, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}