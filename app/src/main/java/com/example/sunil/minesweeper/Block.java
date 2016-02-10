package com.example.sunil.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;

/**
 * Created by Sunil on 10/28/2014.
 */
public class Block extends Button
{
    private boolean isCovered;
    private boolean isMined;
    private boolean isFlagged;
    private boolean isClickable;
    private int surroundingMines;

    public Block(Context context)
    {
        super(context);
    }

    public void setDefaults()
    {
        isCovered = true;
        isMined = false;
        isFlagged = false;
        isClickable = true;
        surroundingMines = 0;

        this.setBackgroundResource(R.drawable.square_blue);
        set_Font();
    }

    public void setNumberOfSurroundingMines(int number)
    {
        this.setBackgroundResource(R.drawable.square_grey);
        updateNumber(number);
    }

    public void setMineIcon(boolean enabled)
    {
        this.setText("M");
        if (!enabled)
        {
            this.setBackgroundResource(R.drawable.square_grey);
            this.setTextColor(Color.RED);
        }
        else
        {
            this.setTextColor(Color.BLACK);
        }
    }

    public void setFlagIcon(boolean enabled)
    {
        this.setText("F");

        if (!enabled)
        {
            this.setBackgroundResource(R.drawable.square_grey);
            this.setTextColor(Color.RED);
        }
        else
        {
            this.setTextColor(Color.BLACK);
        }
    }

    public void setBlockAsDisabled(boolean enabled)
    {
        if (!enabled)
            this.setBackgroundResource(R.drawable.square_grey);
        else
            this.setBackgroundResource(R.drawable.square_blue);
    }

    private void set_Font()
    {
        this.setTypeface(null, Typeface.BOLD);
        this.setTextSize(13);
    }

    public void OpenBlock()
    {
        if (!isCovered)
            return;
        setBlockAsDisabled(false);
        isCovered = false;

        if (hasMine())
            setMineIcon(false);
        else
            setNumberOfSurroundingMines(surroundingMines);
    }

    public void updateNumber(int text)
    {
        if (text != 0)
        {
            this.setText(Integer.toString(text));

            switch (text)
            {
                case 1:
                    this.setTextColor(Color.BLUE);
                    break;
                case 2:
                    this.setTextColor(Color.rgb(0, 100, 0));
                    break;
                case 3:
                    this.setTextColor(Color.RED);
                    break;
                case 4:
                    this.setTextColor(Color.rgb(85, 26, 139));
                    break;
                case 5:
                    this.setTextColor(Color.rgb(139, 28, 98));
                    break;
                case 6:
                    this.setTextColor(Color.rgb(238, 173, 14));
                    break;
                case 7:
                    this.setTextColor(Color.rgb(47, 79, 79));
                    break;
                case 8:
                    this.setTextColor(Color.rgb(71, 71, 71));
                    break;
                case 9:
                    this.setTextColor(Color.rgb(205, 205, 0));
                    break;
            }
        }
    }

    public void plantMine()
    {
        isMined = true;
    }


    public void triggerMine()
    {
        setMineIcon(true);
        this.setTextColor(Color.RED);
    }

    public boolean isCovered()
    {
        return isCovered;
    }

    public boolean hasMine()
    {
        return isMined;
    }

    public void setSurroundingMines(int number)
    {
        surroundingMines = number;
    }

    public int getSurroundingMines()
    {
        return surroundingMines;
    }

    public boolean isFlagged()
    {
        return isFlagged;
    }

    public void setFlagged(boolean flagged)
    {
        isFlagged = flagged;
    }

    public boolean isClickable()
    {
        return isClickable;
    }

    public void setClickable(boolean clickable)
    {
        isClickable = clickable;
    }
}
