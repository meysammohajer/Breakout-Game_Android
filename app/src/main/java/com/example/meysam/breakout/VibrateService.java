package com.example.meysam.breakout;


//****************************************************************
//Author: Meysam Mohajer                                         *
//Game: Breakout                                                 *
//Copyright: 2017                                                *
//Description: A class for handling vibrate                      *
//****************************************************************

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

//Class for vibrate
public class VibrateService extends Service
{
    @Override
    public void onStart(Intent intent, int startId)
    {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        // pass the number of millseconds fro which you want to vibrate the phone here we

        v.vibrate(500);

        //Toast.makeText(getApplicationContext(), "Phone is Vibrating", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
