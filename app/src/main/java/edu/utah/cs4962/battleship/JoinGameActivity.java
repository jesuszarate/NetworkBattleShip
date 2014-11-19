package edu.utah.cs4962.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jesus Zarate on 11/15/14.
 */
public class JoinGameActivity extends Activity
{

    private EditText playerNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params1 =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        params1.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;


        TextView PlayerName = new TextView(this);
        PlayerName.setText("Player's Name");
        PlayerName.setTextSize(20);
        playerNameEditText = new EditText(this);


        Button submitButton = new Button(this);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TODO: Return the player's name and the game name on the intent results
                setIntentResults();
                finish();
            }
        });

        rootLayout.addView(PlayerName, params1);
        rootLayout.addView(playerNameEditText, params1);
        rootLayout.addView(submitButton, params);
        setContentView(rootLayout);
    }

    public void setIntentResults()
    {
        // Include the color the use picked so that it can also be updated in the
        // button preview of the Create Mode.
        Intent resultIntent = new Intent();
        resultIntent.putExtra(BattleShipActivity.PLAYER_NAME, playerNameEditText.getText().toString());

        setResult(Activity.RESULT_OK, resultIntent);
    }
}