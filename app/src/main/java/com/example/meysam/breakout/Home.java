package com.example.meysam.breakout;

//****************************************************************
//Author: Meysam Mohajer                                         *
//Game: Breakout                                                 *
//Copyright: 2017                                                *
//Description: A class for home menu                             *
//****************************************************************

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


//Manage the Home screen
public class Home extends Activity
{

    //Initialize varirables
    private String username = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.homeLayout);
        //rl.setBackgroundColor(Color.CYAN);

        //Listener for play button
        Button play = (Button) findViewById(R.id.btnPlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                promptForResult(new PromptRunnable() {
                    // put whatever code you want to run after user enters a result
                    public void run() {
                        // get the value we stored from the dialog
                        String value = this.getValue();
                        // do something with this value...
                        Intent myIntent = new Intent(getApplication(), Breakout.class);
                        myIntent.putExtra("username", value); //Optional parameters
                        startActivityForResult(myIntent, 0);
                    }
                });

            }
        });


        //Listener for score button
        Button score = (Button) findViewById(R.id.btnScores);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Scores.class);
                //myIntent.putExtra("username", value); //Optional parameters
                startActivityForResult(myIntent, 0);
            }
        });


        //Listener for exit button
        Button exit = (Button) findViewById(R.id.btnExit);
        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });


    }


    //Handler for gettinh username
    void promptForResult(final PromptRunnable postrun) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("User Name");
        alert.setMessage("Enter yourn name please.");
        // Create textbox to put into the dialog
        final EditText input = new EditText(this);
        // put the textbox into the dialog
        alert.setView(input);
        // procedure for when the ok button is clicked.
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                dialog.dismiss();
                // set value from the dialog inside our runnable implementation
                postrun.setValue(value);
                // ** HERE IS WHERE THE MAGIC HAPPENS! **
                // now that we have stored the value, lets run our Runnable
                postrun.run();
                return;
            }
        });

        //Cancel the action
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });
        alert.show();
    }

}

//Handling the dialog box
class PromptRunnable implements Runnable {
    private String v;
    void setValue(String inV) {
        this.v = inV;
    }
    String getValue() {
        return this.v;
    }
    public void run() {
        this.run();
    }
}