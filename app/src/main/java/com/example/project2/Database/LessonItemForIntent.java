package com.example.project2.Database;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.project2.lessonsPackage.LessonItem;

public class LessonItemForIntent extends LessonItem implements Parcelable {
    String level;
    String skill ;
    public LessonItemForIntent(String lesson, String lessonNumberString, int lessonNumber, String lessonRating, String skill, String level) {
        super(lesson, lessonNumberString, lessonNumber, lessonRating);
        this.level=level;
        this.skill = skill;
    }

    protected LessonItemForIntent(Parcel in) {
        super();

        level = in.readString();
        skill = in.readString();
        super.lesson = in.readString();
        super.lessonNumber = in.readInt();
        super.lessonNumberString=in.readString();

    }

    public static final Creator<LessonItemForIntent> CREATOR = new Creator<LessonItemForIntent>() {
        @Override
        public LessonItemForIntent createFromParcel(Parcel in) {
            return new LessonItemForIntent(in);
        }

        @Override
        public LessonItemForIntent[] newArray(int size) {
            return new LessonItemForIntent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(level);
        dest.writeString(skill);
        dest.writeString(lesson);
        dest.writeInt(lessonNumber);
        dest.writeString(lessonNumberString);
    }
}
