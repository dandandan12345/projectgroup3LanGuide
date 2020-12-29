package com.example.project2.Database;

public class LessonsGrade {
    public int lessonNumber;
    public int totalGrade;
    public int grade;

    public int getLessonNumber() {
        return lessonNumber;
    }

    public int getTotalGrade() {
        return totalGrade;
    }

    public int getGrade() {
        return grade;
    }

    public LessonsGrade() {

    }

    public LessonsGrade(int lessonNumber, int totalGrade, int grade) {
        this.lessonNumber = lessonNumber;
        this.totalGrade = totalGrade;
        this.grade = grade;
    }
}
