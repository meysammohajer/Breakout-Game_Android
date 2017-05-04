package com.example.meysam.breakout;

//****************************************************************
//Author: Meysam Mohajer                                         *
//Game: Breakout                                                 *
//Copyright: 2017                                                *
//Description: A class for Handling events and screen touches    *
//****************************************************************

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.graphics.Color.argb;

public class Breakout extends Activity implements SensorEventListener {

    // gameView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    BreakoutView breakoutView;

    //******SENSOR
    // Start with some variables
    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    //Variables for win, lost, vibrate situation
    private boolean userWon;
    private boolean userLost;
    private boolean shaked = false;
    private Vibrator vib;
    private String username;
    private ArrayList<String> list;
    //******SENSOR


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize gameView and set it as the view
        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);

        //******SENSOR
        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

            Intent intent = getIntent();
            username = intent.getStringExtra("username"); //if it's a string you stored.
        list = new ArrayList<String>();
        //To see if the user has won or lost
        userWon = false;
        userLost = false;
        //******SENSOR


    }

    // Here is our implementation of BreakoutView
    // It is an inner class.

    class BreakoutView extends SurfaceView implements Runnable {


        // The size of the screen in pixels
        int screenX;
        int screenY;

        // Game is paused at the start
        boolean paused = true;

        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;

        // This variable tracks the game frame rate
        long fps;

        // This is the thread
        Thread gameThread = null;

        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        SurfaceHolder ourHolder;

        // A boolean which we will set and unset
        volatile boolean playing;

        // This is used to help calculate the fps
        private long timeThisFrame;


        // The players paddle
        Paddle paddle;

        // A ball
        Ball ball;

        // Up to 200 bricks
        Brick[] bricks = new Brick[200];
        int numBricks = 0;

        // The score
        int score = 0;

        // Lives
        int lives = 3;

        // When the we initialize (call new()) on gameView
        public BreakoutView(Context context) {
            super(context);

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            // Get a Display object to access screen details
            Display display = getWindowManager().getDefaultDisplay();
            // Load the resolution into a Point object
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            //Creat the paddle
            paddle = new Paddle(screenX, screenY);

            // Create a ball
            ball = new Ball(screenX, screenY);

            createTheScreenObjects();

        }

        public void createTheScreenObjects() {

            // Put the ball back to the start
            ball.reset(screenX, screenY);

            int brickWidth = screenX / 8;
            int brickHeight = screenY / 10;

            // Build a wall of bricks
            numBricks = 0;
            for (int column = 0; column < 8; column++) {
                for (int row = 0; row < 3; row++) {
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    numBricks++;
                }
            }
            // if game over reset scores and lives
            if (lives == 0) {
                score = 0;
                lives = 3;
                userWon = false;
                userLost = false;
            }

        }

        @Override
        public void run() {
            while (playing) {
                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();
                // Update the frame
                if (!paused) {
                    update();
                }
                // Draw the frame
                draw();
                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1) {
                    fps = 600 / timeThisFrame;
                }

            }

        }

        // Everything that needs to be updated goes in here
        // Movement, collision detection etc.
        public void update() {

            // Move the paddle if required
            paddle.update(fps);

            ball.update(fps);

            // Check for ball colliding with a brick
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bricks[i].getRect(), ball.getRect())) {
                        bricks[i].setInvisible();
                        //If user shakes the phone
                        if(shaked){shaked = false; score = score + 12; continue;}
                        score = score + 10;
                        ball.reverseYVelocity();
                    }
                }
            }


            // Check for ball colliding with paddle
            if (RectF.intersects(paddle.getRect(), ball.getRect())) {
                ball.setRandomXVelocity();
                ball.reverseYVelocity();
                ball.clearObstacleY(paddle.getRect().top - 2);
            }
            // Bounce the ball back when it hits the bottom of screen
            if (ball.getRect().bottom > screenY) {
                ball.reverseYVelocity();
                ball.clearObstacleY(screenY - 2);

                // Lose a life
                lives--;

                //**!!
                paused = true;

                if (lives == 0) {
                    userLost = true;
                    paused = true;
                    saveScore(score);
                    createTheScreenObjects();
                }
            }

            // Bounce the ball back when it hits the top of screen
            if (ball.getRect().top < 0)

            {
                ball.reverseYVelocity();
                ball.clearObstacleY(12);
            }

            // If the ball hits left wall bounce
            if (ball.getRect().left < 0)

            {
                ball.reverseXVelocity();
                ball.clearObstacleX(2);
            }

            // If the ball hits right wall bounce
            if (ball.getRect().right > screenX - 10) {

                ball.reverseXVelocity();
                ball.clearObstacleX(screenX - 22);
            }

            // Pause if cleared screen
            //if (score == numBricks * 10)
            if(userWon)
            {
                paused = true;
                saveScore(score);
                createTheScreenObjects();
            }

        }

        // Draw the newly updated scene
        public void draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                // Draw the background color
                canvas.drawColor(Color.GRAY);//argb(255, 26, 128, 182));
                //canvas.drawColor(argb(255,128,81,20));
                canvas.drawColor(argb(255,110,85,82));

                //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bg4),0,0,null);


                // Choose the brush color for drawing
                paint.setColor(Color.WHITE);//.argb(255, 255, 255, 255));

                // Draw the paddle
                canvas.drawRect(paddle.getRect(), paint);

                // Draw the ball
                canvas.drawRect(ball.getRect(), paint);
                //canvas.drawArc(Float.parseFloat("75"),Float.parseFloat("75"),Float.parseFloat("10"),Float.parseFloat("6.28"),Float.parseFloat("6.28"),Float.parseFloat("6.28"),true,paint);
                // Change the brush color for drawing

                //paint.setColor(Color.GREEN);//argb(255, 249, 129, 0));
                paint.setColor(argb(255,45,150,0));

                // Draw the bricks if visible
                int count = 0;
                for (int i = 0; i < numBricks; i++) {
                    if (bricks[i].getVisibility()) {
                        canvas.drawRect(bricks[i].getRect(), paint);
                        count++;
                    }
                }
                if(count == 0)
                {
                    userWon = true;

                }

                // Choose the brush color for drawing
                paint.setColor(argb(255, 255, 255, 255));

                // Draw the score
                paint.setTextSize(80);
                canvas.drawText("Lives: " + lives + "  " + username + "'s Score: " + score, 20, 80, paint);

                // Has the player cleared the screen?
                if (userWon) {
                    paint.setTextSize(90);
                    canvas.drawText("Congratulations !", 10, screenY / 2, paint);

                    //saveScore(score);

                }

                // Has the player lost?
                if (userLost) {
                    paint.setTextSize(90);
                    canvas.drawText("Game Over!", 10, screenY / 2, paint);


                    //saveScore(score);

                }

                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    paused = false;
                    if (motionEvent.getX() > screenX / 2) {

                        paddle.setMovementState(paddle.RIGHT);
                    } else

                    {
                        paddle.setMovementState(paddle.LEFT);
                    }

                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    paddle.setMovementState(paddle.STOPPED);
                    break;
            }

            return true;
        }

    }
    // This is the end of our BreakoutView inner class

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        breakoutView.resume();

        //*****SENSOR
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
        //*****SENSOR
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        breakoutView.pause();

        //*****SENSOR
        sensorMan.unregisterListener(this);
        //*****SENSOR


    }


    //******SENSOR
    @Override
    //Handle shaking
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if(mAccel > 15){
                //onPause();
                //Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                //vib.vibrate(1000);
                Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                Intent intentVibrate =new Intent(getApplicationContext(),VibrateService.class);
                startService(intentVibrate);
                shaked=true;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }
    //******SENSOR


    //**** store Scores
    public void saveScore(int scr)
    {

        //String score = Integer.toString(scr);
        StringBuilder userscore = new StringBuilder();
        userscore.append(username);
        userscore.append(" ");
        userscore.append(Integer.toString(scr));

        readFile();
        list.add(userscore.toString());

        try {
            //write to the file
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("topscoresFile.txt", Context.MODE_PRIVATE));

            for (String rec : list)
            {
                StringBuilder newl = new StringBuilder();
                newl.append(rec);
                newl.append(System.getProperty("line.separator"));
                outputStreamWriter.write(newl.toString());
            }
            outputStreamWriter.close();
        }
         catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }




    //load records from file to the list
    public void readFile() {

        String line;

        try {
            //open the file and load data
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(openFileInput("topscoresFile.txt")));
            while ((line = buffreader.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// This is the end of the BreakoutGame class