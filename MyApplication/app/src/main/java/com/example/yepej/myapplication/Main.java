package com.example.yepej.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Main extends AppCompatActivity
{

    TextView intro;
    TextView selection;
    ImageView red;
    ImageView yellow;
    Animation rotationAnimation;
    int activePlayer;       //0 - yellow, 1 - red
    int[] board = {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8},
                                {0,3,6}, {1,4,7}, {2,5,8},
                                {0,4,8}, {2,4,6}};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setVariables();
        setActivePlayer();
        animateHomeScreen();
    }

    private void setActivePlayer()
    {
        Random num = new Random();
        activePlayer = num.nextInt(1) + 1;
    }

    //Sets all variables
    private void setVariables()
    {
        intro = ((TextView) findViewById(R.id.intro));
        selection =((TextView) findViewById(R.id.selection));
        red = ((ImageView) findViewById(R.id.redToken));
        yellow = ((ImageView) findViewById(R.id.yellowToken));
        rotationAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animator);

        intro.setTranslationY(-1000f);
        selection.setTranslationY(-1000f);

    }

    //Animates the controls on the home screen
    private void animateHomeScreen()
    {
        intro.animate().translationYBy(1000f).setDuration(1000);
        selection.animate().translationYBy(1000f).setDuration(1500);
        red.animate().alpha(1f).setDuration(5000);
        yellow.animate().alpha(1f).setDuration(5000);
        red.startAnimation(rotationAnimation);
        yellow.startAnimation(rotationAnimation);

    }

    //Used in the homescreen for the player to select token and continues to the game
    public void tokenSelect (View control)
    {
        TextView selectedColor = ((TextView) findViewById(R.id.colorSelected));
        String tokenSelected = getResources().getResourceEntryName(control.getId());
        Button play = ((Button) findViewById(R.id.playButton));
        play.setAlpha(1f);

        if (tokenSelected.equalsIgnoreCase("redtoken"))
        {
            selectedColor.setText("Red");
            Player.token = "Red";
        }
        else
        {
            selectedColor.setText("Yellow");
            Player.token = "Yellow";
        }

    }

    //Starts game
    public void playGame (View control)
    {
        setContentView(R.layout.activity_main);
        showTurn();
    }

    //Shows the current players turn
    private void showTurn()
    {
        if (activePlayer == 0)
        {
            Toast.makeText(this, "Yellow's turn", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Red's turn", Toast.LENGTH_SHORT).show();
        }

    }


    //Shows the token wherever the user touched
    public void showToken (View control)
    {
        ImageView image = ((ImageView) control);
        int position = Integer.parseInt(image.getTag().toString());

        if (board[position] == 2)
        {
            if (activePlayer == 1)
            {
                changeBoardState(position);
                changePlayer();
                image.setImageResource(R.drawable.red);
            }
            else
            {
                changeBoardState(position);
                changePlayer();
                image.setImageResource(R.drawable.yellow);
            }
        }
    }

    //Changes players
    private void changePlayer()
    {
        if (activePlayer == 0)
        {
            activePlayer = 1;
        }
        else
        {
            activePlayer = 0;
        }
    }

    //Sets the current tokens position in the array
    private void changeBoardState (int position)
    {
        board[position] = activePlayer;
        isGameOver();
    }

    //Checks if there is 3 in a row of the same kind
    private void isGameOver()
    {
        for (int[] winningPosition : winningPositions)
        {
            if (board[winningPosition[0]] == board[winningPosition[1]] &&
                board[winningPosition[1]] == board[winningPosition[2]] &&
                board[winningPosition[0]] != 2)
            {
               if (board[winningPosition[0]] == 1)
               {
                   Toast.makeText(this, "Red wins!", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Toast.makeText(this, "Yellow wins!", Toast.LENGTH_SHORT).show();
               }

                disableGame();

                break;
            }
        }
    }

    private void disableGame()
    {
        GridLayout g = ((GridLayout) findViewById(R.id.board));

        for (int i = 0; i < g.getChildCount(); i++)
        {
            View child = g.getChildAt(i);
            child.setEnabled(false);
        }
    }

    public void restartGame(View control)
    {

        setContentView(R.layout.home);
        intro.setTranslationY(-1000f);
        selection.setTranslationY(-1000f);
        yellow.animate().alpha(0f);
        red.animate().alpha(0f);
        animateHomeScreen();
    }
}
