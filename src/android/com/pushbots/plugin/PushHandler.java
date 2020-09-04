package com.pushbots.plugin;

import android.content.BroadcastReceiver;
import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Set;
import java.util.Iterator;
import com.pushbots.push.utils.PBPrefs;

public class PushHandler extends BroadcastReceiver {

	private Context context;
	private Intent intent;
	private static final String TAG = "PB3";
	
	@Override
	public void onReceive(Context context, Intent intent) {

		this.context = context;
		this.intent = intent;
		String action = intent.getAction();
		Log.d(TAG, "action=" + action);
		
		if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
			
			Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_OPEN);
		    String regId = PBPrefs.getToken(this.context);
		    String userId = PBPrefs.getObjectId(this.context);
            bundle.putString("token", regId);
            bundle.putString("userId", userId);
			
			//Send clicked event to cordova		
			try {
				JSONObject json = new JSONObject(PushbotsPlugin.getJson(bundle));
				PushbotsPlugin.sendSuccessData("opened", json);
			} catch (JSONException e) {
				Log.d(TAG, "ERROR=" + e);
				
				e.printStackTrace();
			}
			
			//Record opened notification
            //PushUtils.PushNotificationOpened(context, bundle);
			
			//Start Launch Activity
			String packageName = context.getPackageName();
			Intent resultIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT| Intent.FLAG_ACTIVITY_NEW_TASK);

			// Check for next activity
			String next_activity = bundle.getString("nextActivity");
			if(null != next_activity){
				try {
					Log.i(TAG, "Opening Custom Activity " + next_activity);
					resultIntent = new Intent(context, Class.forName(next_activity));
					resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				} catch (ClassNotFoundException e) {
					//ClassNotFound
					e.printStackTrace();
				}
			}
			
			// Check for open URL
			String open_url = bundle.getString("openURL");
			if( null != open_url &&  ( open_url.startsWith("http://") || open_url.startsWith("https://")) ){
				resultIntent = new Intent("android.intent.action.VIEW", Uri.parse(open_url));
				resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				Log.d(TAG, "Opening url: " + open_url);
			}

			Bundle push_bundle =intent.getBundleExtra("pushData");
			push_bundle.putBundle(PBConstants.EVENT_MSG_OPEN, bundle);
			resultIntent.putExtras(push_bundle);

            if(null != resultIntent) {
                context.startActivity(resultIntent);
            }
			
		}else if (action.equals(PBConstants.EVENT_MSG_RECEIVE)) {
			//Bundle containing all fields of the notification
			Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_RECEIVE);
			
			//Send received event to cordova		
			try {
				JSONObject json = new JSONObject(PushbotsPlugin.getJson(bundle));
				PushbotsPlugin.sendSuccessData("received", json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if(action.startsWith(PBConstants.EVENT_POLL_OPEN)){
            String ButtonId = intent.getStringExtra("button_id");
            Log.d("PB3",  "buttonid: "+  ButtonId + " Poll: " +intent.getBundleExtra(PBConstants.EVENT_POLL_OPEN).getString("poll"));
            //PushUtils.PollClicked(context, intent.getBundleExtra(PBConstants.EVENT_POLL_OPEN), ButtonId);
        }
	}
}