//
//  Pushbtots.h
//  Pushbots Cordova plugin
//
#import "AppDelegate.h"
#import <Cordova/CDV.h>

@interface PushbotsPlugin : CDVPlugin

@property NSString *callbackId;

- (void) initialize:(CDVInvokedUrlCommand*)command;

- (void) updateAlias:(CDVInvokedUrlCommand *)command;
- (void) tag:(CDVInvokedUrlCommand*)command;
- (void) untag:(CDVInvokedUrlCommand*)command;
- (void) debug:(CDVInvokedUrlCommand*)command;
- (void) unregister:(CDVInvokedUrlCommand *)command;
- (void) getRegistrationId:(CDVInvokedUrlCommand *)command;

- (void) clearBadgeCount:(CDVInvokedUrlCommand*)command;
- (void) setBadge:(CDVInvokedUrlCommand*)command;

@end

@interface AppDelegate (PushbotsPlugin)
@end