package com.example.project2.VocabularyPackage;

public class CustomItem {
    String SpinnerName;

    int SpinnerImage;

    public CustomItem(String SpinnerName,int SpinnerImage){
        this.SpinnerImage=SpinnerImage;

        this.SpinnerName=SpinnerName;
    }

    public String getSpinnerItemName(){
        return SpinnerName;
    }
    public int getSpinnerItemImage(){
        return SpinnerImage;
    }
}