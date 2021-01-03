package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project2.Database.UserInfo;
import com.example.project2.utils.DatabaseKeys;
import com.example.project2.utils.Skill;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    TextView writing_test_number,writing_questions_number,writing_progress_bar_text, userNameTextView;
    ProgressBar writing_progress_bar;
    DatabaseReference resultRef;
    String userId ;
    ImageView profile_image;
    private static final int PICK_IMAGE_REQ = 1;
    Uri imageUri;
    StorageReference storageRef;
    UserInfo user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRightTheme();
        super.onCreate(savedInstanceState);

        // get the intent that contain a user object with user information
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        setContentView(R.layout.activity_profile);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        resultRef = FirebaseDatabase.getInstance().getReference().child(DatabaseKeys.GRADES_KEY).child(userId)
                .child(Skill.Writing.toString()).child(DatabaseKeys.RESULT_KEY);
        storageRef = FirebaseStorage.getInstance().getReference().child(DatabaseKeys.PROFILE_IMAGES);
        initializeViews();
        userNameTextView.setText(user.getFullName());
        readDataFromDatabase();

    }
    void initializeViews(){
        writing_test_number = findViewById(R.id.writing_test_number);
        writing_questions_number = findViewById(R.id.writing_questions_number);
        writing_progress_bar_text = findViewById(R.id.writing_progress_bar_text);
        writing_progress_bar = findViewById(R.id.writing_progress_bar);
        userNameTextView=  findViewById(R.id.user_name);
        profile_image = findViewById(R.id.profile_image);

        // set event listener for image to change profile image
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMAGE_REQ);
            }
        });
    }

    private void readDataFromDatabase() {

        // read result form database
        resultRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                   String numTest = snapshot.child(DatabaseKeys.NUMBER_OF_TEST).getValue().toString();
                    String numQues = snapshot.child(DatabaseKeys.TOTAL_NUMBER_OF_QUESTIONS).getValue().toString();
                    int percent = Integer.parseInt(snapshot.child(DatabaseKeys.CORRECT_PERCENT).getValue().toString());
                   writing_test_number.setText(numTest);
                   writing_questions_number.setText(numQues);
                   writing_progress_bar.setProgress(percent);
                   writing_progress_bar_text.setText(percent+getResources().getString(R.string.percent));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // download profile from storage
         storageRef.child(userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profile_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    // upload profile image to storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQ && resultCode == RESULT_OK){
            imageUri = data.getData();
            StorageReference filePath = storageRef.child(userId);
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                // if the image uploads successfully
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        // display image in profile
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().centerCrop().into(profile_image);
                            // every time the user change image we change the value in hasImage which will invoke event in home page to change image in drawerNavigation
                             FirebaseDatabase.getInstance().getReference().child(DatabaseKeys.USERS).child(userId).child(DatabaseKeys.HAS_IMAGE).setValue(false);
                            FirebaseDatabase.getInstance().getReference().child(DatabaseKeys.USERS).child(userId).child(DatabaseKeys.HAS_IMAGE).setValue(true);
                        }
                    });
                }
            });
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