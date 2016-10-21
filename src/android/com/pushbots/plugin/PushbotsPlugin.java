package com.pushbots.plugin;

import com.pushbots.push.Pushbots;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.pushbots.push.utils.PBConstants;

import org.json.JSONException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PushbotsPlugin extends CordovaPlugin {

	private static final String TAG = "PB3";
	private static CallbackContext _callbackContext;
	private static CordovaWebView gWebView;
	public static Activity mActivity;

	@Override
	public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext)  throws JSONException{
		Log.v(TAG, "execute: action=" + action);
		gWebView = this.webView;
		
	    if ("initialize".equals(action)) {
			_callbackContext = callbackContext;		
	       cordova.getThreadPool().execute(new Runnable() {
	            public void run() {
					try{
						final String applicationId = args.getString(0);
						JSONObject options = null;
						options = args.getJSONObject(1).getJSONObject("android");
						final String senderId = options.getString("sender_id");
					
						Pushbots.sharedInstance().init(cordova.getActivity(), "DEBUG", applicationId, senderId);
						Log.v(TAG,"execute: options=" + options.toString());					
						Log.v(TAG,"registerForRemoteNotifications:" + senderId);
						Pushbots.sharedInstance().registerForRemoteNotifications();
						
						Pushbots.sharedInstance().registered(new Pushbots.registeredHandler() {
							@Override
							public void registered(String userId, String registrationId) {
								if (registrationId != null){
									try{
										JSONObject json = new JSONObject();
										json.put("token", registrationId);
										json.put("userId", userId);
										sendSuccessData("registered", json);
									}catch (NullPointerException e){
										Log.e(TAG, "Null");
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}
						});
						noResult();
					} catch (JSONException e) {
                        e.printStackTrace();
                    }
	            }
	        });
			
		}else if("updateAlias".equals(action) || "setAlias".equals(action)){
			final String alias = args.getString(0);
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Pushbots.sharedInstance().setAlias(alias);
					noResult();
				}
			});
	    }else if("removeAlias".equals(action)){
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Pushbots.sharedInstance().removeAlias();
					noResult();
				}
			});
	    }else if("tag".equals(action)){
			final String tag = args.getString(0);
			final JSONArray tags = new JSONArray();
			tags.put(tag);
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Pushbots.sharedInstance().tag(tags);
					noResult();
				}
			});
	    }else if("setTags".equals(action)){
			final JSONArray tags = args.getJSONArray(0);
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Pushbots.sharedInstance().tag(tags);
					noResult();
				}
			});
	    }else if("untag".equals(action)){
			final String tag = args.getString(0);
			final JSONArray tags = new JSONArray();
			tags.put(tag);
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Pushbots.sharedInstance().untag(tags);
					noResult();
				}
			});
	    }else if("removeTags".equals(action)){
			final JSONArray tags = args.getJSONArray(0);
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Pushbots.sharedInstance().untag(tags);
					noResult();
				}
			});
	    } else if("debug".equals(action)){
			final Boolean debug = args.getBoolean(0);
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Pushbots.sharedInstance().debug(debug);
					noResult();
				}
			});
	    }else if("getRegistrationId".equals(action)){
			final Boolean debug = args.getBoolean(0);
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String registrationID = Pushbots.sharedInstance().getGCMRegistrationId();
					if(registrationID != null){
						_callbackContext.success(registrationID);
					}else{
						_callbackContext.fail(registrationID);
					}
				}
			});
	    }else {
            Log.e(TAG, "Invalid action : " + action);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            return false;
        }
		
		return true;
	}

	public static void sendSuccessData(String type, Object data) {

		if (type != null && data != null) {
			JSONObject result = new JSONObject();
			try {
				result.put("type", type);
				result.put("data", data);
				PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
				pluginResult.setKeepCallback(true);
				if (_callbackContext != null) {
					Log.d(TAG, "Sending success result: " + pluginResult.getMessage());
					_callbackContext.sendPluginResult(pluginResult);
				}
			} catch (JSONException e) {
				Log.e(TAG, "could not serialize result for callback");
			}
		}
	}

	public static void fail(String message) {
		PluginResult result = new PluginResult(PluginResult.Status.ERROR, message);
		result.setKeepCallback(true);
		_callbackContext.sendPluginResult(result);
	}

	private static void noResult(){
		PluginResult result = new PluginResult(PluginResult.Status.OK, "");
		result.setKeepCallback(true);
		_callbackContext.sendPluginResult(result);
	}

}