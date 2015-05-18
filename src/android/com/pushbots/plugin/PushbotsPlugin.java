package com.pushbots.plugin;

import com.pushbots.push.Pushbots;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import com.pushbots.push.utils.Log;

public class PushbotsPlugin extends CordovaPlugin {

	public static CordovaWebView gwebView;
	public static Activity mActivity;
	public static JSONObject pendingMessage;
	public static String pendingFunctionName;

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext cb) throws JSONException {

		gwebView = this.webView;
		mActivity = cordova.getActivity();

		if (action.equals("initializeWithAppId")) {
			this.initialize(cb, args);
			return true;
		}

		if (action.equals("initializeWithAppIdAndSenderId")) {
			this.initializeSender(cb, args);
			return true;
		}

		if (action.equals("setAlias")) {
			String alias;
			try {
				alias = args.getString(0);

				if (alias.equals("")) {
					alias = null;
				}

				Pushbots.sharedInstance().setAlias(alias);
				cb.success("Alias Updated Successfully.");
			} catch (JSONException e) {
				cb.error("Error updating Alias.");
				e.printStackTrace();
				return false;
			}
		}

		if (action.equals("tag")) {
			String tag;
			try {
				tag = args.getString(0);
				if (tag.equals("")) {
					tag = null;
				}
				Pushbots.sharedInstance().tag(tag);
				cb.success("Tag Updated Successfully.");
			} catch (JSONException e) {
				cb.error("Error updating tag.");
				e.printStackTrace();
				return false;
			}
		}

		if (action.equals("untag")) {
			String tag;
			try {
				tag = args.getString(0);
				if (tag.equals("")) {
					tag = null;
				}
				Pushbots.sharedInstance().untag(tag);
				cb.success("Tag Updated Successfully.");
			} catch (JSONException e) {
				cb.error("Error updating tag.");
				e.printStackTrace();
				return false;
			}
		}

		if (action.equals("debug")) {
			Boolean debug;
			try {
				debug = args.getBoolean(0);
				Pushbots.sharedInstance().debug(debug);
				cb.success("Device set as debugging device.");
			} catch (JSONException e) {
				cb.error("Error updating debug tag.");
				e.printStackTrace();
				return false;
			}
		}

		//Unregister device from Pushbots
		if (action.equals("unregister")) {
			Pushbots.sharedInstance().unRegister();
			cb.success("Device unregistered Successfully.");
		}
		
		//Get Device registration ID
		if (action.equals("getToken"))
		{
			String registrationID = Pushbots.sharedInstance().regID();
			Log.d("Getting device Token: " +  registrationID);
			cb.success(registrationID);
			return true;
		}
		
		return true;
	}

	public static void sendJavascript(  String funName,  JSONObject _json )
	{
		
		String _d =  "javascript:PushbotsPlugin."+funName+"(" + _json.toString() + ")";
		if(gwebView !=null){
			gwebView.sendJavascript( _d );
		}else{
			pendingMessage = _json;
			pendingFunctionName= funName;
		}

	}
	public static void sendMessage( String funName, JSONObject _json)
	{
		pendingMessage = _json;
		pendingFunctionName = funName;
	}

	private void initialize(final CallbackContext cb, final JSONArray args) {
		gwebView = this.webView;
		if(pendingMessage != null){
			sendJavascript(pendingFunctionName, pendingMessage);
			pendingMessage = null;
		}
		Pushbots.sharedInstance().init(cordova.getActivity());
		Pushbots.sharedInstance().setCustomHandler(Receiver.class);
		cb.success();
	}
	
	private void initializeSender(final CallbackContext cb, final JSONArray args) {
		try {
			gwebView = this.webView;
			if(pendingMessage != null){
				sendJavascript(pendingFunctionName, pendingMessage);
				pendingMessage = null;
			}
			Pushbots.sharedInstance().init(cordova.getActivity(), args.getString(0), args.getString(1), "DEBUG");
			Pushbots.sharedInstance().setCustomHandler(Receiver.class);
			cb.success();
		} catch (JSONException e) {
			cb.error("Error initializing the app.");
			e.printStackTrace();
		}
	}
	
}
