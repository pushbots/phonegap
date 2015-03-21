var PushbotsPlugin = {
	
	// Register the device with Pushbots and ask for push permission
    initialize: function(appId) {
		appId = typeof appId !== 'undefined' ? appId : null;
		cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'initializeWithAppId', [appId] );
    },
	
	
    initializeAndroid: function(appId, sender_id) {
		if(this.isAndroid){
			cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'initializeWithAppIdAndSenderId', [appId, sender_id] );
		}
    },
	
    initializeiOS: function(appId) {
		if(this.isiOS){
			cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'initializeWithAppId', [appId] );
		}
    },
	
	//Check for iOS platform
	isiOS : function () {
	    return ( navigator.userAgent.indexOf("iPhone") > 0 || navigator.userAgent.indexOf("iPad") > 0 || navigator.userAgent.indexOf("iPod") > 0); 
	},
	
	//Check for Android platform
	isAndroid : function () {
	    return ( navigator.userAgent.indexOf("Android") > 0 ); 
	},
	
	//Register device on Pushbots [iOS only]
    registerOnPushbots: function(token) {
		if(this.isiOS){
       	 	cordova.exec( this.success, this.fail, 'PushbotsPlugin', 'registerOnPushbots', [token] );
		}
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