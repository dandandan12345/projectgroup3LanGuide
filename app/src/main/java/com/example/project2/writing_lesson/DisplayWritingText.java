package com.example.project2.writing_lesson;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project2.R;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DisplayWritingText {
    int checkSpinnerCall;
    Context context;
    ArrayList<WritingGap> gaps = new ArrayList<>();
    SpannableStringBuilder spannableStringBuilder;
    TextView textView;
    WritingGap currentGap;
    Spinner spinner;
    String text;
    WritingGapChoices [] choices  ;
    String [] arrayString;
    public DisplayWritingText(String text, TextView textView, Context context, WritingGapChoices [] choices, Spinner spinner)
    {

        this.spinner=spinner;
        this.choices=choices;
        this.text =text;
        this.context= context;
        spannableStringBuilder = new SpannableStringBuilder(text);
        this.textView=textView;
        initItems();
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

    }
    private void   initItems(){
        int x=1;

        // find all gaps in text and create WritingGap object for each
        for (int i =-1;(i=text.indexOf("___"+x+"___", i+1))!=-1;i++ ){
            WritingGap newItem=new WritingGap(choices[x-1]);
            newItem.gapNumber=x;
            newItem.startIndex=i;
            newItem.setSelectedChoice("___"+x+"___");
            newItem.choices=choices[x-1];
            newItem.selectedChoiceNumber=-1;
            gaps.add(newItem);
            spannableStringBuilder.setSpan(new myClickableSpan(x), i,
                    i+7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            x++;
        }
    }
    public  class  myClickableSpan extends ClickableSpan {
        int position;
        public myClickableSpan(int position) {
            this.position=position;
        }
        @Override
        public void onClick(@NonNull View widget) {
            currentGap=gaps.get(position-1);

            TextView parentTextView = (TextView) widget;
            Rect parentTextViewRect = new Rect();
            // Initialize values for the computing of clickedText position
            SpannableString completeText = (SpannableString)(parentTextView).getText();
            Layout textViewLayout = parentTextView.getLayout();

            double startOffsetOfClickedText = completeText.getSpanStart(this);
            double endOffsetOfClickedText = completeText.getSpanEnd(this);
            double startXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int)startOffsetOfClickedText);
            double endXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int)endOffsetOfClickedText);

            // Get the rectangle of the clicked text
            int currentLineStartOffset = textViewLayout.getLineForOffset((int)startOffsetOfClickedText);
            int currentLineEndOffset = textViewLayout.getLineForOffset((int)endOffsetOfClickedText);
            boolean keywordIsInMultiLine = currentLineStartOffset != currentLineEndOffset;
            textViewLayout.getLineBounds(currentLineStartOffset, parentTextViewRect);


            // Update the rectangle position to his real position on screen
            int[] parentTextViewLocation = {0,0};
            parentTextView.getLocationOnScreen(parentTextViewLocation);

            double parentTextViewTopAndBottomOffset = (
                    parentTextViewLocation[1] -
                            parentTextView.getScrollY() +
                            parentTextView.getCompoundPaddingTop()
            );
            parentTextViewRect.top += parentTextViewTopAndBottomOffset;
            parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;

            parentTextViewRect.left += (
                    parentTextViewLocation[0] +
                            startXCoordinatesOfClickedText +
                            parentTextView.getCompoundPaddingLeft() -
                            parentTextView.getScrollX()
            );
            parentTextViewRect.right = (int) (
                    parentTextViewRect.left +
                            endXCoordinatesOfClickedText -
                            startXCoordinatesOfClickedText
            );
            int xCooredinate = (parentTextViewRect.left + parentTextViewRect.right) / 2;
            int yCooredinate = parentTextViewRect.bottom;
            if (keywordIsInMultiLine) {
                xCooredinate = parentTextViewRect.left;
            }

            // show gap number in dropdown list if it is first to the user click on the gap
            if(currentGap.selectedChoiceNumber==-1){
                arrayString = new String[]{
                        currentGap.selectedChoice, currentGap.choices.first, currentGap.choices.second, currentGap.choices.third
                };
            }
            else {
                arrayString = new String[]{
                        currentGap.choices.first, currentGap.choices.second, currentGap.choices.third
                };
            }

            //
            ArrayAdapter<String> adapter = new ArrayAdapter(context,
                    R.layout.support_simple_spinner_dropdown_item, arrayString);

           adapter.setDropDownViewResource(R.layout.writing_gap_dropdown);
            spinner.setAdapter(adapter);
            spinner.setX(xCooredinate);
            spinner.setY(yCooredinate);

            // to avoid out of index rang exception
            if(currentGap.selectedChoiceNumber>arrayString.length-1)
                currentGap.selectedChoiceNumber-=1;
            spinner.setSelection(currentGap.getSelectedChoiceNumber());
            checkSpinnerCall=0;
            spinner.performClick();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(checkSpinnerCall>0){
                        spannableStringBuilder.clear();
                        textView.setText("");
                        int oldLength = currentGap.getSelectedChoice().length();

                        // set clicked gap choice number to the selected item from dropdown
                        currentGap.selectedChoiceNumber=position;

                        String clickedText =" "+arrayString[position]+" ";

                        // set text in clicked gap to selected item
                        currentGap.setSelectedChoice(clickedText);
                        // find difference between old end new length
                        int changeDiff =  currentGap.selectedChoice.length()-oldLength;

                        // change gap text in textview to selected text
                        String firstPart = text.substring(0,currentGap.startIndex)+currentGap.getSelectedChoice();
                        String secondPart = text.substring(currentGap.startIndex + oldLength);
                        spannableStringBuilder.append(firstPart);
                        spannableStringBuilder.append(secondPart);
                        text=firstPart + secondPart;

                        // change start index for all gaps which are after clicked gap
                        for (WritingGap gap:gaps) {
                            if (gap.gapNumber!=currentGap.gapNumber){
                                if(gap.gapNumber>currentGap.gapNumber)
                                    gap.startIndex +=changeDiff;
                            }
                        }

                        // set span for all gap
                        for (WritingGap gap:gaps) {
                            spannableStringBuilder.setSpan(new myClickableSpan(gap.gapNumber), gap.startIndex,
                                    gap.startIndex+gap.selectedChoice.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
                        }
                        textView.setText(spannableStringBuilder);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                        spinner.setVisibility(View.INVISIBLE);
                    }
                    checkSpinnerCall++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public ArrayList<WritingGap> getGaps() {
        return gaps;
    }
    public SpannableStringBuilder getSpannableStringBuilder() {
        return spannableStringBuilder;
    }
}
