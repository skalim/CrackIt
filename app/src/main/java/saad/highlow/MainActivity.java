package saad.highlow;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Random;


public class MainActivity extends ActionBarActivity {
    /* ----------------------------------DECLARATIONS---------------------------------------------*/
    TextSwitcher highLowTextSwitcher;
    TextSwitcher attemptsTextSwitcher;
    TextView intro;
    TextView outro;
    TextView help;
    TextView playAgain;
    Typeface fontInOut;
    Typeface fontButtons;
    NumberPicker np1;
    NumberPicker np2;
    NumberPicker np3;
    MediaPlayer soundWrongGuess;
    MediaPlayer backgroundMusic;
    MediaPlayer soundCorrectGuess;
    Button guessButton;
    Game game;
    ImageView handle;
    Animation rotate;
    /*------------------------------------END OF DECLARATIONS-------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* INITIALISATIONS------------------------------------------------------------------------*/
        backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.backgroundmusic);
        highLowTextSwitcher = (TextSwitcher)findViewById(R.id.highLowTextSwitcher);
        attemptsTextSwitcher = (TextSwitcher)findViewById(R.id.attemptsTextSwitcher);
        handle = (ImageView)findViewById(R.id.handle);
        intro = (TextView)findViewById(R.id.intro);
        outro = (TextView)findViewById(R.id.outro);
        playAgain = (TextView)findViewById(R.id.playAgain);
        help = (TextView)findViewById(R.id.help);
        guessButton = (Button)findViewById(R.id.guessButton);
        fontInOut = Typeface.createFromAsset(getAssets(), "fonts/takecover.ttf");
        fontButtons = Typeface.createFromAsset(getAssets(), "fonts/impact.ttf");
        soundWrongGuess = MediaPlayer.create(getApplicationContext(), R.raw.wrongguess);
        soundCorrectGuess = MediaPlayer.create(getApplicationContext(), R.raw.correctguess);
        rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_picture);
        game = new Game();
        np1 = (NumberPicker)findViewById(R.id.numberPicker1);
        np2 = (NumberPicker)findViewById(R.id.numberPicker2);
        np3 = (NumberPicker)findViewById(R.id.numberPicker3);
        /*-------------------------------------FINISHED ALL INITIALISATIONS-----------------------*/

        backgroundMusic.start();            /*--------START IN ----------*/
        backgroundMusic.setLooping(true);   /*-------APP MUSIC-----------*/
        /*----------------------------------------------------------------------------------------*/

        help.setText("?");
        intro.setTypeface(fontInOut);       /*---SET...*/
        outro.setTypeface(fontInOut);       /*...ALL...*/
        playAgain.setTypeface(fontButtons); /*...FONTS*/
        /*----------------------------------------------------------------------------------------*/

        setPickerOptions(np1);  /*--------NUMBER PICKER----------*/
        setPickerOptions(np2);  /*----------ADJUSTMENTS----------*/
        setPickerOptions(np3);  /*SEE METHOD IMPLEMENTATION BELOW*/
        /*----------------------------------------------------------------------------------------*/

        /*------------------------TEXTSWITCHER TEXTVIEW FACTORIES---------------------------------*/
        highLowTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() { /*---FACTORY 1---*/

            public View makeView() {
                TextView high = new TextView(MainActivity.this);
                high.setLayoutParams(new TextSwitcher.LayoutParams(RadioGroup.LayoutParams.
                        FILL_PARENT, RadioGroup.LayoutParams.FILL_PARENT));
                high.setTextSize(70);

                high.setTextColor(Color.parseColor("#ba1a1a"));
                return high;
            }
        });
        attemptsTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() { /*---FACTORY 2---*/

            public View makeView() {
                TextView attempts = new TextView(MainActivity.this);
                attempts.setLayoutParams(new TextSwitcher.LayoutParams(RadioGroup.LayoutParams.
                        FILL_PARENT, RadioGroup.LayoutParams.FILL_PARENT));
                attempts.setTextSize(20);
                attempts.setTextColor(Color.parseColor("#eaeaec"));
                return attempts;
            }
        });
        /*------------------------DONE WITH TEXTSWITCHER TEXTVIEW FACTORIES-----------------------*/

        /*-------------------------------SETTING TEXTSWITCHER ATTRIBUTES--------------------------*/
        setTextSwitcherProperties(attemptsTextSwitcher, "Attempts: ",
                android.R.anim.fade_in, android.R.anim.fade_out);
        setTextSwitcherProperties(highLowTextSwitcher, " ",
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        /*---------------------------DONE SETTING TEXTSWITCHER ATTRIBUTES-------------------------*/

        /*---LIST OF LISTENERS---
        * 1) GUESS BUTTON
        * 2) INTRO SCREEN
        * 3) PLAY AGAIN BUTTON
        * 4) HELP BUTTON
        * ----------------------------------------------------------------------------------------*/
        guessButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.onUserGuess(np1,np2,np3,attemptsTextSwitcher);

                if ( game.isGuessHigh() ) {
                    highLowTextSwitcher.setText("HIGH");
                    soundWrongGuess.start();
                } else if ( game.isGuessLow() ) {
                    highLowTextSwitcher.setText("LOW");
                    soundWrongGuess.start();
                } else {
                    soundCorrectGuess.start();
                    handle.startAnimation(rotate);
                    highLowTextSwitcher.setVisibility(View.INVISIBLE);
                    guessButton.setVisibility(View.INVISIBLE);
                    outro.setText("You guessed correctly in " + game.attemptCount
                            + " attempts");
                    outro.setVisibility(View.VISIBLE);
                    playAgain.setVisibility(View.VISIBLE);
                }
            }
        });
        /*INTRO SCREEN LISTENER*/
        intro.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intro.setVisibility(View.GONE);
                guessButton.setClickable(true);
                guessButton.setVisibility(View.VISIBLE);
                help.setVisibility(View.VISIBLE);
            }
        });
        /*PLAY AGAIN BUTTON LISTENER*/
        playAgain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        /*HELP BUTTON LISTENER*/
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpScreen.class);
                startActivity(intent);
                finish();
            }
        });

    }
    /*--------------------------------FINISHED LISTENERS------------------------------------------*/

    public void setPickerOptions(NumberPicker p1){
        p1.setMaxValue(9);
        p1.setMinValue(0);
        p1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setNumberPickerTextColor(p1, Color.parseColor("#eaeaec"));
    }

    public void setTextSwitcherProperties(TextSwitcher ts, String text, int inAnimation,
                                          int outAnimation){
        ts.setText(text);
        ts.setInAnimation(AnimationUtils.loadAnimation(this, inAnimation));
        ts.setOutAnimation(AnimationUtils.loadAnimation(this, outAnimation));
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        backgroundMusic.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        backgroundMusic.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundMusic.release();
    }
}

