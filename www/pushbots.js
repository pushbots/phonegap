var PushbotsPlugin = {

	// Register the device with Pushbots and ask for push permission
    initialize: function(appId) {
		appId = typeof appId !== 'undefined' ? appId : null;
		cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'initializeWithAppId', [appId] );
    },


    initializeAndroid: function(appId, sender_id) {
		if(this.isAndroid()){
			cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'initializeWithAppIdAndSenderId', [appId, sender_id] );
		}
    },

    initializeiOS: function(appId) {
		if(this.isiOS()){
			cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'initializeWithAppId', [appId] );
		}
    },

	//Check for iOS platform
	isiOS : function () {
		return /iPhone|iPad|iPod/.test(navigator.userAgent);
	},

	//Check for Android platform
	isAndroid : function () {
	    return ( navigator.userAgent.indexOf("Android") > 0 );
	},

	//Register device on Pushbots [iOS only]
    registerOnPushbots: function(token) {
		if(this.isiOS()){
       	 	cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'registerOnPushbots', [token] );
		}
    },
	
	//Unregister device
    unregister: function() {
        cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'unregister', [] );
    },
	
	//Update device Alias
    setAlias: function(alias) {
        cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'setAlias', [alias] );
    },

	// Tag device
    tag: function(tag) {
        cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'tag', [tag] );
    },

	//untag device
    untag: function(untag) {
        cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'untag', [untag] );
    },

	// Set as debugging device
    debug: function(debug) {
        cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'debug', [debug] );
    },

	//Get device token
    getToken: function(success) {
        cordova.exec( success, this.fail, 'PushbotsPlugin', 'getToken', [] );
    },
	
	//Reset Badge [iOS only]
    resetBadge: function() {
        cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'resetBadge', [] );
    },

	//Set badge [iOS only]
    setBadge: function(count) {
        cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'setBadge', [count] );
    },


	//Handle received Notification
	receivedNotification: function(){
        // fire off deviceready
        var e = document.createEvent('Events');
        e.initEvent('received-notification');
        document.dispatchEvent(e);
	},
	//handle click notification
	onNotificationClick: function(handler){
		if(handler){
			this.msgClickHandler = handler;
		}
	},
	onMsgClick: function(msgPayload){
		if(this.msgClickHandler){
			this.msgClickHandler(msgPayload);
		}
	},
	//Success callback
	success: function(){
		console.log("Success");
	},

	//Failure callback
	fail: function(error){
		console.log("Error", error);
	}
};

module.exports = PushbotsPlugin;