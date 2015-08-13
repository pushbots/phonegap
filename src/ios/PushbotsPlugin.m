#import "PushbotsPlugin.h"

@implementation PushbotsPlugin

- (void)initializeWithAppId:(CDVInvokedUrlCommand*)command {
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
	
		NSString* appId = [command.arguments objectAtIndex:0];

		dispatch_async(dispatch_get_main_queue(), ^{
			[Pushbots sharedInstanceWithAppId:appId];
		});

		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void) setAlias:(CDVInvokedUrlCommand *)command
{
	NSLog(@"Executing setAlias(Alias)");
    
	CDVPluginResult* pluginResult = nil;
    
	NSString* alias = [command.arguments objectAtIndex:0];
	
	[[Pushbots sharedInstance] sendAlias:alias];

	pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) debug:(CDVInvokedUrlCommand *)command
{
	NSLog(@"Executing debug(debug)");
    
	CDVPluginResult* pluginResult = nil;
    
	BOOL debug = [[command.arguments objectAtIndex:0]  isEqual:[NSNumber numberWithInt:1]];
	
	[[Pushbots sharedInstance] debug:debug];

	pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) tag:(CDVInvokedUrlCommand *)command
{
	NSLog(@"Executing tag(Tag)");
    
	CDVPluginResult* pluginResult = nil;
    
	NSString* tag = [command.arguments objectAtIndex:0];
	
	[[Pushbots sharedInstance] tag:tag];

	pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) getToken:(CDVInvokedUrlCommand *)command
{
	NSLog(@"Executing getToken()");
    
	CDVPluginResult* pluginResult = nil;
	
	NSString* deviceId = [[Pushbots sharedInstance] getDeviceID];
	pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:deviceId];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) untag:(CDVInvokedUrlCommand *)command
{
	NSLog(@"Executing untag(Tag)");
    
	CDVPluginResult* pluginResult = nil;
    
	NSString* tag = [command.arguments objectAtIndex:0];
	
	[[Pushbots sharedInstance] untag:tag];

	pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) resetBadge:(CDVInvokedUrlCommand *)command
{
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
	
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] clearBadgeCount];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void) unregister:(CDVInvokedUrlCommand *)command
{
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
	
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] unregister];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}


- (void) setBadge:(CDVInvokedUrlCommand *)command
{
	NSLog(@"Executing setBadge(count)");
    
	CDVPluginResult* pluginResult = nil;
    
	NSString* count = [command.arguments objectAtIndex:0];
	int badge = [count intValue];
	
	[[Pushbots sharedInstance] setBadge:badge];

	pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end