package com.example.firstapp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedList;

import android.util.Log;

public class NetworkActivity implements Runnable{
	private MainActivity activity;
	private String ip;
	private LinkedList<String> queue;
	public NetworkActivity(MainActivity activity, String ip)
	{
		this.activity = activity;
		this.ip = ip;
		queue = new LinkedList<String>();
		
	}
	@Override
	public void run() 
	{
		DatagramSocket sock = null;
		try
		{
			sock = new DatagramSocket();
			
			while (1==1)
			{
				while (!queue.isEmpty())
				{
					 
					String data =  queue.poll();
					queue.clear();
					DatagramPacket pkt = new DatagramPacket(data.getBytes(), data.length());
					pkt.setSocketAddress(new InetSocketAddress(InetAddress.getByName(ip),8001)); 
					sock.send(pkt); 
					
					sock.receive(pkt); //sock.close(); System.out.println("Got msg: " + new String(pkt.getData()))
				}
				Thread.sleep(50);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//activity.MessageBox("Connection crashed");
			sock.close();
		}
		
	}
	public void addDatatoQueue(String data)
	{
		queue.add(data);
	}
	
	

}
