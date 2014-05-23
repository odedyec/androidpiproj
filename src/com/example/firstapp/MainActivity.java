package com.example.firstapp;

import android.os.Bundle;
import android.os.Message;
import android.provider.Settings.System;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VerticalSeekBar;

public class MainActivity extends Activity {
	
	double roll = 0,pitch = 0,yaw = 0;
	double speed = 0;
	boolean switchState = true,manual = true;
	private SensorManager mSensor;
	private Sensor sensor;
	int ctr=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor.registerListener(odedListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        VerticalSeekBar speedBar = (VerticalSeekBar) findViewById(R.id.speed);
        speedBar.setOnSeekBarChangeListener(new speedBarClass(this));
        ToggleButton but = (ToggleButton) findViewById(R.id.FRswitch);
        but.toggle();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
    	return true;
    }
    public void directionHandler(View view)
    {
    	if (switchState)
    		switchState = false;
    	else
    		switchState = true;
    }
    public void manualHandler(View view)
    {
    	if (manual)
    		manual = false;
    	else
    		manual= true;
    }
    public void printHello(View view){
    	MessageBox("Hi, what's up??");
    }
   
    public void MessageBox(final String msg) { 
    	runOnUiThread(new Runnable() { 
    		public void run() { 
    			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this); 
    			dlgAlert.setMessage(msg); 
    			Log.d("speed",""+speed);
    			dlgAlert.setTitle("Oded said..."); 
    			dlgAlert.setPositiveButton("OK", null);
    			dlgAlert.setNegativeButton("No", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MessageBox("and now?");
					}
				});
    			dlgAlert.setCancelable(false); 
    			dlgAlert.create().show(); 
    			} 
    		}
    	); 
	}
    public Bitmap rotateImage(double degree) {
    	Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.sw);
        // create new matrix object
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate((float) degree);
        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }
    private double rad2deg(double num){
    	num = 180/Math.PI*num;
    	num = (num+360)%360;
    	return Math.round(100*num)/100;
    }
    public void speedbarHandler(View view){
    	MessageBox("Speed"+view);
    }
    private SensorEventListener odedListener = new SensorEventListener() 
    {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			double size1 = Math.sqrt(event.values[0]*event.values[0] + event.values[1]*event.values[1]);
			roll = rad2deg(Math.atan2(event.values[0]/size1, event.values[1]/size1)) - 90;
			double size2 = Math.sqrt(event.values[2]*event.values[2] + event.values[1]*event.values[1]);
			yaw = rad2deg(Math.atan2(event.values[2]/size2, event.values[1]/size2));
			double size3 = Math.sqrt(event.values[0]*event.values[0] + event.values[2]*event.values[2]);
			pitch = rad2deg(Math.atan2(event.values[0]/size3, event.values[2]/size3));
			ImageView steeringWheel = (ImageView) findViewById(R.id.steeringWheel);
			if (!manual)
				steeringWheel.setImageBitmap(rotateImage(roll));
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			Log.d("Sensor", "WHAT?!?!?!?!?!");
		}
	};
}
