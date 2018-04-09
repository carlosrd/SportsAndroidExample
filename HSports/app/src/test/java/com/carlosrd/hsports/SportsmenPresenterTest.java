package com.carlosrd.hsports;

import com.carlosrd.hsports.app.sportsmen.SportsmenFragment;
import com.carlosrd.hsports.app.sportsmen.SportsmenPresenter;
import com.carlosrd.hsports.data.SportsDataSource;
import com.carlosrd.hsports.data.SportsRepository;
import com.carlosrd.hsports.data.models.Player;
import com.carlosrd.hsports.data.models.SportPlayers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SportsmenPresenterTest {

    // Spinner options mock
    private static String[] mockSports = {"Sport1", "Sport2", "Sport3"};

    private static ArrayList<SportPlayers> mockSportsPlayers = new ArrayList<SportPlayers>();

    @Mock
    SportsmenFragment mSportsmenView;

    @Mock
    SportsRepository mSportsmenData;

    @Captor
    private ArgumentCaptor<SportsDataSource.LoadPlayersCallback> loadPlayerCallbackCaptor;

    private SportsmenPresenter mSportsmenPresenter;

    @Before
    public void setupmSportsmenPresenter() {
        // Init Mockito
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mSportsmenPresenter = new SportsmenPresenter(mSportsmenData, mSportsmenView);

        // The presenter won't update the view unless it's active.
        when(mSportsmenView.isActive()).thenReturn(true);

        // Mock a SportsPlayers list of 3 items (3 sports)
        mockSportsPlayers = mock(ArrayList.class);
        when(mockSportsPlayers.size()).thenReturn(3);

    }

    @Test
    public void createPresenter_setsThePresenterToView() {

        // Get a reference to the class under test
        mSportsmenPresenter = new SportsmenPresenter(mSportsmenData, mSportsmenView);

        // Then the presenter is set to the view
        verify(mSportsmenView).setPresenter(mSportsmenPresenter);

    }

    @Test
    public void checkIfShowsProgressOnLoadingPlayers() {

        mSportsmenPresenter.loadPlayers();
        verify(mSportsmenView, times(1)).showProgress();

    }

    @Test
    public void loadPlayersFromRepositoryAndLoadIntoView() {

        // Starts loading players
        mSportsmenPresenter.loadPlayers();

        // Callback is captured and invoked with stubbed tasks
        verify(mSportsmenData).loadPlayers(loadPlayerCallbackCaptor.capture());
        loadPlayerCallbackCaptor.getValue().onPlayersLoaded(mockSports,mockSportsPlayers);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(mSportsmenView);
        inOrder.verify(mSportsmenView).showProgress();
        // Then progress indicator is hidden and all players are shown in UI
        inOrder.verify(mSportsmenView).hideProgress();

        ArgumentCaptor<ArrayList> setPlayersArgumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        ArgumentCaptor<String[]> setSportsCaptor = ArgumentCaptor.forClass(String[].class);

        verify(mSportsmenView).setSportsPlayers(setSportsCaptor.capture(), setPlayersArgumentCaptor.capture());

        assertTrue(setPlayersArgumentCaptor.getValue().size() == 3);
        assertEquals(setSportsCaptor.getValue().length, setPlayersArgumentCaptor.getValue().size());

    }

    @Test
    public void loadPlayersFromRepositoryAndGetError() {

        // Starts loading players
        mSportsmenPresenter.loadPlayers();

        // Then progress indicator is shown
        verify(mSportsmenView).showProgress();

        // Callback is captured and invoked with stubbed tasks
        verify(mSportsmenData).loadPlayers(loadPlayerCallbackCaptor.capture());
        loadPlayerCallbackCaptor.getValue().onError(404);

        // Then progress indicator is hidden and error is shown in UI
        verify(mSportsmenView).hideProgress();

        ArgumentCaptor<Integer> setPlayersArgumentCaptor = ArgumentCaptor.forClass(int.class);
        verify(mSportsmenView).setError(setPlayersArgumentCaptor.capture());

        assertTrue(setPlayersArgumentCaptor.getValue() == 404);

    }

    @Test
    public void checkIfShowsMessageOnItemClick() {
        mSportsmenPresenter.onItemClick(1);
        verify(mSportsmenView, times(1)).showMessage(anyString());
    }

    @Test
    public void checkIfRightMessageIsDisplayed() {
        ArgumentCaptor<String> captor = forClass(String.class);
        mSportsmenPresenter.onItemClick(1);
        verify(mSportsmenView, times(1)).showMessage(captor.capture());
        assertThat(captor.getValue(), is("Position 2 clicked"));
    }

}