package edu.utah.cs4962.battleship;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by jesuszarate on 10/24/14.
 */
public class GameListFragment extends Fragment implements ListAdapter
{

    int selectedGame = 0;
    //public static ArrayList<Game> _gameList;
    ListView gameListView;
    private final static int ITEM_COLOR = 0xFF17A090;
    private final static int SELECTED_ITEM_COLOR = 0xFF3AC2B2;

    public void setGameList(ArrayList<Game> gameList)
    {
        if (GameCollection.getInstance().getGamelist().size() == 0)
        {
            for (Game g : gameList)
            {
                GameCollection.getInstance().addGame(g);
            }
        }
    }


    // This list
    NetworkClass.Game[] _gameList;

    public void setGameList(NetworkClass.Game[] gameList)
    {
        _gameList = gameList;
        gameListView.invalidateViews();

//        if(GameCollection.getInstance().getGamelist().size() == 0)
//        {
//            for(NetworkClass.Game g : gameList){
//                GameCollection.getInstance().addGame(g);
//            }
//        }
    }

    //region GameSelectedListener

    public interface OnGameSelectedListener
    {
        public void onGameSelected(GameListFragment gameListFragment, NetworkClass.Game game);
    }

    OnGameSelectedListener _onGameSelectedListener = null;

    public OnGameSelectedListener getOnGameSelectedListener()
    {
        return _onGameSelectedListener;
    }

    public void setOnGameSelectedListener(OnGameSelectedListener _onGameSelectedListener)
    {
        this._onGameSelectedListener = _onGameSelectedListener;
    }
    //endregion GameSelectedListener


    //endregion Listeners

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        gameListView = new ListView(getActivity());
        gameListView.setAdapter(this);
        gameListView.setBackgroundColor(0xFF17A090);
        gameListView.setDividerHeight(10);


        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (_onGameSelectedListener != null)
                {
                    for (int childIndex = 0; childIndex < gameListView.getChildCount(); childIndex++)
                    {
                        gameListView.getChildAt(childIndex).setBackgroundColor(ITEM_COLOR);
                    }
                    view.setBackgroundColor(SELECTED_ITEM_COLOR);

                    // Selected Game
                    _onGameSelectedListener.onGameSelected(GameListFragment.this,
                            _gameList[i]);
//                    _onGameSelectedListener.onGameSelected(GameListFragment.this,
//                            GameCollection.getInstance().getGamelist().get(i));

                    selectedGame = i;
                }
            }
        });
        return gameListView;
    }

    public void updateList()
    {
        gameListView.invalidateViews();
    }


    public void AddItemGameToList(Game newGame)
    {
        GameCollection.getInstance().getGamelist().add(newGame);
        gameListView.invalidateViews();
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int i)
    {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver)
    {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver)
    {

    }

    @Override
    public int getCount()
    {
        //return GameCollection.getInstance().getGamelist().size();
        if (_gameList != null)
            return _gameList.length;
        return 0;
    }

    @Override
    public Object getItem(int i)
    {
        return GameCollection.getInstance().getGamelist().get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TextView gameInfo = new TextView(getActivity());
        gameInfo.setTextColor(Color.WHITE);
        ViewGroup.LayoutParams params = gameInfo.getLayoutParams();

        if (params == null)
        {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else
        {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        gameInfo.setLayoutParams(params);

        if (i != selectedGame)
            gameInfo.setBackgroundColor(ITEM_COLOR);
        else
            gameInfo.setBackgroundColor(SELECTED_ITEM_COLOR);

//        if (GameCollection.getInstance().getGamelist().get(i).GameOver)
//        {
//            gameInfo.setText("Game " + i + ": " + GameCollection.getInstance().getGamelist().get(i).Winner + "\n" +
//                    "Player1 Score: " + GameCollection.getInstance().getGamelist().get(i).getPlayer1Score() + "\n" +
//                    "Player2 Score: " + GameCollection.getInstance().getGamelist().get(i).getPlayer2Score());
//        } else
//        {
//            gameInfo.setText("Game " + i + ": Player" + GameCollection.getInstance().getGamelist().get(i).getPlayersTurn() + " Turn " + "\n" +
//                    "Player1 Score: " + GameCollection.getInstance().getGamelist().get(i).getPlayer1Score() + "\n" +
//                    "Player2 Score: " + GameCollection.getInstance().getGamelist().get(i).getPlayer2Score());
//        }
//        return gameInfo;

        // TODO: SHOW GAMES FROM THE NEW LIST
        if (_gameList != null)
        {
            gameInfo.setText("Game: " + _gameList[i].name + "\n" +
                    " Status: " + _gameList[i].status);
        }
        return gameInfo;
    }

    @Override
    public int getItemViewType(int i)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        return GameCollection.getInstance().getGamelist().size() <= 0;
    }
}
