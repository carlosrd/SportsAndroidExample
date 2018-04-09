package com.carlosrd.hsports.data.source.remote;

import com.carlosrd.hsports.data.SportsDataSource;
import com.carlosrd.hsports.data.models.SportPlayers;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of the remote data source that conects to API.
 */
public class SportsRemoteDataSource implements SportsDataSource {

    private static SportsRemoteDataSource INSTANCE;

    public static SportsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SportsRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private SportsRemoteDataSource() {}

    @Override
    public void loadPlayers(final LoadPlayersCallback callback) {

        // Create a very simple REST adapter which points the GitHub API endpoint.
        SportsAPI client = APIClient.createService(SportsAPI.API_BASE_URL, SportsAPI.class);

        // Fetch sportsmen who speak english
        Call<ArrayList<SportPlayers>> call = client.getSportsmenWhoSpeakEnglish();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<ArrayList<SportPlayers>>() {
            @Override
            public void onResponse(Call<ArrayList<SportPlayers>> call, Response<ArrayList<SportPlayers>> response) {
                // The network call was a success and we got a response
                if (response.isSuccessful()) {

                    ArrayList<SportPlayers> sportsList = response.body();

                    // Prepare sports list for the spinner
                    String[] sports = new String[sportsList.size()];

                    for (int i = 0; i < sportsList.size(); i++)
                        sports[i] = sportsList.get(i).getName();

                    callback.onPlayersLoaded(sports, sportsList);

                } else {
                    callback.onError(response.code());
                }

            }

            @Override
            public void onFailure(Call<ArrayList<SportPlayers>> call, Throwable t) {
                // the network call was a failure
                callback.onError(-1);

            }

        });

    }

}
