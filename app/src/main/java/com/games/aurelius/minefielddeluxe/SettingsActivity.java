package com.games.aurelius.minefielddeluxe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.CheckBox;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String DifficultyKey = "Difficulty";
    public static final String GameModeKey = "GameMode";

    TableLayout tl;
    Button backButton;

//    public float density = getResources().getDisplayMetrics().density;

    class Settings {
        Boolean checked = false;
        String name;
    }

    String choices[][] = new String[][] {
            {"Classic","Multiple Lives"},
            {"Easy", "Medium", "Hard" }};


    List<ArrayList<Settings>> choice = new ArrayList<ArrayList<Settings>>();

    String headers[] = {"Game Mode", "Difficulty"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tl = findViewById(R.id.tableLayout);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(this);

        makeChoicesArray();
        createSettingsViews(choices);
//        System.out.println("choices.length = " + choices.length);
        System.out.println("choices = " + Arrays.deepToString(choices));
//        System.out.println("choices[0].length = " + choices[0].length);
//        System.out.println("choices[1].length = " + choices[1].length);
//        System.out.println("choices[0] = " + Arrays.toString(choices[0]));
//        System.out.println("choices[1] = " + Arrays.toString(choices[1]));

        getSavedSettings();
    }

    private void makeChoicesArray() {
        for (String col[]: choices) {
            ArrayList<Settings> subArray = new ArrayList<Settings>();
//            Settings subArray = new Settings[];
            for (String row: col) {
                subArray.add(new Settings());

            }
            choice.add(subArray);
        }

        Integer i = 0;
        for (String col[]: choices) {
            Integer j = 0;
            for (String row: col) {
                choice.get(i).get(j).name = row;
                j += 1;
            }
            i += 1;
        }

        System.out.println("choices = " + Arrays.deepToString(choices));

    }
    private void createSettingsViews(String[][] settingsArray) {

        Integer numberOfSections = settingsArray.length;
        Typeface typeface = Typeface.createFromAsset(getAssets(),
                "fonts/average.ttf");

        tl.removeAllViews();

        for (int i = 0; i < numberOfSections; i++) {
            Integer numberOfRows = settingsArray[i].length;

            // Add a section header
            TableRow headerRow = new TableRow(this);
            headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            tl.addView(headerRow);
            headerRow.setPadding(10, 10, 10, 40);
            TextView headerCell = new TextView(this);
            headerRow.addView(headerCell);
            headerCell.setText(headers[i]);
            headerCell.setTypeface(typeface);
            headerCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
            headerCell.setHeight(150);
            headerRow.setGravity(Gravity.CENTER);
            headerCell.setGravity(Gravity.CENTER);

            headerRow.setBackgroundColor(Color.rgb(230, 230, 230));

            // Layout
            android.widget.TableRow.LayoutParams layout2 = new android.widget.TableRow.LayoutParams();
            layout2.setMargins(10,20,10,10);
            headerRow.setLayoutParams(layout2);
            headerRow.setLayoutParams(layout2);

            // Add a padding row
//            TableRow row2 = new TableRow(this);
//            row2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 50));
//            tl.addView(row2);
//            row2.setPadding(10, 0, 10, 10);
//            row2.setBackgroundColor(Color.rgb(255, 155, 155));
//            android.widget.TableRow.LayoutParams layout2 = new android.widget.TableRow.LayoutParams();
//            layout2.setMargins(20,10,10,30);
//            row2.setLayoutParams(layout2);

            for (int j = 0; j < numberOfRows; j++) {
                TableRow row = new TableRow(this);
//                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 150));
                tl.addView(row);
                row.setPadding(10, 0, 10, 10);
                row.setGravity(Gravity.CENTER);

                row.setBackgroundColor(Color.rgb(255, 255, 255));
//                row.layout(10,10,10,10);



                CheckBox cell = new CheckBox(this);
//            TextView cell = new TextView(this);
                row.addView(cell);
//                cell.setBackgroundColor(Color.rgb(140, 45, 45));

                // Layout
                android.widget.TableRow.LayoutParams layout = new android.widget.TableRow.LayoutParams();
                layout.rightMargin = 10;

//                layout.height = 150;
//                layout.width = headerCell.getWidth();
//            layout.width = cellWidth;
                layout.setMargins(20,10,20,10);
                cell.setLayoutParams(layout);
                cell.setMinimumHeight(100);
                cell.setBackgroundColor(Color.rgb(255, 255, 255));
//                cell.setGravity(Gravity.CENTER);
//                System.out.println("row.getHeight()" + row.getHeight());
//                System.out.println("cell.getHeight()" + cell.getHeight());


                // Set IndexPath tags for the cells
                IndexPath indexPath = new IndexPath();
                indexPath.x = i;
                indexPath.y = j;
                cell.setTag(indexPath);
                cell.setId(indexPath.x * 100 + indexPath.y);
//                System.out.println("Tag set x = " + indexPath.x + " y = " + indexPath.y);
//                System.out.println("cell.getTag() = " + cell.getTag());

                // Set text of cells
                cell.setText(settingsArray[i][j]);
//                System.out.println("cell.getId() = " + cell.getId());
//                System.out.println("cell.getText() = " + cell.getText());

                // Text attributes
                cell.setTypeface(typeface);
                cell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
//                System.out.println("cell.getHeight()" + cell.getHeight());

//                cell.setGravity(Gravity.CENTER);
//                cell.setBackgroundColor(Color.TRANSPARENT);
//                cell.setTextScaleX(2);
//            cell.setNumber(game.minefield.mineCellSquare.get(i).get(j).number);
//                cell.revealNumber();
//                cell.setNumber(1);
//                cell.revealNumber();

//                countNumber += 1;
//                System.out.println("column");

                // Set onClick handler
                cell.setOnClickListener(this);
//                layout.setMargins(20,0,10,20);
                row.setLayoutParams(layout);

                // Add a padding row
//                TableRow row3 = new TableRow(this);
//                row2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 50));
//                tl.addView(row3);
//                row3.setPadding(10, 0, 10, 10);
//                row3.setBackgroundColor(Color.rgb(255, 155, 155));
//                android.widget.TableRow.LayoutParams layout3 = new android.widget.TableRow.LayoutParams();
//                layout3.setMargins(20,0,10,30);
//                row3.setLayoutParams(layout3);

            }
        }


        System.out.println("Buttons are created");


    }

    public void getSavedSettings() {
        // Saved Settings
//        Integer choiceGameMode = 0;
//        Integer choiceDifficulty = 0;

        Integer choiceGameMode = sharedPreferences.getInt(GameModeKey, 0);
        Integer choiceDifficulty = sharedPreferences.getInt(DifficultyKey, 0);

        // Check saved settings
        IndexPath indexPath1 = new IndexPath();
        indexPath1.x = 0;
        indexPath1.y = choiceGameMode;

        IndexPath indexPath2 = new IndexPath();
        indexPath2.x = 1;
        indexPath2.y = choiceDifficulty;

        checkChoice(choiceGameMode, 0);
        checkChoice(choiceDifficulty, 1);
    }

    private void checkChoice(Integer row, Integer col) {

//        for (Integer j = 0; j < choices[0].length; j++) {
//            System.out.println("choice.get(0).get(j).name = " + choice.get(0).get(j).name);
//        }
//
//        for (Integer j = 0; j < choices[1].length; j++) {
//            System.out.println("choice.get(1).get(j).name = " + choice.get(1).get(j).name);
//        }

//        Integer col = indexPath.x;
//        Integer row = indexPath.y;

        for (Integer i = 0; i < choices[col].length; i++) {
            Integer viewID = col * 100 + i;
            CheckBox otherCell = findViewById(viewID);
            choice.get(col).get(i).checked = false;
            otherCell.setChecked(false);
            System.out.println("i = " + i + ", col = " + col);
//            System.out.println("otherCell.isChecked() = " + otherCell.isChecked());
        }

        Integer viewID2 = col * 100 + row;
//        Integer viewID2 = 101;
        CheckBox cell = findViewById(viewID2);

//        System.out.println("col = " + col + ", row = " + row);
//        System.out.println("cell.getText() = " + cell.getText());
//        System.out.println("choice.get(col).get(row).name = " + choice.get(col).get(row).name);

        choice.get(col).get(row).checked = true;
        cell.setChecked(true);

        if (col == 0) {
            editor.putInt(GameModeKey, row);
            editor.commit();
        } else if (col == 1) {
            editor.putInt(DifficultyKey, row);
            editor.commit();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == backButton.getId()) {
//            finishActivity(0);
            finish();
        }
        if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            Integer number = checkBox.getId();
            IndexPath indexPath = new IndexPath();
            indexPath.x = number / 100;
            indexPath.y = number % 100;

//            System.out.println("indexPath.x = " + indexPath.x);
//            System.out.println("indexPath.y = " + indexPath.y);
//            System.out.println("number = " + number);

            checkChoice(indexPath.y, indexPath.x);
        }
    }
}
