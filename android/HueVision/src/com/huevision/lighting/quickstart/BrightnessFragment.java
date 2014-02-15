package com.huevision.lighting.quickstart;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLightState;
import com.philips.lighting.quickstart.R;

public class BrightnessFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
	
	private SeekBar valueBar;
	private PHHueSDK phHueSDK;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_brightness, container, false);
        
        valueBar=(SeekBar) rootView.findViewById(R.id.valuebar);
        valueBar.setOnSeekBarChangeListener(this);         
        
        phHueSDK = PHHueSDK.create();
        return rootView;
    }
	
	public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
                        // Log the progress
        Log.d("brightness", "Brightness is: "+progress);
                        
    }

    public void onStartTrackingTouch(SeekBar seekBar) {}

    public void onStopTrackingTouch(SeekBar seekBar) {
    	Log.d("brightness-after", "Brightness is: "+ seekBar.getProgress());
        int value = (int) (seekBar.getProgress() * 2.55);
        
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            // To validate your lightstate is valid (before sending to the bridge) you can use:  
            // String validState = lightState.validateState();
            
            lightState.setBrightness(value);
            bridge.updateLightState(light, lightState);
            //bridge.updateLightState(light, lightState, listener);   // If no bridge response is required then use this simpler form.
        }	
        
    }
}
