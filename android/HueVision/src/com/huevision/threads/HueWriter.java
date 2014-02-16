package com.huevision.threads;

import android.util.Log;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

public class HueWriter extends Thread{
    protected int hue;
    protected int sat;
    protected int bri;
    protected boolean on;
    
    protected boolean changed;
    
    protected int numLights;
    private PHHueSDK phHueSDK;
    public HueWriter(int numLights){
    	Log.d("FOOBAR","Huewriter created.");
        this.numLights = numLights;
        this.on=false;
        this.hue=0;
        this.sat=0;
        this.bri=0;
        this.phHueSDK = PHHueSDK.create();
        this.changed = true;
    }
    
    public void setHue(int h){this.hue=h; this.changed=true;}
    public void setSat(int s){this.sat=s; this.changed=true;}
    public void setBri(int b){this.bri=b; this.changed=true;}
    public void setHSV(int h, int s, int b){this.hue=h; this.sat=s; this.bri=b; this.changed=true;}
    public void setOn(boolean on){this.on=on; this.changed=true;}
    
    int getBri(){return bri;}
    int getSat(){return sat;}
    int getHue(){return hue;}
    public boolean getOn(){return on;}
    
    public void run(){
    	Log.d("FOOBAR","Huewriter started.");
        try{
            //URL hue_url = new URL("http://192.168.1.120/api/arctangent1759/lights/"+myLight+"/state");
            PHBridge bridge = phHueSDK.getSelectedBridge();
            while(true){
            	/**
                HttpURLConnection httpCon = (HttpURLConnection) hue_url.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setRequestMethod("PUT");
                OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
                out.write("{\"hue\":"+this.hue+", \"sat\":"+this.sat+", \"bri\":"+this.bri+", \"on\":"+this.on+"}");
                out.close();
                httpCon.getInputStream();
            	**/
            	if (changed){
                	PHLightState lightState = new PHLightState();
    	            // To validate your lightstate is valid (before sending to the bridge) you can use:  
    	            // String validState = lightState.validateState();

                	lightState.setOn(on);
    	            lightState.setHue(hue);
    	            lightState.setSaturation(sat-1<0?0:sat-1);
    	            lightState.setBrightness(bri);
    	            
    	            for (PHLight light : bridge.getResourceCache().getAllLights()) {
    	            	bridge.updateLightState(light, lightState);
    	            }
    	            changed=false;
            	}
            	sleep(10);
            }
        }catch (Exception e){System.out.println(e);System.exit(-1);}
    }
    public static void main(String[] args){
        HueWriter h1 = new HueWriter(1);
        HueWriter h2 = new HueWriter(2);
        HueWriter h3 = new HueWriter(3);
        h1.start();
        h2.start();
        h3.start();
        int t = 0;
        while (true){
            try{
                t=t+1;
                h1.setHSV((int)Math.floor(65535*0.5*(Math.sin(.1*t)+1)),(int)Math.floor(255*0.5*(Math.sin(.3*t)+1)),(int)Math.floor(255*0.5*(Math.sin(.5*t)+1)));
                h2.setHSV((int)Math.floor(65535*0.5*(Math.sin(.1*t)+1)),(int)Math.floor(255*0.5*(Math.sin(.3*t)+1)),(int)Math.floor(255*0.5*(Math.sin(.5*t)+1)));
                h3.setHSV((int)Math.floor(65535*0.5*(Math.sin(.1*t)+1)),(int)Math.floor(255*0.5*(Math.sin(.3*t)+1)),(int)Math.floor(255*0.5*(Math.sin(.5*t)+1)));
                Thread.sleep(1000);
            }catch(Exception e){System.out.println(e);System.exit(-1);}
        }
    }
}
