# phonegap
PushBots' official module for Phonegap

https://pushbots.com/developer/docs/phonegap

##Installation
```bash
cordova plugin add com.pushbots.push
```

##Usage
1. Intialize Pushbots in deviceReady section:
```javascript
onDeviceReady: function() {
	app.receivedEvent('deviceready');

	if( PushbotsPlugin.isiOS() ){
		PushbotsPlugin.initializeiOS("PUSHBOTS_APP_ID");
	}
	
	if( PushbotsPlugin.isAndroid() ){
		PushbotsPlugin.initializeAndroid("PUSHBOTS_APP_ID", "GCM_SENDER_ID");
	}
}
```


2. Methods to use it:
```javascript
//iOS && Android
//Set Alias
PushbotsPlugin.setAlias("alias");
//Tag Device
PushbotsPlugin.tag("tag");
//unTag device
PushbotsPlugin.untag("tag1");
//Enable debug mode
PushbotsPlugin.debug(true);
//Unregister device from Pushbots
PushbotsPlugin.unregister();
//Get device token
PushbotsPlugin.getToken(function(token){
	console.log(token);
});

//iOS only
//Reset Badge
PushbotsPlugin.resetBadge();
//Set badge
PushbotsPlugin.setBadge(10);
 ```
 
 
 3. To handle Notifications payload on Click [Android only]
 
Add this method to **index.js**
```javascript
 function myMsgClickHandler(msg){
     console.log("Clicked On notification" + JSON.stringify(msg));
     alert(JSON.stringify(msg));
 }
 ```

Then set Notification click event:

```javascript
PushbotsPlugin.onNotificationClick(myMsgClickHandler);
 ```