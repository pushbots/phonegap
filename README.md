# phonegap/Cordova Plugin for Pushbots

> PushBots' official module for Phonegap/Cordova

https://www.pushbots.help/install-pushbots-in-your-app-or-website/cordova-phonegap-and-phonegap-build/integrating-pushbots-in-your-cordovaphonegap-app


## Installation

This requires phonegap/cordova CLI 7.1.0+

```bash
cordova plugin add pushbots-cordova-plugin --save
```

## Usage

1. Intialize Pushbots in deviceReady section:
```javascript
window.plugins.PushbotsPlugin.initialize("PUSHBOTS_APPLICATIONID", {"android":{"sender_id":"SENDER_ID"}});

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
 

## Phonegap build

add Pushbots plugin to config.xml:

```xml
<gap:plugin name="pushbots-cordova-plugin" spec="1.6.x" source="npm" />
<preference name="android-minSdkVersion" value="15" />
<preference name="phonegap-version" value="cli-7.1.0" />
<preference name="android-build-tool" value="gradle" />

 ```
