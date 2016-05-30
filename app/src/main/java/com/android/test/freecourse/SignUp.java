package com.android.test.freecourse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.test.freecourse.dao.DatabaseHelper;
import com.android.test.freecourse.model.User;

/**
 * Created by TOAN on 5/18/2016.
 */
public class SignUp extends Activity {

    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }

    public void onSignUpClick(View v)
    {
        if(v.getId()== R.id.Bsignupbutton)
        {
            EditText uname = (EditText)findViewById(R.id.TFuname);
            EditText pass1 = (EditText)findViewById(R.id.TFpass1);
            EditText pass2 = (EditText)findViewById(R.id.TFpass2);

            String unamestr = uname.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();

            if(!pass1str.equals(pass2str))
            {
                //popup msg
                Toast pass = Toast.makeText(SignUp.this , "Passwords don't match!" , Toast.LENGTH_SHORT);
                pass.show();
            }
            else
            {
                //insert the detail user into database
                User user = new User();
                user.setUsername(unamestr);
                user.setPassword(pass1str);

                helper.insertUser(user);

                //popup msg
                Toast pass = Toast.makeText(SignUp.this , "Register has been completed please login!" , Toast.LENGTH_SHORT);
                pass.show();
            }

        }

    }
}
