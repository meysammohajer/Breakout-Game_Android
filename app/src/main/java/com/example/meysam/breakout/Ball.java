package com.example.meysam.breakout; /**
 * Created by Meysam on 4/5/2017.
 */

//********************************
//Author: Meysam Mohajer         *
//Game: Breakout                 *
//Copyright: 2017                *
//Description: A class for Ball  *
//********************************

import android.graphics.RectF;
import java.util.Random;

//Class for Ball
public class Ball {

    RectF rect;
    float xVelocity;
    float yVelocity;
    float ballWidth = 14;
    float ballHeight = 14;

    public Ball(int screenX, int screenY){

        // Start the ball travelling straight up at 100 pixels per second
        xVelocity = 200;
        yVelocity = -400;

        // Place the ball in the centre of the screen at the bottom
        // Make it a 10 pixel x 10 pixel square
        rect = new RectF();

    }

    public RectF getRect(){
        return rect;
    }

    //Update the variables
    public void update(long fps){
        rect.left = rect.left + (xVelocity / fps);
        rect.top = rect.top + (yVelocity / fps);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top - ballHeight;
    }

    //Get the positions
    public void reverseYVelocity(){
        yVelocity = -yVelocity;
    }

    public void reverseXVelocity(){
        xVelocity = - xVelocity;
    }

    //Set the velocity
    public void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseXVelocity();
        }
    }

    //Method for clearObstacleY
    public void clearObstacleY(float y){
        rect.bottom = y;
        rect.top = y - ballHeight;
    }

    //Method for clearObstacleX
    public void clearObstacleX(float x){
        rect.left = x;
        rect.right = x + ballWidth;
    }

    //Method for reset the ball
    public void reset(int x, int y){
        rect.left = x / 2;
        rect.top = y - 20;
        rect.right = x / 2 + ballWidth;
        rect.bottom = y - 20 - ballHeight;
    }

}