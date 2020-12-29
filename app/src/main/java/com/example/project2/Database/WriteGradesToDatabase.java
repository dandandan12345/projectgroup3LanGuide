package com.example.project2.Database;

import androidx.annotation.NonNull;

import com.example.project2.utils.DatabaseKeys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WriteGradesToDatabase {
   final private LessonItemForIntent lessonItemForIntent;
    String userId ;
    DatabaseReference lessonDatabase;
    DatabaseReference ratingsDatabase;
    DatabaseReference readResultRef;
    int numberOfTest;
    int totalQuestions;
    int percentOfCorrect;
    boolean isFirstAttempt;
    int currentCorrect;
    public WriteGradesToDatabase(LessonItemForIntent lessonItemForIntent, String userId) {
        this.lessonItemForIntent = lessonItemForIntent;
        this.userId = userId;
        getDatabaseReference();
        checkIfIsFirstAttempt();
        readResult();

    }

    private void getDatabaseReference() {
        lessonDatabase = FirebaseDatabase.getInstance().getReference(DatabaseKeys.GRADES_KEY)
                .child(userId).child(lessonItemForIntent.skill).child(lessonItemForIntent.level)
                .child(lessonItemForIntent.getLesson()+" "+lessonItemForIntent.getLessonNumber());
        ratingsDatabase=FirebaseDatabase.getInstance().getReference(DatabaseKeys.GRADES_KEY)
                .child(userId).child(lessonItemForIntent.skill).child(lessonItemForIntent.level).child(DatabaseKeys.RATINGS_KEY)
                .child(lessonItemForIntent.getLessonNumber()+"");
        readResultRef=FirebaseDatabase.getInstance().getReference(DatabaseKeys.GRADES_KEY)
                .child(userId).child(lessonItemForIntent.skill).child(DatabaseKeys.RESULT_KEY);
    }
    public void WriteGrade(LessonsGrade lessonsGrade){
        lessonDatabase.setValue(lessonsGrade);
        int totalQu = lessonsGrade.getTotalGrade();
        int correctQu = lessonsGrade.getGrade();
        int rate = (int) (correctQu / (totalQu/100.0));
        ratingsDatabase.setValue(rate);
        writeResult(isFirstAttempt, lessonsGrade.totalGrade, lessonsGrade.grade);
        checkIfIsFirstAttempt();

    }

    private void checkIfIsFirstAttempt() {
        DatabaseReference checkAttemptRef= FirebaseDatabase.getInstance().getReference(DatabaseKeys.GRADES_KEY)
                .child(userId).child(lessonItemForIntent.skill).child(lessonItemForIntent.level);
        checkAttemptRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(lessonItemForIntent.getLesson()+" "+lessonItemForIntent.getLessonNumber())){
                    isFirstAttempt =false;
                    currentCorrect = Integer.valueOf(snapshot.child(lessonItemForIntent.getLesson()+" "+lessonItemForIntent.getLessonNumber())
                            .child(DatabaseKeys.LESSON_GRADE_KEY).getValue().toString());

                }else{
                    isFirstAttempt=true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readResult(){
        readResultRef.addListenerForSingleValueEvent  (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //
                if(dataSnapshot.getValue()!=null){


                    numberOfTest =Integer.valueOf( dataSnapshot.child(DatabaseKeys.NUMBER_OF_TEST).getValue().toString());
                    totalQuestions = Integer.valueOf( dataSnapshot.child(DatabaseKeys.TOTAL_NUMBER_OF_QUESTIONS).getValue().toString());
                    percentOfCorrect =  Integer.valueOf( dataSnapshot.child(DatabaseKeys.CORRECT_PERCENT).getValue().toString());


                    //Log.d("numberOfTest", "numberOfTest = "+nn+"");

                }else{

                    readResultRef.child(DatabaseKeys.NUMBER_OF_TEST).setValue(0);
                    readResultRef.child(DatabaseKeys.TOTAL_NUMBER_OF_QUESTIONS).setValue(0);
                    readResultRef.child(DatabaseKeys.CORRECT_PERCENT).setValue(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    private void writeResult(boolean isFirst, int lessonsQuestionsNumber, int numberOfCorrectAnswers) {
        int totalTest;
        int totalQu;
        int totalCorrect;
        int percent;
        if(isFirst){

           totalTest   =numberOfTest+1;
           totalQu = totalQuestions+lessonsQuestionsNumber;

           totalCorrect = (int)Math.round((totalQuestions /100.0)* percentOfCorrect);
            totalCorrect += numberOfCorrectAnswers;
            percent =(int)(totalCorrect/(totalQu/100.0));

            readResultRef.child(DatabaseKeys.NUMBER_OF_TEST).setValue(totalTest);
            readResultRef.child(DatabaseKeys.TOTAL_NUMBER_OF_QUESTIONS).setValue(totalQu);
            readResultRef.child(DatabaseKeys.CORRECT_PERCENT).setValue(percent);

        }else {
            totalQu = totalQuestions;
            totalCorrect = (int)Math.round(((totalQuestions /100.0)* percentOfCorrect));
            totalCorrect = totalCorrect - currentCorrect;
            totalCorrect = totalCorrect +numberOfCorrectAnswers;
            percent =(int)(totalCorrect/(totalQu/100.0));
            readResultRef.child(DatabaseKeys.CORRECT_PERCENT).setValue(percent);
        }

    }
}
