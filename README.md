# phonegap
PushBots' official module for Phonegap
http://pushbots.com/developer/docs/phonegap

##Installation
```bash
cordova plugin add com.pushbots.push
```

##Usage
1. Intialize Pushbots in deviceReady section:
```javascript
  onDeviceReady: function() {
      app.receivedEvent('deviceready');
      PushbotsPlugin.initialize("PUSHBOTS_APPID");
  }
  
  ```


2. For Android only, Open res/values/pushbots.xml and update your Application ID/GCM sender ID:
```xml
  <?xml version="1.0" encoding="utf-8"?>
  <resources>
      <!-- Pushbots Application ID  -->
      <string name="pb_appid">548ef5901d0ab1</string>
      <!-- GCM Sender ID -->
      <string name="pb_senderid">48849973</string>
      <!-- Pushbots Log Level  log Tag "PB2" -->

      <string name="pb_logLevel">DEBUG</string>
  </resources>
        
  ```
2. Methods to use it:
```javascript

  //Set Alias
  PushbotsPlugin.setAlias("ALIAS");
  //Tag Device
PushbotsPlugin.tag("tag");
//unTag device
PushbotsPlugin.untag("tag1");


//iOS only
//Reset Badge
PushbotsPlugin.resetBadge();
//Set badge
PushbotsPlugin.setBadge(10);
 ```
