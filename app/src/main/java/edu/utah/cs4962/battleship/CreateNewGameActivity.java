package edu.utah.cs4962.battleship;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jesus Zarate on 11/15/14.
 */
public class CreateNewGameActivity extends Activity
{
    private EditText gameNameEditText;
    private EditText playerNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        TextView GameNameTextView = new TextView(this);
        GameNameTextView.setText("Game Name");
        gameNameEditText = new EditText(this);

        TextView PlayerName = new TextView(this);
        PlayerName.setText("Player1's Name");
        playerNameEditText = new EditText(this);

        rootLayout.addView(GameNameTextView);
        rootLayout.addView(gameNameEditText);
        rootLayout.addView(PlayerName);
        rootLayout.addView(playerNameEditText);

        setContentView(rootLayout);
    }
}
