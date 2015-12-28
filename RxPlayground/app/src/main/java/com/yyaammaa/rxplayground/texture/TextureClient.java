package com.yyaammaa.rxplayground.texture;

import com.yyaammaa.rxplayground.wasabeat.Wasabeat;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

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
        sectionsObs, Observable.just(response), (sections, response1) -> {
          Article article = new Article();
          article.title = response1.title;
          article.description = response1.description;
          article.sections = sections;
          return article;
        });
  }

  // Observable<Track> と SectionResponseをzipしてList<Section>をつくる
  private static Observable<List<Section>> createSections(ArticleResponse response) {
    Observable<Track> trackObs = loadTracks(response);
    Observable<SectionResponse> sectionResponseObs = Observable.from(response.sections);
    return Observable.zip(
        trackObs, sectionResponseObs, (track, sectionResponse) -> {
          Section section = new Section();
          section.text = sectionResponse.text;
          section.title = sectionResponse.title;
          section.track = track;
          return section;
        })
        .toList();
  }

  private static Observable<Track> loadTracks(ArticleResponse response) {
    Observable<ArticleResponse> aa = Observable.just(response);
    return aa
        .flatMap(articleResponse -> {
          List<Integer> trackIds = new ArrayList<>();
          for (SectionResponse sectionResponse : articleResponse.sections) {
            trackIds.add(sectionResponse.trackId);
          }
          return Observable.from(trackIds);
        })
        .flatMap(TextureClient::getTrack);
  }

  private static Observable<Track> getTrack(int id) {
    return Wasabeat.createApiClient().getTrack(id);
  }

  // サーバからのレスポンス (擬似)
  private static ArticleResponse getResponse() {
    ArticleResponse articleResponse = new ArticleResponse();
    articleResponse.title = "衝撃的だった10曲を並べる";
    articleResponse.description = "それを踏まえて「じゃあ音楽でやってみたらどうなるんだ？」と試してみたところ、Google Play MusicやApple Music Storeなどで手に入る曲で、十分、とあるテーマのもと10曲をピックアップすることが出来きました。特に僕は音楽を語れるほど音楽に関する語彙を持っているわけではないので、難しいことは考えず、自分の人生にとって「衝撃的」だった曲というテーマ設定にしてみました。ってことで、なぜ衝撃的だったのか？という回想付きでご紹介。よろしければ、ご覧ください！";

    List<SectionResponse> sectionResponses = new ArrayList<>();

    SectionResponse section1 = new SectionResponse();
    section1.trackId = 84166;
    section1.title = "01. Blowin' In the Wind - Bob Dylan";
    section1.text = "いきなり渋い曲なんだけど、中学の英語の授業の時にひたすら歌わされたビートルズの影響で、洋楽のロックやフォークが気になった僕。 有名ドコロを攻めようと前提知識ナシにボブ・ディランのCDをドキドキしながら買った。そして流して最初に流れたのがこの邦題「風に吹かれて」。 正直な感想は「なんだこりゃ、これが音楽なのか？」というものだったが、確かに今思うと日本の当時のポップソングをTVで耳にしていたことから考えると当然だ。 しかしいいものは良い。ボブ・ディランをスルメの如く聴いていた中学生の時期がありました。";
    sectionResponses.add(section1);

    SectionResponse section2 = new SectionResponse();
    section2.trackId = 1134441;
    section2.title = "02. Daddy, Brother, Lover, Little Boy - Mr.Big";
    section2.text = "ボブ・ディランに限らず、影響を受けて、中学生の僕はギターを始めた。 すると個人的な興味と周りの仲間の影響で「技巧」に憧れを持つようになってきた。ヤバイよ。ポール・ギルバートの速弾きヤバイよ。 そしてマキタのドリル！ギターのピックアップという部分にマキタ製のドリルを押し当て、そのノイズ音を拾わせるその奏法？は奇想天外であった。 彼みたいに速弾きが出来るようになりたいなーと思っていたが、とある雑誌についていた彼の「手形」に手を当ててみて絶望した。 「指の長さが違いすぎる」僕のような短い指では速弾きは無理だと諦めて中学生活はだいたい終わりを迎えた。";
    sectionResponses.add(section2);

    SectionResponse section3 = new SectionResponse();
    section3.trackId = 1104201;
    section3.title = "03. Can't Stop Lovin' You - Van Halen";
    section3.text = "とはいえ、USロックは好きだった。特にエディー・ヴァン・ヘイレンがつくるメロディラインとギターソロがポップでもあり叙情的でもあり好みだ。 高校生になった僕はひたすら中古CDショップに通い、彼らのアルバムを全て揃えたものだった。";
    sectionResponses.add(section3);

    SectionResponse section4 = new SectionResponse();
    section4.trackId = 1126348;
    section4.title = "04. Wonderwall - Oasis";
    section4.text = "USのハードロックに傾倒していたわけだが、転機が起こる。同じサッカー部で後ほど同じバンドを組むことになる友達がオアシスの「Morning Glory」をさらっと貸してくれる。 これこそ正に衝撃的だった。同じギターを主軸としたサウンドなのに、なぜ彼らは派手な速弾きをせずとも、圧力のある音を生み出せるのか？ それでいて「歌」が最強だ。なんでこんなことが出来るんだ？とノエル・ギャラガーのことを調べまくった。コピーもしたし、海賊版の未発表曲も漁った。とにかく彼はすごかった。";
    sectionResponses.add(section4);

    SectionResponse section5 = new SectionResponse();
    section5.trackId = 1134381;
    section5.title = "05. Why Does It Always Rain On Me? - Travis";
    section5.text = "UKのロックも聴くようになって、印象的なのは「Travis」である。 とあるフジロック・フェスティバルでの彼らの演奏はちょうど雨が降るシチュエーションでの「Why Does It Always Rain On Me?」 どうして僕にはいつも雨が降るの？「それは17歳の時に嘘をついたからだよ」という歌詞もぐっとくる。";
    sectionResponses.add(section5);

    articleResponse.sections = sectionResponses;
    return articleResponse;
  }

//  private static ArticleResponse getResponse() {
//    ArticleResponse articleResponse = new ArticleResponse();
//    articleResponse.title = "おすすめの10曲";
//    articleResponse.description = "東京・下北沢B＆Bにて10月31日、『Jazz The New Chapter 3』の発刊を記念したイベント『ジャズミュージシャンが奏でる、まだ名前の付いてない新しい音楽』が開催された。同イベントは、2014年2月に発刊されたジャズの新潮流についてのガイドブック『Jazz The New Chapter』（JTNC）の3作目発刊を祝して行われたもの。シリーズの監修を務めたジャズ評論家の柳樂光隆氏と、元コミックナタリーの編集長であり、2016年よりバークリー音楽大学へ入学するベーシスト・唐木元氏の2人が、JTNCとはどんな音楽なのか、“別ジャンルを使って拡張されたジャズ”という視点から詳しく語り合った。";
//
//    List<SectionResponse> sectionResponses = new ArrayList<>();
//
//    SectionResponse section1 = new SectionResponse();
//    section1.trackId = 84166;
//    section1.title = "Cazzette - Sleepless (Young Bombs Remix)";
//    section1.text = "カナダはバンクーバーの二人組。Cazzetteのトラックが途中からすごく美メロミックスになっていて素敵です。キレイな音を作るアーティストですね。今後に注目です。";
//    sectionResponses.add(section1);
//
//    SectionResponse section2 = new SectionResponse();
//    section2.trackId = 1134441;
//    section2.title = "Erde&Aurtas - Liberal";
//    section2.text = "EDM Bananaも応援しているERdeがAurtasとコラボした作品。普段聴いているプレイリストにがっつり入っています。";
//    sectionResponses.add(section2);
//
//    SectionResponse section3 = new SectionResponse();
//    section3.trackId = 1104201;
//    section3.title = "Disclosure - Latch (J-Kraken Remix)";
//    section3.text = "アメリカ在住、22歳のEDMアーティストJ-KrakenによるDisclosure \"Latch\"のリミックス。J-Kraken、髪型がちょっとスネ夫っぽい！アジアンアメリカンっぽい顔つきですね。がっつりリミックスを書けて来る感じですね。カリフォルニア出身だからから、サウンドも明るくてポジティブな感じがします。Ariana Grandeのリミックスもよいです。";
//    sectionResponses.add(section3);
//
//    SectionResponse section4 = new SectionResponse();
//    section4.trackId = 1126348;
//    section4.title = "Vicetone - Heat";
//    section4.text = "Protocol Recordings、SpininnからリリースしたこともあるVicetoneですが、なんと今度はRevealedからのリリース。オランダEDMレーベル制覇してる感じがありますね。相変わらずのVicetone節でかっこいいです。Vicetoneは良曲が多いので、もっとブレイクすると嬉しいですね！";
//    sectionResponses.add(section4);
//
//    SectionResponse section5 = new SectionResponse();
//    section5.trackId = 1134381;
//    section5.title = "Mike Mago & Dragonette - Outlines";
//    section5.text = "Oliver HeldensにハマったのでDeep House系の曲を探していたら発見したトラック。オランダのHouse系アーティストMike MagoとカナダのエレクトロポップバンドDragonetteがコラボした作品。ボーカルとDeep House的サウンドが素晴らしくマッチしています。";
//    sectionResponses.add(section5);
//
//    articleResponse.sections = sectionResponses;
//    return articleResponse;
//  }

}