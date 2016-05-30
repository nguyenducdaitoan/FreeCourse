package com.android.test.freecourse.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.test.freecourse.Display;

/**
 * SessionManager
 * <p></p>
 * @author ToanNDD
 * @version 1.0.0
 * created 2016/29/05
 * company bonsey
 */
public class SessionManager {
    //Shared Preferences
    SharedPreferences pref;

    //Editor for Shared preferences
    Editor editor;

    //Context
    Context _context;

    //Shared pref mode
    int PRIVATE_MODE = 0;

    //Sharedpref file name
    private final String PREF_NAME = "FreeCoursePref";

    //All Shared Preferences Keys
    private final String IS_LOGIN = "IsLoggedIn";

    //Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(){
        //Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If true it will redirect user to display page
     * Else won't do anything
     * */
    public void checkLogin(){
        //Check login status
        if(this.isLoggedIn()){
            //user is logged redirect him to display Activity
            Intent i = new Intent(_context, Display.class);
            //Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Quick check for login
     * **/
    //Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
