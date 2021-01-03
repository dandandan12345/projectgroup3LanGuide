package com.example.project2.lessonsPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.R;

import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonAdapterViewHolder> {
    ArrayList<com.example.project2.lessonsPackage.LessonItem> mItemList;
    OnLessonListener mOnLessonListener;

    public LessonAdapter(ArrayList<LessonItem> items, OnLessonListener onLessonListener ){
        this.mItemList = items;
        this.mOnLessonListener = onLessonListener;
    }

    @NonNull
    @Override
    public LessonAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lessons_row, parent, false);
        LessonAdapterViewHolder lessonAdapterViewHolder = new LessonAdapterViewHolder(view, mOnLessonListener);
        return lessonAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapterViewHolder holder, int position) {
        LessonItem currentLesson = mItemList.get(position);
        holder.textViewLesson.setText(R.string.lessonForLessons);
        holder.textViewLessonNumber.setText(currentLesson.getLessonNumberString());
        // TODO change rating from percent to star
        float rating =((Float.parseFloat(currentLesson.getLessonRating())*4)/100);
        holder.ratingBar.setRating(rating);
    }
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class LessonAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewLesson;
        public TextView textViewLessonNumber;
        public RatingBar ratingBar;
        OnLessonListener onLessonListener;

        public LessonAdapterViewHolder(@NonNull View itemView,OnLessonListener onLessonListener ) {
            super(itemView);
            textViewLesson = itemView.findViewById(R.id.lessonText);
            textViewLessonNumber = itemView.findViewById(R.id.lessonNumber);
            ratingBar = itemView.findViewById(R.id.lessonRatingBar);
            this.onLessonListener = onLessonListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLessonListener.onLessonClick(getAdapterPosition());
        }
    }

    public interface OnLessonListener{
        void onLessonClick(int position);
    }
}
