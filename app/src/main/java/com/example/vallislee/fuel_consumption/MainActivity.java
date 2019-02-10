package com.example.vallislee.fuel_consumption;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vallislee.fuel_consumption.database.DBHandler;
import com.example.vallislee.fuel_consumption.security.SaltedHash;

public class MainActivity extends AppCompatActivity {

    DBHandler mydb;
    EditText email_address,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHandler(this);

        Button LoginBtn = (Button) findViewById(R.id.Login_btn);
        TextView RegisBtn = (TextView) findViewById(R.id.Register_btn1);

        email_address = (EditText) findViewById(R.id.loginemail_EditText);
        password = (EditText) findViewById(R.id.loginPw_EditText);

        LoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Sanitization san = new Sanitization();
                if (san.email_sanitization(email_address.getText().toString())){
                    if(password.getText().toString().length() < 19) {
                        if(mydb.exist(email_address.getText().toString())){
                            String[] user = mydb.getUser(email_address.getText().toString());
                            byte[] user_salt = mydb.getUser_salt(email_address.getText().toString());
                            if(user != null) {
                                SaltedHash sh = new SaltedHash();
                                String hash = user[1];
                                String input_hash = sh.generateHash(password.getText().toString(),"MD5",user_salt);

                                // authenticated
                                if(input_hash.equals(hash)){
                                    Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_LONG).show();
                                    Intent auth_intent = new Intent(MainActivity.this, auth_main.class);
                                    auth_intent.putExtra("email_address",email_address.getText().toString());
                                    startActivity(auth_intent);

                                }else{
                                    Toast.makeText(MainActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    Intent currentIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(currentIntent);
                                }

                            }else{
                                Toast.makeText(MainActivity.this, "email does not exist", Toast.LENGTH_LONG).show();
                                Intent currentIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(currentIntent);
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "email does not exist", Toast.LENGTH_LONG).show();
                            Intent currentIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(currentIntent);
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "password length must be less than 18", Toast.LENGTH_LONG).show();
                        Intent currentIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(currentIntent);
                    }
                }else{
                    Toast.makeText(MainActivity.this, "wrong email format", Toast.LENGTH_LONG).show();
                    Intent currentIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(currentIntent);
                }
            }

        });

        RegisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regisIntent = new Intent(getApplicationContext(), register.class);
                //to register page
                startActivity(regisIntent);
            }
        });

    }
}
