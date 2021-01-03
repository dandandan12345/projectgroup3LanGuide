package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.icu.number.IntegerWidth;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.project2.Database.UserInfo;
import com.example.project2.utils.DatabaseKeys;
import com.example.project2.utils.Skill;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    NavigationView mNavigationView;
    Dialog popupDialog;
    UserInfo userInfo;
    DatabaseReference usersDatabaseReference;
    StorageReference  mStorageReference;
    String userId;
    DatabaseReference imageReference;
    ImageView imageInDrawer;
    View navigationHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRightTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hom_page);


        // get userId and references to users
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child(DatabaseKeys.USERS).child(userId);
        imageReference =FirebaseDatabase.getInstance().getReference().child(DatabaseKeys.USERS).child(userId).child(DatabaseKeys.HAS_IMAGE);

        Toolbar mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        navigationHeaderView = mNavigationView.getHeaderView(0);
        imageInDrawer = navigationHeaderView.findViewById(R.id.nav_image);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         popupDialog = new Dialog(this);
         // get a reference to images folder in database
        mStorageReference = FirebaseStorage.getInstance().getReference().child(DatabaseKeys.PROFILE_IMAGES);
        readDataFromDatabase();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //item.setChecked(true);
        switch (item.getItemId()){
            // TODO send userInfo to profile so we don't need to load from database again
            case R.id.profile_nav:
                item.setChecked(false);
                Intent intent = new Intent(this, Profile.class);
                intent.putExtra("user", userInfo);
                startActivity(intent);
                break;
            case R.id.settings_nav:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                finish();

                break;
            case R.id.log_out_nav:
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.help_nav:
                //FirebaseAuth.getInstance().signOut();
                //Intent loginIntent = new Intent(MainActivity.this, Login.class);
                //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Intent helpIntent= new Intent(MainActivity.this,Help.class);
                startActivity(helpIntent);
                finish();
                break;
            case R.id.about_us_nav:
                Intent aboutUsIntent = new Intent(this, aboutUs.class);
                startActivity(aboutUsIntent);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    /*
    * when the drawer is open and the Back button is pressed,
    * only close the drawer instead of the current home activity.
    * Then, if the user presses the Back button again, the home activity should be closed.*/
    // TODO Fix backPressed

    @Override
   public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    public void chooseLevel(View view) {
        Skill clickedActivity;
       int clickedButton = view.getId();
       if(clickedButton == R.id.readingBtn){
           clickedActivity=Skill.Reading;

       }else if(clickedButton == R.id.listeningBtn){
           clickedActivity=Skill.Listening;

       }else if (clickedButton == R.id.writingBtn){
           clickedActivity=Skill.Writing;

       }else if (clickedButton == R.id.vocabularyBtn){
           clickedActivity=Skill.Vocabulary;


       }else {
           clickedActivity=null;

       }
/*       switch (activity){
           case R.id.readingBtn:
               clickedActivity=Skill.Reading;
               break;
           case R.id.listeningBtn:
               clickedActivity=Skill.Listening;
               break;
           case R.id.writingBtn:
               clickedActivity=Skill.Writing;
               break;
           case R.id.vocabularyBtn:
               clickedActivity=Skill.Vocabulary;
               break;
           default:
               clickedActivity=null;
       }*/
       if(clickedActivity != null){
           chooseLevelDialog dialog = new chooseLevelDialog(this, clickedActivity);
           dialog.show();
       }
    }


    void readDataFromDatabase(){
        // get all user information from database
        usersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfo = snapshot.getValue(UserInfo.class);
                setUserNameAndCountryInNavigationView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // this event will run every time the user change profile image
        imageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setImageInDrawerNavigation(snapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setImageInDrawerNavigation(false);
            }
        });
    }

    // TODO
    private void setUserNameAndCountryInNavigationView() {

        TextView userName= navigationHeaderView.findViewById(R.id.nav_user_name);
        TextView country = navigationHeaderView.findViewById(R.id.nav_country_name);
        country.setText(userInfo.getCountry());
        userName.setText(userInfo.getFullName());


    }
    // download profile from storage
    private void setImageInDrawerNavigation(Boolean hasImage) {
        if(hasImage){
            try {
                mStorageReference.child(userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(imageInDrawer);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }catch (Exception e){
                imageInDrawer.setImageResource(R.drawable.ic_profile);
            }
        }
        else
        {
            imageInDrawer.setImageResource(R.drawable.ic_profile);
        }
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