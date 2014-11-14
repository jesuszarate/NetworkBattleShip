package edu.utah.cs4962.battleship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jesuszarate on 10/31/14.
 */
public class GameCollection
{
    //Map<Integer, Game> _gameListMap = new HashMap<Integer, Game>();
    ArrayList<Game> _gamelist = new ArrayList<Game>();

    static GameCollection _instance = null;

    static GameCollection getInstance()
    {
        if (_instance == null)
        {
            _instance = new GameCollection();
        }
        return _instance;
    }

    private GameCollection()
    {
    }

    public ArrayList<Game> getGamelist()
    {
        return _gamelist;
    }

    public Game getGame(int identifier)
    {
        return _gamelist.get(identifier);
    }

    public void addGame(Game game)
    {
        _gamelist.add(game);
    }

    public void removeGame(int identifier)
    {
        _gamelist.remove(identifier);
    }

    public class GameInfo
    {
        public String Winner;
        public String Player1MissilesLaunched;
        public String Player2MissilesLaunched;
        public boolean inProgress;
    }
}
