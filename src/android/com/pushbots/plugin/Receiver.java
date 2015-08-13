package com.pushbots.plugin;

import com.pushbots.push.Pushbots;
import com.pushbots.push.DefaultPushHandler;
import java.util.HashMap;
import android.os.Bundle;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class Receiver extends DefaultPushHandler {
	
	
	private static final Object NULL = new Object() {
		@Override public boolean equals(Object o) {
			return o == this || o == null; // API specifies this broken equals implementation
		}
		@Override public String toString() {
			return "null";
		}
	};
	 
	private static Object wrap(Object o) {
		if (o == null) {
			return NULL;
		}
		if (o instanceof JSONArray || o instanceof JSONObject) {
			return o;
		}
		if (o.equals(NULL)) {
			return o;
		}
		try {
			if (o instanceof Collection) {
				return new JSONArray((Collection) o);
			} else if (o.getClass().isArray()) {
				return new JSONArray(o);
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
	
	public void onMsgClick(Bundle Data) {
		try{
			JSONObject json = new JSONObject();
			Set<String> keys = Data.keySet();
			for (String key : keys) {
				try {
					json.put(key, wrap(Data.get(key)));
				} catch(JSONException e) {
					//Handle exception here
				}
			}
			PushbotsPlugin.sendMessage("onMsgClick",json);

		}catch (Exception e){
			
		}
	}

}
