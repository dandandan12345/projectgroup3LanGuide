package com.example.project2.lessonsPackage;

import android.widget.RatingBar;

public class LessonItem {
    String lesson ;
    int lessonNumber;
    String  lessonNumberString;
    float lessonRating;
    /*
    * @param lessonNumberString is number of lesson which will will bi displayed on the screen
    * @param lessonNumber this will not be displayed on the screen, here is the real number of the lesson
    *   */
    public LessonItem(String lesson, String lessonNumberString, int lessonNumber, double lessonRating){
        this.lesson=lesson;
        this.lessonNumberString = lessonNumberString;
        this.lessonNumber = lessonNumber;
        this.lessonRating=(float) lessonRating;
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

    public float getLessonRating() {
        return lessonRating;
    }
    public void setLessonRating(float lessonRating) {
        this.lessonRating = lessonRating;
    }
}
