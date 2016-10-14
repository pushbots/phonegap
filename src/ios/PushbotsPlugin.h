//
//  Pushbtots.h
//  Pushbots Cordova plugin
//
#import "AppDelegate.h"
#import <Cordova/CDV.h>
#import <Pushbots/Pushbots.h>

@interface PushbotsPlugin : CDVPlugin

@property NSString *callbackId;
@property (nonatomic, strong) NSDictionary *notificationPayload;
@property (strong, nonatomic) Pushbots *PushbotsClient;

- (void) initialize:(CDVInvokedUrlCommand*)command;

- (void) updateAlias:(CDVInvokedUrlCommand *)command;
- (void) tag:(CDVInvokedUrlCommand*)command;
- (void) untag:(CDVInvokedUrlCommand*)command;
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