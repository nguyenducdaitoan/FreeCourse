package com.android.test.freecourse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.android.test.freecourse.dao.DatabaseHelper;
import com.android.test.freecourse.util.SessionManager;

/**
 * MainActivity of the app
 * <p></p>
 * @author ToanNDD
 * @version 1.0.0
 * created 2016/29/05
 * company bonsey
 */
public class MainActivity extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);
    //Session Manager Class
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Session class instance
        session = new SessionManager(getApplicationContext());

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to DisplayActivity if user is logged in
         * */
        session.checkLogin();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onButtonClick(View v)
    {
        if(v.getId() == R.id.Blogin) {

            EditText strUsername = (EditText)findViewById(R.id.TFusername);
            String username = "", pass = "";
            if (strUsername != null) {
                 strUsername.getText().toString();
            }
            EditText strPassword = (EditText)findViewById(R.id.TFpassword);
            if (strPassword != null) {
                pass = strPassword.getText().toString();
            }

            String password = helper.searchPassword(username);
            if(pass.equals(password)) {
                //Creating user login session
                session.createLoginSession();
                //Staring MainActivity
                Intent i = new Intent(MainActivity.this, Display.class);
                startActivity(i);
            } else {
                Toast temp = Toast.makeText(MainActivity.this , "Username and password don't match!" , Toast.LENGTH_SHORT);
                temp.show();
            }
        }
        if(v.getId() == R.id.Bsignup)
        {
            Intent i = new Intent(MainActivity.this, SignUp.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
