package com.pushbots.plugin;

import android.content.Context;
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
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Collection;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import android.content.SharedPreferences;


public class PushbotsPlugin extends CordovaPlugin {

	private static final String TAG = "PB3";
	private static CallbackContext _callbackContext;
	private static CordovaWebView gWebView;
	public static Activity mActivity;
	private Pushbots.LOG_LEVEL currentLogCatlevel = Pushbots.LOG_LEVEL.NONE;
	private Pushbots.LOG_LEVEL currentLogUilevel = Pushbots.LOG_LEVEL.NONE;

	/**
		* Gets the application context.
		* @return the application context
		*/
	private Context getApplicationContext() {
		return this.cordova.getActivity().getApplicationContext();
	}

	@Override
	public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext)  throws JSONException{
		Log.v(TAG, "execute: action=" + action);
		gWebView = this.webView;
		
		if ("initialize".equals(action)) {
			try{
				_callbackContext = callbackContext;
				final String applicationId = args.getString(0);
				JSONObject options = null;
				options = args.getJSONObject(1).getJSONObject("android");
				final String senderId = options.getString("sender_id");
				final String fcmappId = options.getString("fcm_app_id");
				final String webAPIKey = options.getString("web_api_key");
				final String projectId = options.getString("project_id");

				new Pushbots.Builder(getApplicationContext())
								.setFcmAppId(fcmappId)
								.setLogLevel(Pushbots.LOG_LEVEL.DEBUG)
								.setWebApiKey(webAPIKey)
								.setPushbotsAppId(applicationId)
								.setProjectId(projectId)
								.setSenderId(senderId)
								.build();
								
				// Pushbots.sharedInstance().setCustomHandler(PushHandler.class);
				Log.v(TAG,"execute: options=" + options.toString());					
						
				Log.v(TAG,"registerForRemoteNotifications:" + senderId );
						
		
				
		        Pushbots.sharedInstance().idsCallback(new Pushbots.idHandler() {
		            @Override
		            public void userIDs(String userId, String registrationId) {
						if (registrationId != null && userId != null){
							try{
								JSONObject json = new JSONObject();
								json.put("token", registrationId);
								json.put("userId", userId);
								sendSuccessData("user", json);
							}catch (NullPointerException e){
								Log.e(TAG, "Null");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
		            }
		        });
				
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
				Pushbots.sharedInstance().openedCallback(new Pushbots.notificationOpenedHandler() {
					@Override
					public void opened(Bundle notification) {
						try {
							sendSuccessData("opened", PushbotsPlugin.getJson(notification));
						}catch(Exception e) {

						}
					}
				});

				Pushbots.sharedInstance().receivedCallback(new Pushbots.notificationReceivedHandler() {
					@Override
					public void received(Bundle notification) {
						try {
							sendSuccessData("received", PushbotsPlugin.getJson(notification));
						}catch(Exception e) {

						}
					}
				});

				Pushbots.sharedInstance().notificationReceivedSilently(new Pushbots.NotificationReceivedSilently() {
					@Override
					public void received(Bundle notification) {
						try {
							sendSuccessData("receivedSilent", PushbotsPlugin.getJson(notification));
						}catch(Exception e) {

						}
					}
				});
				noResult();
			} catch (JSONException e) {
				e.printStackTrace();
			}
	         
		}else if("updateAlias".equals(action) || "setAlias".equals(action)){
			final String alias = args.getString(0);
			Pushbots.sharedInstance().setAlias(alias);
			noResult();
		}else if("setName".equals(action)){
			final String name = args.getString(0);
			Pushbots.sharedInstance().setName(name);
			noResult();
		}else if("setFirstName".equals(action)){
			final String f_name = args.getString(0);
			Pushbots.sharedInstance().setFirstName(f_name);
			noResult();
		}else if("setLastName".equals(action)){
			final String l_name = args.getString(0);
			Pushbots.sharedInstance().setLastName(l_name);
			noResult();
		}else if("setEmail".equals(action)){
			final String email = args.getString(0);
			Pushbots.sharedInstance().setEmail(email);
			noResult();
		}else if("setGender".equals(action)){
			final String gender = args.getString(0);
			Pushbots.sharedInstance().setGender(gender);
			noResult();
		}else if("setPhone".equals(action)){
			final String phone = args.getString(0);
			Pushbots.sharedInstance().setEmail(phone);
			noResult();
		}else if("removeAlias".equals(action)){
			Pushbots.sharedInstance().removeAlias();
			noResult();
		}else if("trackEvent".equals(action)){
			final String event_key = args.getString(0);
			Pushbots.sharedInstance().trackEvent(event_key);
			noResult();	
		}else if("tag".equals(action)){
			final String tag = args.getString(0);
			final JSONArray tags = new JSONArray();
			tags.put(tag);
			Pushbots.sharedInstance().tag(tags);
			noResult();
		}else if("update".equals(action)){
			final JSONObject json = args.getJSONObject(0);			
			Pushbots.update(json);
			noResult();
		}else if("setTags".equals(action)){
			final JSONArray tags = args.getJSONArray(0);
			Pushbots.sharedInstance().tag(tags);
			noResult();
		}else if("untag".equals(action)){
			final String tag = args.getString(0);
			final JSONArray tags = new JSONArray();
			tags.put(tag);
			Pushbots.sharedInstance().untag(tags);
			noResult();
		}else if("removeTags".equals(action)){
			final JSONArray tags = args.getJSONArray(0);
			Pushbots.sharedInstance().untag(tags);
			noResult();
		} else if("debug".equals(action)){
			final Boolean debug = args.getBoolean(0);
			Pushbots.sharedInstance().debug(debug);
			noResult();
		}else if("toggleNotifications".equals(action)){
			final Boolean notifications_sub = args.getBoolean(0);
			Pushbots.sharedInstance().toggleNotifications(notifications_sub);
			noResult();
		}else if("getRegistrationId".equals(action)){
			String registrationID = Pushbots.sharedInstance().getGCMRegistrationId();
			if(registrationID != null){
				sendStringResult(callbackContext, registrationID);
			}else{
				fail(callbackContext, "null registration Id");
			}
		}else if("isNotificationEnabled".equals(action)){
			Boolean isNotificationEnabled = Pushbots.sharedInstance().isNotificationEnabled();
			sendBooleanResult(isNotificationEnabled);
		}else if("done".equals(action)){
			return true;
		} else if ("isInitialized".equals(action)) {
			callbackContext.success(String.valueOf(Pushbots.isInitialized()));
		} else if ("setLogLevel".equals(action)) {
			Pushbots.setLogLevel(getLogLevel(args.getString(0)), getLogLevel(args.getString(1)));
			noResult();
		} else if ("shareLocation".equals(action)) {
			Pushbots.shareLocation(args.getBoolean(0));
			noResult();
		} else if("isSharingLocation".equals(action)) {
			callbackContext.success(String.valueOf(Pushbots.isSharingLocation()));
		} else if ("isRegistered".equals(action)) {
			callbackContext.success(String.valueOf(Pushbots.isRegistered()));
		} else {
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
	
	public static void fail(CallbackContext callbackContext, String message) {
		PluginResult result = new PluginResult(PluginResult.Status.ERROR, message);
		result.setKeepCallback(true);
		callbackContext.sendPluginResult(result);
	}

	public static void sendBooleanResult(boolean value) {
		PluginResult result = new PluginResult(PluginResult.Status.OK, value);
		result.setKeepCallback(true);
		_callbackContext.sendPluginResult(result);
	}

	public static void sendStringResult(CallbackContext callbackContext, String value) {
		PluginResult result = new PluginResult(PluginResult.Status.OK, value);
		result.setKeepCallback(true);
		callbackContext.sendPluginResult(result);
	}
	private static void noResult(){
		PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
		result.setKeepCallback(true);
		_callbackContext.sendPluginResult(result);
	}

	
	public static String getJson(final Bundle bundle) {
		if (bundle == null) return null;
		JSONObject jsonObject = new JSONObject();

		for (String key : bundle.keySet()) {
			Object obj = bundle.get(key);
			try {
				jsonObject.put(key, wrap(bundle.get(key)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObject.toString();
	}

	private static Pushbots.LOG_LEVEL getLogLevel(String logLevel) {
		if (logLevel.equals("DEBUG")){
			return Pushbots.LOG_LEVEL.DEBUG;
		}else if (logLevel.equals("VERBOSE")){
			return Pushbots.LOG_LEVEL.VERBOSE;
		}else if (logLevel.equals("INFO")) {
			return Pushbots.LOG_LEVEL.INFO;
		}else if (logLevel.equals("WARNING")) {
			return Pushbots.LOG_LEVEL.WARNING;
		}else if (logLevel.equals("ERROR")) {
			return Pushbots.LOG_LEVEL.ERROR;
		}else if (logLevel.equals("WTF")) {
			return Pushbots.LOG_LEVEL.WTF;
		}else {
			return Pushbots.LOG_LEVEL.NONE;
		}
	}

	private static Object wrap(Object o) {
		if (o == null) {
			return JSONObject.NULL;
		}
		if (o instanceof JSONArray || o instanceof JSONObject) {
			return o;
		}
		if (o.equals(JSONObject.NULL)) {
			return o;
		}
		try {
			if (o instanceof Collection) {
				return new JSONArray((Collection) o);
			} else if (o.getClass().isArray()) {
				return toJSONArray(o);
			}
			if (o instanceof Map) {
				return new JSONObject((Map) o);
			}
	        if (o instanceof Boolean ||
	                o instanceof Byte ||
	                o instanceof Character ||
	                o instanceof Double ||
	                o instanceof Float ||
	                o instanceof Integer ||
	                o instanceof Long ||
	                o instanceof Short ||
	                o instanceof String) {
	            return o;
	        }
			if (o.getClass().getPackage().getName().startsWith("java.")) {
				return o.toString();
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	private static JSONArray toJSONArray(Object array) throws JSONException {
		JSONArray result = new JSONArray();
		if (!array.getClass().isArray()) {
			throw new JSONException("Not a primitive array: " + array.getClass());
		}
		final int length = Array.getLength(array);
		for (int i = 0; i < length; ++i) {
			result.put(wrap(Array.get(array, i)));
		}
		return result;
	}
}