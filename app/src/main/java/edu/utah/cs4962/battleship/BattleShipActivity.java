package edu.utah.cs4962.battleship;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class BattleShipActivity extends Activity
{
    public final static String PLAYERS_TURN = "players_turn";
    public FragmentManager fragmentManager;
    public GameFragment _gameFragment;
    public GameListFragment _gameListFragment;
    public FragmentTransaction _addTransaction;
    public NetworkClass _networkClass;

    LinearLayout secondLayout = null;
    LinearLayout gameListLayout = null;
    FrameLayout gameLayout = null;

    LinearLayout.LayoutParams params = null;

    Gson _gson = new Gson();

    // Key -> gameId, Value -> playerId
    HashMap<String, String> MyNetworkGames = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
            savedInstanceState = null;

        super.onCreate(savedInstanceState);

        if (isTabletDevice(getResources()))
        {
            TabletMode();
        } else
        {
            PhoneMode();
        }
    }

    private void PhoneMode()
    {
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout firstLayout = new LinearLayout(this);
        firstLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        firstLayout.addView(buttonLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        _gameFragment = new GameFragment();
        _gameListFragment = new GameListFragment();

        // Second Layout
        secondLayout = new LinearLayout(this);
        secondLayout.setOrientation(LinearLayout.HORIZONTAL);

        gameListLayout = new LinearLayout(this);
        gameListLayout.setId(11);
        gameListLayout.setBackgroundColor(Color.CYAN);

        gameLayout = new FrameLayout(this);
        gameLayout.setId(10);

//region NewGameButton

        Button newGameButton = new Button(this);
        newGameButton.setText("New Game");
        buttonLayout.addView(newGameButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        newGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Game newGame = new Game();
                // Add the game to the Fragment List.
                _gameListFragment.AddItemGameToList(newGame);
            }
        });

        LinearLayout.LayoutParams backButtonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backButtonParams.gravity = Gravity.RIGHT;
        Button backButton = new Button(this);
        backButton.setText("Back");
        buttonLayout.addView(backButton, backButtonParams);

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setProperWindowSize(true);

                showWindow("List");

                onPause();
            }
        });

        _gameListFragment.setOnGameSelectedListener(new GameListFragment.OnGameSelectedListener()
        {
            @Override
            public void onGameSelected(GameListFragment gameListFragment, NetworkClass.Game g)
            {
                NetworkClass.Game game = g;

                setProperWindowSize(false);

                //_gameFragment.setGame(game);

            }
        });

        _gameFragment.setOnUpdateGameListListener(new GameFragment.OnUpdateGameListListener()
        {
            @Override
            public void OnUpdateGameList(GameFragment gameFragment)
            {
                _gameListFragment.updateList();
            }
        });

//endregion NewGameButton

        secondLayout.addView(gameListLayout, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 100
        ));

        params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 0);

        secondLayout.addView(gameLayout, params);


        fragmentManager = getFragmentManager();
        _addTransaction = fragmentManager.beginTransaction();

        _addTransaction.add(10, _gameFragment);

        _addTransaction.add(11, _gameListFragment);

        _addTransaction.commit();

        rootLayout.addView(firstLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        rootLayout.addView(secondLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 90
        ));
        setContentView(rootLayout);
    }

    private void TabletMode()
    {
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout firstLayout = new LinearLayout(this);
        firstLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        firstLayout.addView(buttonLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        _gameFragment = new GameFragment();
        _gameListFragment = new GameListFragment();

        // Second Layout
        secondLayout = new LinearLayout(this);
        secondLayout.setOrientation(LinearLayout.HORIZONTAL);

        gameListLayout = new LinearLayout(this);
        gameListLayout.setId(11);
        gameListLayout.setBackgroundColor(Color.CYAN);

        gameLayout = new FrameLayout(this);
        gameLayout.setId(10);

        _networkClass = new NetworkClass();
        _networkClass.getGameList(this); // Get game list from network.

        //region <Network Listeners>

        _networkClass.setOnGameListArrivedListener(new NetworkClass.OnGameListArrivedListener()
        {
            @Override
            public void OnGameListArrived(NetworkClass networkClass, NetworkClass.Game[] _games)
            {
                _gameListFragment.setGameList(_games);
            }
        });

        _networkClass.setOnBattleGridUpdatedListener(new NetworkClass.OnBattleGridUpdatedListener()
        {
            @Override
            public void OnBattleGridUpdated(NetworkClass networkClass, NetworkClass.BattleGrid battleGrid)
            {
                if (battleGrid != null)
                {
                    _gameFragment.setGame(battleGrid);
                } else
                {
                    Toast.makeText(BattleShipActivity.this, "You cannot play this game", Toast.LENGTH_SHORT).show();
                }
            }
        });

        _networkClass.set_onGameDetailArrivedListener(new NetworkClass.OnGameDetailArrivedListener()
        {
            @Override
            public void OnGameDetailArrived(NetworkClass networkClass, NetworkClass.GameDetail gameDetail)
            {
                NetworkClass.GameDetail g = gameDetail;
            }
        });

        //endregion <Network Listeners>


//region NewGameButton

        Button newGameButton = new Button(this);
        newGameButton.setText("New Game");
        buttonLayout.addView(newGameButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        newGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Game newGame = new Game();
//                // Add the game to the Fragment List.
//                _gameListFragment.AddItemGameToList(newGame);


                // Get the game stuff
                // _networkClass.requestNewGame(BattleShipActivity.this, "Name of the game goes in here", "Figure out a way to get player name in here");

                //Start the new game activity
                startNewGameActivity(BattleShipActivity.this);
            }
        });


        _gameListFragment.setOnGameSelectedListener(new GameListFragment.OnGameSelectedListener()
        {
            @Override
            public void onGameSelected(GameListFragment gameListFragment, NetworkClass.Game g)
            {

                //_networkClass.requestGameDetail(BattleShipActivity.this, g.id);

                // Get the player id to retrieve the battle grid.
                String playerId = MyNetworkGames.get(g.id);


                // Request the game.
                // Will only retrieve if the game
                // corresponds to this player and if the game is in progress.
                _networkClass.initBattleGrid(BattleShipActivity.this, g.id, playerId);

//                _gameFragment.setGame(game);
            }
        });

        _gameFragment.setOnUpdateGameListListener(new GameFragment.OnUpdateGameListListener()
        {
            @Override
            public void OnUpdateGameList(GameFragment gameFragment)
            {
                _gameListFragment.updateList();
            }
        });

//endregion NewGameButton


        secondLayout.addView(gameListLayout, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 20
        ));

        params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 80);

        secondLayout.addView(gameLayout, params);


        fragmentManager = getFragmentManager();
        _addTransaction = fragmentManager.beginTransaction();

        _addTransaction.add(10, _gameFragment);

        _addTransaction.add(11, _gameListFragment);

        _addTransaction.commit();

        rootLayout.addView(firstLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        rootLayout.addView(secondLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 90
        ));
        setContentView(rootLayout);
    }


    private void showWindow(String whatWindow)
    {
        if (whatWindow.equals("List"))
        {
            gameListLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 100));

            gameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 0));

        } else if (whatWindow.equals("Fragment"))
        {
            gameListLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 0));

            gameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 100));
        }
    }

    public void setProperWindowSize(boolean isListView)
    {
        if (!isListView)
        {
            gameListLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 0));

            gameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 100));

        } else
        {
            gameListLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 20));

            gameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 80));
        }

    }

    private boolean isTabletDevice(Resources resources)
    {
        int screenLayout = resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean isScreenLarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE);
        boolean isScreenXlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        return (isScreenLarge || isScreenXlarge);
    }

    private void startNewGameActivity(Context context)
    {
        Intent intent = new Intent(context, CreateNewGameActivity.class);

        startActivityForResult(intent, 6);
    }

    public void startGameFragment()
    {
        _addTransaction = fragmentManager.beginTransaction();
        _addTransaction.replace(10, _gameFragment);

        _addTransaction.addToBackStack(null);
        _addTransaction.commit();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        File filesDir = getFilesDir();
        try
        {
            // Retrieve the selected game.
            File file = new File(filesDir, "selectedGame.txt");
            FileReader textReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(textReader);

            try
            {
                _gameListFragment.selectedGame = Integer.parseInt(bufferedReader.readLine());
            } catch (Exception e)
            {
                String ex = e.toString();
            }
            bufferedReader.close();

            // Retrieve Game List
            file = new File(filesDir, "gameList.txt");
            textReader = new FileReader(file);

            bufferedReader = new BufferedReader(textReader);
            String jsonGameList;
            jsonGameList = bufferedReader.readLine();

            Type gameListType = new TypeToken<ArrayList<Game>>()
            {
            }.getType();
            ArrayList<Game> gameList = _gson.fromJson(jsonGameList, gameListType);

            _gameListFragment.setGameList(gameList);
            bufferedReader.close();

            // Retrieve the users network games.
            file = new File(filesDir, "myNetworkGames.txt");
            textReader = new FileReader(file);

            bufferedReader = new BufferedReader(textReader);
            String jsonNetworkGames = bufferedReader.readLine();

            Type networkGamesType = new TypeToken<HashMap<String, String>>()
            {
            }.getType();
            HashMap<String, String> networkGames = _gson.fromJson(jsonNetworkGames, networkGamesType);

            MyNetworkGames = networkGames;

            bufferedReader.close();

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        File filesDir = getFilesDir();
        String jsonGameList = _gson.toJson(GameCollection.getInstance().getGamelist());

        // Uncomment this line of code to be able to set a fresh version of the app.
        //String jsonGameList = _gson.toJson(new ArrayList<Game>());
        try
        {
            // Save the game list.
            File file = new File(filesDir, "gameList.txt");
            FileWriter textWriter = null;
            textWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);

            bufferedWriter.write(jsonGameList);
            bufferedWriter.close();

            // Save the selected game
            file = new File(filesDir, "selectedGame.txt");
            textWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(textWriter);

            bufferedWriter.write(_gameListFragment.selectedGame + "");
            bufferedWriter.close();


            // Save the users network games.
            // TODO:
            file = new File(filesDir, "myNetworkGames.txt");
            textWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(textWriter);

            // Uncomment this to populate the network game list.
            MyNetworkGames = new HashMap<String, String>();
            MyNetworkGames.put("91b428bd-9fe4-487c-8aba-946040a6392c", "b891ff56-ef53-4e50-8b11-f0070bbf4f02");

            String jsonNetWorkGames = _gson.toJson(MyNetworkGames);
            bufferedWriter.write(jsonNetWorkGames);
            bufferedWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public void startTrasitionScreenActivity(String playersTurn)
    {
//        Intent intent = new Intent(BattleShipActivity.this, TransitionScreen.class);
//
//        intent.putExtra(PLAYERS_TURN, playersTurn);
//        this.startActivity(intent);
    }


}
