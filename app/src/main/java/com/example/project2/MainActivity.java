package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    NavigationView mNavigationView;
    Dialog popupDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hom_page);
        Toolbar mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         popupDialog = new Dialog(this);

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
            case R.id.profile_nav:
                item.setChecked(false);
                Intent intent = new Intent(this, Profile.class);
                startActivity(intent);
                break;
            case R.id.settings_nav:

                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    /*
    * when the drawer is open and the Back button is pressed,
    * only close the drawer instead of the current home activity.
    * Then, if the user presses the Back button again, the home activity should be closed.*/
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    public void chooseLevel(View view) {
        Skill clickedActivity;

//
//       chooseLevelDialog dialog = new  chooseLevelDialog(MainActivity.this);
       int activity = view.getId();
       switch (activity){
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
       }
       if(clickedActivity !=null){
           chooseLevelDialog dialog = new chooseLevelDialog(this, clickedActivity);
           dialog.show();
       }
/*
        popupDialog.setContentView(R.layout.level_popup);
        //Set the background of the dialog's root view to transparent, to  hide the corners in custom layout.
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancelBtn;
        cancelBtn= popupDialog.findViewById(R.id.popupCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });
        if(skill ==R.id.readingBtn){

        }else if (skill ==R.id.writingBtn)
        {

        }
        popupDialog.show();*/

//        Toast.makeText(MainActivity.this, "dfdfdf", Toast.LENGTH_LONG);
//        dialog.show();
//        Log.d("hhh", "lll");
//        String level = dialog.getLevel();

    }

}