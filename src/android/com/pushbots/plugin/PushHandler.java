package com.pushbots.plugin;

import com.pushbots.push.PBNotificationIntent;
import com.pushbots.push.Pushbots;
import com.pushbots.push.DefaultPushHandler;
import com.pushbots.push.utils.Log;
import com.pushbots.push.utils.PBConstants;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Set;
import java.util.Iterator;

public class PushHandler extends DefaultPushHandler {

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
		Bundle pushDataBundle = intent.getBundleExtra("pushData");
		Log.e(action);
		if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
			
			Pushbots pushInstance = Pushbots.sharedInstance();
			if(!pushInstance.isInitialized()){
				Pushbots.sharedInstance().init(context.getApplicationContext());
			}

			if(PBNotificationIntent.notificationsArray != null) {
				PBNotificationIntent.notificationsArray = null;
			}

			String packageName = context.getPackageName();
			Intent resultIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_NO_HISTORY);
			
			// check for open URL
			if(null != pushDataBundle.getString("openURL") &&  ( pushDataBundle.getString("openURL").startsWith("http://") || pushDataBundle.getString("openURL").startsWith("https://") ) ){
				resultIntent = new Intent("android.intent.action.VIEW", Uri.parse(pushDataBundle.getString("openURL")));
				resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				Log.d("we are now opening url: " + pushDataBundle.getString("openURL"));
			}

			resultIntent.putExtra("pushData", intent.getBundleExtra("pushData"));
			Pushbots.sharedInstance().startActivity(resultIntent);

			HashMap<?, ?> PushdataOpened = (HashMap<?, ?>) bundle.get(PBConstants.EVENT_MSG_OPEN);
			JSONObject json = new JSONObject(PushdataOpened);
			PushbotsPlugin.sendSuccessData("opened", json);
			
		}else if (action.equals(PBConstants.EVENT_MSG_RECEIVE)) {

			HashMap<?, ?> PushdataReceived = (HashMap<?, ?>) bundle.get(PBConstants.EVENT_MSG_RECEIVE);
			JSONObject json = new JSONObject(PushdataReceived);
			PushbotsPlugin.sendSuccessData("received", json);
			
		}else if (action.equals(PBConstants.EVENT_REG)){
			try {
				JSONObject json = new JSONObject().put("deviceToken", bundle.get(PBConstants.EVENT_REG).toString());
				PushbotsPlugin.sendSuccessData("registered", json);
			} catch (JSONException e) {
				Log.e("execute: Got JSON Exception " + e.getMessage());
			}

		}
	}

}