package edu.utah.cs4962.battleship;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by jesuszarate on 11/1/14.
 */
public class GameListActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FrameLayout gameLayout = new FrameLayout(this);
        gameLayout.setId(10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootLayout.addView(gameLayout, params);

        GameListFragment _gameListFragment = new GameListFragment();

        fragmentTransaction.add(11, _gameListFragment);

        fragmentTransaction.commit();

        setContentView(rootLayout);
    }
}
