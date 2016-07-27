package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;


import java.security.PublicKey;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import android.util.Log;

public class NativeScan extends CordovaPlugin {
    private CallbackContext callbackContext;

    private BroadcastReceiver mReceiver = null;
    private Intent intentService = new Intent("com.hyipc.core.service.barcode.BarcodeService2D");
    public static final String ACTION_BARCODE_SERVICE_BROADCAST = "action_barcode_broadcast";
    public static final String KEY_BARCODE_STR = "key_barcode_string";
	private String strBarcode = "";


    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        
        mReceiver = new BarcodeReceiver(callbackContext);

        if (action.equals("scan")) {
            scan();
            return true;
        } else {
            return false;
        }
    }

    public void scan() {
        intentService.putExtra("KEY_ACTION", "UP");

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intentService.putExtra("KEY_ACTION", "DOWN");


        IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_BARCODE_SERVICE_BROADCAST);
        cordova.getActivity().startService(intentService);
		cordova.getActivity().registerReceiver(mReceiver, filter);
    }

    public class BarcodeReceiver extends BroadcastReceiver {

        private CallbackContext callbackContext;

        public BarcodeReceiver (CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
        }

		public void onReceive(Context ctx, Intent intent) {
            if (intent.getAction().equals(ACTION_BARCODE_SERVICE_BROADCAST)) {
                strBarcode = intent.getExtras().getString(KEY_BARCODE_STR);
                Log.i("scanare", strBarcode);
                callbackContext.success(strBarcode);
			}
		}
	}
}