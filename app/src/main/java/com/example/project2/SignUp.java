package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeText();

        Spinner spinner=(Spinner)findViewById(R.id.signUpSpinner);


        String[] spinnerValues = getResources().getStringArray(R.array.studentOrTeacherEnglish);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, spinnerValues);

        spinner.setAdapter(adapter);

    }
    void initializeText()
    {
        TextView fullNameField = findViewById(R.id.signUpFullName);
        TextView emailField = findViewById(R.id.signUpEmail);
        TextView countryField = findViewById(R.id.signUpCountry);
        TextView passwordField = findViewById(R.id.signUpPassword);
        TextView confirmPasswordField = findViewById(R.id.signUpConfirmPassword);
        TextView lable = findViewById(R.id.signUpChooseARoleLable);
        Button button = findViewById(R.id.signUpButton);
        TextView termsOfUseText = findViewById(R.id.signUpTermsOfUseLabel);

        fullNameField.setHint(R.string.signUpfullNameHintEnglish);
        emailField.setHint(R.string.signUpEmailHintEnglish);
        countryField.setHint(R.string.signUpCountryHintEnglish);
        passwordField.setHint(R.string.signUpPasswordHintEnglish);
        confirmPasswordField.setHint(R.string.signUpConfirmPasswordHintEnglish);
        lable.setText(R.string.signUpChooseRoleLable);
        button.setText(R.string.signUpButtonTextEnglish);
        termsOfUseText.setText(R.string.TermsOfUseTextEnglish);
    }
}
