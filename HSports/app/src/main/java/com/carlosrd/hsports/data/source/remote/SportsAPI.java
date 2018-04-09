package com.carlosrd.hsports.data.source.remote;

import com.carlosrd.hsports.data.models.SportPlayers;

import java.util.ArrayList;

import retrofit2.Call;

import retrofit2.http.GET;

/**
 * Sports API Interface Methods
 */

public interface SportsAPI {

    String API_BASE_URL = "https://api.myjson.com";

    // GET Sportmen who speak english
    @GET("/bins/66851")
    Call<ArrayList<SportPlayers>> getSportsmenWhoSpeakEnglish();

}
