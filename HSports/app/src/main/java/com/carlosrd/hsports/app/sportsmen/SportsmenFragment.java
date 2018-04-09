package com.carlosrd.hsports.app.sportsmen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carlosrd.hsports.R;
import com.carlosrd.hsports.data.models.Player;
import com.carlosrd.hsports.data.models.SportPlayers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Display a list of {@link SportPlayers}. User can choose to view players grouped by sport.
 */
public class SportsmenFragment extends Fragment implements SportsmenContract.View {

    private View mProgressBar;
    private View mPlayersViewer;     // Groups spinner and recyclerview

    private Spinner mSportsSpinner;
    private boolean mIsFirstTime = true;

    private RecyclerView mPlayersRecyclerView;
    private PlayersRecyclerAdapter mPlayersAdapter;

    private TextView mLblError;

    private SportsmenContract.Presenter mSportsmenPresenter;

    public static SportsmenFragment newInstance() {
        return new SportsmenFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSportsmenPresenter.loadPlayers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSportsmenPresenter.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.sportsmen_fragment, container, false);

        mProgressBar = (ProgressBar)root.findViewById(R.id.frag_sportsmen_progress_bar);
        mPlayersViewer = (LinearLayout)root.findViewById(R.id.frag_sportsmen_players_viewer);

        mSportsSpinner = (Spinner) root.findViewById(R.id.frag_sportsmen_spinner);
        mPlayersRecyclerView = (RecyclerView) root.findViewById(R.id.frag_sportsmen_recycler_view);

        mLblError = (TextView) root.findViewById(R.id.frag_sportsmen_lbl_error);

        // Capture ActionBar Menu options
        setHasOptionsMenu(true);

        return root;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                mSportsmenPresenter.loadPlayers();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sportsmen, menu);
    }

    @Override
    public void setPresenter(SportsmenContract.Presenter presenter) {
        mSportsmenPresenter = presenter;
    }

    @Override
    public void showProgress() {

        mProgressBar.setVisibility(View.VISIBLE);
        mLblError.setVisibility(View.GONE);
        mPlayersViewer.setVisibility(View.GONE);

    }

    @Override
    public void hideProgress() {

      //  loadProgress.dismiss();
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setSportsPlayers(String[] sportsList, final ArrayList<SportPlayers> sportsPlayers) {

        mLblError.setVisibility(View.GONE);
        mPlayersViewer.setVisibility(View.VISIBLE);

        // Set the spinner wich groups the players by sports
        mSportsSpinner.setAdapter(new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, sportsList));

        mSportsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setPlayersListRecycle(sportsPlayers.get(position).getPlayers());
                mSportsmenPresenter.setCurrentSportPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        // Reset savedInstanceState
        if (mIsFirstTime) {
            mSportsSpinner.setSelection(mSportsmenPresenter.getCurrentSportPosition(), false);
            mIsFirstTime = false;
        }

    }

    @Override
    public void setError(int statusCode) {

        mPlayersViewer.setVisibility(View.GONE);
        mLblError.setVisibility(View.VISIBLE);

        if (statusCode != -1) {
            String message = getString(R.string.frag_sportsmen_lbl_error_status_code);
            mLblError.setText(message.replace("@statusCode", String.valueOf(statusCode)));
        }
        else
            mLblError.setText(getString(R.string.frag_sportsmen_lbl_unknown_error));

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void setPlayersListRecycle(ArrayList<Player> playersList) {

        if (mPlayersAdapter == null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mPlayersRecyclerView.setLayoutManager(layoutManager);

            mPlayersAdapter = new PlayersRecyclerAdapter(getActivity(), playersList,
                    new PlayersListItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    mSportsmenPresenter.onItemClick(position);
                }
            });

            mPlayersRecyclerView.setAdapter(mPlayersAdapter);

        } else {

            mPlayersAdapter.updateDataSet(playersList);

        }

    }


    // ADAPTADOR RECYCLER VIEW
    // **********************************************************************

    private static class PlayersRecyclerAdapter extends RecyclerView.Adapter<PlayersRecyclerAdapter.ViewHolder> {

        private ArrayList<Player> playersList;

        private static PlayersListItemClickListener listener;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView imgPlayer;
            private TextView lblName;
            private TextView lblSurname;
            private TextView lblDateOfBirth;

            public ViewHolder(View item) {

                super(item);

                imgPlayer = (ImageView) item.findViewById(R.id.list_item_player_img_player);
                lblName = (TextView) item.findViewById(R.id.list_item_player_lbl_name);
                lblSurname = (TextView) item.findViewById(R.id.list_item_player_lbl_surname);
                lblDateOfBirth = (TextView) item.findViewById(R.id.list_item_player_lbl_date_birth);

                item.setOnClickListener(this);
                item.setClickable(true);

            }

            @Override
            public void onClick(View view) {

                listener.onItemClick(view, getLayoutPosition());

            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public PlayersRecyclerAdapter(Context ctx,
                                      ArrayList<Player> playersList,
                                      PlayersListItemClickListener listener) {

            this.playersList = playersList;
            this.listener = listener;

        }


        // Create new views (invoked by the layout manager)
        @Override
        public PlayersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_sportsmen, parent, false);

            PlayersRecyclerAdapter.ViewHolder vh = new PlayersRecyclerAdapter.ViewHolder(v);

            return vh;

        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(PlayersRecyclerAdapter.ViewHolder holder, int position) {

            Picasso.get()
                    .load(playersList.get(position).getImageURL())
                    .resize(74, 74)
                    .centerCrop()
                    .placeholder(R.drawable.person_placeholder)
                    .into(holder.imgPlayer);

            holder.lblName.setText(playersList.get(position).getName());
            holder.lblSurname.setText(playersList.get(position).getSurname());
            holder.lblDateOfBirth.setText(playersList.get(position).getDateOfBirth());

        }


        // Return the size of dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {

            return playersList.size();

        }

        public void updateDataSet(ArrayList<Player> playersList) {
            this.playersList = playersList;
            notifyDataSetChanged();
        }
    }

    // Interface to capture onClick event of items
    public interface PlayersListItemClickListener {

        void onItemClick(View v, int position);

    }
}
