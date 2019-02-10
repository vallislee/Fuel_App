package com.example.vallislee.fuel_consumption;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vallislee.fuel_consumption.database.DBHandler;

import java.util.Calendar;


public class fill_up extends AppCompatActivity {

    DBHandler mydb;
    EditText fillup_litres,fillUp_price,fillup_mileage;
    TextView fillup_date, fillup_cost;
    Button fillup_submit, fillup_cancel, fillup_date_btn;

    Calendar c;
    DatePickerDialog dpd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_up);

        mydb = new DBHandler(this);

        fillup_submit = (Button) findViewById(R.id.fillup_submit_btn);
        fillup_cancel = (Button) findViewById(R.id.fillup_cancel_btn);
        fillup_date_btn = (Button) findViewById(R.id.fillup_selectdate_btn);

        fillup_date = (TextView) findViewById(R.id.fillup_date_EditText);
        fillup_cost = (TextView) findViewById(R.id.fillup_cost_EditText);
        fillup_litres = (EditText) findViewById(R.id.fillup_litres_EdiText);
        fillUp_price = (EditText) findViewById(R.id.fillup_price_EditText);
        fillup_mileage = (EditText) findViewById(R.id.fillup_mileage_EditText);


        Intent intent = getIntent();
        final String email_address = intent.getStringExtra("email_address");

        fillup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent auth_intent = new Intent(fill_up.this, auth_main.class);
                auth_intent.putExtra("email_address", email_address);
                startActivity(auth_intent);
            }
        });

        // submit adding fuel to database
        fillup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date_str = fillup_date.getText().toString();
                String price_str = fillUp_price.getText().toString();
                String litres_str = fillup_litres.getText().toString();
                String mileage_str = fillup_mileage.getText().toString();

                if(!date_str.equals("")){
                    if(!price_str.equals("")){
                        if(!litres_str.equals("")){
                            if(!mileage_str.equals("")){

                                // correct data
                                if(mydb.fillup_insert(email_address,date_str,mileage_str,price_str,litres_str)){
                                    Toast.makeText(fill_up.this, "Data added successfully", Toast.LENGTH_LONG).show();
                                    Intent auth_intent = new Intent(fill_up.this, auth_main.class);
                                    auth_intent.putExtra("email_address", email_address);
                                    startActivity(auth_intent);
                                }else{
                                    Toast.makeText(fill_up.this, "Data added not successfully", Toast.LENGTH_LONG).show();
                                    Intent auth_intent = new Intent(fill_up.this, fill_up.class);
                                    auth_intent.putExtra("email_address", email_address);
                                    startActivity(auth_intent);
                                }

                            }else{
                                Toast.makeText(fill_up.this, "Please Enter Mileage", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(fill_up.this, "Please Enter Litres", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(fill_up.this, "Please Enter Gas Price", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(fill_up.this, "Please Select a date", Toast.LENGTH_LONG).show();
                }
            }
        });



        // date picker
        fillup_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(fill_up.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        fillup_date.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                        dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                    }
                },year, month, day);
                dpd.show();
                fillup_date_btn.setVisibility(View.GONE);
            }
        });

        fillup_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(fill_up.this, fillup_date.getText().toString(), Toast.LENGTH_LONG).show();
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(fill_up.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        fillup_date.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                        dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        fillUp_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!fillUp_price.getText().toString().equals("")) {
                    if(!fillup_litres.getText().toString().equals("")){
                        String price_str = fillUp_price.getText().toString();
                        String litres_str = fillup_litres.getText().toString();
                        Double cost = Double.parseDouble(price_str) * Double.parseDouble(litres_str);
                        fillup_cost.setText(String.valueOf(Math.round(cost*100.0)/100.0));
                    }else{
                        fillup_cost.setText("");
                    }
                }else{
                    fillup_cost.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fillup_litres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!fillup_litres.getText().toString().equals("")){
                    if(!fillUp_price.getText().toString().equals("")) {
                        String price_str = fillUp_price.getText().toString();
                        String litres_str = fillup_litres.getText().toString();
                        Double cost = Double.parseDouble(price_str) * Double.parseDouble(litres_str);
                        fillup_cost.setText(String.valueOf(Math.round(cost*100.0)/100.0));
                    }else{
                        fillup_cost.setText("");
                    }
                }else{
                    fillup_cost.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }
}
