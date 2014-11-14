package edu.utah.cs4962.battleship;

/**
 * Created by jesuszarate on 10/28/14.
 */
public class Game
{

    Player player1;
    Player player2;

    String Winner;

    boolean GameOver = false;

    private int playersTurn = 1;

    //region getter/setters
    public int getPlayersTurn()
    {
        return playersTurn;
    }

    public void setPlayersTurn(int playersTurn)
    {
        this.playersTurn = playersTurn;
    }
    //endregion getter/setters

    //region Listeners
    //region BattleWon Listener
    public interface OnBattleWonListener
    {
        public void OnBattleWon(Game game, String Winner);
    }

    OnBattleWonListener _onBattleWonListener = null;

    public void setOnBattleWonListener(OnBattleWonListener onBattleWonListener)
    {
        this._onBattleWonListener = onBattleWonListener;
    }
    //endregion BattleWon Listener
//endregion Listeners

    public Game()
    {
        player1 = new Player(1);
        player2 = new Player(2);


    }

    public Player getPlayer(int playerID)
    {
        if (playerID == GameFragment.PLAYER1)
        {
            return player1;
        } else //if (playerID == GameFragment.PLAYER2)
        {
            return player2;
        }
    }

    public int getPlayer1Score()
    {
        return player1.PlayersScore;
    }

    public int getPlayer2Score()
    {
        return player2.PlayersScore;
    }

    public int getPlayer1MissilesFired()
    {
        return player1.PlayersMissilesFired;
    }

    public int getPlayer2MissilesFired()
    {
        return player2.PlayersMissilesFired;
    }
}
