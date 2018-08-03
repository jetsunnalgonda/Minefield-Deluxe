package com.games.aurelius.minefielddeluxe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MinecellView extends RelativeLayout {

    ImageView minecellBackground;
    ImageView numberView;
    ImageView flagView;

    public MinecellView(Context context) {
        super(context);
        init(context);
    }
    public MinecellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        minecellBackground = new ImageView(context);
        numberView = new ImageView(context);
        flagView = new ImageView(context);

        this.addView(numberView);
        this.addView(minecellBackground);
        this.addView(flagView);

//        hideNumber();
        hideFlag();

        minecellBackground.setImageResource(
                getResources().getIdentifier(
                        "mine_closed",
                        "drawable",
                        BuildConfig.APPLICATION_ID));
//                        "com.games.aurelius.minefielddeluxe"));

        flagView.setImageResource(
                getResources().getIdentifier(
                        "flag_up",
                        "drawable",
                        BuildConfig.APPLICATION_ID));
        setNumber(0);

//        System.out.println("MinecellView initialized");
    }

    public void setNumber(Integer number) {
        String numberImage = "number_" + number;

        if (number == -1) {
            numberImage = "bomb";
        }

//        System.out.println(numberImage);

        numberView.setImageResource(
                getResources().getIdentifier(
                        numberImage,
                        "drawable",
                        BuildConfig.APPLICATION_ID));
    }

    public void hideNumber() {
        minecellBackground.setVisibility(View.VISIBLE);
    }
    public void revealNumber() {
        minecellBackground.setVisibility(View.INVISIBLE);
    }

    public void hideFlag() {
        flagView.setVisibility(View.INVISIBLE);
    }
    public void revealFlag() {
        flagView.setVisibility(View.VISIBLE);
    }

}
