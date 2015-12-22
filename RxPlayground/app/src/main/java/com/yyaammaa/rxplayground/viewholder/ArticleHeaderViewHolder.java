package com.yyaammaa.rxplayground.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yyaammaa.rxplayground.R;
import com.yyaammaa.rxplayground.texture.Article;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleHeaderViewHolder {

  private final View mRootView;
  private final Context mContext;

  @Bind(R.id.item_article_header_title) TextView mTitle;
  @Bind(R.id.item_article_header_text) TextView mText;

  private Article mArticle;

  public ArticleHeaderViewHolder(Context context) {
    mContext = context;
    mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_article_header, null);
    ButterKnife.bind(this, mRootView);
    setUpViews();
  }

  public View getRootView() {
    return mRootView;
  }

  public void bind(Article article) {
    mArticle = article;

    mTitle.setText(article.title);
    mText.setText(article.description);
  }

  private void setUpViews() {

  }

}