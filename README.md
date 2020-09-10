# phonegap/Cordova Plugin for Pushbots

> PushBots' official module for Phonegap/Cordova

https://www.pushbots.help/install-pushbots-in-your-app-or-website/cordova-phonegap-and-phonegap-build/integrating-pushbots-in-your-cordovaphonegap-app


## Installation

```bash
cordova plugin add pushbots-cordova-plugin --save
```

## Usage

1. Intialize Pushbots in deviceReady section:

**Firebase credentials:** https://www.pushbots.help/en/articles/498201-the-google-part-firebase-credentials

```javascript
window.plugins.PushbotsPlugin.initialize("5f4bb916f2e7634ba83f1b93", {"android":{"sender_id":"SENDER_ID", "fcm_app_id":"FCM_APP_ID", "web_api_key":"WEB_API_KEY", "project_id":"PROJECT_ID"}});

// Only with First time registration
window.plugins.PushbotsPlugin.on("registered", function(token){
	console.log("Registration Id:" + token);
});

//Get user registrationId/token and userId on PushBots, with evey launch of the app even launching with notification
window.plugins.PushbotsPlugin.on("user:ids", function(data){
	console.log("user:ids" + JSON.stringify(data));
});
```


2. Methods to use it:
```javascript

window.plugins.PushbotsPlugin.setAlias("Test");
window.plugins.PushbotsPlugin.removeAlias();

//Add or remove Single tag
window.plugins.PushbotsPlugin.tag("tag1");
window.plugins.PushbotsPlugin.untag("tag1");

//Add or remove array of tags
window.plugins.PushbotsPlugin.setTags(["tag1"]);
window.plugins.PushbotsPlugin.removeTags(["tag1"]);

window.plugins.PushbotsPlugin.debug(true);

//Track event
window.plugins.PushbotsPlugin.trackEvent("added_to_cart");

//ShareLocation with prompting
window.plugins.PushbotsPlugin.shareLocationPrompt(true);
//Sharelocation without prompting
window.plugins.PushbotsPlugin.shareLocation(true);
//Set log level with alert
window.plugins.PushbotsPlugin.setLogLevelWithUI({"logLevel":3, "showAlert":true});
//set log level without alert
window.plugins.PushbotsPlugin.setLogLevel(3);


//unsubscribe user from receiving notifications
window.plugins.PushbotsPlugin.toggleNotifications(false);

//iOS only

//Reset Badge
window.plugins.PushbotsPlugin.resetBadge();
//Set badge
window.plugins.PushbotsPlugin.setBadge(10);
//Increment badge count
window.plugins.PushbotsPlugin.incrementBadgeCountBy(1);
//Decrement badge count
window.plugins.PushbotsPlugin.decrementBadgeCountBy(10);
 ```
 
 
 3. To handle Notification events:

```javascript
// Should be called once app receive the notification [foreground/background]
window.plugins.PushbotsPlugin.on("notification:received", function(data){
	console.log("received:" + JSON.stringify(data));
	
	//iOS: [foreground/background]
	console.log("notification received from:" + data.cordova_source);
	//Silent notifications Only [iOS only]
	//Send CompletionHandler signal with PushBots notification Id
	window.plugins.PushbotsPlugin.done(data.pb_n_id);
});

window.plugins.PushbotsPlugin.on("notification:clicked", function(data){
	// var userToken = data.token; 
       // var userId = data.userId;
  	console.log("clicked:" + JSON.stringify(data));
});

window.plugins.PushbotsPlugin.setName("name");
window.plugins.PushbotsPlugin.setFirstName("first name");
window.plugins.PushbotsPlugin.setLastName("last name");
window.plugins.PushbotsPlugin.setEmail("email");
window.plugins.PushbotsPlugin.setGender("M");
window.plugins.PushbotsPlugin.setPhone("+2100");

 ```
 

## Phonegap [DEPRECARTED]

https://blog.phonegap.com/update-for-customers-using-phonegap-and-phonegap-build-cc701c77502c