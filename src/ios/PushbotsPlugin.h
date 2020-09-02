//
//  Pushbtots.h
//  Pushbots Cordova plugin
//
#import "AppDelegate.h"
#import <Cordova/CDV.h>
#import <Pushbots/Pushbots.h>

@interface PushbotsPlugin : CDVPlugin{
	 void (^completionHandler)(UIBackgroundFetchResult);
}

@property NSString *callbackId;
@property (nonatomic, strong) NSDictionary *notificationPayload;
@property (nonatomic, strong) NSMutableDictionary *silentHandler;

- (void) initialize:(CDVInvokedUrlCommand*)command;

- (void) updateAlias:(CDVInvokedUrlCommand *)command;
- (void) removeAlias:(CDVInvokedUrlCommand *)command;
- (void) update:(CDVInvokedUrlCommand*)command;
- (void) tag:(CDVInvokedUrlCommand*)command;
- (void) untag:(CDVInvokedUrlCommand*)command;
- (void) setTags:(CDVInvokedUrlCommand *)command;
- (void) removeTags:(CDVInvokedUrlCommand *)command;
//new features
//  Set log levels
- (void) setLogLevel:(CDVInvokedUrlCommand*)command;
- (void) setLogLevelWithUI:(CDVInvokedUrlCommand *)command;
// share Location
- (void) shareLocation:(CDVInvokedUrlCommand*)command;
- (void) shareLocationPrompt:(CDVInvokedUrlCommand *)command;
//Custom attributes [Personlized push notifications and user profile]
- (void) setPhone:(CDVInvokedUrlCommand *)command;
- (void) setGender:(CDVInvokedUrlCommand *)command;
- (void) setEmail:(CDVInvokedUrlCommand *)command;
- (void) setLastName:(CDVInvokedUrlCommand *)command;
- (void) setFirstName:(CDVInvokedUrlCommand *)command;
- (void) setName:(CDVInvokedUrlCommand *)command;

- (void) trackEvent:(CDVInvokedUrlCommand*)command;

- (void) toggleNotifications:(CDVInvokedUrlCommand *)command;
- (void) debug:(CDVInvokedUrlCommand*)command;
- (void) unregister:(CDVInvokedUrlCommand *)command;
- (void) getRegistrationId:(CDVInvokedUrlCommand *)command;

//iOS Badge
- (void) clearBadgeCount:(CDVInvokedUrlCommand*)command;
- (void) setBadge:(CDVInvokedUrlCommand*)command;
- (void) incrementBadgeCountBy:(CDVInvokedUrlCommand*)command;
- (void) decrementBadgeCountBy:(CDVInvokedUrlCommand*)command;

@end