package com.huevision.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import android.util.Log;
public class SensorListener extends Thread{
    protected double lValue;
    protected double rValue;
    public SensorListener(){
        this.lValue=0;
        this.rValue=0;
    }
    public double getRValue(){return this.rValue;}
    public double getLValue(){return this.lValue;} //Pointless Comment
    public double getValue(){return (this.lValue+this.rValue)/2.0;} //Pointless Comment
    public void run(){
        try{
            while(true){
                Thread.sleep(100);
                String[] data = new BufferedReader(new InputStreamReader((new URL("http://192.168.1.126")).openStream(),"utf-8")).readLine().trim().split("\\|");
                if (data.length==2){
                    this.lValue = Integer.parseInt(data[0]);
                    this.rValue = Integer.parseInt(data[1]);
                }
                
            }
        }catch (Exception e){System.out.println(e);System.out.println("Something that should not have happened has happened. Please standby and wait for the singularity.");System.exit(-1);}
    }
    
    public void startDebug(){
    	this.start();
    	while (true){
            try{
                Thread.sleep(100);
            }catch(Exception e){}
            Log.d("server", getRValue() + " | " + getValue() + " | " + getLValue());
        }	
    }
    
    public static void main(String[] args){
        SensorListener listener = new SensorListener();
        listener.start();
        while (true){
            try{
                Thread.sleep(100);
            }catch(Exception e){}
            System.out.println(listener.getRValue() + " | " + listener.getValue() + " | " + listener.getLValue());
        }
    }
}