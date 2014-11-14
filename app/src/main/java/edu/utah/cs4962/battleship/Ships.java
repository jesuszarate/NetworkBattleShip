package edu.utah.cs4962.battleship;

import android.graphics.Color;
import android.graphics.Path;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by jesuszarate on 10/26/14.
 */
public class Ships
{
    public MyShips myShips;
    public OponentsShips oponentsShips;
    //    final private int LEFT = 0;
//    final private int UP = 1;
    final private int RIGHT = 0;
    final private int DOWN = 1;

    HashSet<Integer> takenPositions;

    public interface OnMissileFiredListener
    {
        public void onMissileFired(Ships ships);
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

    public Ships()
    {
        myShips = new MyShips();
        oponentsShips = new OponentsShips();
        takenPositions = new HashSet<Integer>();
    }

    public String checkIfHit(int cell)
    {
        for (int[] ship : myShips.ships.values())
        {
            for (int shipPosition = 0; shipPosition < ship.length; shipPosition++)
            {
                if (ship[shipPosition] == cell)
                {
                    return Player.HIT;
                }
            }
        }
        return Player.MISS;
    }


    public class MyShips
    {
        public HashMap<String, int[]> ships;

        public MyShips()
        {
            ships = new HashMap<String, int[]>();
            int[] AirCraftCarrier = {-1, -2, -3, -4, -5};
            int[] Battleship = {-1, -2, -3, -4};
            int[] Submarine = {-1, -2, -3};
            int[] Cruiser = {-1, -2, -3};
            int[] Destroyer = {-1, -2};

            ships.put("AirCraftCarrier", AirCraftCarrier);
            ships.put("Battleship", Battleship);
            ships.put("Submarine", Submarine);
            ships.put("Cruiser", Cruiser);
            ships.put("Destroyer", Destroyer);
        }

    }

    public class OponentsShips
    {
        public HashMap<String, int[]> ships;

        public OponentsShips()
        {
            ships = new HashMap<String, int[]>();
            ships.put("AirCraftCarrier", new int[5]);
            ships.put("Battleship", new int[4]);
            ships.put("Submarine", new int[3]);
            ships.put("Cruiser", new int[3]);
            ships.put("Destroyer", new int[2]);
        }
    }

    public void setMyShipsRandomly()
    {
        setAirCraftCarrier();
        setBattleship();
        setSubmarine();
        setCruiser();
        setDestroyer();

    }

    private void setDestroyer()
    {
        boolean correctPositioning = false;
        while (correctPositioning == false)
        {
            int position = randomPositionOfShip();

            int direction = randomShipDirection();

            int arrayIndex = 0;
            switch (direction)
            {
                case RIGHT:
                    correctPositioning = getRightCell("Destroyer", 2, position, arrayIndex);
                    break;
                case DOWN:
                    correctPositioning = getBottomCell("Destroyer", 2, position, arrayIndex);
                    break;
            }
        }
    }

    private void setCruiser()
    {
        boolean correctPositioning = false;
        while (correctPositioning == false)
        {
            int position = randomPositionOfShip();

            int direction = randomShipDirection();

            int arrayIndex = 0;

            switch (direction)
            {
                case RIGHT:
                    correctPositioning = getRightCell("Cruiser", 3, position, arrayIndex);
                    break;
                case DOWN:
                    correctPositioning = getBottomCell("Cruiser", 3, position, arrayIndex);
                    break;
            }
        }
    }

    private void setSubmarine()
    {
        boolean correctPositioning = false;
        while (correctPositioning == false)
        {
            int position = randomPositionOfShip();

            int direction = randomShipDirection();

            int arrayIndex = 0;

            switch (direction)
            {
                case RIGHT:
                    correctPositioning = getRightCell("Submarine", 3, position, arrayIndex);
                    break;
                case DOWN:
                    correctPositioning = getBottomCell("Submarine", 3, position, arrayIndex);
                    break;
            }
        }
    }

    private void setBattleship()
    {
        boolean correctPositioning = false;
        while (correctPositioning == false)
        {
            int position = randomPositionOfShip();

            int direction = randomShipDirection();

            int arrayIndex = 0;

            switch (direction)
            {
                case RIGHT:
                    correctPositioning = getRightCell("Battleship", 4, position, arrayIndex);
                    break;
                case DOWN:
                    correctPositioning = getBottomCell("Battleship", 4, position, arrayIndex);
                    break;
            }

        }
    }

    private void setAirCraftCarrier()
    {
        boolean correctPositioning = false;
        while (correctPositioning == false)
        {
            int position = randomPositionOfShip();

            int direction = randomShipDirection();

            int arrayIndex = 0;

            switch (direction)
            {
                case RIGHT:
                    correctPositioning = getRightCell("AirCraftCarrier", 5, position, arrayIndex);
                    break;
                case DOWN:
                    correctPositioning = getBottomCell("AirCraftCarrier", 5, position, arrayIndex);
                    break;
            }

        }
    }

    private void getTopCell(String shipType, int shipSize, int position, int arrayIndex)
    {
        int range = position - shipSize;
        for (; position > range; position--)
        {
            myShips.ships.get(shipType)[arrayIndex] = position;
            arrayIndex++;
        }
    }

    private boolean getRightCell(String shipType, int shipSize, int position, int arrayIndex)
    {
        int range = position + shipSize;
        int pos = position;
        int actualPostion = 0;
        for (; position < range; position++)
        {
            try
            {
                actualPostion = pos + (arrayIndex * 10);
                if (actualPostion > 100)
                {
                    throw new IndexOutOfBoundsException();
                }

                if(takenPositions.contains(actualPostion))
                {
                   return false;
                }
                myShips.ships.get(shipType)[arrayIndex] = actualPostion;
                takenPositions.add(actualPostion);
                arrayIndex++;
            } catch (IndexOutOfBoundsException e)
            {
                return false;
            }
        }
        return true;
    }

    private boolean getBottomCell(String shipType, int shipSize, int position, int arrayIndex)
    {
        int colRange = position;
        int range = position + shipSize;
        for (; position < range; position++)
        {

            try
            {
//                if (shipIsNotinThisPosition(position))
//                {
//                    throw new IndexOutOfBoundsException();
//                }
                if (position != colRange && (position % 10) < (colRange % 10))
                {
                    throw new IndexOutOfBoundsException();
                }
                if (takenPositions.contains(position))
                {
                    return false;
                }
                myShips.ships.get(shipType)[arrayIndex] = position;
                takenPositions.add(position);
                arrayIndex++;
            } catch (IndexOutOfBoundsException e)
            {
                return false;
            }
        }
        return true;
    }

    private boolean shipIsNotinThisPosition(int position)
    {
        for (int[] ships : myShips.ships.values())
        {
            for (int shipPos = 0; shipPos < ships.length; shipPos++)
            {
                if (ships[shipPos] == position)
                {
                    return false;
                }
            }
        }
        return true;
    }


    private Random randomGenerator = new Random(System.currentTimeMillis());

    public int randomPositionOfShip()
    {
        int randomInt = randomGenerator.nextInt(100);
        return randomInt;
    }

    public int randomShipDirection()
    {
        int randomInt = randomGenerator.nextInt(2);
        return randomInt;
    }
}


