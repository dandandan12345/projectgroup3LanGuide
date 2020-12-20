package com.example.project2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.project2.writing_lesson.DisplayWritingText;
import com.example.project2.writing_lesson.WritingGap;
import com.example.project2.writing_lesson.WritingGapChoices;

import java.util.ArrayList;

public class WritingLesson extends AppCompatActivity {
    String text2;

    String question;
    TextView textView_writingText;
    Spinner spinner;
    TextView title;
    DisplayWritingText displayWritingText;
    TextView writing_lesson_question;
    ArrayList<WritingGap> gaps;
    Dialog resultDialog;
    Boolean isSubmitted;
    Button submitButton;
    WritingGapChoices[] choices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_lesson);
        Intent intent = getIntent();
        // get text from string file
        text2 = getResources().getString(R.string.writing_text) ;
        question=getResources().getString(R.string.writing_question_text);
        title = findViewById(R.id.writing_lesson_title);
        submitButton = findViewById(R.id.writingSubmitButton);
        title.setText( intent.getStringExtra("Lesson"));
        textView_writingText = findViewById(R.id.textView_writingText);
        isSubmitted=false;
        choices = new WritingGapChoices[] {
                new WritingGapChoices("AAA", "BBB", "CCCCC", "AAA", 1),
                new WritingGapChoices("DDD", "EEEE", "FF", "FF", 2),
                new WritingGapChoices("GGGG", "HHHHHH", "IIIIIIIII", "GGGG", 3),
        };
        writing_lesson_question =findViewById(R.id.writing_lesson_question);
        writing_lesson_question.setText(question);
        spinner = findViewById(R.id.spinner);
        displayWritingText = new DisplayWritingText(text2, textView_writingText, this, choices,spinner);
        initializeResultDialog();
    }

    // clicking submit button
    public void submitTest(View view) {
        int totalQuestions=0;
        int correctAnswers=0;

        if(!isSubmitted){
            String text=String.valueOf(displayWritingText.getSpannableStringBuilder());
            SpannableString spannableString = new SpannableString(text);
            gaps=displayWritingText.getGaps();
            // check every gap if the answer is right make it green and if false make it red
            for(WritingGap gap:gaps){
                totalQuestions++;
                if (gap.getSelectedChoice().trim().equals(gap.choices.getCorrect().trim())){
                    spannableString.setSpan(new ForegroundColorSpan(Color.GREEN),
                            gap.getStartIndex(),gap.getStartIndex()+gap.getSelectedChoice().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE  );
                    correctAnswers++;
                }else{
                    spannableString.setSpan(new ForegroundColorSpan(Color.RED), gap.getStartIndex(),
                            gap.getStartIndex()+gap.getSelectedChoice().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE  );
                }
            }

            // put colored text in textview
            textView_writingText.setText(spannableString);
            isSubmitted=true;
            showResultDialog(correctAnswers,totalQuestions);

        }else {
            isSubmitted=false;
            submitButton.setText(getResources().getString(R.string.submit));
            displayWritingText = new DisplayWritingText(text2, textView_writingText, WritingLesson.this, choices,spinner);
        }
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
                submitButton.setText(getResources().getString(R.string.try_aging));
            }
        });
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultDialog.dismiss();
                isSubmitted=false;
                submitButton.setText(getResources().getString(R.string.submit));
                displayWritingText = new DisplayWritingText(text2, textView_writingText, WritingLesson.this, choices,spinner);
            }
        });
        resultDialog.show();
    }

    // show a dialog to the user if he press back button without submitting the test
    // to confirm that he want to exit
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
}