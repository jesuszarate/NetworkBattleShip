package edu.utah.cs4962.battleship;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by jesuszarate on 11/14/14.
 */
public class NetworkClass
{

    private static final String BASE_URL = "http://battleship.pixio.com/api/games/";

    Gson _gson = new Gson();


    //region <Listeners>

    public interface OnGameListArrivedListener
    {
        public void OnGameListArrived(NetworkClass networkClass, Game[] _games);
    }

    OnGameListArrivedListener _onGameListArrivedListener = null;

    public void setOnGameListArrivedListener(OnGameListArrivedListener onGameListArrivedListener)
    {
        this._onGameListArrivedListener = onGameListArrivedListener;
    }


    public interface OnGameDetailArrivedListener
    {
        public void OnGameDetailArrived(NetworkClass networkClass, NetworkClass.GameDetail gameDetail);
    }
    OnGameDetailArrivedListener _onGameDetailArrivedListener = null;

    public void set_onGameDetailArrivedListener(OnGameDetailArrivedListener _onGameDetailArrivedListener)
    {
        this._onGameDetailArrivedListener = _onGameDetailArrivedListener;
    }

    public interface OnNewGameInfoArrivedListener
    {
        public void OnGameInfoArrived(NetworkClass networkClass, HashMap<String, String> gameInfo);
    }
    OnNewGameInfoArrivedListener _onNewGameInfoArrivedListener = null;

    public void setOnNewGameInfoArrivedListener(OnNewGameInfoArrivedListener onNewGameInfoArrivedListener)
    {
        this._onNewGameInfoArrivedListener = onNewGameInfoArrivedListener;
    }
    //endregion <Listeners>


    //region GameList

    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void getGameList(Context context)
    {
        // Gets the URL from the UI's text field.
        String stringUrl = BASE_URL;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new DownloadGameListTask().execute(stringUrl);
        } else
        {
            //textView.setText("No network connection available.");
        }
    }

    private class DownloadGameListTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {

            // params comes from the execute() call: params[0] is the url.
            try
            {
                return downloadUrl(urls[0]);
            } catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Game[] _games = parseJson(result);

            _onGameListArrivedListener.OnGameListArrived(NetworkClass.this, _games);
        }

    }
    //endregion GameList

    //region GameDetail
    public void requestGameDetail(Context context, String GameId)
    {
        // Gets the URL from the UI's text field.
        String stringUrl = BASE_URL + GameId;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new DownloadGameDetailTask().execute(stringUrl);
        } else
        {
            //textView.setText("No network connection available.");
        }

    }

    private class DownloadGameDetailTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {

            // params comes from the execute() call: params[0] is the url.
            try
            {
                return downloadUrl(urls[0]);
            } catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            GameDetail gameDetail = parseGameDetail(result);

            // Call the listener here letting us know that we
            // have the game detail now.
            _onGameDetailArrivedListener.OnGameDetailArrived(NetworkClass.this, gameDetail);
        }
    }

    private GameDetail parseGameDetail(String result)
    {

        Type gameType = new TypeToken<GameDetail>()
        {
        }.getType();
        //_games = _gson.fromJson(result, gameType);
        return _gson.fromJson(result, gameType);

    }
    //endregion GameDetail

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException
    {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try
        {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    private Game[] parseJson(String result)
    {
        Type gameType = new TypeToken<Game[]>()
        {
        }.getType();

        return _gson.fromJson(result, gameType);
    }


    //region <Get Battle Grid>

    public interface OnBattleGridUpdatedListener
    {
        public void OnBattleGridUpdated(NetworkClass networkClass, BattleGrid battleGrid);
    }

    OnBattleGridUpdatedListener _onBattleGridUpdatedListener = null;

    public void setOnBattleGridUpdatedListener(OnBattleGridUpdatedListener onBattleGridUpdatedListener)
    {
        this._onBattleGridUpdatedListener = onBattleGridUpdatedListener;
    }

    public boolean initBattleGrid(Context context, String GameId, String playerId)
    {
        // Gets the URL from the UI's text field.
        String stringUrl = BASE_URL + GameId + "/board";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new DownloadBattleGridTask().execute(stringUrl, "playerId", playerId);
            return true;
        } else
        {
            return false;
        }
    }

    private class DownloadBattleGridTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return requestBattleGrid(urls[0], urls[1], urls[2]);
            } catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                BattleGrid battleGrid = getBattleGrid(result);

                _onBattleGridUpdatedListener.OnBattleGridUpdated(NetworkClass.this, battleGrid);

            } catch (Exception e)
            {

                _onBattleGridUpdatedListener.OnBattleGridUpdated(NetworkClass.this, null);
            }
        }
    }

    private String requestBattleGrid(String myurl, String idTag, String playerId) throws IOException
    {
        InputStream is = null;

        try
        {
            JSONObject jsonobj = new JSONObject();

            jsonobj.put(idTag, playerId);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(myurl);

            StringEntity stringEntity = new StringEntity(jsonobj.toString());

            stringEntity.setContentType("application/json");
            httppostreq.setEntity(stringEntity);

            HttpResponse httpResponse = httpclient.execute(httppostreq);

            String responseText;

            responseText = EntityUtils.toString(httpResponse.getEntity());

            return responseText;

        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
        return null;
    }

    private BattleGrid getBattleGrid(String result)
    {
        Type gameType = new TypeToken<BattleGrid>()
        {
        }.getType();
        return _gson.fromJson(result, gameType);
    }

    class BattleGrid
    {
        Cell[] playerBoard;
        Cell[] oponentBoard;
//        HashMap<String, Cell[]> playerBoard;
//        HashMap<String, Cell[]> oponentBoard;
    }

    class Cell
    {
        int xPos;
        int yPos;
        String status;
    }

    //endregion <Get Battle Grid>


    //region <Create New Game>

    public void requestNewGame(Context context, String gameName, String playerName)
    {
        String stringUrl = "http://battleship.pixio.com/api/games/";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new CreateNewGameTask().execute(stringUrl, "gameName", gameName, "playerName", playerName);
        } else
        {
            //textView.setText("No network connection available.");
        }
    }

    private class CreateNewGameTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return requestNewGameFromNetwork(strings[0], strings[1], strings[2], strings[3], strings[4]);
            } catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            HashMap<String, String> gameInfo = getNewGameInfo(result);

            _onNewGameInfoArrivedListener.OnGameInfoArrived(NetworkClass.this, gameInfo);

//            Iterator it = gameDetail.entrySet().iterator();
//            while (it.hasNext())
//            {
//                Map.Entry pairs = (Map.Entry) it.next();
//                if (pairs.getKey().equals("gameId"))
//                {
//                    GAME_ID = pairs;
//                } else
//                    PLAYER_ID = pairs;
//
//                it.remove(); // avoids a ConcurrentModificationException
//            }
        }
    }

    private String requestNewGameFromNetwork(String myurl, String gameNameTag, String gameName,
                                  String playerNameTag, String playerName) throws IOException
    {
        InputStream is = null;

        try
        {
            JSONObject jsonobj = new JSONObject();

            jsonobj.put(gameNameTag, gameName);
            jsonobj.put(playerNameTag, playerName);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(myurl);

            StringEntity stringEntity = new StringEntity(jsonobj.toString());

            stringEntity.setContentType("application/json");
            httppostreq.setEntity(stringEntity);

            HttpResponse httpResponse = httpclient.execute(httppostreq);

            String responseText;

            responseText = EntityUtils.toString(httpResponse.getEntity());

            return responseText;

        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
        return null;
    }

    private HashMap<String, String> getNewGameInfo(String result)
    {
        Type gameType = new TypeToken<HashMap<String, String>>()
        {
        }.getType();
        return _gson.fromJson(result, gameType);
    }

    //endregion <Create New Game>

    class Game
    {
        String id;
        String name;
        String status;
    }

    class GameDetail
    {
        String id;
        String name;
        String player1;
        String player2;
        String winner;
    }
}
