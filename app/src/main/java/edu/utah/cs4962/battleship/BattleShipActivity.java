package edu.utah.cs4962.battleship;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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


public class BattleShipActivity extends Activity
{
    public final static String PLAYERS_TURN = "players_turn";
    public FragmentManager fragmentManager;
    public GameFragment _gameFragment;
    GameListFragment _gameListFragment;
    FragmentTransaction _addTransaction;

    LinearLayout secondLayout = null;
    LinearLayout gameListLayout = null;
    FrameLayout gameLayout = null;

    LinearLayout.LayoutParams params = null;

    Gson _gson = new Gson();

    boolean TransitionScreenUp = false;

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
            public void onGameSelected(GameListFragment gameListFragment, Game g)
            {
                Game game = g;

                setProperWindowSize(false);

                _gameFragment.setGame(game);

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


        _gameListFragment.setOnGameSelectedListener(new GameListFragment.OnGameSelectedListener()
        {
            @Override
            public void onGameSelected(GameListFragment gameListFragment, Game g)
            {
                Game game = g;

                _gameFragment.setGame(game);

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
            File file = new File(filesDir, "gameList.txt");
            FileWriter textWriter = null;
            textWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);

            bufferedWriter.write(jsonGameList);
            bufferedWriter.close();

            file = new File(filesDir, "selectedGame.txt");
            textWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(textWriter);

            bufferedWriter.write(_gameListFragment.selectedGame + "");
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
