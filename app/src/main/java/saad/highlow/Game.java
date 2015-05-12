package saad.highlow;

import android.widget.NumberPicker;
import android.widget.TextSwitcher;

import java.util.Random;

/**
 * Created by Saad on 11/05/2015.
 *
 */
public class Game{
    private int guess;
    private int theNumber;
    int attemptCount;

    public Game(){
        Random rn = new Random();
        theNumber = rn.nextInt(1000);
        guess = 0;
        attemptCount = 0;
    }

    /*ACTIONS TO TAKE EVERYTIME USER MAKES A GUESS
    * * SET guess TO USER'S CURRENT GUESS
    * * INCREASE ATTEMPT COUNT
    * * SHOW USER NEW ATTEMPT COUNT
    *
    * @param p1 left-most number picker
    * @param p2 middle number picker
    * @param p3 right number picker
    * @param attempts TextSwitcher that shows number of attempts
    * */
    public void onUserGuess(NumberPicker p1, NumberPicker p2, NumberPicker p3, TextSwitcher attempts){
        String number = String.valueOf(p1.getValue())+String.valueOf(p2.getValue())
                +String.valueOf(p3.getValue());
        guess = Integer.parseInt(number);
        attemptCount++;
        attempts.setText("Attempts: " + String.valueOf(attemptCount));
    }

    /*
    * ---METHODS TO CHECK WHETHER---
    * --USER GUESS HIGHER OR LOWER--
    * -----THAN ACTUAL NUMBER-----*/
    public boolean isGuessHigh(){
        return guess>theNumber;
    }

    public boolean isGuessLow(){
        return guess<theNumber;
    }
}
