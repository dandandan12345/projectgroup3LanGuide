package com.example.project2.writing_lesson;

public class WritingGap {
   public WritingGapChoices choices;
    int gapNumber;
    int startIndex;
    int selectedChoiceNumber;
    String selectedChoice;
    String correctChoice;
    public WritingGap(WritingGapChoices gapChoices){
        this.choices=gapChoices;
    }

    public int getGapNumber() {
        return gapNumber;
    }

    public void setGapNumber(int gapNumber) {
        this.gapNumber = gapNumber;
    }

    public int getStartIndex() {
        return startIndex;
    }


    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setSelectedChoice(String selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public int getSelectedChoiceNumber() {
        return selectedChoiceNumber;
    }

    public String getCorrectChoice() {
        return correctChoice;
    }

    public void setCorrectChoice(String correctChoice) {
        this.correctChoice = correctChoice;
    }

    public String getSelectedChoice() {
        return selectedChoice;
    }
}
