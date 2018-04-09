package com.carlosrd.hsports.data;

import com.carlosrd.hsports.app.sportsmen.SportsmenContract;
import com.carlosrd.hsports.data.models.SportPlayers;

import java.util.ArrayList;

/**
 * Main entry point for accessing Sports data.
 * Also include callbacks for methods (loadPlayers() in this case)
 */
public interface SportsDataSource {

    interface LoadPlayersCallback {

        void onPlayersLoaded(String[] sportsList, ArrayList<SportPlayers> sportPlayers);

        void onError(int statusCode);

    }

    void loadPlayers(LoadPlayersCallback callback);

}
