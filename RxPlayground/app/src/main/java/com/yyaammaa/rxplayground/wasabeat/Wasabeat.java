package com.yyaammaa.rxplayground.wasabeat;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public final class Wasabeat {

  private static final String ENDPOINT = "http://api.wasabeat.com/v1/";

  public static WasabeatApiClient createApiClient() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Wasabeat.ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    return retrofit.create(WasabeatApiClient.class);
  }

//  public static Section from(Track track, SectionResponse response) {
//    return Observable.zip(
//        Observable.just(track), Observable.just(response),
//        new Func2<Track, SectionResponse, Section>() {
//          @Override
//          public Section call(Track track, SectionResponse response) {
//            Section sec = new Section();
//            sec.title = response.title;
//            sec.text = response.text;
//            sec.track = track;
//            return sec;
//          }
//        }).toBlocking().single();
//  }
//
//  public static List<Section> from(List<Track> tracks, List<SectionResponse> responses) {
//    return Observable.zip(
//        Observable.from(tracks),
//        Observable.from(responses),
//        new Func2<Track, SectionResponse, Section>() {
//          @Override
//          public Section call(Track track, SectionResponse response) {
//            Section sec = new Section();
//            sec.title = response.title;
//            sec.text = response.text;
//            sec.track = track;
//            return sec;
//          }
//        }).toList().toBlocking().single();
//  }

}