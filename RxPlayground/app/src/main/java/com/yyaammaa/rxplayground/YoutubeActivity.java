package com.yyaammaa.rxplayground;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.yyaammaa.rxplayground.util.Dummy;
import com.yyaammaa.rxplayground.util.Logr;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

// memo: https://developers.google.com/youtube/android/player/reference/com/google/android/youtube/player/YouTubeThumbnailView.OnInitializedListener?hl=ja

// memo: http://www.billboard-japan.com/special/detail/1447

public class YoutubeActivity extends YouTubeBaseActivity {

  @BindString(R.string.youtube_api_key) String mApiKey;
  @Bind(R.id.act_youtube_playerview) YouTubePlayerView mPlayerView;
  @Bind(R.id.act_youtube_playerview2) YouTubePlayerView mPlayerView2;
  @Bind(R.id.act_youtube_text) TextView mTextView;

  private YoutubeActivity self = this;

  private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

  public static Intent createIntent(Context caller) {
    return new Intent(caller, YoutubeActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.act_youtube);
    ButterKnife.bind(this);

    setUpViews();
  }

  @Override
  protected void onDestroy() {
    Logr.e("onDestroy");
    mCompositeSubscription.unsubscribe();
    super.onDestroy();
  }

  private void setUpViews() {

    mTextView.setText(Dummy.text());

    // 例によってGenymotionではダメ
    mPlayerView.initialize(mApiKey, new YouTubePlayer.OnInitializedListener() {
      @Override
      public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                          YouTubePlayer player,
                                          boolean wasRestored) {
        Logr.e("onInitializationSuccess");

        player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
          @Override
          public void onPlaying() {
            Logr.e("onPlaying");
          }

          @Override
          public void onPaused() {
            Logr.e("onPaused");
          }

          @Override
          public void onStopped() {
            Logr.e("onStopped");
          }

          @Override
          public void onBuffering(boolean b) {
            Logr.e("onBuffering");
          }

          @Override
          public void onSeekTo(int i) {
            Logr.e("onSeekTo");
          }
        });

        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
          @Override
          public void onLoading() {
            Logr.e("onLoading");
          }

          @Override
          public void onLoaded(String s) {
            Logr.e("onLoaded");
          }

          @Override
          public void onAdStarted() {
            Logr.e("onAdStarted");
          }

          @Override
          public void onVideoStarted() {
            Logr.e("onVideoStarted");
          }

          @Override
          public void onVideoEnded() {
            Logr.e("onVideoEnded");
          }

          @Override
          public void onError(YouTubePlayer.ErrorReason errorReason) {
            Logr.e("onError");
          }
        });

        player.loadVideo("3HQfmubKDww");

      }

      @Override
      public void onInitializationFailure(YouTubePlayer.Provider provider,
                                          YouTubeInitializationResult error) {
        Logr.e("onInitializationFailure: " + error.toString());
      }
    });

    // 2つはダメぽい
//    mPlayerView2.initialize(mApiKey, new YouTubePlayer.OnInitializedListener() {
//      @Override
//      public void onInitializationSuccess(YouTubePlayer.Provider provider,
//                                          YouTubePlayer player,
//                                          boolean wasRestored) {
//        Logr.e("onInitializationSuccess");
//
//        player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
//          @Override
//          public void onPlaying() {
//            Logr.e("onPlaying");
//          }
//
//          @Override
//          public void onPaused() {
//            Logr.e("onPaused");
//          }
//
//          @Override
//          public void onStopped() {
//            Logr.e("onStopped");
//          }
//
//          @Override
//          public void onBuffering(boolean b) {
//            Logr.e("onBuffering");
//          }
//
//          @Override
//          public void onSeekTo(int i) {
//            Logr.e("onSeekTo");
//          }
//        });
//
//        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
//          @Override
//          public void onLoading() {
//            Logr.e("onLoading");
//          }
//
//          @Override
//          public void onLoaded(String s) {
//            Logr.e("onLoaded");
//          }
//
//          @Override
//          public void onAdStarted() {
//            Logr.e("onAdStarted");
//          }
//
//          @Override
//          public void onVideoStarted() {
//            Logr.e("onVideoStarted");
//          }
//
//          @Override
//          public void onVideoEnded() {
//            Logr.e("onVideoEnded");
//          }
//
//          @Override
//          public void onError(YouTubePlayer.ErrorReason errorReason) {
//            Logr.e("onError");
//          }
//        });
//
//        //player.loadVideo("t-e4YJ-3Cb0");
//        player.cueVideo("t-e4YJ-3Cb0");
//      }
//
//      @Override
//      public void onInitializationFailure(YouTubePlayer.Provider provider,
//                                          YouTubeInitializationResult error) {
//        Logr.e("onInitializationFailure: " + error.toString());
//      }
//    });

  }

}