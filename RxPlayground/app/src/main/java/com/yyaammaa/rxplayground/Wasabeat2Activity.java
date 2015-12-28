package com.yyaammaa.rxplayground;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.yyaammaa.rxplayground.adapter.SectionListAdapter;
import com.yyaammaa.rxplayground.texture.Article;
import com.yyaammaa.rxplayground.texture.Section;
import com.yyaammaa.rxplayground.texture.TextureClient;
import com.yyaammaa.rxplayground.util.ContextUtils;
import com.yyaammaa.rxplayground.util.Logr;
import com.yyaammaa.rxplayground.viewholder.ArticleHeaderViewHolder;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class Wasabeat2Activity extends ActionBarActivity {

  @Bind(R.id.act_wasabeat2_list_view) ListView mListView;

  private ArticleHeaderViewHolder mArticleHeaderViewHolder;
  private SectionListAdapter mAdapter;

  private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
  private Map<Track, MediaPlayer> mPlayers;
  private int mCurrentPinnedPosition = -1;

  public static Intent createIntent(Context caller) {
    return new Intent(caller, Wasabeat2Activity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.act_wasabeat2);
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
    mListView.addHeaderView(mArticleHeaderViewHolder.getRootView(), null, false);

    // 十分に長いfooterが無いと最後の曲の再生ができないね
    View footer = LayoutInflater.from(this).inflate(R.layout.item_article_footer, null);
    mListView.addFooterView(footer);
    mListView.setAdapter(mAdapter);
    mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
    mCompositeSubscription.add(
        prepare(article)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Boolean>() {
              @Override
              public void onCompleted() {
                Logr.e("preparePlayers: onCompleted");
              }

              @Override
              public void onError(Throwable throwable) {
                Logr.e("preparePlayers: onError");
                Toast.makeText(
                    getApplicationContext(),
                    "failed to prepare mediaplayers", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
              }

              @Override
              public void onNext(Boolean aBoolean) {
                Logr.e("preparePlayers: onNext: " + aBoolean);
                if (aBoolean) {
                  Toast.makeText(
                      getApplicationContext(), "ready to play", Toast.LENGTH_SHORT).show();
                } else {
                  Toast.makeText(
                      getApplicationContext(),
                      "ready to play (but something is wrong)", Toast.LENGTH_SHORT).show();
                }
              }
            })
    );
  }

  /**
   * prepare成功でtrue
   */
  private Observable<Boolean> prepare(Article article) {
    return Observable.just(article)
        .doOnSubscribe(() -> {
          // ここもsubscribeOnで指定したスレッドで実行される
          Logr.e("prepare: doOnSubscribe: isUiThread = "
              + ContextUtils.isUiThread(getApplicationContext()));
          mPlayers = new HashMap<>();
        })
        .flatMap(article1 -> {
          List<Track> tracks = new ArrayList<>();
          for (Section sec : article1.sections) {
            tracks.add(sec.track);
          }
          return Observable.from(tracks);
        })
        .filter(track -> {
          if (track.urls == null) {
            Logr.e("track.urls is null, title = " + track.title);
            return false;
          }
          if (track.urls.sample == null) {
            Logr.e("track.urls.sample is null, title = " + track.title);
            return false;
          }

          mPlayers.put(
              track,
              MediaPlayer.create(getApplicationContext(), Uri.parse(track.urls.sample))
          );
          return true;
        })
        .toList()
        .map(tracks -> mPlayers.size() == tracks.size());
  }

  private void setArticle(final Article article) {
    mArticleHeaderViewHolder.bind(article);
    mAdapter.addAll(article.sections);

    preparePlayers(article);
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

}