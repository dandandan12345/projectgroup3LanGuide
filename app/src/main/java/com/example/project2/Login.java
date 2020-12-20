package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeText();

        SpannableString redString = new SpannableString(getResources().getString(R.string.loginSignUpTextEnglish));
        int indexOfQuestionMark = getResources().getString(R.string.loginSignUpTextEnglish).indexOf("?");
        int lastIndexOfString = getResources().getString(R.string.loginSignUpTextEnglish).length();

        ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
        StyleSpan bold = new StyleSpan(Typeface.BOLD);
        loginBtn = findViewById(R.id.LogInButton);
        redString.setSpan(bold,indexOfQuestionMark+1,lastIndexOfString,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        redString.setSpan(red,indexOfQuestionMark+1,lastIndexOfString, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = findViewById(R.id.signUpText);
        textView.setText(redString);


        findViewById(R.id.signUpText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
    void initializeText()
    {
        TextView emailField = findViewById(R.id.loginEmail);
        EditText passwordField = findViewById(R.id.loginPassword);
        Button loginButton = findViewById(R.id.LogInButton);

        emailField.setHint(R.string.loginEmailHintEnglish);
        passwordField.setHint(R.string.loginPasswordHintEnglish);
        loginButton.setHint(R.string.loginButtonText);
    }


    public void LogIn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}