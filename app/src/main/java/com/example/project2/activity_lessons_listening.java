package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.Database.LessonItemForIntent;
import com.example.project2.Database.LessonsGrade;
import com.example.project2.Database.WriteGradesToDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.logging.LogRecord;

public class activity_lessons_listening extends AppCompatActivity {
    //Timer variables
    final long COUNT_DOWN_IN_MILLIS=30000;
    CountDownTimer countDownTimer;
    long timeLeftInMillis;

    TextView elapsedTime, remainingTime,title, listeningDescription, questionTitles, answered, countDownText;
    String fullText;

    //Button playBtn;
     Button next, back;
    SeekBar positionBar;
    MediaPlayer mp;
    int totalTime;
    RadioGroup radioGroup1;
    RadioButton alt1, alt2, alt3;

    //Connection to database and result
    Dialog resultDialog;
    Boolean isSubmitted;
    WriteGradesToDatabase writeGradesToDatabase;
    LessonItemForIntent lessonItemForIntent;

    int total=0, count=0, flag=0, number=1, correct=0, marks=0;

    String questions[] = {
            "1 - What is the main topic of the email?",
            "2 - What is true about the Erasmus agreement in question?",
            "3 - What does the author of the email need from the recipient?",
            "4 - It is certain that the agreement is valid through the year ..."
    };

    String answers[] = {"Renewing an existing agreement","It was signed by two people","a signature", "2020"};

    String opt[] = {
            "Renewing an existing agreement","Writing a new document","Search for an institution to cooperate with",
            "It was signed by a person and a university","It was signed by two universities","It was signed by two people",
            "a signature","a signed document","a written statement",
            "2019","2020","2021"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setRightTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_listening);

        //Connection to database and result
        Intent intent = getIntent();
        lessonItemForIntent = intent.getParcelableExtra("lessonObject");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        writeGradesToDatabase = new WriteGradesToDatabase(lessonItemForIntent, userId);
        title=(TextView) findViewById(R.id.lesson_listeningTitle);

        title.setText( getString(R.string.lesson, lessonItemForIntent.getLessonNumber()));
        fullText=getResources().getString(R.string.readingText);
        isSubmitted=false;

        initializeResultDialog();


        //Create timer
        timeLeftInMillis=COUNT_DOWN_IN_MILLIS;
        countDownText=(TextView) findViewById(R.id.countDownText);
        startCountDown();

        //Assign variables
        elapsedTime=(TextView) findViewById(R.id.elapsedTime);
        remainingTime=(TextView) findViewById(R.id.remainingTime);
        //playBtn=(Button) findViewById(R.id.playBtn);
        positionBar=(SeekBar) findViewById(R.id.positionBar);
        listeningDescription=(TextView) findViewById(R.id.listeningDescription);

        //Show text in Textviews
        fullText=getResources().getString(R.string.listeningText);
        listeningDescription.setText(fullText);



        //Audio player
        mp=MediaPlayer.create(this,R.raw.music);
        mp.setLooping(false);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime=mp.getDuration();
        positionBar.setMax(totalTime);

        //PositionBar listener
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Thread update positionBar & timelabel
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp != null){
                    try {
                        Message msg= new Message();
                        msg.what=mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {}
                }
            }
        }).start();


        //Questions part
        next=(Button)findViewById(R.id.next);
        back=(Button) findViewById(R.id.back);
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


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int result=number/total;
                RadioButton getValue = (RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId());
                String valueText;

                if(radioGroup1.getCheckedRadioButtonId()==-1)
                {
                    valueText ="Wrong";
                    //return;
                }

                else
                {
                    valueText = getValue.getText().toString();
                }

                if(total>number)
                {
                    number++;
                }

                answered.setText((number) + "/" + total);

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


                if(result==1) {
                    next.setText("Submit");
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

                if(correct>0)
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



    private Handler handler= new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            int currentPosition=msg.what;
            //update positionBar
            positionBar.setProgress(currentPosition);
            //update labels
            String elpTime=createTimeLabel(currentPosition);
            elapsedTime.setText(elpTime);
            String remTime=createTimeLabel(totalTime-currentPosition);
            remainingTime.setText("-" + remTime);

        }
    };

    public String createTimeLabel(int time)
    {
        String timelabel="";
        int min=time/1000/60;
        int sec=time/1000%60;

        timelabel=min+":";
        if(sec<10) timelabel +="0";
        timelabel+=sec;

        return timelabel;
    }

    public void playAudioClick(View v)
    {

        if(!mp.isPlaying())
        {
            //Pause
            mp.start();
            //playBtn.setBackgroundResource(R.drawable.stop);
        }
        else {

            //playing
            mp.pause();
            //playBtn.setBackgroundResource(R.drawable.play);

        }

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
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(getIntent());
                    }
                });
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

    private void startCountDown()
    {
        countDownTimer= new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis=millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                playTwice();
            }
        }.start();
    }

    public void updateCountDownText()
    {
        int minute=(int)(timeLeftInMillis/1000)/60;
        int second=(int)(timeLeftInMillis/1000)%60;
        String timeFormated=String.format(Locale.getDefault(), "%02d:%02d", minute, second);
        countDownText.setText(timeFormated);
    }

    //Play audio twice
    public void playTwice() {

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (count < 1) {
                    mp.start();
                    count++;
                }
            }
        });
        mp.start();
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

    //When the user press back button on his phone
    @Override
    public void onBackPressed(){
        mp.stop();

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


    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(timeLeftInMillis==0)
            mp.start();
    }



}
