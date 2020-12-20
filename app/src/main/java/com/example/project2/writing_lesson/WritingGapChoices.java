package com.example.project2.writing_lesson;

public class WritingGapChoices {
    int itemNumber;
    String second;
    String first;
    String third;
    String correct;
    public WritingGapChoices(String firstChoice, String secondChoice, String  thirdChoice, String correctChoice, int itemNumber){
        this.first=firstChoice;
        this.second = secondChoice;
        this.third= thirdChoice;
        this.correct = correctChoice;
        this.itemNumber= itemNumber;
    }
    public String getCorrect() {
        return correct;
    }
}
