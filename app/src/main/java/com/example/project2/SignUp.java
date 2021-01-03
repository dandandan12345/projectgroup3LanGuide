package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.project2.Database.UserInfo;
import com.example.project2.utils.DatabaseKeys;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button signUpButton;
    TextView fullNameField,emailField,countryField,passwordField,confirmPasswordField;
    Spinner roleSpinner ;
    DatabaseReference usersDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRightTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeText();
        Spinner spinner=(Spinner)findViewById(R.id.signUpSpinner);
        mAuth = FirebaseAuth.getInstance();
        usersDatabase = FirebaseDatabase.getInstance().getReference(DatabaseKeys.USERS);

        String[] spinnerValues = getResources().getStringArray(R.array.studentOrTeacherEnglish);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, spinnerValues);

        spinner.setAdapter(adapter);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUpNewUser();
            }
        });

    }

    private void singUpNewUser() {
        String fullName = fullNameField.getText().toString().trim();
        String email=emailField.getText().toString().trim();
        String  password=passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        String country = countryField.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString().trim();

        // check all fields
        if(fullName.isEmpty()){
            fullNameField.setError(getResources().getString(R.string.error_name_required));
            fullNameField.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailField.setError(getResources().getString(R.string.error_email_required));
            emailField.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailField.setError(getResources().getString(R.string.error_invalid_email));
            emailField.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordField.setError(getResources().getString(R.string.error_password_required));
            passwordField.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordField.setError(getResources().getString(R.string.error_short_password));
            passwordField.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            passwordField.setError(getResources().getString(R.string.error_passwords_not_matching));
            passwordField.requestFocus();
            return;
        }
        if(country.isEmpty()){
            countryField.setError(getResources().getString(R.string.error_country_required));
            countryField.requestFocus();
            return;
        }
        progressDialog.setMessage(getResources().getString(R.string.message_signing_up));
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Sign in success,

                    // save user information in database
                    UserInfo userInfo = new UserInfo(fullName, country, email, role);
                    String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    usersDatabase.child(userId).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                // if user information is saved in database
                                Intent loginIntent = new Intent(SignUp.this, Login.class);
                                loginIntent.putExtra("newUser", true);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginIntent);
                            }else {
                                // if fails to save user information in database
                            }
                        }
                    });
                }else {
                    // If signing in fails
                }
                progressDialog.dismiss();
            }
        });
    }
    void initializeText()
    {
         fullNameField = findViewById(R.id.signUpFullName);
         emailField = findViewById(R.id.signUpEmail);
         countryField = findViewById(R.id.signUpCountry);
        passwordField = findViewById(R.id.signUpPassword);
        confirmPasswordField = findViewById(R.id.signUpConfirmPassword);
        TextView lable = findViewById(R.id.signUpChooseARoleLable);
        signUpButton = findViewById(R.id.signUpButton);
        TextView termsOfUseText = findViewById(R.id.signUpTermsOfUseLabel);
        roleSpinner = findViewById(R.id.signUpSpinner);
        fullNameField.setHint(R.string.signUpfullNameHintEnglish);
        emailField.setHint(R.string.signUpEmailHintEnglish);
        countryField.setHint(R.string.signUpCountryHintEnglish);
        passwordField.setHint(R.string.signUpPasswordHintEnglish);
        confirmPasswordField.setHint(R.string.signUpConfirmPasswordHintEnglish);
        lable.setText(R.string.signUpChooseRoleLable);
        signUpButton.setText(R.string.signUpButtonTextEnglish);
        termsOfUseText.setText(R.string.TermsOfUseTextEnglish);
        progressDialog = new ProgressDialog(this);
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
