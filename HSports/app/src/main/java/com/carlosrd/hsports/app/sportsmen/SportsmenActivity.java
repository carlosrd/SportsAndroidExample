package com.carlosrd.hsports.app.sportsmen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.carlosrd.hsports.R;
import com.carlosrd.hsports.data.SportsRepository;
import com.carlosrd.hsports.data.source.remote.SportsRemoteDataSource;
import com.carlosrd.hsports.util.AppUtils;

public class SportsmenActivity extends AppCompatActivity {

    private static final String CURRENT_SPORT_POSITION = "CURRENT_SPORT_POSITION";

    private SportsmenPresenter mSportsmenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sportsmen_activity);

        // Setup ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.act_sportsmen_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.app_name));

        // Setup the fragment
        SportsmenFragment fragment = (SportsmenFragment)
                getSupportFragmentManager().findFragmentById(R.id.act_sportmen_content_frame);

        if (fragment == null) {

            // Create the fragment
            fragment = SportsmenFragment.newInstance();
            AppUtils.addFragmentToActivity(
                    getSupportFragmentManager(),fragment, R.id.act_sportmen_content_frame);

        }

        // Create the presenter betwenn Sports Data Repository and Fragment View
        mSportsmenPresenter = new SportsmenPresenter(
                SportsRepository.getInstance(SportsRemoteDataSource.getInstance()), fragment);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            int currentPosition = savedInstanceState.getInt(CURRENT_SPORT_POSITION);
            mSportsmenPresenter.setCurrentSportPosition(currentPosition);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(CURRENT_SPORT_POSITION, mSportsmenPresenter.getCurrentSportPosition());

        super.onSaveInstanceState(outState);

    }
}
