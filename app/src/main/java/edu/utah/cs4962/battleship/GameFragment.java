package edu.utah.cs4962.battleship;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Jesus Zarate on 10/24/14.
 */
public class GameFragment extends Fragment
{
    final public int _player1ID = 1;
    final public int _player2ID = 2;
    Game _game;

    static final String PlayerOne = "PLAYER 1";
    static final String PlayerTwo = "PLAYER 2";

    final static public int PLAYER1 = 1;
    final static public int PLAYER2 = 2;
    public Player player1;
    public Player player2;
    int _playersTurn = 1;

    public final static String ALLOWED_TO_TOUCH = "allowed_to_touch";
    public boolean _allowedToTouch = true;

    Timer timer = new Timer();

    public final static String GAME_STATUS_DONE = "DONE";
    public final static String GAME_STATUS_WAITING = "WAITING";
    public final static String GAME_STATUS_PLAYING = "PLAYING";



    //region Listeners

    BattleGridView battleGridView = null;

    //region UpdateListViewsListener

    public interface OnUpdateGameListListener
    {
        public void OnUpdateGameList(GameFragment gameFragment);
    }

    OnUpdateGameListListener _onUpdateGameListListener = null;

    public void setOnUpdateGameListListener(OnUpdateGameListListener onUpdateGameListListener)
    {
        this._onUpdateGameListListener = onUpdateGameListListener;
    }
    //endregion UpdateListViewsListener

    public interface OnLaunchMissileListener
    {
        public void OnLauchMissile(GameFragment gameFragment, int xPos, int yPos);
    }
    OnLaunchMissileListener _onLaunchMissileListener = null;

    public void setOnMissileLaunchListener(OnLaunchMissileListener onMissileLaunchListener)
    {
        this._onLaunchMissileListener = onMissileLaunchListener;
    }
    //endregion

    // TODO: UPDATE EVERYTHING IN THIS METHOD TO MATCH THE INFORMATION ON THE NETWORK GAME.

    public void InitializeGame(Game game)
    {
        _game = game;
        player1 = _game.getPlayer(PLAYER1);
        player2 = _game.getPlayer(PLAYER2);
        _playersTurn = _game.getPlayersTurn();
        _allowedToTouch = true;
    }

    public void recordAttack(int xPos, int yPos, String attackResult)
    {
        battleGridView.recordAttack(xPos, yPos, attackResult);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        battleGridView = new BattleGridView(getActivity(), _player1ID);

        return battleGridView;
    }

    public void setGame(HashMap<String, NetworkClass.Cell[]> battleGrid)
    {
        battleGridView.setUpGridBackground();
        int cellPostion = 0;
        for (int position = 0; position < 200; position++)
        {
            CellView cellView = new CellView(getActivity());

            cellView.setGridPosition(cellPostion);

            battleGridView._childrenIndex.put(cellPostion, position);

            cellPostion++;

            battleGridView.addView(cellView, 55, 55);
            cellView.setOnCellTouchListener(new CellView.OnCellTouchListener()
            {
                @Override
                public void onCellTouched(CellView c)
                {
                    if (c.getGridPosition() <= 100 )
                    {

                        int xPos = c.getGridPosition() / 10;
                        int yPos = c.getGridPosition() % 10;

                        BattleShipActivity.LaunchedMissileAtPosition = c.getGridPosition();

                        // Launch missile.
                        _onLaunchMissileListener.OnLauchMissile(GameFragment.this, xPos, yPos);

                    }
                }
            });
        }
        battleGridView.switchPlayersBattleGrid(battleGrid);
    }

    public void setGame(Game game)
    {
        //InitializeGame(game);
        battleGridView.setUpGridBackground();
        int cellPostion = 0;
        for (int position = 0; position < 200; position++)
        {
            CellView cellView = new CellView(getActivity());

            cellView.setGridPosition(cellPostion);

            battleGridView._childrenIndex.put(cellPostion, position);

            cellPostion++;

            battleGridView.addView(cellView, 55, 55);
            cellView.setOnCellTouchListener(new CellView.OnCellTouchListener()
            {
                @Override
                public void onCellTouched(CellView c)
                {
                    if (c.getGridPosition() <= 100 )//&& !_game.GameOver)
                    {
//                        if (_game.getPlayersTurn() == PLAYER1 && _allowedToTouch)
//                        {
//                            _allowedToTouch = false;
//                            _game.getPlayer(PLAYER1).LaunchMissile(c.getGridPosition());
//
//                        } else if (_game.getPlayersTurn() == PLAYER2 && _allowedToTouch)
//                        {
//                            _allowedToTouch = false;
//                            _game.getPlayer(PLAYER2).LaunchMissile(c.getGridPosition());
//                        }
                    }
                }
            });
        }

        //region CommentedCode
/*
        if (_playersTurn == 1)
        {
            battleGridView.switchPlayersBattleGrid(player1, player2);
        } else
        {
            battleGridView.switchPlayersBattleGrid(player2, player1);
        }

        //region <Fire Missiles>
        player1.setOnMissileFiredListener(new Player.OnMissileFiredListener()
        {
            @Override
            public void OnMissileFired(Player p, int cell)
            {
                //player1.addMyAttackToOponentGrid(cell);
                player2.OponentFiredMissile(cell);
            }
        });


        player2.setOnMissileFiredListener(new Player.OnMissileFiredListener()
        {
            @Override
            public void OnMissileFired(Player p, int cell)
            {
                player1.OponentFiredMissile(cell);
            }
        });

        //endregion <Fire Missiles>

        //region MissileAlreadyFiredAtThisPosition

        player1.setMissileAlreadyFiredAtThisPositionListener(new Player.OnMissileAlreadyFiredAtThisPositionListener()
        {
            @Override
            public void OnMissileAlreadyFiredAtThisPosition()
            {
                _allowedToTouch = true;
            }
        });

        player2.setMissileAlreadyFiredAtThisPositionListener(new Player.OnMissileAlreadyFiredAtThisPositionListener()
        {
            @Override
            public void OnMissileAlreadyFiredAtThisPosition()
            {
                _allowedToTouch = true;
            }
        });

        //endregion MissileAlreadyFiredAtThisPosition

        //region Missile Attack Result
        player1.setOnMissileAttackResultListener(new Player.OnMissileAttackResultListener()
        {
            @Override
            public void OnMissileAttackResult(Player p, int cell, String hitMiss)
            {
                if (hitMiss.equals(Player.HIT))
                {
                    battleGridView.cellHit(cell);
                    player2.updateAttackResult(cell, GameModel.HIT);
                    _onUpdateGameListListener.OnUpdateGameList(GameFragment.this);

                    _allowedToTouch = true;
                } else
                {
                    battleGridView.cellMiss(cell);
                    player2.updateAttackResult(cell, GameModel.MISS);
                    _onUpdateGameListListener.OnUpdateGameList(GameFragment.this);

                    timer.schedule(new SwitchPlayerTask(), 2000);
                }

            }
        });
        player2.setOnMissileAttackResultListener(new Player.OnMissileAttackResultListener()
        {
            @Override
            public void OnMissileAttackResult(Player p, int cell, String hitMiss)
            {
                if (hitMiss.equals(Player.HIT))
                {
                    battleGridView.cellHit(cell);
                    player1.updateAttackResult(cell, GameModel.HIT);
                    _allowedToTouch = true;
                    _onUpdateGameListListener.OnUpdateGameList(GameFragment.this);
                } else
                {
                    battleGridView.cellMiss(cell);
                    player1.updateAttackResult(cell, GameModel.MISS);
                    _onUpdateGameListListener.OnUpdateGameList(GameFragment.this);
                    timer.schedule(new SwitchPlayerTask(), 2000);
                }

            }
        });
        //endregion Missile Attack Result

        //region PlayerWon

        player1.setOnPlayerWonListener(new Player.OnPlayerWonListener()
        {
            @Override
            public void OnPlayerWon(Player p)
            {
                _game.Winner = "PLAYER1 WON!";
                _game.GameOver = true;

            }
        });

        player2.setOnPlayerWonListener(new Player.OnPlayerWonListener()
        {
            @Override
            public void OnPlayerWon(Player p)
            {
                _game.Winner = "PLAYER2 WON!";
                _game.GameOver = true;
                Toast.makeText(getActivity(), _game.Winner, Toast.LENGTH_SHORT).show();
//                _onBattleWonListener.OnBattleWon(Game.this, Winner);
            }
        });

        //endregion PlayerWon
        */

        //endregion CommentedCode
    }


    public Player getPlayersTurn(int playerID)
    {
        return playerID == 1 ? player1 : player2;
    }

    public Player getOponent(int currentPlayerID)
    {
        return currentPlayerID == 1 ? player2 : player1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            _allowedToTouch = data.getBooleanExtra(ALLOWED_TO_TOUCH, false);

            _playersTurn = _playersTurn == PLAYER1 ? PLAYER2 : PLAYER1;
            _game.setPlayersTurn(_playersTurn);
            Player currentPlayer = getPlayersTurn(_playersTurn);
            Player oponentPlayer = getOponent(_playersTurn);
            battleGridView.switchPlayersBattleGrid(currentPlayer, oponentPlayer);

            String ActivityLabel = _playersTurn == PLAYER1 ? PlayerOne : PlayerTwo;
            getActivity().setTitle(ActivityLabel);

            _onUpdateGameListListener.OnUpdateGameList(GameFragment.this);

        }
    }

    public static String PLAYERS_TURN = "players_turn";

    class SwitchPlayerTask extends TimerTask
    {
        public void run()
        {
            Intent intent = new Intent(getActivity(), TransitionScreen.class);

            if (_game.getPlayersTurn() == Player.Player1ID)
            {
                intent.putExtra(PLAYERS_TURN, PlayerOne);

            } else
            {
                intent.putExtra(PLAYERS_TURN, PlayerTwo);
            }
            startActivityForResult(intent, 6);
        }
    }

}
