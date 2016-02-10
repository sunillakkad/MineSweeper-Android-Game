package com.example.sunil.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import java.util.Random;

public class MinesweeperGame extends Activity
{
    private ImageButton gamebtn;
    private TableLayout mineField;
    private Block blocks[][];
    private int block_Dim = 40;;
    private int totalRows = 16;
    private int totalColumns = 16;
    private int totalMines = 50;
    private boolean areMinesSet;
    private boolean isGameOver;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gamebtn = (ImageButton) findViewById(R.id.new_game);
        gamebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                endExistingGame();
                startNewGame();
            }
        });
        mineField = (TableLayout)findViewById(R.id.MineField);
        showDialog("Click on a New Game Button", 4000);
    }

    private void startNewGame()
    {
        createMineField();
        showMineField();
        isGameOver = false;
    }

    private void showMineField()
    {
        for (int row = 1; row < totalRows + 1; row++)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(block_Dim * totalColumns, block_Dim));

            for (int column = 1; column < totalColumns + 1; column++)
            {
                blocks[row][column].setLayoutParams(new TableRow.LayoutParams(block_Dim, block_Dim));
                blocks[row][column].setPadding(0,0,0,0);
                tableRow.addView(blocks[row][column]);
            }
            mineField.addView(tableRow,new TableLayout.LayoutParams(block_Dim * totalColumns, block_Dim));
        }
    }

    private void endExistingGame()
    {
        gamebtn.setBackgroundResource(R.drawable.new_game);
        mineField.removeAllViews();
        areMinesSet = false;
        isGameOver = false;
    }

    private void createMineField()
    {
        blocks = new Block[totalRows + 2][totalColumns + 2];
        for (int row = 0; row < totalRows + 2; row++)
        {
            for (int column = 0; column < totalColumns + 2; column++)
            {
                blocks[row][column] = new Block(this);
                blocks[row][column].setDefaults();
                final int curr_Row = row;
                final int curr_Col = column;

                blocks[row][column].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (!areMinesSet)
                        {
                            areMinesSet = true;
                            setMines(curr_Row, curr_Col);
                        }
                        if (!blocks[curr_Row][curr_Col].isFlagged())
                        {
                            block_Uncover(curr_Row, curr_Col);

                            if (blocks[curr_Row][curr_Col].hasMine())
                                finishGame(curr_Row,curr_Col);
                            if (checkGameWin())
                                winGame();
                        }
                    }
                });

                blocks[row][column].setOnLongClickListener(new View.OnLongClickListener()
                {
                    public boolean onLongClick(View view)
                    {
                        if (!blocks[curr_Row][curr_Col].isCovered() && (blocks[curr_Row][curr_Col].getSurroundingMines() > 0) && !isGameOver)
                        {
                            int flaggedBlocks = 0;
                            for (int pre_Row = -1; pre_Row < 2; pre_Row++)
                            {
                                for (int pre_Col = -1; pre_Col < 2; pre_Col++)
                                {
                                    if (blocks[curr_Row + pre_Row][curr_Col + pre_Col].isFlagged())
                                    {
                                        flaggedBlocks++;
                                    }
                                }
                            }

                            if (flaggedBlocks == blocks[curr_Row][curr_Col].getSurroundingMines())
                            {
                                for (int pre_Row = -1; pre_Row < 2; pre_Row++)
                                {
                                    for (int pre_Col = -1; pre_Col < 2; pre_Col++)
                                    {
                                        if (!blocks[curr_Row + pre_Row][curr_Col + pre_Col].isFlagged())
                                        {
                                            block_Uncover(curr_Row + pre_Row, curr_Col + pre_Col);

                                            if (blocks[curr_Row + pre_Row][curr_Col + pre_Col].hasMine())
                                                finishGame(curr_Row + pre_Row, curr_Col + pre_Col);
                                            if (checkGameWin())
                                                winGame();
                                        }
                                    }
                                }
                            }
                            return true;
                        }


                        if (blocks[curr_Row][curr_Col].isClickable() && (blocks[curr_Row][curr_Col].isEnabled() || blocks[curr_Row][curr_Col].isFlagged()))
                        {
                            blocks[curr_Row][curr_Col].setBlockAsDisabled(false);
                            blocks[curr_Row][curr_Col].setFlagIcon(true);
                            blocks[curr_Row][curr_Col].setFlagged(true);
                        }
                        return true;
                    }
                });
            }
        }
    }

    private boolean checkGameWin()
    {
        for (int row = 1; row < totalRows + 1; row++)
        {
            for (int column = 1; column < totalColumns + 1; column++)
            {
                if (!blocks[row][column].hasMine() && blocks[row][column].isCovered())
                    return false;
            }
        }
        return true;
    }

    private void winGame()
    {
        isGameOver = true;
        gamebtn.setBackgroundResource(R.drawable.new_game);

        for (int row = 1; row < totalRows + 1; row++)
        {
            for (int column = 1; column < totalColumns + 1; column++)
            {
                blocks[row][column].setClickable(false);

                if (blocks[row][column].hasMine())
                {
                    blocks[row][column].setBlockAsDisabled(false);
                    blocks[row][column].setFlagIcon(true);
                }
            }
        }
        showDialog("Congratulation!! You won in the GAME", 4000);
    }

    private void finishGame(int curr_Row, int curr_Col)
    {
        isGameOver = true;
        gamebtn.setBackgroundResource(R.drawable.new_game);

        for (int row = 1; row < totalRows + 1; row++)
        {
            for (int column = 1; column < totalColumns + 1; column++)
            {
                blocks[row][column].setBlockAsDisabled(false);

                if (blocks[row][column].hasMine() && !blocks[row][column].isFlagged())
                    blocks[row][column].setMineIcon(false);
                if (!blocks[row][column].hasMine() && blocks[row][column].isFlagged())
                    blocks[row][column].setFlagIcon(false);
                if (blocks[row][column].isFlagged())
                    blocks[row][column].setClickable(false);
            }
        }
        blocks[curr_Row][curr_Col].triggerMine();
        showDialog("Oops!! It was mines, GAME OVER. Click on New Game", 4000);
    }

    private void setMines(int curr_Row, int curr_Col)
    {
        Random rand = new Random();
        int mineRow, mineColumn;

        for (int row = 0; row < totalMines; row++)
        {
            mineRow = rand.nextInt(totalColumns);
            mineColumn = rand.nextInt(totalRows);
            if ((mineRow + 1 != curr_Col) || (mineColumn + 1 != curr_Row))
            {
                if (blocks[mineColumn + 1][mineRow + 1].hasMine())
                    row--;
                blocks[mineColumn + 1][mineRow + 1].plantMine();
            }
            else
            {
                row--;
            }
        }
        int mine_Count;

        for (int row = 0; row < totalRows + 2; row++)
        {
            for (int column = 0; column < totalColumns + 2; column++)
            {
                mine_Count = 0;
                if ((row != 0) && (row != (totalRows + 1)) && (column != 0) && (column != (totalColumns + 1)))
                {
                    for (int pre_Row = -1; pre_Row < 2; pre_Row++)
                    {
                        for (int pre_Col = -1; pre_Col < 2; pre_Col++)
                        {
                            if (blocks[row + pre_Row][column + pre_Col].hasMine())
                                mine_Count++;
                        }
                    }
                    blocks[row][column].setSurroundingMines(mine_Count);
                }
                else
                {
                    blocks[row][column].setSurroundingMines(9);
                    blocks[row][column].OpenBlock();
                }
            }
        }
    }

    private void block_Uncover(int click_Row, int click_Col)
    {
        if (blocks[click_Row][click_Col].hasMine() || blocks[click_Row][click_Col].isFlagged())
            return;

        blocks[click_Row][click_Col].OpenBlock();
        if (blocks[click_Row][click_Col].getSurroundingMines() != 0 )
            return;
        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 3; column++)
            {
                if (blocks[click_Row + row - 1][click_Col + column - 1].isCovered()
                        && (click_Row + row - 1 > 0) && (click_Col + column - 1 > 0)
                        && (click_Row + row - 1 < totalRows + 1) && (click_Col + column - 1 < totalColumns + 1))
                {
                    block_Uncover(click_Row + row - 1, click_Col + column - 1 );
                }
            }
        }
        return;
    }


    private void showDialog(String message, int milliseconds)
    {
        Toast dialog = Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_LONG);

        dialog.setGravity(Gravity.CENTER, 0, 0);
        dialog.setDuration(milliseconds);
        dialog.show();
    }
}
