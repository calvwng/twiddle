package com.cpe409.twiddle.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * User: Bazlur Rahman Rokon
 * Date: 5/12/13 - 3:15 PM
 */
public class ExpandableTextView extends TextView implements View.OnClickListener {
  private final String TAG = ExpandableTextView.class.getSimpleName();
  private boolean isClicked = false;
  private int trimLength = 200;
  private String text = "This is a placeholder description for an Adventure. \n" +
          "It has a trim length of " + trimLength + " characters. \n" +
          "TwiddleTwiddleTwiddleTwiddleTwiddleTwiddleTwiddleTwiddle" +
          "TwiddleTwiddleTwiddleTwiddleTwiddleTwiddleTwiddleTwiddle" +
          "TwiddleTwiddleTwiddleTwiddleTwiddleTwiddleTwiddleTwiddle";
  private int spanLength = 0;
  private boolean spannable = true;

  public void setTrimLength(int trimLength) {
    this.trimLength = trimLength;
    notifyChange();
  }

  public void setText(String text) {
    this.text = text;
    notifyChange();
  }

  public void setText(String text, int spanLength, boolean spannable) {
    this.text = text;
    this.spannable = spannable;
    notifyChange();
  }


  private void notifyChange() {
    if (!isClicked) {
      super.setText(getTrimmedText(), BufferType.SPANNABLE);
    } else {
      super.setText(this.text, BufferType.SPANNABLE);
    }
    if (spannable) {
      Spannable s = (Spannable) getText();
      StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
      s.setSpan(boldSpan, 0, spanLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
  }

  private String getTrimmedText() {
    String trimmed = this.text;
    if (this.text.length() > trimLength) {
      return this.text.substring(0, trimLength) + "...(Show More)";
    }
    return trimmed;
  }

  public ExpandableTextView(Context context) {
    super(context);
    initialize();
  }

  public ExpandableTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
//    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
//    this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trim_length, 150);
//    this.text = typedArray.getString(R.styleable.ExpandableTextView_android_text);
//    this.spanLength = typedArray.getInt(R.styleable.ExpandableTextView_span_length, 10);
//    this.spannable = typedArray.getBoolean(R.styleable.ExpandableTextView_spannable, false);
    initialize();
  }

  public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  private void initialize() {
    setOnClickListener(this);
    notifyChange();
  }

  @Override
  public void onClick(View v) {
    isClicked = !isClicked;
    notifyChange();
  }
}
