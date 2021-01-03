package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.Database.LessonItemForIntent;
import com.example.project2.Database.LessonsGrade;
import com.example.project2.Database.WriteGradesToDatabase;
import com.example.project2.writing_lesson.DisplayWritingText;
import com.google.firebase.auth.FirebaseAuth;

public class activity_lessons_reading extends AppCompatActivity {
    RadioGroup radioGroup1;
    TextView title, lessonText, readingDescription,  questionTitles, answered;
    String fullText, fullText2;
    RadioButton alt1, alt2, alt3;
    Button next, back;

    //Connection to database and result
    Dialog resultDialog;
    Boolean isSubmitted;
    WriteGradesToDatabase writeGradesToDatabase;
    LessonItemForIntent lessonItemForIntent;


    String questions[] = {
            "1 - What is the main goal?",
            "2 - What is the subject?",
            "3 - What did you get?",
            "4 - Your name?"
    };

    String answers[] = {"Option 1","Alt 2","Choice 3", "Javad"};

    String opt[] = {
            "Option 1","Option 2","Option 3",
            "Alt 1","Alt 2","Alt 3",
            "Choice 1","Choice 2","Choice 3",
            "Javad","Daniel","Elmi"
    };

    int flag=0, number=1, total=0, correct=0, marks=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRightTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_reading);

        //Connection to database and result
        Intent intent = getIntent();
        lessonItemForIntent = intent.getParcelableExtra("lessonObject");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        writeGradesToDatabase = new WriteGradesToDatabase(lessonItemForIntent, userId);
        title=(TextView) findViewById(R.id.lesson_number);
        title.setText( getString(R.string.lesson, lessonItemForIntent.getLessonNumber()));;
        lessonText=(TextView) findViewById(R.id.lessonText);
        fullText=getResources().getString(R.string.readingText);
        lessonText.setText(fullText);
        isSubmitted=false;


        //Show description text
        readingDescription=(TextView) findViewById(R.id.readingDescription);
        fullText2=getResources().getString(R.string.readingDes);
        readingDescription.setText(fullText2);

        //Questions
        next=(Button)findViewById(R.id.next);
        back=(Button)findViewById(R.id.back);
        answered=(TextView) findViewById(R.id.answerdQuestions);
        radioGroup1=(RadioGroup)findViewById(R.id.radiogroup1);
        questionTitles=(TextView) findViewById(R.id.questionTitle);
        alt1=(RadioButton)findViewById(R.id.alt1);
        alt2=(RadioButton)findViewById(R.id.alt2);
        alt3=(RadioButton)findViewById(R.id.alt3);
        questionTitles.setText(questions[flag]);
        totalQuestions();
        alt1.setText(opt[0]);
        alt2.setText(opt[1]);
        alt3.setText(opt[2]);
        initializeResultDialog();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(radioGroup1.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(total>number)
                {
                    number++;
                }

                answered.setText((number) + "/" + total);
                RadioButton getValue = (RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId());
                String valueText = getValue.getText().toString();

                if(valueText.equals(answers[flag])) {
                    correct++;
                    //Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                }

                flag++;

                if(flag<questions.length)
                {
                    questionTitles.setText(questions[flag]);
                    alt1.setText(opt[flag*3]);
                    alt2.setText(opt[flag*3+1]);
                    alt3.setText(opt[flag*3+2]);
                }

                int result=number/total;

                if(result==1) {
                    next.setText("Submit");
                }

                else
                {
                    marks=correct;
                }

                if(flag>=total)
                {
                    showResultDialog(correct, total);
                }

                radioGroup1.clearCheck();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag--;
                number--;
                correct--;

                if(flag==0)
                {
                    questionTitles.setText(questions[flag]);
                    alt1.setText(opt[0]);
                    alt2.setText(opt[1]);
                    alt3.setText(opt[2]);
                }

                else if(flag<0)
                {
                    Toast.makeText(getApplicationContext(), "You can not back!",Toast.LENGTH_SHORT).show();
                    flag=0;
                    correct=0;
                }

                else if(flag<questions.length)
                {
                    questionTitles.setText(questions[flag]);
                    alt1.setText(opt[flag*3]);
                    alt2.setText(opt[flag*3+1]);
                    alt3.setText(opt[flag*3+2]);
                    next.setText("Next");

                }


                if(number<=1)
                {
                    number=1;
                }

                answered.setText(number + "/" + total);


            }
        });



    }

    public int totalQuestions()
    {
        return total=questions.length;
    }

    private void initializeResultDialog()
    {
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

        /*
         * to write grades to database
         * first create new object of LessonsGrade
         * then call WriteGrade function in writeGradesToDatabase class, pass LessonsGrade object to it
         *  */

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
                next.setText(getResources().getString(R.string.try_aging));
            }
        });
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();
                isSubmitted=false;
                next.setText(getResources().getString(R.string.submit));

                //Restart the activity
                finish();
                startActivity(getIntent());
            }
        });


        resultDialog.show();
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