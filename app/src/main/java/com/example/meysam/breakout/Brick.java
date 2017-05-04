package com.example.meysam.breakout;

import android.graphics.RectF;

//****************************************************************
//Author: Meysam Mohajer                                         *
//Game: Breakout                                                 *
//Copyright: 2017                                                *
//Description: A class for Bricks                                *
//****************************************************************

//Class for Brick
public class Brick {

    //Variables
    private RectF rect;
    private boolean isVisible;

    //Constructor
    public Brick(int row, int column, int width, int height){

        //Initialize varables
        isVisible = true;
        int padding = 1;

        //Make the brick
        rect = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }

    //Get varaibles
    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}