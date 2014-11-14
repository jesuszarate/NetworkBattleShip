package edu.utah.cs4962.battleship;

import java.util.HashMap;


/**
 * Created by jesuszarate on 10/28/14.
 */
public class Player
{
    public static final int Player1ID = 1;
    public static final int Player2ID = 2;

    private int _playerID = -1;
    HashMap<Integer, Integer> _myGameGrid;
    HashMap<Integer, Integer> _oponentGameGrid;
    HashMap<Integer, Integer> _oponentsAttacks;
    Ships _ships = new Ships();
    final static String HIT = "HIT";
    final static String MISS = "MISS";

    int PlayersScore = 0;
    int PlayersMissilesFired = 0;

    int FinalScore  = 17;

    //region Listeners

    //region WonListener
    public interface OnPlayerWonListener
    {
        public void OnPlayerWon(Player p);
    }
    OnPlayerWonListener _onPlayerWonListener = null;

    public OnPlayerWonListener getOnPlayerWonListener()
    {
        return _onPlayerWonListener;
    }

    public void setOnPlayerWonListener(OnPlayerWonListener onPlayerWonListener)
    {
        this._onPlayerWonListener = onPlayerWonListener;
    }
    //endregion WonListener

    //region MissileFiredListener

    public interface OnMissileFiredListener
    {
        public void OnMissileFired(Player p, int cell);
    }

    OnMissileFiredListener _onMissileFiredListener = null;

    public OnMissileFiredListener getOnMissileFiredListener()
    {
        return _onMissileFiredListener;
    }

    public void setOnMissileFiredListener(OnMissileFiredListener _onMissileFiredListener)
    {
        this._onMissileFiredListener = _onMissileFiredListener;
    }

    //endregion

    //region MissileAttackResultListener

    public interface OnMissileAttackResultListener
    {
        public void OnMissileAttackResult(Player p, int cell, String hitMiss);
    }

    OnMissileAttackResultListener _onMissileAttackResultListener = null;

    public OnMissileAttackResultListener getOnMissileAttackResultListener()
    {
        return _onMissileAttackResultListener;
    }

    public void setOnMissileAttackResultListener(OnMissileAttackResultListener _onMissileAttackResultListener)
    {
        this._onMissileAttackResultListener = _onMissileAttackResultListener;

    }
    //endregion MissileAttackResultListener


    //region MissileAlreadyFiredAtThisPositionListener

    public interface OnMissileAlreadyFiredAtThisPositionListener{
        public void OnMissileAlreadyFiredAtThisPosition();
    }
    OnMissileAlreadyFiredAtThisPositionListener _onMissileAlreadyFiredAtThisPositionListener = null;

    public void setMissileAlreadyFiredAtThisPositionListener(OnMissileAlreadyFiredAtThisPositionListener onMissileAlreadyFiredAtThisPositionListener)
    {
        this._onMissileAlreadyFiredAtThisPositionListener = onMissileAlreadyFiredAtThisPositionListener;
    }
    //endregion MissileAlreadyFiredAtThisPositionListener

    //endregion

    public Player(int playerID)
    {
        _playerID = playerID;
        _myGameGrid = new HashMap<Integer, Integer>();
        _oponentGameGrid = new HashMap<Integer, Integer>();
        _oponentsAttacks = new HashMap<Integer, Integer>();

        for (int cellIndex = 0; cellIndex < 100; cellIndex++)
        {
            _myGameGrid.put(cellIndex, GameModel.WATER);
            //_oponentGameGrid.put(cellIndex, GameModel.WATER);
        }
        _ships.setMyShipsRandomly();
    }


    public void addOponentAttack(int cell, int miss)
    {
        _oponentsAttacks.put(cell, miss);
    }

    public void updateAttackResult(int cell, int result)
    {
        if (result == GameModel.HIT)
        {
            PlayersScore++;
            if(PlayersScore >= FinalScore){
                _onPlayerWonListener.OnPlayerWon(Player.this);
            }
        }
        _oponentGameGrid.put(cell, result);
    }

    /**
     * Record the missile fired.
     */
    public void LaunchMissile(int cell)
    {
        // TODO: Update grid to indicate missile fired
        if (recordMissileFired(cell))
        {
            PlayersMissilesFired++;
            _onMissileFiredListener.OnMissileFired(this, cell);
        }
        else {
            _onMissileAlreadyFiredAtThisPositionListener.OnMissileAlreadyFiredAtThisPosition();
        }
    }


    /**
     * Check if the missile fired is a hit on one of the
     * ships.
     */
    public void OponentFiredMissile(int cell)
    {
        String result = _ships.checkIfHit(cell);

        if (result.equals(MISS))
        {
            _oponentsAttacks.put(cell, GameModel.MISS);
        } else if (result.equals(HIT))
        {
            _oponentsAttacks.put(cell, GameModel.HIT);
        }

        _onMissileAttackResultListener.OnMissileAttackResult(this, cell, result);
    }

    /**
     * If missile has already been launched at this position
     * it will return false. Otherwise it will return true.
     *
     * @param cell
     */
    private boolean recordMissileFired(int cell)
    {
        if (_oponentGameGrid.containsKey(cell))
        {
            int result = _oponentGameGrid.get(cell);

            if (result == GameModel.MISS || result == GameModel.HIT)
            {
                return false;
            } else
            {
                return true;
            }
        }
        return true;
    }
}
