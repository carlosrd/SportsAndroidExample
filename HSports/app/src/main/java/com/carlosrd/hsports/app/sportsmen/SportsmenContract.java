package com.carlosrd.hsports.app.sportsmen;

import com.carlosrd.hsports.data.models.SportPlayers;

import java.util.ArrayList;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface SportsmenContract {

    interface View {

        void setPresenter(Presenter presenter);

        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void setSportsPlayers(String[] sportsList, final ArrayList<SportPlayers> sportsPlayers);

        void setError(int statusCode);

        boolean isActive();

    }

    interface Presenter {

        void loadPlayers();

        void onDestroy();

        void onItemClick(int position);

        void setCurrentSportPosition(int position);

        int getCurrentSportPosition();

    }

}
