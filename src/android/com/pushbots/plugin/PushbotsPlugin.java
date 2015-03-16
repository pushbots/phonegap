package com.pushbots.plugin;

import com.pushbots.push.Pushbots;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PushbotsPlugin extends CordovaPlugin {
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext cb) throws JSONException {
		
		if (action.equals("initializeWithAppId")) {
			this.initialize(cb, args);
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
		
		return true;
	}
	
	private void initialize(final CallbackContext cb, final JSONArray args) {
		Pushbots.sharedInstance().init(cordova.getActivity());
		cb.success();
	}
}
