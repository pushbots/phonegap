"use strict";

/**
 * Provide communication between the Cordova JavaScript and the native environment. 
 * 
 * @param {exec~successCallback} successFunction - Success function callback. Assuming your exec() call completes successfully, this function is invoked.
 * @param {exec~failCallback} failFunction - Error function callback. If the operation doesn't complete successfully, this function is invoked.
 * @param {string} service - The name of the service to call into on the native side. This is mapped to a native class.
 * @param {string} action - The name of the action to call into. This is picked up by the native class receiving the cordova.exec() call, and essentially maps to a class's method.
 * @param {args} Arguments to pass into the native environment.
 */
var exec = require("cordova/exec");

/**
 * The Cordova plugin title.
 */
var SERVICE_TITLE = "PushbotsPlugin";

var PushbotsPlugin = function() {};


/**
* Initialize Pushbots Plugin.
* Returns an instance of the PushbotsPlugin class
*
* @param {string} app_id - Pushbots Application ID
* @param {Object} options - platform-specific options
* @see {@link https://pushbots.com/../../#Options}
*/
PushbotsPlugin.prototype.initialize = function(app_id, options) {
	
	this._events = {};
	
	/* VALIDATE APP_ID */
	// Pushbots Application ID is required
	if (typeof app_id === 'undefined') {
		console.error('app_id argument is required.');
	}

	var checkForAppId = new RegExp("^[0-9a-fA-F]{24}$");
	if (!checkForAppId.test(app_id)) {
		console.error('app_id argument is not valid.');
	}

	this.app_id = app_id;
	
	/* VALIDATE PLATFORM-SPECIFIC OPTIONS*/
	// iOS:
	// Android:
	this.options = options;
	
	var that = this;
	var success = function(data){
		if (data && typeof data.type !== 'undefined') {
			//Registration event
			if(data.type === "registered"){
				that.fire("registered", data.data.deviceToken);
			// Received Notification
			}else if(data.type === "received"){
				if(data.source != undefined){
					data.data.cordova_source = data.source ;
				}
				that.fire("notification:received", data.data);
			// Opened Notification
			}else if(data.type === "opened"){
				that.fire("notification:clicked", data.data);
			// User info
			}else if(data.type === "user"){
				that.fire("user:ids", data.data);
			}
		}else{
			console.log(data);
		}
	};
	
	var fail = function(error){
		console.error(error);
	};
    //Intialize Pushbots
	exec(success, fail, SERVICE_TITLE, 'initialize', [this.app_id, this.options]);
};

/**
* Watch events and store its callback
*
* @param {string} eventName
* @param {callback} callback
*/
PushbotsPlugin.prototype.on = function (eventName, callback) {
	
	if (typeof callback !== 'function')
		return;
	
	if (! this._events.hasOwnProperty(eventName)) {
		this._events[eventName] = [];
	}
	this._events[eventName].push(callback);
	
};

/**
* Execute events
*
* @param {string} eventName
* @param {Object} data - data to handle on event execution
*/
PushbotsPlugin.prototype.fire = function (eventName, data) {
	if (this._events.hasOwnProperty(eventName)) {
		var cbs = this._events[eventName];
		for (var i = 0, len = cbs.length; i < len; i++) {
			if(data){
				cbs[i](data);
			}
		}
	}
};


/**** Pushbots Method ****/

/**
* Update Alias of the device on Pushbots
*
* @param {string} alias
*/
PushbotsPlugin.prototype.updateAlias = PushbotsPlugin.prototype.setAlias =  function(alias){
	exec(undefined, undefined, SERVICE_TITLE, 'updateAlias', [alias]);
};

/**
* Remove Alias of the device on Pushbots
*
* @param {string} alias
*/
PushbotsPlugin.prototype.removeAlias = function(alias){
	exec(undefined, undefined, SERVICE_TITLE, 'removeAlias', []);
};


/**
* Add tag to the device on Pushbots
*
* @param {string} tag
*/
PushbotsPlugin.prototype.tag = function(tag){
	exec(undefined, undefined, SERVICE_TITLE, 'tag', [tag]);
};


/**
* Add multiple tags to the device on Pushbots
*
* @param {string} tag
*/
PushbotsPlugin.prototype.setTags = function(tags){
	exec(undefined, undefined, SERVICE_TITLE, 'setTags', [tags]);
};


/**
* Update device info on PushBots
*
* @param {Object} update_obj

    tags: (array) set device tags.
    tags_add: (array) add tags.
    tags_remove: (array) remove tags.
    alias: (String) set device alias.
    debug: (Boolean) Set device debug status for sandbox.
    subscribed: (Boolean) subscribe/unsubscribe from Push notifications.
    location: (Array) [lat, lng] update device location.
*/
PushbotsPlugin.prototype.update = function(update_obj){
	exec(undefined, undefined, SERVICE_TITLE, 'update', [update_obj]);
};

/**
* Remove multiple tags from the device on Pushbots
*
* @param {string} tag
*/
PushbotsPlugin.prototype.removeTags = function(tags){
	exec(undefined, undefined, SERVICE_TITLE, 'removeTags', [tags]);
};

/**
* [iOS Only]
* Reset device badge locally and on Pushbots.
*
*/
PushbotsPlugin.prototype.resetBadge = function(){
	exec(undefined, undefined, SERVICE_TITLE, 'clearBadgeCount', []);
};


/**
* [iOS Only]
* Set device badge locally and on Pushbots.
*
*/
PushbotsPlugin.prototype.setBadge = function(badge){
	exec(undefined, undefined, SERVICE_TITLE, 'setBadge', [badge]);
};


/**
* [iOS Only]
* Decrement device badge locally and on Pushbots.
*
*/
PushbotsPlugin.prototype.decrementBadgeCountBy = function(count){
	exec(undefined, undefined, SERVICE_TITLE, 'decrementBadgeCountBy', [count]);
};


/**
* [iOS Only]
* Increment device badge locally and on Pushbots.
*
*/
PushbotsPlugin.prototype.incrementBadgeCountBy = function(count){
	exec(undefined, undefined, SERVICE_TITLE, 'incrementBadgeCountBy', [count]);
};



/**
* [iOS Only]
* Silent notifications Support
* CompletionHandler for Silent iOS notifications
*
*/
PushbotsPlugin.prototype.done = function(notification_id, success, fail){
	if (!success) { success = function() {}; }
	if (!fail) { fail = function() {}; }
	
	exec(undefined, undefined, SERVICE_TITLE, 'done', [notification_id]);
};

/**
* Remove tag from the device on Pushbots
*
* @param {string} tag
*/
PushbotsPlugin.prototype.untag = function(tag){
	exec(undefined, undefined, SERVICE_TITLE, 'untag', [tag]);
};

/**
* Set debug flag for the device for Sandbox
*
* @param {boolean} debug
*/
PushbotsPlugin.prototype.debug = function(debug){
	exec(undefined, undefined, SERVICE_TITLE, 'debug', [debug]);
};

/**
* Subscribe/Unsubscribe users from receiving notificaitons
*
* @param {boolean} notifications_sub
*/
PushbotsPlugin.prototype.toggleNotifications = function(notifications_sub){
	exec(undefined, undefined, SERVICE_TITLE, 'toggleNotifications', [notifications_sub]);
};

/**
* Retrieve Stored Device token
*
* @param {callback} success
*/
PushbotsPlugin.prototype.getRegistrationId = function(success, fail){
	exec(success, fail, SERVICE_TITLE, 'getRegistrationId', []);
};

/**
* Unregister the device from Pushbots servers.
*
* @param {callback} success
*/
PushbotsPlugin.prototype.unregister = function(){
	exec(undefined, undefined, SERVICE_TITLE, 'unregister', []);
};

if(!window.plugins)
    window.plugins = {};

/**
* Initialize Pushbots Plugin.
* Returns an instance of the PushbotsPlugin class
*
* @param {string} app_id - Pushbots Application ID
* @param {Object} options - platform-specific options
* @return {PushbotsPlugin} instance
* @see {@link https://pushbots.com/../../#Options}
*/
if (!window.plugins.PushbotsPlugin){
    window.plugins.PushbotsPlugin = new PushbotsPlugin();
}

module.exports = PushbotsPlugin;