//
//  Pushbtots.h
//  Pushbots Cordova plugin
//
#import <Cordova/CDV.h>
#import <Pushbots/Pushbots.h>


@interface PushbotsPlugin : CDVPlugin
- (void) initializeWithAppId:(CDVInvokedUrlCommand*)command;
- (void) setAlias:(CDVInvokedUrlCommand*)command;
- (void) tag:(CDVInvokedUrlCommand*)command;
- (void) debug:(CDVInvokedUrlCommand*)command;
- (void) untag:(CDVInvokedUrlCommand*)command;
- (void) resetBadge:(CDVInvokedUrlCommand*)command;
- (void) setBadge:(CDVInvokedUrlCommand*)command;

@end