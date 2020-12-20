package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.writing_lesson.DisplayWritingText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

public class VocabularyTest extends AppCompatActivity {

    int startIndex = 0;
    int indexChange = 0;
    int charsOfCurrentWord = 0;
    int charsOfUserInput = 0;

    int k = 0;

    int clickableSpanIndex = 0;

    SpannableString testString;

    List<String> answers = new ArrayList<String>();

    Dialog resultDialog;
    Button submitButtonVocabulary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);
        Intent intent = getIntent();

        TextView lessonLabel = findViewById(R.id.lessonLabel);
        lessonLabel.setText(intent.getStringExtra("Lesson"));

        String field = "Finance";
        String typeOfSkill = "Writing, vocabulary";
        String typeOfTask = "Spelling (of words often used when dealing with a budget)";
        String task = "For each of the underlined words in the text decide whether it is spelled correctly. If it is written correctly, double tap on the line to make the underline green; otherwise, spell it correctly.";
        String test = "The European Union (EU) has been providing <u>budget</u> support to Cambodia in the education sector " +
                "since 2003 on the basis of sound and comprehensive plans to improve performance in  the  sector  and  to" +
                " gradually  implement  public  finance  management  (PFM)  reforms,  as well as continued improvements in both areas." +
                " Over the same period Cambodia has made steady progress in poverty <u>redduction</u> in the last decade underpinned by high <u>economic</u> growth." +
                " However important challenges remain in the education sector, such as the need to increase enrolment and retention at secondary level," +
                " to improve quality at all levels and to reduce regional and social disparities. Addressing these requires Government to increase its resources" +
                " <u>alocated</u> to the sector. The further scaling up of budget support provided by the EU to the sector, as proposed, building on a recently" +
                " agreed programme, will enhance the support to Government's efforts to reverse the fall in the share of Government <u>reccurent</u> funds" +
                " provided  to  the  Ministry  of  Education,  Youth  and  Sports  (MoEYS)  by  supporting  an increase of Government <u>resorses</u> <u>avarded</u>" +
                " to specific interventions aimed at improving key service  delivery  indicators  related  to  access,  equity  and  quality  in  the  sector." +
                "  It  will  also encourage  Government  to  continue  strengthening  its  PFM  systems  and  increase  budget <u>transperrency</u>." +
                " The proposed amount is a top up to a recently signed programme covering the period 2014-2016.  An  Addendum to the ongoing Financing" +
                " <u>Agrement</u> will be signed. This additional amount should cover one additional year and in effect lead to more than a doubling of" +
                " the yearly amount for the period 2014-2016. This increased level will be possibly <u>maintained</u> in 2017 with the additional financing" +
                " <u>foresen</u> under the MIP 2014-2020.                                                             (European Comission, 2014)";


        initializeTest(field,typeOfSkill,typeOfTask,task,test);

        EditText testText = findViewById(R.id.testText);
        EditText invisibleField = findViewById(R.id.invisibleEditText);


        invisibleField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction()!=KeyEvent.ACTION_DOWN)  //so the onkeylistener doesnt go off twice
                    return true;

                EditText tt = findViewById(R.id.testText);
                Editable text = tt.getText();

                if(KeyEvent.KEYCODE_DEL == keyCode)
                {
                    if(charsOfCurrentWord-2 + charsOfUserInput <= 0)  //man kan inte ta radera mer bokstäver än vad som finns i ordet
                        return true;
                    text.replace(startIndex+indexChange-1,startIndex+indexChange," ");
                    charsOfUserInput = charsOfUserInput - 1;
                    indexChange--;
                }
                return true;
            }
        });


        invisibleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                invisibleField.setSelection(invisibleField.getText().length());  // make sure that the cursur is always last in the edittext
                EditText tt = findViewById(R.id.testText);
                Editable text = tt.getText();
                //String input = s.toString();
                String sText = s.toString(); //.substring(invisibleLeangth);
                Character letter =  sText.charAt(sText.length()-1);
                String input = letter.toString();

                charsOfUserInput = charsOfUserInput +1;
                if(charsOfUserInput < 1)
                {
                    text.replace(startIndex+indexChange,startIndex+indexChange+1,input);;
                    indexChange++;
                }
                else {
                    charsOfUserInput = 0;
                    if(s.length() != 0)
                    {
                        invisibleField.removeTextChangedListener(this);
                        String invisibleText = invisibleField.getText().toString();
                        invisibleField.setText(invisibleText.substring(0, invisibleText.length() - 1));
                        invisibleField.addTextChangedListener(this);
                    }
                }

                answers.add("budget");
                answers.add("reduction");
                answers.add("economic");
                answers.add("allocated");
                answers.add("recurrent");
                answers.add("resources");
                answers.add("awarded");
                answers.add("transparency");
                answers.add("agreement");
                answers.add("maintained");
                answers.add("foreseen");


                submitButtonVocabulary = findViewById(R.id.submitButtonVocabulary);
                submitButtonVocabulary.setOnClickListener(v -> {
                    int rightAnswers = 0;
                    String finishedTest = testText.getText().toString();
                    for (String answer:answers){
                        if(finishedTest.contains(answer)){
                            rightAnswers++;
                        }
                    }
                    showResultDialog(rightAnswers,answers.size());
                });
            }
        });

        initializeResultDialog();
    }

    void initializeTest(String field, String typeOfSkill,String typeOfTask,String task,String test) {

        TextView testInfo = findViewById(R.id.testInformation);
        String fieldLable = "<b>Field: </b>";
        testInfo.setText(Html.fromHtml(fieldLable + field));
        testInfo.append("\n\n");
        String skillLable = "<b>Type of skill: </b>";
        testInfo.append(Html.fromHtml(skillLable + typeOfSkill));
        testInfo.append("\n\n");
        String typeOfTaskLable = "<b>Type of task: </b>";
        testInfo.append(Html.fromHtml(typeOfTaskLable + typeOfTask));
        testInfo.append("\n\n");
        String taskLable = "<b>Task: </b>";
        testInfo.append(Html.fromHtml(taskLable + task));
        testInfo.append("\n");
        //getTagValues(test);

        handleTestText(test);







        /*
        final TextView testTextView = findViewById(R.id.testText);
        testTextView.setText(testString);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            testTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

         */




        // testTextView.setText(Html.fromHtml(test));


    }

    void handleTestText(String test)
    {
        testString = new SpannableString(Html.fromHtml(test));
        final String str = test;
        List<String> testStrings = getTagValues(test);
        int i = 0;
        for(String name: testStrings)
        {
            name = name.replace("<u>","");
            name = name.replace("</u>","");
            String tagFreeString = testString.toString();
            int index = tagFreeString.indexOf(name);           //find the index of where the questions are and make them clickable
            int temp = name.length();
            testString.setSpan(new ForegroundColorSpan(Color.parseColor("#F08080")), index-1, index+name.length()+1, 0);
            testString.setSpan(new UnderlineSpan(),index-1,index+name.length()+1,0);
            EditText invisibleText = findViewById(R.id.invisibleEditText);

            ClickableSpan clickableSpanQuestions = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    invisibleText.clearFocus();
                    k++;
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if(k == 1){
                                invisibleText.setEnabled(true);
                                invisibleText.requestFocus();
                                InputMethodManager softKeyBoard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                softKeyBoard.showSoftInput(invisibleText, 0);
                            }
                            else if(k == 2){
                                hideKeyboard(invisibleText);
                            }
                            k = 0;
                        }
                    };
                    if(k == 1)
                    {

                        handler.postDelayed(runnable,800);

                        startIndex = index+temp;
                        charsOfCurrentWord = temp+2;
                        charsOfUserInput = 0;
                        indexChange = 0;

                    }
                    else if(k == 2)
                    {
                        startIndex = index+temp;
                        charsOfCurrentWord = temp+2;
                        charsOfUserInput = 0;
                        indexChange = 0;
                        changeSpanColor(testString);
                        hideKeyboard(invisibleText);
                    }
                    else if(k == 3)
                    {
                        k = 0;
                    }
                }

                @Override
                public void updateDrawState(TextPaint tp){
                    tp.setColor(Color.parseColor("#27D5F6"));
                }
            };
            ClickableSpan restOfText = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    hideKeyboard(invisibleText);
                }
                @Override
                public void updateDrawState(TextPaint tp){
                    tp.setColor(Color.parseColor("#FFFFFF"));
                }

            };
            testString.setSpan(restOfText,clickableSpanIndex,index,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            clickableSpanIndex = index + name.length();
            testString.setSpan(clickableSpanQuestions, index, index+name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            i++;
        }

        EditText testTextView = findViewById(R.id.testText);

        testTextView.setText(testString,TextView.BufferType.SPANNABLE);
        testTextView.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private static final Pattern TAG_REGEX = Pattern.compile("<u>(.+?)</u>", Pattern.DOTALL);

    private List<String> getTagValues(final String str) {
        final TextView testTextView = findViewById(R.id.testText);
        SpannableString spanString = new SpannableString(str);
        //Matcher matcher=Pattern.compile("@(\"<u>(.+?)</u>\"+)").matcher(spanString);
        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = TAG_REGEX.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group());
            /*
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#0000FF")), matcher.start()+4, matcher.end(), 0);
            final String tag = matcher.group(0);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    //testTextView.setEnabled(true);
                    //testTextView.getEditableText();
                    Log.e("click", "click " + tag);
                    EditText invisibleText = findViewById(R.id.invisibleEditText);
                    EditText testText = findViewById(R.id.testText);
                    invisibleLeangth = invisibleText.length();
                    previousTextLeangth = 0;
                    invisibleText.setEnabled(true);
                    invisibleText.requestFocus();
                    Editable text = testText.getText();
                    String textToBeInserted = "HI";
                    //int index = matcher.start();
                    startIndex = text.toString().indexOf(tag);
                    endIndex = text.toString().lastIndexOf(tag);

                    //text.toString().replace("/<a>/g","");
                    //text.insert(text.toString().indexOf(tag),textToBeInserted);
                    //StringBuffer buffer = new StringBuffer(str);
                    //buffer.insert(matcher.start(),  invisibleText.getText().toString());
                    //String test=tag.replace(TAG_REGEX.toString() ,"");

                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);

                }
            };
            spanString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //tagValues.add(matcher.group(1));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            testTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        testTextView.setMovementMethod(LinkMovementMethod.getInstance());
        //testTextView.append(Html.fromHtml(String.valueOf(spanString)));
        testTextView.setText(spanString,TextView.BufferType.SPANNABLE);
       // testTextView.setText(testTextView.getText().toString().replace("/<a>/g",""));


             */
        }
        return tagValues;
    }

    public void hideKeyboard(View view){
        //View view = this.getCurrentFocus();
        if(view != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    void changeSpanColor(SpannableString testString)
    {
        EditText testTextView = findViewById(R.id.testText);
        testString = new SpannableString(testTextView.getText());

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.GREEN);
        testString.setSpan(colorSpan, startIndex-charsOfCurrentWord+1,startIndex,0);


        testTextView.setMovementMethod(LinkMovementMethod.getInstance());
        testTextView.setText(testString);
    }
    private void initializeResultDialog() {
        resultDialog =new Dialog(this, R.style.resultDialogStyle);
        resultDialog.setContentView(R.layout.result_dialog_layout);
        Window window = resultDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        resultDialog.setCancelable(true);
        window.setAttributes(wlp);
    }

    private void showResultDialog(int correctAnswersNumber, int totalQuestionsNumber) {
        TextView totalQuestions= resultDialog.findViewById(R.id.resultDialogTotalQuestions);
        TextView correctTextView = resultDialog.findViewById(R.id.resultDialogCorrectAnswers);
        TextView wrongAnswers = resultDialog.findViewById(R.id.resultDialogWrongAnswers);
        ImageView statusImage = resultDialog.findViewById(R.id.status_image);
        ImageView closeDialogIcon= resultDialog.findViewById(R.id.closeDialog);
        Button dialogButton = resultDialog.findViewById(R.id.resultDialogTryAgain);
        totalQuestions.setText(String.valueOf(totalQuestionsNumber));
        int wrong = totalQuestionsNumber - correctAnswersNumber;
        correctTextView.setText(String.valueOf(correctAnswersNumber));
        wrongAnswers.setText(String.valueOf(wrong));
        // if wrong answers if greater than correct display sad emoji
        if(wrong > correctAnswersNumber){
            statusImage.setImageResource(R.drawable.sad_emoji);
        }
        else {
            statusImage.setImageResource(R.drawable.happy_emoji);

        }
        closeDialogIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();
                submitButtonVocabulary.setText(getResources().getString(R.string.try_aging));
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                resultDialog.dismiss();
                isSubmitted=false;
                submitButton.setText(getResources().getString(R.string.submit));
                displayWritingText = new DisplayWritingText(text2, textView_writingText, WritingLesson.this, choices,spinner);

                 */
            }
        });


        resultDialog.show();
    }
}