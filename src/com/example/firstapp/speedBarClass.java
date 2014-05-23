package com.example.firstapp;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class speedBarClass implements OnSeekBarChangeListener
{
	private MainActivity activity;
	public speedBarClass(MainActivity activity)
	{
		this.activity = activity;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) 
	{
		// TODO Auto-generated method stub
		activity.speed = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) 
	{
		// TODO Auto-generated method stub
		
	}
	
}
