package com.example.project2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.project2.Database.LessonItemForIntent;
import com.example.project2.Database.LessonsGrade;
import com.example.project2.Database.WriteGradesToDatabase;
import com.example.project2.VocabularyPackage.Question;
import com.example.project2.VocabularyPackage.VocabularyLesson;
import com.google.firebase.auth.FirebaseAuth;
import com.example.project2.Database.WriteGradesToDatabase;

import java.util.ArrayList;
import java.util.List;

public class VocabularyTest extends AppCompatActivity {

    EditText invisibleField;
    EditText testText;
    Spinner spinner;
    TextView testInfo;

    List<String> answers = new ArrayList<String>();

    Dialog resultDialog;
    Button submitButtonVocabulary;
    TextView currentEdit;
    LessonItemForIntent lessonItemForIntent;
    WriteGradesToDatabase writeGradesToDatabase;
    boolean isSubmitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRightTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);
        Intent intent = getIntent();
        lessonItemForIntent = intent.getParcelableExtra("lessonObject");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        writeGradesToDatabase = new WriteGradesToDatabase(lessonItemForIntent, userId);
        TextView lessonLabel = findViewById(R.id.lessonLabel);
        lessonLabel.setText( getString(R.string.lesson, lessonItemForIntent.getLessonNumber()));;

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
        isSubmitted=false;

        String field = "Finance";
        String typeOfSkill = "Writing, vocabulary";
        String typeOfTask = "Spelling (of words often used when dealing with a budget)";
        String task = "For each of the yellow underlined words in the text decide whether it is spelled correctly. If it is written correctly, press the word and press correct otherwise, press edit to spell it correctly.";
        String test = "The European Union (EU) has been providing <u>budget</u> support to Cambodia in the education sector " +
                "since 2003 on the basis of sound and comprehensive plans to improve performance in  the  sector  and  to" +
                " gradually  implement  public  finance  management  (PFM)  reforms,  as well as continued improvements in both areas." +
                " Over the same period Cambodia has made steady progress in poverty <u>redduction</u> in the last decade underpinned by high <u>economic</u> growth." +
                " However important challenges remain in the education sector, such as the need to increase enrolment and retention at secondary level," +
                " to improve quality at all levels and to reduce regional and social disparities. Addressing these requires Government to increase its resources" +
                " <u>alocated</u> to the sector. The further scaling up of budget support provided by the EU to the sector, as proposed, building on a recently" +
                " agreed programme, will enhance the support to Government's efforts to reverse the fall in the share of Government <u>reccurent</u> funds" +
                " provided  to  the  Ministry  of  Education,  Youth  and  Sports  (MoEYS)  by  supporting  an increase of Government <u>resources</u> <u>avarded</u>" +
                " to specific interventions aimed at improving key service  delivery  indicators  related  to  access,  equity  and  quality  in  the  sector." +
                "  It  will  also encourage  Government  to  continue  strengthening  its  PFM  systems  and  increase  budget <u>transperrency</u>." +
                " The proposed amount is a top up to a recently signed programme covering the period 2014-2016.  An  Addendum to the ongoing Financing" +
                " <u>Agrement</u> will be signed. This additional amount should cover one additional year and in effect lead to more than a doubling of" +
                " the yearly amount for the period 2014-2016. This increased level will be possibly <u>maintained</u> in 2017 with the additional financing" +
                " <u>foresen</u> under the MIP 2014-2020.                                                             (European Comission, 2014)";


        testText = findViewById(R.id.testText);
        spinner = findViewById(R.id.SpinnerChoices);
        invisibleField = findViewById(R.id.invisibleEditText);
        testInfo=findViewById(R.id.testInformation);
        currentEdit = findViewById(R.id.showCurrentEdit);


        VocabularyLesson vocabularyLesson = new VocabularyLesson(test, testText,invisibleField, this, spinner,testInfo,currentEdit);

        initializeInstructions(field,typeOfSkill,typeOfTask,task);

        TextView currentEdit = findViewById(R.id.showCurrentEdit);




        invisibleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(vocabularyLesson.focusChanged == true)
                {
                    vocabularyLesson.focusChanged = false;
                    // previousSLength = 0;
                }
                else
                {

                    if(s.length() <= vocabularyLesson.charsOfCurrentWord)
                    {
                        EditText tt = findViewById(R.id.testText);
                        Editable text = tt.getText();
                        int start = vocabularyLesson.startIndex; //-vocabularyLesson.charsOfCurrentWord;
                        // int end = vocabularyLesson.startIndex-vocabularyLesson.charsOfCurrentWord+vocabularyLesson.indexChange;
                        int spaces = vocabularyLesson.charsOfCurrentWord - s.length();
                        String input = addspace(spaces,s.toString());
                        text.replace(start,start+vocabularyLesson.charsOfCurrentWord,input);
                    }
                    else{                                                       //om man skriver fler bokstäver än tillåtet tas den sista bokstaven på ordet bort från invisibleField
                        String temp = s.toString();
                        temp = temp.substring(0, temp.length() - 1);
                        s.clear();
                        s.insert(0,temp);
                    }
                }
            }
        });


        submitButtonVocabulary = findViewById(R.id.submitButtonVocabulary);
        submitButtonVocabulary.setOnClickListener(v -> {
            if(submitButtonVocabulary.getText().toString().equals(getResources().getString(R.string.try_aging)))
            {
                isSubmitted=false;
                finish();
                startActivity(getIntent());
            }
            else
            {
                int answerCount = 0;
                for(Question question: vocabularyLesson.placeholders)
                {
                    if(question.answered == true){
                        answerCount++;
                    }
                }
                if(answerCount == answers.size()){
                    int i = 0;
                    int start = 0;
                    int end = 0;
                    int rightAnswers = 0;
                    String finishedTest = testText.getText().toString();
                    SpannableString colorString = new SpannableString(finishedTest);
                    for (String answer:answers){
                        String hello = vocabularyLesson.placeholders.get(i).placeHolder;
                        start = vocabularyLesson.placeholders.get(i).index;
                        end = start + vocabularyLesson.placeholders.get(i).placeHolder.length();
                        if(finishedTest.contains(answer)){
                            rightAnswers++;
                            colorString.setSpan(new ForegroundColorSpan(Color.GREEN),start,end,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                        else{
                            colorString.setSpan(new ForegroundColorSpan(Color.RED),start,end,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                        i++;
                    }
                    testText.setText(colorString);
                    isSubmitted=true;
                    showResultDialog(rightAnswers,answers.size());
                }
                else{
                    Toast.makeText(this,"You have not answered all the questions",Toast.LENGTH_LONG).show();
                }
            }
        });

        initializeResultDialog();
    }


    void initializeInstructions(String field, String typeOfSkill,String typeOfTask,String task) {
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

        LessonsGrade newGrade = new LessonsGrade(lessonItemForIntent.getLessonNumber(), totalQuestionsNumber,correctAnswersNumber );
        writeGradesToDatabase. readResult();
        writeGradesToDatabase.WriteGrade(newGrade);

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
                isSubmitted=false;
                finish();
                startActivity(getIntent());
                /*
                resultDialog.dismiss();
                isSubmitted=false;
                submitButtonVocabulary.setText(getResources().getString(R.string.submit));
                vocabularyLesson = new DisplayWritingText(text2, textView_writingText, WritingLesson.this, choices,spinner);

                 */
            }
        });


        resultDialog.show();
    }

    String addspace(int i, String str)
    {
        StringBuilder str1 = new StringBuilder();
        str1.append(str);
        for(int j=0;j<i;j++)
        {
            str1.append(" ");
        }
        return str1.toString();

    }
    @Override
    public void onBackPressed() {
        if(!isSubmitted){
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
            builder.setCancelable(false);
            builder.setMessage(getResources().getString(R.string.exit_without_submitting_message));
            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    finish();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.No),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog alert=builder.create();
            alert.show();
        }
        else {
            super.onBackPressed();
        }
    }
    private void setRightTheme(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLight = sharedPreferences.getBoolean(getString(R.string.theme_key), true);
        if(isLight){
            setTheme(R.style.lightMode);
        }else {
            setTheme(R.style.darkMode);
        }
    }
}