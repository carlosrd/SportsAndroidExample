package com.carlosrd.hsports.app.sportsmen;

import com.carlosrd.hsports.data.SportsDataSource;
import com.carlosrd.hsports.data.SportsRepository;
import com.carlosrd.hsports.data.models.SportPlayers;

import java.util.ArrayList;

/**
 * Listens to user actions from the UI ({@link SportsmenFragment}), retrieves the data and updates the
 * UI as required.
 */
public class SportsmenPresenter implements SportsmenContract.Presenter,
        SportsDataSource.LoadPlayersCallback {

    private SportsRepository mSportsmenData;
    private SportsmenContract.View mSportsmenView;

    private int mCurrentSportPosition = 0;

    public SportsmenPresenter(SportsRepository repository, SportsmenContract.View view) {

        mSportsmenData = repository;
        mSportsmenView = view;

        mSportsmenView.setPresenter(this);

    }

    @Override
    public void loadPlayers() {

        if (mSportsmenView.isActive()) {
            mSportsmenView.showProgress();
            mSportsmenData.loadPlayers(this);
        }

    }



    @Override
    public void onDestroy() {

        mSportsmenView = null;
        mSportsmenData = null;

    }

    @Override
    public void onItemClick(int position) {

        String msg = "Position " + (position+1) + " clicked";

        mSportsmenView.showMessage(msg);

    }

    @Override
    public void setCurrentSportPosition(int position) {
        mCurrentSportPosition = position;
    }

    @Override
    public int getCurrentSportPosition() {
        return mCurrentSportPosition;
    }


    // LISTENERS
    // *********************************************************

    @Override
    public void onPlayersLoaded(String[] sportsList, ArrayList<SportPlayers> sportPlayers) {

        if (mSportsmenView.isActive()){
            mSportsmenView.setSportsPlayers(sportsList,sportPlayers);
            mSportsmenView.hideProgress();
        }

    }

    @Override
    public void onError(int statusCode) {

        if (mSportsmenView.isActive()){
            mSportsmenView.hideProgress();
            mSportsmenView.setError(statusCode);
        }

    }

}
