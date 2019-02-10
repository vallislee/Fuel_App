package com.example.vallislee.fuel_consumption;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vallislee.fuel_consumption.database.DBHandler;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class Driving extends AppCompatActivity implements SensorEventListener {

    DBHandler mydb;
    TextView score_txt;

    private TextView drive_email,x,y,z;
    private Sensor mySensor;
    private SensorManager SM;

    private LineChart mChart;
    private Thread thread;
    private boolean plotData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        final Button Drive_start = (Button) findViewById(R.id.driving_start_btn);
        final Button Drive_stop = (Button) findViewById(R.id.driving_stop_btn);
        drive_email = (TextView) findViewById(R.id.driving_emailview);
        score_txt = (TextView) findViewById(R.id.score_view);

        Drive_stop.setVisibility(View.GONE);

        Intent intent = getIntent();
        final String email_address = intent.getStringExtra("email_address");
        drive_email.setText(email_address);

        final TextView driving_email = (TextView) findViewById(R.id.driving_emailview);
        driving_email.setText(email_address);

        // create sensormanager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Register sensor listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        if(mySensor != null) {
            SM.registerListener(this, mySensor, SM.SENSOR_DELAY_NORMAL);
        }

        mChart = (LineChart) findViewById(R.id.c1);

        mChart.getDescription().setEnabled(true);
        mChart.getDescription().setText("Real Time Plot");
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);


        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);

        feedMultiple();


        // Assign TextView
        x = (TextView) findViewById(R.id.driving_x_g);
        y = (TextView) findViewById(R.id.driving_y_g);
        z = (TextView) findViewById(R.id.driving_z_g);


        mydb = new DBHandler(this);
        Drive_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score_txt.setText("100");
                Drive_start.setVisibility(View.GONE);
                Drive_stop.setVisibility(View.VISIBLE);
            }
        });
        if(!x.getText().toString().equals("")){
            if(Double.valueOf(x.getText().toString())/9.8 > 1.25 || Double.valueOf(y.getText().toString())/9.8 > 1.25 ||
                    Double.valueOf(z.getText().toString())/9.8 > 1.25) {
                score_txt.setText(Integer.valueOf(score_txt.getText().toString()) - 5);
            }
        }


        Drive_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydb.add_score(email_address,score_txt.getText().toString());
                Drive_stop.setVisibility(View.GONE);
                Drive_start.setVisibility(View.VISIBLE);
            }
        });


    }

    private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }


    private void addEntry(SensorEvent event) {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if(set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), event.values[0]+5),0);
            data.notifyDataChanged();
            mChart.setMaxVisibleValueCount(150);
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.MAGENTA);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        x.setText(String.valueOf(Math.round((event.values[0]/9.81)*100.0)/100.0));
        y.setText(String.valueOf(Math.round((event.values[1]/9.81)*100.0)/100.0));
        z.setText(String.valueOf(Math.round((event.values[2]/9.81)*100.0)/100.0));

        if(plotData) {
            addEntry(event);
            plotData = false;

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    protected void onResume() {
        super.onResume();
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        SM.unregisterListener(Driving.this);
        thread.interrupt();
        super.onDestroy();
    }




}
