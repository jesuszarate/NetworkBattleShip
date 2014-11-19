package edu.utah.cs4962.battleship;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by jesuszarate on 11/14/14.
 */
public class NetworkClass
{

    private static final String BASE_URL = "http://battleship.pixio.com/api/games/";
    public static final String GAME_ID = "gameId";
    public static final String PLAYER_ID = "playerId";

    Gson _gson = new Gson();

    private Context _context;

    public NetworkClass(Context context)
    {
        _context = context;
    }


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

    public interface OnErrorReceivedListener
    {
        public void OnErrorReceived(NetworkClass networkClass, String errorMessage);
    }

    OnErrorReceivedListener _onErrorReceivedListener = null;

    public void setOnErrorReceivedListener(OnErrorReceivedListener onErrorReceivedListener)
    {
        this._onErrorReceivedListener = onErrorReceivedListener;
    }

    public interface OnJoinRequestReceivedListener
    {
        public void OnJoinRequestReceived(NetworkClass networkClass);
    }

    OnJoinRequestReceivedListener _onJoinRequestReceivedListener = null;

    public void setOnJoinRequestReceivedListener(OnJoinRequestReceivedListener _onJoinRequestReceivedListener)
    {
        this._onJoinRequestReceivedListener = _onJoinRequestReceivedListener;
    }

    public interface OnPlayerIdReceivedListener
    {
        public void OnPlayerIdReceived(NetworkClass networkClass, String playerId);
    }

    OnPlayerIdReceivedListener _onPlayerIdReceivedListener = null;

    public void setOnPlayerIdReceivedListener(OnPlayerIdReceivedListener onPlayerIdReceivedListener)
    {
        this._onPlayerIdReceivedListener = onPlayerIdReceivedListener;
    }

    public interface OnNeedToUpdateBattleGridListener
    {
        public void OnNeedToUpdateBattleGrid(NetworkClass networkClass, String playersTurn, boolean myTurn);
    }

    OnNeedToUpdateBattleGridListener _onNeedToUpdateBattleGridListener = null;

    public void setOnNeedToUpdateBattleGridListener(OnNeedToUpdateBattleGridListener onNeedToUpdateBattleGridListener)
    {
        this._onNeedToUpdateBattleGridListener = onNeedToUpdateBattleGridListener;
    }

    public interface OnMissileLaunchResultArrivedListener
    {
        public void OnMissileLaunchResultArrived(NetworkClass networkClass, String result);
    }

    OnMissileLaunchResultArrivedListener _onMissileLaunchResultArrivedListener = null;

    public void setOnMissileLaunchResultArrivedListener(OnMissileLaunchResultArrivedListener onMissileLaunchResultArrivedListener)
    {
        this._onMissileLaunchResultArrivedListener = onMissileLaunchResultArrivedListener;
    }

    public interface OnGameEndedListener
    {
        public void OnGameEnded(NetworkClass networkClass, String winner);
    }
    OnGameEndedListener _onGameEndedListener = null;

    public void setOnGameEndedListener(OnGameEndedListener onGameEndedListener)
    {
        this._onGameEndedListener = onGameEndedListener;
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
            try
            {
                Game[] _games = parseJson(result);

                _onGameListArrivedListener.OnGameListArrived(NetworkClass.this, _games);
            }
            catch (Exception e)
            {
                //_onErrorReceivedListener.OnErrorReceived(NetworkClass.this, result);
            }
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
        public void OnBattleGridUpdated(NetworkClass networkClass, HashMap<String, Cell[]> battleGrid);
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
                HashMap<String, Cell[]> battleGrid = getBattleGrid(result);

                if (battleGrid != null)
                {
                    _onBattleGridUpdatedListener.OnBattleGridUpdated(NetworkClass.this, battleGrid);
                } else if (!result.equals("{\"message\":\"The player does not belong to this game\"}"))
                {
                    _onJoinRequestReceivedListener.OnJoinRequestReceived(NetworkClass.this);
//                    _onErrorReceivedListener.OnErrorReceived(NetworkClass.this, result);
                }

            }catch (NullPointerException ne){
                // Do nothing if there was a NullPointerException.
                Log.e("NullPointerException In GetGrid", ne.toString());
            }
            catch (Exception e)
            {

                if (result.equals("Player id is not a valid GUID"))
                {
                    _onJoinRequestReceivedListener.OnJoinRequestReceived(NetworkClass.this);
                } else
                {
                    //TODO: Use the message given by result to display the proper message to the user i.e.
                    _onErrorReceivedListener.OnErrorReceived(NetworkClass.this, result);
                }
                //_onBattleGridUpdatedListener.OnBattleGridUpdated(NetworkClass.this, null);
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

    private HashMap<String, Cell[]> getBattleGrid(String result)
    {
        Type gameType = new TypeToken<HashMap<String, Cell[]>>()
        {
        }.getType();

        return _gson.fromJson(result, gameType);
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
            try
            {
                HashMap<String, String> gameInfo = getNewGameInfo(result);

                _onNewGameInfoArrivedListener.OnGameInfoArrived(NetworkClass.this, gameInfo);
            } catch (Exception e)
            {
                _onNewGameInfoArrivedListener.OnGameInfoArrived(NetworkClass.this, null);
            }
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

    //region <Join Game>

    public void JoinGame(Context context, String gameId, String playerName)
    {
        String playerNameTag = "playerName";

        String stringUrl = "http://battleship.pixio.com/api/games/" + gameId + "/join";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new JoinGameTask().execute(stringUrl, playerNameTag, playerName);
        } else
        {
            //textView.setText("No network connection available.");
        }

    }

    private class JoinGameTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return requestJoinGame(strings[0], strings[1], strings[2]);
            } catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            // Do something with the returned information
            String playerId = getPlayerId(result);

            if (playerId != null)
                _onPlayerIdReceivedListener.OnPlayerIdReceived(NetworkClass.this, playerId);
            //_onJoinRequestReceivedListener.OnJoinRequestReceived(NetworkClass.this, playerId);

            //getBattleGrid(gameId);

        }

    }

    private String requestJoinGame(String myurl, String playerNameTag, String playerName) throws IOException
    {
        InputStream is = null;

        try
        {
            JSONObject jsonobj = new JSONObject();

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

    private String getPlayerId(String result)
    {
        try
        {
            Type gameType = new TypeToken<HashMap<String, String>>()
            {
            }.getType();
            HashMap<String, String> Guid = _gson.fromJson(result, gameType);
            return Guid.get("playerId");
        } catch (Exception e)
        {
            return null;
        }
    }
    //endregion <Join Game>

    //region <Launch Missile>
    public void LaunchMissile(Context context, String gameId, String playerId, int xPos, int yPos)
    {
        String playerNameTag = "playerName";

        String stringUrl = "http://battleship.pixio.com/api/games/" + gameId + "/guess";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new LaunchMissileTask().execute(stringUrl, playerNameTag, playerId, xPos + "", yPos + "");
        } else
        {
            //textView.setText("No network connection available.");
        }

    }

    private class LaunchMissileTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return requestMissileLaunch(strings[0], strings[1], strings[2], strings[3], strings[4]);
            } catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            // Do something with the returned information
            HashMap<String, String> missileAttackResult = parseMissileAttackResult(result);

            if (missileAttackResult != null)
            {
                if (missileAttackResult.containsKey("hit"))
                {
                    if (missileAttackResult.get("hit").equals("true"))
                        _onMissileLaunchResultArrivedListener.OnMissileLaunchResultArrived(NetworkClass.this, BattleGridView.HIT);
                    else
                        _onMissileLaunchResultArrivedListener.OnMissileLaunchResultArrived(NetworkClass.this, BattleGridView.MISS);
                }

                else if (missileAttackResult.containsKey("message"))
                {
                    String message = missileAttackResult.get("message");

                    _onErrorReceivedListener.OnErrorReceived(NetworkClass.this, message);
                }
            }

        }

    }

    private String requestMissileLaunch(String myurl, String playerNameTag, String playerId, String xPos, String yPos) throws IOException
    {
        InputStream is = null;

        try
        {
            HashMap<String, String> missileAttack = new HashMap<String, String>();
            missileAttack.put("playerId", playerId);
            missileAttack.put("xPos", xPos);
            missileAttack.put("yPos", yPos);

            String jsonMissileAttack = _gson.toJson(missileAttack);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(myurl);

            StringEntity stringEntity = new StringEntity(jsonMissileAttack);

            stringEntity.setContentType("application/json");
            httppostreq.setEntity(stringEntity);

            HttpResponse httpResponse = httpclient.execute(httppostreq);

            String responseText;

            responseText = EntityUtils.toString(httpResponse.getEntity());

            return responseText;

        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    private HashMap<String, String> parseMissileAttackResult(String result)
    {
        try
        {
            Type gameType = new TypeToken<HashMap<String, String>>()
            {
            }.getType();
            HashMap<String, String> missileAttackRes = _gson.fromJson(result, gameType);
            return missileAttackRes;
        } catch (Exception e)
        {
            return null;
        }
    }
    //endregion <Launch Missile>

    //region <CheckStatus>
    public void CheckGameStatus(Context context, String gameId, String playerId)
    {
        String stringUrl = "http://battleship.pixio.com/api/games/" + gameId + "/status";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            new GameStatusTask().execute(stringUrl, playerId);
        } else
        {
            //textView.setText("No network connection available.");
        }

    }

    private class GameStatusTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return requestGameStatus(strings[0], strings[1]);
            } catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                // Do something with the returned information
                HashMap<String, String> gameStatus = parseGameStatus(result);

                if (gameStatus != null)
                {
                    if (gameStatus.containsKey("winner") && !gameStatus.get("winner").equals("IN PROGRESS"))
                    {
                        // If there was a winner Let the user know who won.
                        _onGameEndedListener.OnGameEnded(NetworkClass.this, gameStatus.get("winner"));
                    }
                    else if (gameStatus.containsKey("isYourTurn") && gameStatus.get("isYourTurn").equals("true"))
                    {
                        // Update the game grid and allows the user to launch a missile.
                        _onNeedToUpdateBattleGridListener.OnNeedToUpdateBattleGrid(NetworkClass.this, "Your Turn", true);
                    }
                    else
                    {
                        _onNeedToUpdateBattleGridListener.OnNeedToUpdateBattleGrid(NetworkClass.this, "Opponent's Turn", false);
                    }


                    if (gameStatus.containsKey("message"))
                    {
                        String message = gameStatus.get("message");

                        _onErrorReceivedListener.OnErrorReceived(NetworkClass.this, message);
                    }
                }

            }
            catch (Exception e)
            {

            }
        }

    }

    private String requestGameStatus(String myurl, String playerId) throws IOException
    {
        InputStream is = null;

        try
        {
            HashMap<String, String> missileAttack = new HashMap<String, String>();
            missileAttack.put("playerId", playerId);

            String jsonMissileAttack = _gson.toJson(missileAttack);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(myurl);

            StringEntity stringEntity = new StringEntity(jsonMissileAttack);

            stringEntity.setContentType("application/json");
            httppostreq.setEntity(stringEntity);

            HttpResponse httpResponse = httpclient.execute(httppostreq);

            String responseText;

            responseText = EntityUtils.toString(httpResponse.getEntity());

            return responseText;

        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    private HashMap<String, String> parseGameStatus(String result)
    {
        try
        {
            Type gameType = new TypeToken<HashMap<String, String>>()
            {
            }.getType();
            HashMap<String, String> missileAttackRes = _gson.fromJson(result, gameType);
            return missileAttackRes;
        } catch (Exception e)
        {
            return null;
        }
    }
    //endregion <CheckStatus>

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
