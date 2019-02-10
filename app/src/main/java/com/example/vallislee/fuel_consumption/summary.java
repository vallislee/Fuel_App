package com.example.vallislee.fuel_consumption;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vallislee.fuel_consumption.database.DBHandler;

public class summary extends AppCompatActivity {

    DBHandler mydb;
    TextView LfillUp_date, fuel_consum_text, driving_score, mileage_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        mydb = new DBHandler(this);

        Button return_btn = (Button) findViewById(R.id.sum_return_btn);
        LfillUp_date = (TextView) findViewById(R.id.last_fillUp_EditText);
        fuel_consum_text = (TextView) findViewById(R.id.fuel_con_EditText);
        driving_score = (TextView) findViewById(R.id.drive_behavior_EditText);
        mileage_text = (TextView) findViewById(R.id.mileage_EditText);

        Intent intent = getIntent();
        final String email_address = intent.getStringExtra("email_address");

        final TextView sum_email_view = (TextView) findViewById(R.id.sum_emailView);
        sum_email_view.setText(email_address);

        // check whether current user have fill up data or not
        if(mydb.fillUp_exist(email_address)){
            String date_str = mydb.last_fillup_date(email_address);
            Double mileages = mydb.mileages_fillup(email_address);
            Double litres = mydb.litres_fillup(email_address);


            LfillUp_date.setText(date_str);
            mileage_text.setText(String.valueOf(mileages));
            Double fule_c = (Math.round((litres/mileages)*100.0)/100.0);
            fuel_consum_text.setText(String.valueOf(Math.round((fule_c*100)*100.0)/100.0) + " L/100km");

            // Driving Behavior .........
            Double scores = mydb.get_score(email_address);
            if(scores != 0) {
                if(scores >= 80) {
                    driving_score.setText("Good");
                }else if(scores >= 70 && scores < 80) {
                    driving_score.setText("Normal");
                }else if(scores < 70) {
                    driving_score.setText("Bad");
                }
            }else{
                driving_score.setText("None");
            }


        }else{
            Toast.makeText(summary.this, "no data", Toast.LENGTH_LONG).show();
        }


        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent return_intent = new Intent(summary.this, auth_main.class);
                return_intent.putExtra("email_address", email_address);
                startActivity(return_intent);
            }
        });
    }

}
