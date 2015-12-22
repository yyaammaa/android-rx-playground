package com.yyaammaa.rxplayground.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yyaammaa.rxplayground.R;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrackItemViewHolder {

  private final View mRootView;
  private final Context mContext;

  @Bind(R.id.item_track_artist_name) TextView mArtistNameView;
  @Bind(R.id.item_track_track_name) TextView mTrackNameView;
  @Bind(R.id.item_track_image) ImageView mCoverImageView;

  private Track mTrack;

  public TrackItemViewHolder(Context context) {
    mContext = context;
    mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_track, null);
    ButterKnife.bind(this, mRootView);
    setUpViews();
  }

  public View getRootView() {
    return mRootView;
  }

  public void bind(Track track) {
    mTrack = track;

    mArtistNameView.setText(mTrack.artist.name);
    mTrackNameView.setText(mTrack.title);
    Picasso.with(mContext).load(mTrack.images.medium).into(mCoverImageView);
  }

  private void setUpViews() {

  }

}