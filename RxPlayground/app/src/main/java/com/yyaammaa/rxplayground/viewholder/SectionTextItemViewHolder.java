package com.yyaammaa.rxplayground.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yyaammaa.rxplayground.R;
import com.yyaammaa.rxplayground.texture.Section;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SectionTextItemViewHolder {

  private final View mRootView;
  private final Context mContext;

  @Bind(R.id.item_section_text_title) TextView mTitleView;
  @Bind(R.id.item_section_text_text) TextView mTextView;

  private Section mSection;

  public SectionTextItemViewHolder(Context context) {
    mContext = context;
    mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_section_text, null);
    ButterKnife.bind(this, mRootView);
    setUpViews();
  }

  public View getRootView() {
    return mRootView;
  }

  public void bind(Section section) {
    mSection = section;

    mTitleView.setText(mSection.title);
    mTextView.setText(mSection.text);
  }

  private void setUpViews() {

  }

}