package com.pushbots.plugin;

import com.pushbots.push.Pushbots;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.pushbots.push.utils.Log;
import com.pushbots.push.utils.PBConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PushbotsPlugin extends CordovaPlugin {

	private static CallbackContext _callbackContext;
	public static CordovaWebView gwebView;
	public static Activity mActivity;

	@Override
	public boolean execute(final String action,final JSONArray args, final CallbackContext cb) throws JSONException {

		gwebView = this.webView;
		mActivity = cordova.getActivity();

		if (action.equals("initialize")) {
			_initialize(args, cb);
			return true;
		}

		if (action.equals("updateAlias")) {
			_updateAlias(args, cb);
			return true;
		}

		if (action.equals("tag")) {
			_tag(args, cb);
			return true;
		}

		if (action.equals("untag")) {
			_untag(args, cb);
			return true;
		}

		if (action.equals("debug")) {
			_debug(args, cb);
			return true;
		}

		if (action.equals("unregister")) {
			_unregister(args, cb);
			return true;
		}

		if (action.equals("getRegistrationId")){
			String registrationID = Pushbots.sharedInstance().regID();
			cb.success(registrationID);
			return true;
		}

		return true;
	}

	private void _initialize(JSONArray args, CallbackContext cb) throws JSONException {

		final String applicationId = args.getString(0);
		JSONObject options = null;
		options = args.getJSONObject(1).getJSONObject("android");
		final String senderId = options.getString("sender_id");

		_callbackContext = cb;

		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Initialize Pushbots Plugin
				Pushbots.sharedInstance().init(cordova.getActivity(), applicationId, senderId, "DEBUG");
				Pushbots.sharedInstance().setCustomHandler(PushHandler.class);
				
				// Check for attached Push data on opening the App from a notification
                Bundle bundle = cordova.getActivity().getIntent().getBundleExtra("pushData");
                if(bundle != null){
                    try{
                        JSONObject json = new JSONObject();
                        Set<String> keys = bundle.keySet();
                        Iterator<String> it = keys.iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            json.put(key, bundle.get(key));
                        }
                        sendSuccessData("opened", json);
                    }catch (NullPointerException e){
                        Log.e("Null");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
				noResult();
			}
		});
	}

	private void _updateAlias(JSONArray args, CallbackContext cb) throws JSONException {

		final String alias = args.getString(0);

		_callbackContext = cb;

		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Pushbots.sharedInstance().setAlias(alias);
				noResult();
			}
		});
	}

	private void _tag(JSONArray args, CallbackContext cb) throws JSONException {

		final String tag = args.getString(0);

		_callbackContext = cb;

		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Pushbots.sharedInstance().tag(tag);
				noResult();
			}
		});
	}

	private void _untag(JSONArray args, CallbackContext cb) throws JSONException {

		final String tag = args.getString(0);

		_callbackContext = cb;

		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Pushbots.sharedInstance().untag(tag);
				noResult();
			}
		});
	}

	private void _debug(JSONArray args, CallbackContext cb) throws JSONException {

		final Boolean debug = args.getBoolean(0);

		_callbackContext = cb;

		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Pushbots.sharedInstance().debug(debug);
				noResult();
			}
		});
	}

	private void _unregister(JSONArray args, CallbackContext cb) throws JSONException {

		_callbackContext = cb;

		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Pushbots.sharedInstance().unRegister();
				noResult();
			}
		});
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
					Log.d("Sending success result: " + pluginResult.getMessage());
					_callbackContext.sendPluginResult(pluginResult);
				}
			} catch (JSONException e) {
				Log.e("could not serialize result for callback");
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