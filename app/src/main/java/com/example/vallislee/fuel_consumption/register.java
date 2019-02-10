package com.example.vallislee.fuel_consumption;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vallislee.fuel_consumption.database.DBHandler;
import com.example.vallislee.fuel_consumption.security.SaltedHash;


public class register extends AppCompatActivity {

    DBHandler myDb;
    EditText editEmail, editPw1, editPw2;
    String algorithm = "MD5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myDb = new DBHandler(this);

        editEmail = (EditText) findViewById(R.id.regisemail_EditText);
        editPw1 = (EditText) findViewById(R.id.regisPw_EditText);
        editPw2 = (EditText) findViewById(R.id.regisPw_EditText2);

        Button cancelBtn = (Button) findViewById(R.id.regisCancel_btn);
        Button regisBtn = (Button) findViewById(R.id.regisReg_btn);

        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sanitization san = new Sanitization();
                if (san.email_sanitization(editEmail.getText().toString())){
                    if(myDb.exist(editEmail.getText().toString()) == false) {
                        if(editPw1.getText().toString().length() < 19) {
                            if(editPw1.getText().toString().equals(editPw2.getText().toString())){
                                SaltedHash sh = new SaltedHash();
                                byte[] salt_byte = sh.generateSalt();
                                //String salt_str = Base64.getEncoder().encodeToString(salt_byte);
                                String salted_hash = sh.generateHash(editPw1.getText().toString(),algorithm,salt_byte);
                                boolean isRegistered = myDb.userregis(editEmail.getText().toString(), salted_hash, salt_byte);

                                if (isRegistered == true){
                                    Toast.makeText(register.this, "Registered", Toast.LENGTH_LONG).show();
                                    Intent LoginIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(LoginIntent);
                                }else{
                                    Toast.makeText(register.this, "Not Registered", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }else{
                        Toast.makeText(register.this, "e-mail existed", Toast.LENGTH_LONG).show();
                        Intent regisIntent = new Intent(getApplicationContext(), register.class);
                        startActivity(regisIntent);
                    }
                }else{
                    Toast.makeText(register.this, "Please enter a valid e-mail address", Toast.LENGTH_LONG).show();
                    Intent regisIntent = new Intent(getApplicationContext(), register.class);
                    startActivity(regisIntent);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent(getApplicationContext(), MainActivity.class);
                // Back to MainActivity(homepage)
                startActivity(cancelIntent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent cancelIntent = new Intent(getApplicationContext(), MainActivity.class);
        // Back to MainActivity(homepage)
        startActivity(cancelIntent);
    }
}
