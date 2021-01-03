package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Login extends AppCompatActivity {

    Button loginBtn;
    private FirebaseAuth mAuth;
    TextView emailField, passwordField;
    boolean isNewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setRightTheme();
        String language = sharedPreferences.getString(getString(R.string.language_key), getString(R.string.pref_language_value_english));

        setLocal(language);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        isNewUser = intent.getBooleanExtra("newUser", false);
        initializeText();
        mAuth = FirebaseAuth.getInstance();

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
              //  startSigningIn();
                Intent intent = new Intent(Login.this, SignUp.class);
              startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSigningIn();
            }
        });
    }



    void initializeText()
    {
         emailField = findViewById(R.id.loginEmail);
         passwordField = findViewById(R.id.loginPassword);
        Button loginButton = findViewById(R.id.LogInButton);

        emailField.setHint(R.string.loginEmailHintEnglish);
        passwordField.setHint(R.string.loginPasswordHintEnglish);
        loginButton.setHint(R.string.loginButtonText);
    }


    public void LogIn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if the user is already logged in
        if(!isNewUser){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }

    // if the user is logged in go to home page direct
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            Intent mainPageIntent = new Intent(Login.this, MainActivity.class);
            mainPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainPageIntent);
        }
    }
    private void startSigningIn() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if(email.isEmpty()|| email.equals(null)){
            emailField.setError(getString(R.string.error_email_required));
            emailField.requestFocus();
            return;
        }
        if(password.isEmpty() || password.equals(null)){
            passwordField.setError(getString(R.string.error_password_required));
            passwordField.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainPageIntent = new Intent(Login.this, MainActivity.class);
                    mainPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainPageIntent);
                    finish();
                }else {
                    // TODO when email or password is invalid
                }
            }
        });
    }

    private void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration con = new Configuration();
        con.locale = locale;
        getBaseContext().getResources().updateConfiguration(con, getBaseContext().getResources().getDisplayMetrics());
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