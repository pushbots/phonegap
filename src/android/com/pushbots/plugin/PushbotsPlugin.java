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
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Collection;
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
						Pushbots.sharedInstance().setCustomHandler(PushHandler.class);
						Log.v(TAG,"execute: options=" + options.toString());					
						Pushbots.sharedInstance().registerForRemoteNotifications();
						
						Log.v(TAG,"registerForRemoteNotifications:" + senderId );
						
						if(null != cordova.getActivity().getIntent().getExtras()){
							if(cordova.getActivity().getIntent().hasExtra(PBConstants.EVENT_MSG_OPEN)){
								//Send opened event to cordova
								try {
									JSONObject json = new JSONObject(PushbotsPlugin.getJson(cordova.getActivity().getIntent().getExtras().getBundle(PBConstants.EVENT_MSG_OPEN)));
									PushbotsPlugin.sendSuccessData("opened", json);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
						
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
	    }else if("toggleNotifications".equals(action)){
			final Boolean notifications_sub = args.getBoolean(0);
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Pushbots.sharedInstance().toggleNotifications(notifications_sub);
					noResult();
				}
			});
		}else if("getRegistrationId".equals(action)){
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String registrationID = Pushbots.sharedInstance().getGCMRegistrationId();
					if(registrationID != null){
						_callbackContext.success(registrationID);
					}else{
						_callbackContext.error("null registration Id");
					}
				}
			});
	    }else if("isNotificationEnabled".equals(action)){
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Boolean isNotificationEnabled = Pushbots.sharedInstance().isNotificationEnabled();
					_callbackContext.success();
				}
			});
	    }else if("done".equals(action)){
			return true;
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