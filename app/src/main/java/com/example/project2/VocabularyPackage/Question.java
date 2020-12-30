package com.example.project2.VocabularyPackage;

public class Question
{
    public String placeHolder;
    public int index;
    public boolean answered = false;

    public Question(String _word, int _index)
    {
        placeHolder = _word;
        index = _index;
    }
}