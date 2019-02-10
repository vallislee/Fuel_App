package com.example.vallislee.fuel_consumption;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class auth_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main);


        Button setting_btn = (Button) findViewById(R.id.accountSET_btn);
        Button sum_btn = (Button) findViewById(R.id.summary_btn);
        Button fill_btn = (Button) findViewById(R.id.fillup_btn);
        Button drive_btn = (Button) findViewById(R.id.driving_btn);
        Button sig_out_btn = (Button) findViewById(R.id.auth_signout_btn);

        Intent intent = getIntent();
        final String email_address = intent.getStringExtra("email_address");

        // showing the current sign in email address
        final TextView account_email_view = (TextView) findViewById(R.id.account_email_text);
        account_email_view.setText(email_address);

        // account setting
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(auth_main.this, account_setting.class);
                setting_intent.putExtra("email_address", email_address);
                startActivity(setting_intent);
            }
        });

        // sign out
        sig_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent = new Intent(auth_main.this, MainActivity.class);
                startActivity(main_intent);
            }
        });

        // summary button
        sum_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent summary_intent = new Intent(auth_main.this, summary.class);
                summary_intent.putExtra("email_address", email_address);
                startActivity(summary_intent);
            }
        });

        // fill up button
        fill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fillup_intent = new Intent(auth_main.this, fill_up.class);
                fillup_intent.putExtra("email_address", email_address);
                startActivity(fillup_intent);
            }
        });


        // driving mode button
        drive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent drive_intent = new Intent(auth_main.this, Driving.class);
                drive_intent.putExtra("email_address", email_address);
                startActivity(drive_intent);
            }
        });

    }

    //disable back button
    @Override
    public void onBackPressed(){

    }
}
