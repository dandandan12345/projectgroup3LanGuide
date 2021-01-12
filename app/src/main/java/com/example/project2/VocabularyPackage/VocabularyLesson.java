package com.example.project2.VocabularyPackage;


import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.project2.VocabularyPackage.Question;

public class VocabularyLesson {

    public boolean focusChanged = false;
    public int charsOfCurrentWord=0;
    public int startIndex=0;
    public int checkSpinnerCall;
    int wordMargin = 4;   //hur många extra bokstäver man kan skriva utöver de bokstäver som finns i placeholdern.
    float xCoordinate=0.0f;
    float yCoordinate=0.0f;
    boolean keyBoardUp = false;
    Context context;
    public SpannableStringBuilder spannableStringBuilder;
    public EditText textView;
    TextView _info;
    SpannableString greyEdit;
    boolean _lightTheme;
    int Orange = (Color.parseColor("#FF4500"));
    int purple= (Color.rgb(153,50,204));
    int Yellow =(Color. rgb(255, 165, 0));
    int Grey= (Color.GRAY);
    int orangeDark =(Color.rgb(255, 165, 0));

    Spinner spinner;
    public String text;
    SpannableString testString;
    EditText _edittext;
    TextView currentEdit;

    public List<Question> placeholders = new ArrayList<Question>();
    ArrayList<CustomItem> customItemArrayList;

    int orange = Color. rgb(255, 165, 0);


    public VocabularyLesson(String text, EditText textView, EditText editText, Context context,
                            Spinner spinner, TextView info,TextView _currentEdit,boolean _lightTheme)
    {

        this.spinner=spinner;
        this.text =text;
        this.context= context;
        this._edittext=editText;
        this._lightTheme=_lightTheme;
        spannableStringBuilder = new SpannableStringBuilder(Html.fromHtml(text));
        this.textView=textView;
        this._info=info;
        handleTestText(text);
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnTouchListener(touchListener);
        this.currentEdit = _currentEdit;
    }

    private static final Pattern TAG_REGEX = Pattern.compile("<u>(.+?)</u>", Pattern.DOTALL);

    private List<String> getTagValues(final String str) {
        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = TAG_REGEX.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group());
        }
        return tagValues;
    }
    public void handleTestText(String test) {
        testString = new SpannableString(Html.fromHtml(test));

        List<String> testStrings = getTagValues(test);

        for (String name : testStrings) {
            name = name.replace("<u>", "");
            name = name.replace("</u>", "");
            String tagFreeString = testString.toString();

            int startIndex = tagFreeString.indexOf(name);//find the index of where the questions are.
            int wordLeangth = name.length();

            //tagFreeString = tagFreeString.substring(0,startIndex+wordLeangth)+"  "+tagFreeString.substring(startIndex+wordLeangth);
            //tagFreeString = tagFreeString.substring(0,startIndex)+"  "+tagFreeString.substring(startIndex);

            //spannableStringBuilder.insert(startIndex+wordLeangth,"  ");
            //spannableStringBuilder.insert(startIndex-(wordMargin/2), "  ");

            spannableStringBuilder.setSpan(new myClickableSpan(startIndex,wordLeangth),startIndex,startIndex+wordLeangth,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            placeholders.add(new Question(name,startIndex));
        }
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void createNewSpannableParts(int start, int end,String text)
    {
        spannableStringBuilder = new SpannableStringBuilder(text);

        for(Question placeholder: placeholders)
        {
            int startIndex = placeholder.index;
            int endIndex = startIndex + placeholder.placeHolder.length();
            int wordLeangth = placeholder.placeHolder.length();

            spannableStringBuilder.setSpan(new myClickableSpan(startIndex,wordLeangth),startIndex,endIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(placeholder.answered == true)
            {
                if(_lightTheme){
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(purple), startIndex,startIndex+wordLeangth,0);
                }
                else{
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(orangeDark), startIndex,startIndex+wordLeangth,0);
                }

            }

        }

        spannableStringBuilder.setSpan(new myClickableSpan(start,text.length()),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(_lightTheme){
            spannableStringBuilder.setSpan(new ForegroundColorSpan(purple), start,end,0);
        }
        else{
            spannableStringBuilder.setSpan(new ForegroundColorSpan(orangeDark), start,end,0);
        }
        //spannableStringBuilder.setSpan(new ForegroundColorSpan(orange), start,end,0);
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void incrementIndexes(int start, String newPlaceholder)
    {
        for(Question placeholder: placeholders)
        {
            if(placeholder.index == start)
            {
                placeholder.placeHolder = newPlaceholder;
            }
            else if(placeholder.index > start)
            {
                placeholder.index++;
            }
        }
    }
    public void decrementIndexes(int start, String newPlaceholder)
    {
        for(Question placeholder: placeholders)
        {
            if(placeholder.index == start)
            {
                placeholder.placeHolder = newPlaceholder;
            }
            else if(placeholder.index > start)
            {
                placeholder.index--;
            }
        }
    }
    public void changeSpanColor(int color)
    {
        testString = new SpannableString(textView.getText());

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableStringBuilder.setSpan(colorSpan, startIndex,startIndex+charsOfCurrentWord,0);


        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableStringBuilder);
    }
    public  class  myClickableSpan extends ClickableSpan {
        int position;
        int _startIndex;
        int currentwordChars;

        public myClickableSpan(int position, int _charsofcurrentWord) {
            this._startIndex=position;
            this.currentwordChars=_charsofcurrentWord;
            this.position=position;
        }
        @Override
        public void onClick(@NonNull View widget) {
            startIndex=_startIndex;
            charsOfCurrentWord=currentwordChars; //add two more letters to the edited word

            if(_edittext.getText().toString().trim().length() != 0)
            {
                focusChanged = true;
                _edittext.setText("");
            }
            if(keyBoardUp == true)
            {
                hideKeyboard(_edittext);
                currentEdit.setVisibility(View.GONE);
                keyBoardUp = false;
            }


            customItemArrayList=getCustomList();
            CustomAdapter adapter=new CustomAdapter(context,customItemArrayList);
            spinner.setAdapter(adapter);
            //determine where spinner appears
            spinner.setX(xCoordinate);
            spinner.setY(yCoordinate);
            spinner.setVisibility(View.VISIBLE);
            spinner.performClick();
            checkSpinnerCall=0;


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position==0){

                    }
                    else if(position==1){
                        for(Question word: placeholders)
                        {
                            if(word.index == startIndex){
                                String subStr = spannableStringBuilder.toString().substring(startIndex,startIndex+charsOfCurrentWord);
                                if(subStr.equals(word.placeHolder))                // om man har ändrat på ett ord kan man inte göra det grönt...ska utforska bättre lösningar
                                {
                                    if(_lightTheme){
                                        changeSpanColor(purple);
                                    }
                                    else{
                                        changeSpanColor(orangeDark);
                                    }
                                    word.answered = true;
                                }
                            }
                        }
                    }
                    else if(position==2){
                        if(_lightTheme){
                            changeSpanColor(purple);
                        }
                        else{
                            changeSpanColor(orangeDark);
                        }
                        String currEdit = "Now editing: ";
                        int greyStart = currEdit.length();
                        int greyEnd = 0;
                        for(Question word: placeholders)
                        {
                            if(word.index == startIndex){
                                currEdit = currEdit + word.placeHolder;
                                word.answered = true;
                                greyEnd = greyStart+word.placeHolder.length();
                            }
                        }
                        greyEdit = new SpannableString(currEdit);
                        if(_lightTheme){
                            greyEdit.setSpan(new ForegroundColorSpan(Orange),greyStart,greyEnd,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                        else{
                            greyEdit.setSpan(new ForegroundColorSpan(Grey),greyStart,greyEnd,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        }

                        currentEdit.setText(greyEdit);                 //visa andvändaren vilket ord som stod från början innan man ändrade på det i en textview
                        currentEdit.setVisibility(View.VISIBLE);


                        _edittext.setEnabled(true);
                        _edittext.requestFocus();
                        Editable text = spannableStringBuilder;//textView.getText();

                        for(int i=0;i<charsOfCurrentWord;i++){

                            text.replace(startIndex+charsOfCurrentWord-i-1,startIndex+charsOfCurrentWord-i," ");  //remove letters from the word that the user has chosen to edit
                            _edittext.setSelection(_edittext.getText().length());

                        }
                        textView.setText(spannableStringBuilder);


                        InputMethodManager inputMethodManager =
                                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInputFromWindow(
                                textView.getApplicationWindowToken(),
                                InputMethodManager.SHOW_FORCED, 0);
                        keyBoardUp = true;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    public SpannableStringBuilder getSpannableStringBuilder() {
        return spannableStringBuilder;
    }

    public void hideKeyboard(View view){
        //View view = this.getCurrentFocus();
        if(view != null)
        {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }


    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // save the X,Y coordinates
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                xCoordinate= event.getX();
                yCoordinate = event.getY()+(float)_info.getHeight();
            }

            return false;
        }
    };

    private ArrayList<CustomItem> getCustomList() {
        customItemArrayList=new ArrayList<>();
        //customItemArrayList.add(new CustomItem("CHOOSE AN OPTION",0));
        customItemArrayList.add(new CustomItem("",0));
        customItemArrayList.add(new CustomItem("CORRECT",R.drawable.ic_baseline_check_24));
        customItemArrayList.add(new CustomItem("EDIT",R.drawable.ic_baseline_edit_24));
        //customItemArrayList.add(new CustomItem("Hello",R.drawable.ic_baseline_check_24));


        return customItemArrayList;
    }

}


