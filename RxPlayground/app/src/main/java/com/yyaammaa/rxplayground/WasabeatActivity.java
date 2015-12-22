package com.yyaammaa.rxplayground;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.yyaammaa.rxplayground.adapter.SectionListAdapter;
import com.yyaammaa.rxplayground.texture.Article;
import com.yyaammaa.rxplayground.texture.ArticleResponse;
import com.yyaammaa.rxplayground.texture.Section;
import com.yyaammaa.rxplayground.texture.SectionResponse;
import com.yyaammaa.rxplayground.util.Logr;
import com.yyaammaa.rxplayground.view.PinnedSectionListView;
import com.yyaammaa.rxplayground.viewholder.ArticleHeaderViewHolder;
import com.yyaammaa.rxplayground.wasabeat.Wasabeat;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class WasabeatActivity extends ActionBarActivity {

  @Bind(R.id.act_wasabeat_list_view) PinnedSectionListView mPinnedSectionListView;

  private ArticleHeaderViewHolder mArticleHeaderViewHolder;
  private SectionListAdapter mAdapter;

  private MediaPlayer mMediaPlayer;
  private int mCurrentPinnedPosition = -1;

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
  }

  private void onTrackSelected(final Track track) {
    Logr.e("onTrackSelected: " + track.title);

    new Thread(new Runnable() {
      @Override
      public void run() {
        // 切り替え時にレイテンシがあるので予め全部用意したほうがいいかなあ
        if (mMediaPlayer != null) {
          mMediaPlayer.release();
          mMediaPlayer = null;
        }
        mMediaPlayer = MediaPlayer.create(WasabeatActivity.this, Uri.parse(track.urls.sample));
        mMediaPlayer.start();
      }
    }).start();
  }

  private void setArticle(final Article article) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mArticleHeaderViewHolder.bind(article);
        mAdapter.addAll(article.sections);
      }
    });
  }

  private void load() {
    ArticleResponse response = getResponse();
    Observable<Article> obs = createArticle(response);
    obs.observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
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
            // TODO: ここはUI Threadではないぽい。。何故?
            setArticle(article);
          }
        });
  }

  // ArticleResponseからarticleをつくる
  private Observable<Article> createArticle(ArticleResponse response) {
    Observable<List<Section>> sectionsObs = createSections(response);
    return Observable.zip(
        sectionsObs, Observable.just(response),
        new Func2<List<Section>, ArticleResponse, Article>() {
          @Override
          public Article call(List<Section> sections, ArticleResponse response) {
            Article article = new Article();
            article.title = response.title;
            article.description = response.description;
            article.sections = sections;
            return article;
          }
        })
        .observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread());

  }

  // Observable<Track> と SectionResponseをzipしてList<Section>をつくる
  private Observable<List<Section>> createSections(ArticleResponse response) {
    Observable<Track> trackObs = loadTracks(response);
    Observable<SectionResponse> sectionResponseObs = Observable.from(response.sections);
    return Observable.zip(
        trackObs, sectionResponseObs, new Func2<Track, SectionResponse, Section>() {
          @Override
          public Section call(Track track, SectionResponse sectionResponse) {
            Section section = new Section();
            section.text = sectionResponse.text;
            section.title = sectionResponse.title;
            section.track = track;
            return section;
          }
        })
        .observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
        .toList();
  }

  private Observable<Track> loadTracks(ArticleResponse response) {
    Observable<ArticleResponse> aa = Observable.just(response);
    return aa
        .observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
        .flatMap(new Func1<ArticleResponse, Observable<Integer>>() {
          @Override
          public Observable<Integer> call(ArticleResponse articleResponse) {
            List<Integer> trackIds = new ArrayList<>();
            for (SectionResponse sectionResponse : articleResponse.sections) {
              trackIds.add(sectionResponse.trackId);
            }
            return Observable.from(trackIds);
          }
        })
        .flatMap(new Func1<Integer, Observable<Track>>() {
          @Override
          public Observable<Track> call(Integer integer) {
            return getTrack(integer);
          }
        });
  }

  private Observable<Track> getTrack(int id) {
    Observable<Track> trackObservable = Wasabeat.createApiClient().getTrack(id);
    trackObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    return trackObservable;
  }

  // サーバからのレスポンス (擬似)
  private ArticleResponse getResponse() {
    ArticleResponse articleResponse = new ArticleResponse();
    articleResponse.title = "おすすめの10曲";
    articleResponse.description = "東京・下北沢B＆Bにて10月31日、『Jazz The New Chapter 3』の発刊を記念したイベント『ジャズミュージシャンが奏でる、まだ名前の付いてない新しい音楽』が開催された。同イベントは、2014年2月に発刊されたジャズの新潮流についてのガイドブック『Jazz The New Chapter』（JTNC）の3作目発刊を祝して行われたもの。シリーズの監修を務めたジャズ評論家の柳樂光隆氏と、元コミックナタリーの編集長であり、2016年よりバークリー音楽大学へ入学するベーシスト・唐木元氏の2人が、JTNCとはどんな音楽なのか、“別ジャンルを使って拡張されたジャズ”という視点から詳しく語り合った。";

    List<SectionResponse> sectionResponses = new ArrayList<>();

    SectionResponse section1 = new SectionResponse();
    section1.trackId = 84166;
    section1.title = "Cazzette - Sleepless (Young Bombs Remix)";
    section1.text = "カナダはバンクーバーの二人組。Cazzetteのトラックが途中からすごく美メロミックスになっていて素敵です。キレイな音を作るアーティストですね。今後に注目です。";
    sectionResponses.add(section1);

    SectionResponse section2 = new SectionResponse();
    section2.trackId = 1134441;
    section2.title = "Erde&Aurtas - Liberal";
    section2.text = "EDM Bananaも応援しているERdeがAurtasとコラボした作品。普段聴いているプレイリストにがっつり入っています。";
    sectionResponses.add(section2);

    SectionResponse section3 = new SectionResponse();
    section3.trackId = 1104201;
    section3.title = "Disclosure - Latch (J-Kraken Remix)";
    section3.text = "アメリカ在住、22歳のEDMアーティストJ-KrakenによるDisclosure \"Latch\"のリミックス。J-Kraken、髪型がちょっとスネ夫っぽい！アジアンアメリカンっぽい顔つきですね。がっつりリミックスを書けて来る感じですね。カリフォルニア出身だからから、サウンドも明るくてポジティブな感じがします。Ariana Grandeのリミックスもよいです。";
    sectionResponses.add(section3);

    SectionResponse section4 = new SectionResponse();
    section4.trackId = 1126348;
    section4.title = "Vicetone - Heat";
    section4.text = "Protocol Recordings、SpininnからリリースしたこともあるVicetoneですが、なんと今度はRevealedからのリリース。オランダEDMレーベル制覇してる感じがありますね。相変わらずのVicetone節でかっこいいです。Vicetoneは良曲が多いので、もっとブレイクすると嬉しいですね！";
    sectionResponses.add(section4);

    SectionResponse section5 = new SectionResponse();
    section5.trackId = 1134381;
    section5.title = "Mike Mago & Dragonette - Outlines";
    section5.text = "Oliver HeldensにハマったのでDeep House系の曲を探していたら発見したトラック。オランダのHouse系アーティストMike MagoとカナダのエレクトロポップバンドDragonetteがコラボした作品。ボーカルとDeep House的サウンドが素晴らしくマッチしています。";
    sectionResponses.add(section5);

    articleResponse.sections = sectionResponses;
    return articleResponse;
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