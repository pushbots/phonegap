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
@property (strong, nonatomic) Pushbots *PushbotsClient;
@property (nonatomic, strong) NSMutableDictionary *silentHandler;

- (void) initialize:(CDVInvokedUrlCommand*)command;

- (void) updateAlias:(CDVInvokedUrlCommand *)command;
- (void) removeAlias:(CDVInvokedUrlCommand *)command;

- (void) tag:(CDVInvokedUrlCommand*)command;
- (void) untag:(CDVInvokedUrlCommand*)command;
- (void) toggleNotifications:(CDVInvokedUrlCommand *)command;
- (void) debug:(CDVInvokedUrlCommand*)command;
- (void) unregister:(CDVInvokedUrlCommand *)command;
- (void) getRegistrationId:(CDVInvokedUrlCommand *)command;
- (void) notificationOpened;

- (void) clearBadgeCount:(CDVInvokedUrlCommand*)command;
- (void) setBadge:(CDVInvokedUrlCommand*)command;

@end

@interface AppDelegate (PushbotsPlugin)
@property (nonatomic, retain) NSDictionary *launchNotification;
@end