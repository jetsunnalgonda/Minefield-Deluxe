package com.games.aurelius.minefielddeluxe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.games.aurelius.minefielddeluxe.Minefield.GameResult;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.games.aurelius.minefielddeluxe.Minefield.GameResult.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String DifficultyKey = "Difficulty";
    public static final String GameModeKey = "GameMode";

    // region Outlets
    TableLayout tl;
    TextView bombLabel;
    TextView livesLabel;
    ImageButton newGameButton;
    ImageButton flagButton;
    TextView timerLabel;
    Button settingsButton;
    // endregion

    // The game
    MinefieldGame game;

    // region Settings
    Integer cellWidth;
    Integer sizeOfField;
    Integer numberOfBombs;
    Integer numberOfRemainingBombs;
    Integer numberOfLives;
    Boolean flagUp;
    Timer timer;
    Boolean timerStarted;
    Integer seconds;
    Integer timerInstances;
    // endregion

    // Other variables
    Boolean userInteractionsEnabled;
    Integer gameMode;
    Integer difficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        timer = new Timer();
        timerStarted = false;
        timerInstances = 0;

//        PACKAGE_NAME = getApplicationContext().getPackageName();

        // Find outlets
        tl = (TableLayout)findViewById(R.id.tableLayout);
//        System.out.println("onCreate tl.getId() = " + tl.getId());
        bombLabel = findViewById(R.id.bombView);
        livesLabel = findViewById(R.id.livesView);
        newGameButton = findViewById(R.id.NewGameButton);
        flagButton = findViewById(R.id.flagButton);
        timerLabel = findViewById(R.id.timerView);
        settingsButton = findViewById(R.id.settingsButton);

        // Attach onClick listeners
        flagButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

//        ViewTreeObserver viewTreeObserver = tl.getViewTreeObserver();
//        if (viewTreeObserver.isAlive()) {
//            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    tl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    viewWidth = tl.getWidth();
//                }
//            });
//        }

        newGame();
        resetViews();
    }

    public void runTimer() {
//        System.out.println("timer = " + timer);
//        if (timerStarted) { timer.cancel(); }
//        if (seconds > 0) { timer.cancel(); }
        System.out.println("timerInstances = " + timerInstances);
        timerInstances++;

        if (timerInstances == 1) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            timerStarted = true;
                            timerLabel.setText("" + seconds);
                            seconds++;
                        }
                    });
                }
            }, 0, 1000);
        }

    }

//    public void runTimer() {
////        System.out.println("timer = " + timer);
//        if (timerStarted) { timer.cancel(); }
//
//        timer.scheduleAtFixedRate(new ScheduledTimer(), 0, 1000);
//    }
    public void setGameVariables() {
        seconds = 0;
        timerStarted = false;
        timerLabel.setText("0");

        gameMode = sharedPreferences.getInt(GameModeKey, 0);
        difficulty = sharedPreferences.getInt(DifficultyKey, 0);

        System.out.println("gameMode = " + gameMode);
        System.out.println("difficulty = " + difficulty);


        if (gameMode == 0) {
            numberOfLives = 1;
            livesLabel.setVisibility(View.INVISIBLE);
        } else if (gameMode == 1) {
            numberOfLives = 3;
            livesLabel.setText("Lives:\n" + numberOfLives);
            livesLabel.setVisibility(View.VISIBLE);
        }

        switch (difficulty) {
            case 0:
                sizeOfField = 8;
                numberOfBombs = 6;
                break;
            case 1:
                sizeOfField = 10;
                numberOfBombs = 15;
                break;
            case 2:
                sizeOfField = 20;
                numberOfBombs = 40;
                break;
        }

//        sizeOfField = 8;
//        numberOfBombs = 6;
//        numberOfLives = 1;
        numberOfRemainingBombs = numberOfBombs;

//        livesLabel.setVisibility(View.INVISIBLE);


    }

    public void resetViews() {
        refreshTopViews();
        calculateCellSize();
        createButtonViews(sizeOfField, sizeOfField);

        flagUp = true;
        toggleFlag();

        setSmilyFace(0);

        refreshTopViews();

        userInteractionsEnabled = true;
    }
    public void refreshTopViews() {
        bombLabel.setText(numberOfRemainingBombs.toString());
    }

    public void newGame() {
        System.out.println("newGame");

        System.out.println("numberOfBombs = " + numberOfBombs);

        setGameVariables();

        runTimer();

        game = new MinefieldGame(numberOfBombs, sizeOfField, numberOfLives);

        for (int i = 0; i < sizeOfField; i++) {
            for (int j = 0; j < sizeOfField; j++) {
                IndexPath indexPath2 = new IndexPath();
                indexPath2.x = i;
                indexPath2.y = j;
//                game.minefield.mineCellSquare.get(i).get(j).opened = true;
//                System.out.println("game.minefield.mineCellSquare.get(i).get(j).opened = " + game.minefield.mineCellSquare.get(i).get(j).opened);

            }
        }



//        print("Starting game")
//        setGameVariables()
//        if gameMode == 0 {
//            cellSize = calculateCellSize()
//            game.minefield.gameResult = .Continuing
//                    game = MinefieldGame(numberOfBombs: numberOfBombs, sizeOfField: sizeOfField)
//            runTimer()
//            //        resetViews()
//        } else if gameMode == 1 {
//            cellSize = calculateCellSize()
//            game.minefield.gameResult = .Continuing
//                    game = MinefieldGame(numberOfBombs: numberOfBombs, sizeOfField: sizeOfField, numberOfLives: numberOfLives)
//            runTimer()
//        }
//        print("Game started")
    }



    private void calculateCellSize() {

//        int tableWidth = tl.getWidth();
        int tableWidth = Resources.getSystem().getDisplayMetrics().widthPixels - 110;
        cellWidth = (tableWidth / sizeOfField) - 10;

        System.out.println("tableWidth = " + tableWidth);
        System.out.println("cellWidth = " + cellWidth);

//        return cellWidth;
    }

    private void createButtonViews(int rows, int cols) {
//        Log.d("createButtonViews","Buttons are created");

//        TableLayout tl = (TableLayout)findViewById(R.id.tableLayout);


//        TableRow row = new TableRow(this);
//        TextView tv = new TextView(this);
//        tv.setText("This is text");
//
//        tl.addView(row);
//        row.addView(tv);
//        int countNumber = 0;

        tl.removeAllViews();

        for (int i = 0; i < rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            tl.addView(row);
            row.setPadding(0,0,10,10);

//            System.out.println("new row");
            for (int j = 0; j < cols; j++) {

                MinecellView cell = new MinecellView(this);
                row.addView(cell);
//                cell.setBackgroundColor(Color.rgb(140, 45, 45));

                // Layout
                android.widget.TableRow.LayoutParams layout = new android.widget.TableRow.LayoutParams();
                layout.rightMargin = 10;
                layout.height = cellWidth;
                layout.width = cellWidth;
                cell.setLayoutParams(layout);

                // Set IndexPath tags for the cells
                IndexPath indexPath = new IndexPath();
                indexPath.x = i;
                indexPath.y = j;
                cell.setTag(indexPath);
                cell.setId(indexPath.x * 100 + indexPath.y);
//                System.out.println("Tag set x = " + indexPath.x + " y = " + indexPath.y);
//                System.out.println("cell.getTag() = " + cell.getTag());

                cell.setNumber(game.minefield.mineCellSquare.get(i).get(j).number);
//                cell.revealNumber();
//                cell.setNumber(1);
//                cell.revealNumber();

//                countNumber += 1;
//                System.out.println("column");

                // Set onClick handler
                cell.setOnClickListener(this);

            }
        }


        System.out.println("Buttons are created");

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == newGameButton.getId()) {
            // New game
            newGame();
            resetViews();
        } else if (view.getId() == settingsButton.getId()) {
            // New game
            Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(myIntent, 0);
        }

        if (!userInteractionsEnabled) { return; }

        // Flag view is clicked
        if (view instanceof ImageButton) {
            if (view.getId() == flagButton.getId()) {
                // Toggle Flag up or down
                toggleFlag();
            }
        }

        // region If clicked on a mine cell do this
        if (view instanceof MinecellView) {
            MinecellView minecellView = (MinecellView) view;
            IndexPath indexPath = new IndexPath();
            indexPath = (IndexPath) minecellView.getTag();
            System.out.println("Clicked x = " + indexPath.x + " y = " + indexPath.y);
            if (flagUp) {
                flagCell(indexPath);
            } else {
                revealCells(indexPath);
            }
        }
        // endregion


    }

    private void flagCell(IndexPath indexPath) {
        Integer i = indexPath.x;
        Integer j = indexPath.y;

        MinecellView cell = (MinecellView) tl.findViewById(i * 100 + j);

        if (game.minefield.mineCellSquare.get(i).get(j).opened) { return; }

        if (!cell.flagView.isShown() && numberOfRemainingBombs > 0) {
            numberOfRemainingBombs -= 1;
            cell.revealFlag();
            game.minefield.mineCellSquare.get(i).get(j).flagged = true;
        } else if (cell.flagView.isShown() && numberOfRemainingBombs <= numberOfBombs) {
            numberOfRemainingBombs += 1;
            cell.hideFlag();
            game.minefield.mineCellSquare.get(i).get(j).flagged = false;
        }
        refreshTopViews();
    }

    private void toggleFlag() {
        if (flagUp) {
            flagUp = false;
            flagButton.setImageResource(
                    getResources().getIdentifier(
                            "flag_down",
                            "drawable",
                            BuildConfig.APPLICATION_ID));
        } else {
            flagUp = true;
            flagButton.setImageResource(
                    getResources().getIdentifier(
                            "flag_up",
                            "drawable",
                            BuildConfig.APPLICATION_ID));

        }
    }

    public void setSmilyFace(Integer smile) {
        if (smile == 0) {
            newGameButton.setImageResource(
                    getResources().getIdentifier(
                            "smily_face",
                            "drawable",
                            BuildConfig.APPLICATION_ID));

        } else if (smile == 1) {
            newGameButton.setImageResource(
                    getResources().getIdentifier(
                            "dizzy_face",
                            "drawable",
                            BuildConfig.APPLICATION_ID));

        } else if (smile == 2) {
            newGameButton.setImageResource(
                    getResources().getIdentifier(
                            "cool_face",
                            "drawable",
                            BuildConfig.APPLICATION_ID));

        }
    }
    void revealCells(IndexPath indexPath) {

        System.out.println("revealCells indexPath.x = " + indexPath.x + " indexPath.y = " + indexPath.y);
        System.out.println("tl.getId() = " + tl.getId());
        game.minefield.openCells(indexPath);
        MinecellView cell;
//        System.out.println("MinecellView cell");

        for (int i = 0; i < sizeOfField; i++) {
            for (int j = 0; j < sizeOfField; j++) {
                IndexPath indexPath2 = new IndexPath();
                indexPath2.x = i;
                indexPath2.y = j;
//                System.out.println("i = " + i + " j = " + j);
//                cell = tl.findViewWithTag(indexPath2);
                cell = tl.findViewById(i * 100 + j);
//                System.out.println("cell.getTag() = " + cell.getTag());
//                cell.hideNumber();
                if (game.minefield.mineCellSquare.get(i).get(j).opened) {
                    cell.revealNumber();
                }

            }
        }
        numberOfLives = game.minefield.numberOfLives;
        livesLabel.setText("Lives:\n" + numberOfLives);
        if (game.minefield.gameResult != Continuing) { endGame(indexPath); }
    }
    public void endGame(IndexPath indexPath) {
        Integer i = indexPath.x;
        Integer j = indexPath.y;

        switch (game.minefield.gameResult) {
        case Failed:
            timer.cancel();
//            timer = null;
//            timer.purge();
            timerInstances--;
            System.out.println("Game ended!");
            System.out.println("You lost!");
            MinecellView cell = tl.findViewById(i * 100 + j);
            cell.setBackgroundColor(0xFFFF0000);
            userInteractionsEnabled = false;
            setSmilyFace(1);
//            newGameButton.setTitle("😵", for: .normal)
            break;
        case Success:
            timer.cancel();
//            timer = null;
//            timer.purge();
            timerInstances--;
            System.out.println("Game ended!");
            System.out.println("Success!");
            setSmilyFace(2);
            userInteractionsEnabled = false;
            break;
        case Continuing:
            System.out.println("You are continuing the game");
            userInteractionsEnabled = true;
            break;
        }

    }
}

class IndexPath {
    Integer x;
    Integer y;
//    public IndexPath(Integer x, Integer y) {
//        this.x = x;
//        this.y = y;
//    }
}