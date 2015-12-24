package com.yyaammaa.rxplayground;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.yyaammaa.rxplayground.adapter.SectionListAdapter;
import com.yyaammaa.rxplayground.texture.Article;
import com.yyaammaa.rxplayground.texture.Section;
import com.yyaammaa.rxplayground.texture.TextureClient;
import com.yyaammaa.rxplayground.util.Logr;
import com.yyaammaa.rxplayground.view.PinnedSectionListView;
import com.yyaammaa.rxplayground.viewholder.ArticleHeaderViewHolder;
import com.yyaammaa.rxplayground.wasabeat.Wasabeat;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class WasabeatActivity extends ActionBarActivity {

  @Bind(R.id.act_wasabeat_list_view) PinnedSectionListView mPinnedSectionListView;

  private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

  private ArticleHeaderViewHolder mArticleHeaderViewHolder;
  private SectionListAdapter mAdapter;

  private Map<Track, MediaPlayer> mPlayers;
  private int mCurrentPinnedPosition = -1;
  private Handler mHandler = new Handler();

  public static Intent createIntent(Context caller) {
    return new Intent(caller, WasabeatActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.act_wasabeat);
    ButterKnife.bind(this);

    setUpViews();
    load();
  }

  @Override
  protected void onStop() {
    Logr.e("onStop");
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    Logr.e("onDestroy");
    mCompositeSubscription.unsubscribe();
    super.onDestroy();
  }

  private void setUpViews() {
    mAdapter = new SectionListAdapter(this);

    mArticleHeaderViewHolder = new ArticleHeaderViewHolder(this);
    mPinnedSectionListView.addHeaderView(mArticleHeaderViewHolder.getRootView(), null, false);

    // 十分に長いfooterが無いと最後の曲の再生ができないね
    View footer = LayoutInflater.from(this).inflate(R.layout.item_article_footer, null);
    mPinnedSectionListView.addFooterView(footer);

    mPinnedSectionListView.setAdapter(mAdapter);

    mPinnedSectionListView.setShadowVisible(true);
    mPinnedSectionListView.setEventListener(new PinnedSectionListView.EventListener() {
      @Override
      public void onPinned(int position) {
        if (position < 1) {
          return;
        }

        if (mCurrentPinnedPosition != position - 1) {
          mCurrentPinnedPosition = position - 1;
          //Logr.e("" + mCurrentPinnedPosition);
          onTrackSelected((Track) mAdapter.getItem(mCurrentPinnedPosition));
        }
      }
    });
    mPinnedSectionListView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
      }

      @Override
      public void onScroll(AbsListView view,
                           int firstVisibleItem,
                           int visibleItemCount,
                           int totalItemCount) {
      }
    });
  }

  private void onTrackSelected(final Track track) {
    Logr.e("onTrackSelected: " + track.title);

    if (mPlayers == null) {
      Logr.e("onTrackSelected: player is null");
      return;
    }

    // 全部止めてから再生する

    for (Track tr : mPlayers.keySet()) {
      MediaPlayer player = mPlayers.get(tr);
      if (player.isPlaying()) {
        Logr.e("stop: " + tr.title);
        player.stop();
        player.prepareAsync();
      }
    }

    MediaPlayer pl = mPlayers.get(track);
    pl.start();
    Logr.e("start: " + track.title);

  }

  private void preparePlayers(final Article article) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        mPlayers = new HashMap<>();
        for (Section sec : article.sections) {
          mPlayers.put(
              sec.track,
              MediaPlayer.create(getApplicationContext(), Uri.parse(sec.track.urls.sample)));
        }
        Logr.e("preparePlayers: finished");
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(getApplicationContext(), "ready to play", Toast.LENGTH_SHORT).show();
          }
        });
      }
    }).start();
  }

  private void setArticle(final Article article) {

    mArticleHeaderViewHolder.bind(article);
    mAdapter.addAll(article.sections);

    //preparePlayers(article);
  }

  private void load() {
    Subscription subs = TextureClient.loadArticle()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Article>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
          }

          @Override
          public void onNext(Article article) {
            Logr.e("onNext: title = " + article.title + ", section size = " + article.sections.size());
            for (Section sec : article.sections) {
              Logr.e("track title = " + sec.track.title + ", title = " + sec.title);
            }

            setArticle(article);
          }
        });
    mCompositeSubscription.add(subs);
  }

  private void get1() {
    Observable<Track> obs = Wasabeat.createApiClient().getTrack(84166);
    obs
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Track>() {
          @Override
          public void onCompleted() {
            Logr.e("onCompleted");
          }

          @Override
          public void onError(Throwable throwable) {
            Logr.e("onError");
            throwable.printStackTrace();
          }

          @Override
          public void onNext(Track track) {
            Logr.e("onNext: " + track.title + ", by " + track.artist.name);
          }
        });

  }

}