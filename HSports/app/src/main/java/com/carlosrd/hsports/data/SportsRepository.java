package com.carlosrd.hsports.data;
/**
 * Concrete implementation to load sports intformation from the data sources (remote, local, etc).
 */
public class SportsRepository implements SportsDataSource {

    private static SportsRepository INSTANCE;

    private final SportsDataSource mSportsRemoteDataSource;

    // Prevent direct instantiation
    private SportsRepository(SportsDataSource sportsRemoteDataSource) {

        this.mSportsRemoteDataSource = sportsRemoteDataSource;

    }

    public static SportsRepository getInstance(SportsDataSource tasksRemoteDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new SportsRepository(tasksRemoteDataSource);
        }
        return INSTANCE;

    }

    @Override
    public void loadPlayers(final LoadPlayersCallback callback) {

        mSportsRemoteDataSource.loadPlayers(callback);

    }

}
