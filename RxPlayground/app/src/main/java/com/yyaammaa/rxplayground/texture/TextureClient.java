package com.yyaammaa.rxplayground.texture;

import com.yyaammaa.rxplayground.wasabeat.Wasabeat;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public final class TextureClient {

  private TextureClient() {
    // No instances.
  }

  public static Observable<Article> loadArticle() {
    return createArticle(getResponse());
  }

  // ArticleResponseからarticleをつくる
  private static Observable<Article> createArticle(ArticleResponse response) {
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
        });
  }

  // Observable<Track> と SectionResponseをzipしてList<Section>をつくる
  private static Observable<List<Section>> createSections(ArticleResponse response) {
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
        .toList();
  }

  private static Observable<Track> loadTracks(ArticleResponse response) {
    Observable<ArticleResponse> aa = Observable.just(response);
    return aa
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

  private static Observable<Track> getTrack(int id) {
    return Wasabeat.createApiClient().getTrack(id);
  }

  // サーバからのレスポンス (擬似)
  private static ArticleResponse getResponse() {
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

}