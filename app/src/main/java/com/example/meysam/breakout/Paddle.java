package com.example.meysam.breakout;

 //****************************************************************
 //Author: Meysam Mohajer                                         *
 //Game: Breakout                                                 *
 //Copyright: 2017                                                *
 //Description: A class for Paddle                                *
 //****************************************************************

import android.graphics.RectF;

public class Paddle {

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speed that the paddle will move
    private float paddleSpeed;

    // RectF is an object that holds four coordinates - just what we need
    private RectF rect;

    // How long and high our paddle will be
    private float length;
    private float height;


    // Which ways can the paddle move
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Is the paddle moving and in which direction
    private int paddleMoving = STOPPED;

    //For limiting the screen
    public int screenLimit;

    // This the the constructor method
    public Paddle(int screenX, int screenY){
        // 130 pixels wide and 20 pixels high
        length = 180;
        height = 40;

        // Start paddle in roughly the sceen centre
        x = screenX / 2;
        y = screenY - 20;

        rect = new RectF(x, y, x + length, y + height);

        // How fast is the paddle in pixels per second
        paddleSpeed = 400;

        screenLimit = screenX;
    }

    // This is a getter method to make the rectangle that
    public RectF getRect(){
        return rect;
    }

    // This method will be used to change/set if the paddle is going left, right or nowhere
    public void setMovementState(int state){
        paddleMoving = state;
    }

    // This update method will be called from update in BreakoutView
    public void update(long fps){
        if(paddleMoving == LEFT){

            x = x - paddleSpeed / fps;
        }

        if(paddleMoving == RIGHT){
            x = x + paddleSpeed / fps;
        }

        rect.left = x;
        rect.right = x + length;

        // If the ball hits left wall bounce
        if (rect.left < 0){paddleMoving = STOPPED;}

        // If the ball hits right wall bounce
        if (rect.right > screenLimit){paddleMoving = STOPPED;}
    }

}
