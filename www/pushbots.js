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
				that.fire("notification:received", data.data);
			// Opened Notification
			}else if(data.type === "opened"){
				that.fire("notification:clicked", data.data);
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
PushbotsPlugin.prototype.updateAlias = function(alias){
	exec(this.success, this.fail, SERVICE_TITLE, 'updateAlias', [alias]);
};

/**
* Add tag to the device on Pushbots
*
* @param {string} tag
*/
PushbotsPlugin.prototype.tag = function(tag){
	exec(this.success, this.fail, SERVICE_TITLE, 'tag', [tag]);
};

/**
* [iOS Only]
* Reset device badge locally and on Pushbots.
*
*/
PushbotsPlugin.prototype.resetBadge = function(){
	exec(this.success, this.fail, SERVICE_TITLE, 'clearBadgeCount', []);
};


/**
* [iOS Only]
* Set device badge locally and on Pushbots.
*
*/
PushbotsPlugin.prototype.setBadge = function(badge){
	exec(this.success, this.fail, SERVICE_TITLE, 'setBadge', [badge]);
};

/**
* Remove tag from the device on Pushbots
*
* @param {string} tag
*/
PushbotsPlugin.prototype.untag = function(tag){
	exec(this.success, this.fail, SERVICE_TITLE, 'untag', [tag]);
};

/**
* Set debug flag for the device for Sandbox
*
* @param {boolean} debug
*/
PushbotsPlugin.prototype.debug = function(debug){
	exec(this.success, this.fail, SERVICE_TITLE, 'debug', [debug]);
};

/**
* Retrieve Stored Device token
*
* @param {callback} success
*/
PushbotsPlugin.prototype.getRegistrationId = function(success){
	exec(success, this.fail, SERVICE_TITLE, 'getRegistrationId', []);
};

/**
* Unregister the device from Pushbots servers.
*
* @param {callback} success
*/
PushbotsPlugin.prototype.unregister = function(){
	exec(this.success, this.fail, SERVICE_TITLE, 'unregister', []);
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