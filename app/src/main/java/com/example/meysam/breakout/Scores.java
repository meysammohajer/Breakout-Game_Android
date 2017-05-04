package com.example.meysam.breakout;

//****************************************************************
//Author: Meysam Mohajer                                         *
//Game: Breakout                                                 *
//Copyright: 2017                                                *
//Description: A class for showing scores                        *
//****************************************************************

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


//Class for showing scores
public class Scores extends Activity {

    //required variables
    ListView list, list_head;
    ArrayList<HashMap<String, String>> mylist, mylist_title;
    ListAdapter adapter_title, adapter;
    HashMap<String, String> map1, map2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //Initialize
        list = (ListView) findViewById(R.id.listView2);
        list_head = (ListView) findViewById(R.id.listView1);
        showActivity();
    }


    //Method for handling reading data and show it
    public void showActivity() {

        mylist = new ArrayList<HashMap<String, String>>();
        mylist_title = new ArrayList<HashMap<String, String>>();
        /**********Display the headings************/

        //Managing the HashMap
        map1 = new HashMap<String, String>();
        map1.put("usrname", "Username");
        map1.put("scr", " Score");
        mylist_title.add(map1);

        //Handle the headers
        try {
            adapter_title = new SimpleAdapter(this, mylist_title, R.layout.row,
                    new String[]{"usrname", "scr"}, new int[]{
                    R.id.usrname, R.id.scr});
            list_head.setAdapter(adapter_title);
        } catch (Exception e) {

        }

        /**********Display the contents************/

        try {
            //Open the file
            InputStream inputStream = openFileInput("topscoresFile.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                String all = "";
                int inx = 1;
                //read data line by line
                //StringBuilder temp = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {

                    String fi[] = receiveString.split(" ");
                    map2 = new HashMap<String, String>();

                    //Store the content in the arraylist
                    map2.put("usrname", fi[0]);
                    map2.put("scr", fi[1]);
                    mylist.add(map2);
                }
                inputStream.close();


                //Sort the data based on the score
                Collections.sort(mylist, new Comparator<HashMap<String, String>>() {
                    @Override
                    public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                        int first = Integer.parseInt(o1.get("scr"));
                        int second = Integer.parseInt(o2.get("scr"));
                        return Integer.compare(second, first);
                    }
                });


            }
        } catch (Exception e) {
            Log.e("login activity", "File not found: " + e.toString());
        }


        //Set the adapter for listview
        try {
            adapter = new SimpleAdapter(this, mylist, R.layout.row,
                    new String[]{"usrname", "scr"}, new int[]{
                    R.id.usrname, R.id.scr});
            //adapter.getCount() = new

            list.setAdapter(adapter);
        } catch (Exception e) {

        }

    }
}






    //Read data from file and load to the list
/*
    private String[] readFromFile() {

        //for showing name and number in the listview
        ArrayList<String> showlist=new ArrayList<String>();

        try {
            //Open the file
            InputStream inputStream = openFileInput("topscores5.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                String all = "";
                int inx = 1;
                //read data line by line
                //StringBuilder temp = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    StringBuilder stringBuilder = new StringBuilder();

                    //split data by tab
                    String fi[] = receiveString.split(" ");
                    stringBuilder.append("\t");
                    stringBuilder.append(fi[0]);
                    stringBuilder.append("\t\t");
                    stringBuilder.append(fi[1]);
                    stringBuilder.append("\n");

                    //add name and number to the showlist
                    showlist.add(stringBuilder.toString());

                    //all += Integer.toString(inx);

                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        Collections.sort(showlist);

        //String[] stockArr = new String[1];
        //stockArr[0] = "meysam   1";
        String[] stockArr = new String[showlist.size()];

        return showlist.toArray(stockArr);
        //return showlist.toArray(topScores);
    }
}
*/