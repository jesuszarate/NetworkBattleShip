package edu.utah.cs4962.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Jesus Zarate on 10/24/14.
 */
public class CellView extends View
{
    private int _gridPosition = 0;
    public Ships _ships;

    public CellView(Context context)
    {
        super(context);
        setBackgroundColor(Color.BLUE);
    }

    public int getGridPosition()
    {
        return _gridPosition;
    }

    public void setGridPosition(int _gridPosition)
    {
        this._gridPosition = _gridPosition;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        _onCellTouchListener.onCellTouched(this);
//        Toast.makeText(getContext(), "" + _gridPosition, Toast.LENGTH_SHORT).show();
        return super.onTouchEvent(event);
    }

    public interface OnCellTouchListener
    {
        public void onCellTouched(CellView cellView);
    }

    OnCellTouchListener _onCellTouchListener = null;

    public OnCellTouchListener getOnCellTouchListener()
    {
        return _onCellTouchListener;
    }

    public void setOnCellTouchListener(OnCellTouchListener _onCellTouchListener)
    {
        this._onCellTouchListener = _onCellTouchListener;
    }
}
