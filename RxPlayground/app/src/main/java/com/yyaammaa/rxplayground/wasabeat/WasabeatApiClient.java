package com.yyaammaa.rxplayground.wasabeat;

import com.yyaammaa.rxplayground.wasabeat.model.Track;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface WasabeatApiClient {

  // e.g. http://api.wasabeat.com/v1/tracks/84166.json
  @GET("tracks/{id}.json")
  Observable<Track> getTrack(@Path("id") int id);

  @GET("tracks/{id}.json")
  Observable<Response<Track>> getTrackResponse(@Path("id") int id);

}