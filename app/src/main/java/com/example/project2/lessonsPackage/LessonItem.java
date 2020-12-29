package com.example.project2.lessonsPackage;

import android.util.Log;
import android.widget.RatingBar;

public class LessonItem {
    protected String lesson ;
    protected int lessonNumber;
    protected String  lessonNumberString;
    protected float lessonRating;

    public LessonItem() {

    }
    /*
     * @param lessonNumberString is number of lesson which will will bi displayed on the screen
     * @param lessonNumber this will not be displayed on the screen, here is the real number of the lesson
     *   */
    public LessonItem(String lesson, String lessonNumberString, int lessonNumber, String lessonRating){
        this.lesson=lesson;
        this.lessonNumberString = lessonNumberString;
        this.lessonNumber = lessonNumber;
        if(lessonRating == null){
            this.lessonRating = (float)0;
        }else{
            try {
                this.lessonRating=Float.parseFloat(lessonRating);
            }catch (Exception e){
                this.lessonRating = 0;
            }
        }
        Log.d("GGG", "Rating: "+this.lessonRating);
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public void setLessonNumberString(String lessonNumberString) {
        this.lessonNumberString = lessonNumberString;
    }



    public int getLessonNumber() {
        return lessonNumber;
    }

    public String getLesson() {
        return lesson;
    }

    public String getLessonNumberString() {
        return lessonNumberString;
    }

    public String getLessonRating() {
        return String.valueOf(lessonRating);
    }
    public void setLessonRating(float lessonRating) {
        this.lessonRating = lessonRating;
    }
}
