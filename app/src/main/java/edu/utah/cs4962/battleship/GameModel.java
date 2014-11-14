package edu.utah.cs4962.battleship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by jesuszarate on 10/28/14.
 */
public class GameModel
{
    public ArrayList<Game> Games;
    public static int WATER = 0;
    public static int MISS = 1;
    public static int HIT = 2;

    public GameModel()
    {
        Games = new ArrayList<Game>();
    }

    public void CreateNewGame()
    {
        Games.add(new Game());
    }

    public void UpdateGame()
    {

    }

    public void DeleteGame()
    {

    }

    public String ReadGame()
    {
        return "";
    }

}
