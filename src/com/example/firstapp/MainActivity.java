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
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VerticalSeekBar;

public class MainActivity extends Activity {
	NetworkActivity network;
	float mPreviousX = 0,mPreviousY = 0;
	String ip = "132.73.200.197";
	double roll = 0,pitch = 0,yaw = 0;
	double speed = 0,steer = 0;
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
        Runnable r = new NetworkActivity(this, ip);
        Thread t = new Thread(r);
        t.start();
        this.network = (NetworkActivity) r;
        
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
   
    
    
    public Bitmap rotateImage(double degree) {
    	Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.sw);
        // create new matrix object
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate((float) (180/Math.PI*degree));
        //src = Bitmap.createScaledBitmap(src, 500, 500, true);
        //matrix.setScale(0.5f,0.5f);//Math.abs((float)Math.sin(degree)), Math.abs((float)Math.sin(degree)));
        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
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
			{
				steeringWheel.setImageBitmap(rotateImage(Math.PI/180*roll));
				network.addDatatoQueue("roll= "+roll+"   pitch= "+pitch);
			}
			
		}
		private double rad2deg(double num){
	    	num = 180/Math.PI*num;
	    	num = (num+360)%360;
	    	return Math.round(100*num)/100;
	    }
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			Log.d("Sensor", "WHAT?!?!?!?!?!");
		}
	};
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
//	@Override
//	public boolean onTouchEvent(MotionEvent e) {
//	    // MotionEvent reports input details from the touch screen
//	    // and other input controls. In this case, you are only
//	    // interested in events where the touch position changed.
//
//	    float x = e.getX();
//	    float y = e.getY();
//
//	    switch (e.getAction()) {
//	        case MotionEvent.ACTION_MOVE:
//
//	            float dx = x - mPreviousX;
//	            float dy = y - mPreviousY;
//
//	            // reverse direction of rotation above the mid-line
//	            if (y > 164) {
//	              dx = dx * -1 ;
//	            }
//
//	            // reverse direction of rotation to left of the mid-line
//	            if (x < 29 ) {
//	              dy = dy * -1 ;
//	            }
//
//	            steer -= (dx + dy)*0.2;
//	            ImageView steeringWheel = (ImageView) findViewById(R.id.steeringWheel);
//				steeringWheel.setImageBitmap(rotateImage(steer));
//	            
//	    }
//
//	    mPreviousX = x;
//	    mPreviousY = y;
//	    return true;
//	}
}
