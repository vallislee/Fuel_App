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

public class account_setting extends AppCompatActivity {

    DBHandler mydb;
    EditText old_pw, new_pw1, new_pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        mydb = new DBHandler(this);

        Button reset_btn = (Button) findViewById(R.id.AS_reset_btn);
        old_pw = (EditText) findViewById(R.id.AS_old_pw);
        new_pw1 = (EditText) findViewById(R.id.AS_new_pw1);
        new_pw2 = (EditText) findViewById(R.id.AS_new_pw2);


        Intent intent = getIntent();
        final String email_address = intent.getStringExtra("email_address");

        // showing the current sign in email address
        final TextView account_email_view = (TextView) findViewById(R.id.AS_email_address);
        account_email_view.setText(email_address);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(old_pw.getText().toString().length() < 19) {
                    String[] user = mydb.getUser(email_address);
                    byte[] user_salt = mydb.getUser_salt(email_address);
                    if(user != null) {
                        SaltedHash sh = new SaltedHash();
                        String hash = user[1];
                        String input_hash = sh.generateHash(old_pw.getText().toString(),"MD5",user_salt);

                        // if the old password is correct
                        if(input_hash.equals(hash)){
                            if(new_pw1.getText().toString().equals(new_pw2.getText().toString())){

                                SaltedHash as_sh = new SaltedHash();
                                byte[] salt_byte = as_sh.generateSalt();

                                String salted_hash = sh.generateHash(new_pw1.getText().toString(),"MD5",salt_byte);
                                boolean is_Updated = mydb.update_password(email_address, salted_hash, salt_byte);
                                if(is_Updated == true){
                                    Toast.makeText(account_setting.this, "password reset successfully", Toast.LENGTH_LONG).show();
                                    Intent auth_intent = new Intent(account_setting.this, auth_main.class);
                                    auth_intent.putExtra("email_address", email_address);
                                    startActivity(auth_intent);
                                }else{
                                    Toast.makeText(account_setting.this, "password not reset", Toast.LENGTH_LONG).show();
                                    Intent auth_intent = new Intent(account_setting.this, auth_main.class);
                                    auth_intent.putExtra("email_address", email_address);
                                    startActivity(auth_intent);
                                }

                            }else{
                                Toast.makeText(account_setting.this, "password does not match", Toast.LENGTH_LONG).show();
                                Intent as_intent = new Intent(account_setting.this, account_setting.class);
                                as_intent.putExtra("email_address", email_address);
                                startActivity(as_intent);
                            }

                        }else{
                            Toast.makeText(account_setting.this, "incorrect old password", Toast.LENGTH_LONG).show();
                            Intent as_intent = new Intent(account_setting.this, account_setting.class);
                            as_intent.putExtra("email_address", email_address);
                            startActivity(as_intent);
                        }
                    }else{
                        Toast.makeText(account_setting.this, "error", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(account_setting.this, "password exceed maximum length ", Toast.LENGTH_LONG).show();
                    Intent as_intent = new Intent(account_setting.this, account_setting.class);
                    as_intent.putExtra("email_address", email_address);
                    startActivity(as_intent);
                }
            }
        });


    }

    @Override
    public void onBackPressed(){
        Intent intent = getIntent();
        final String email_address = intent.getStringExtra("email_address");

        Intent cancelIntent = new Intent(account_setting.this, auth_main.class);
        cancelIntent.putExtra("email_address", email_address);
        startActivity(cancelIntent);
    }
}
