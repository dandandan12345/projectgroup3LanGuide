package com.example.project2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.R;

public class activity_lessons_listening extends AppCompatActivity {
    TextView elapsedTime, remainingTime,title, listeningDescription;
    String fullText;
    Button playBtn;
    SeekBar positionBar;
    MediaPlayer mp;
    int totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_listening);

        //Assign variables
        elapsedTime=(TextView) findViewById(R.id.elapsedTime);
        remainingTime=(TextView) findViewById(R.id.remainingTime);
        playBtn=(Button) findViewById(R.id.playBtn);
        positionBar=(SeekBar) findViewById(R.id.positionBar);
        title=(TextView) findViewById(R.id.lesson_listeningTitle);
        listeningDescription=(TextView) findViewById(R.id.listeningDescription);

        //Show text in Textviews
        fullText=getResources().getString(R.string.listeningText);
        listeningDescription.setText(fullText);

        //Show right title for lessons
        Intent intent=getIntent();
        title.setText(intent.getStringExtra("Lesson"));

        //Audio player
        mp=MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
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
            //Det är pause
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        }
        else {

            //playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);


        }

    }
}