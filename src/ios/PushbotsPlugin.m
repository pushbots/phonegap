#import "PushbotsPlugin.h"
#import <Cordova/CDV.h>
#import <Pushbots/Pushbots.h>
#import <objc/runtime.h>
#import <objc/message.h>

static char launchNotificationKey;

@implementation PushbotsPlugin

@synthesize notificationPayload;
@synthesize callbackId;

- (void) initialize:(CDVInvokedUrlCommand*)command {
	[self.commandDelegate runInBackground:^{
        
		//Pushbots Application Id
		NSString* appId = [command.arguments objectAtIndex:0];
		
		self.callbackId = command.callbackId;
		dispatch_async(dispatch_get_main_queue(), ^{
			//Ask for Push permission && create Pushbots sharedInstance
			[Pushbots sharedInstanceWithAppId:appId];
		});
        
		CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[result setKeepCallbackAsBool:YES];
		[self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
        
        if (notificationPayload){
            [self notificationOpened];
        }
	}];
}

- (void)didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    if (self.callbackId != nil) {
		// Register the token on Pushbots
	    [[Pushbots sharedInstance] registerOnPushbots:deviceToken];
	
		// Send the event
	    NSString *token = [[[[deviceToken description] stringByReplacingOccurrencesOfString:@"<"withString:@""]
	                        stringByReplacingOccurrencesOfString:@">" withString:@""]
	                       stringByReplacingOccurrencesOfString: @" " withString: @""];
        
        // Send event of type "registered" with the token
        NSMutableDictionary* responseDict = [NSMutableDictionary dictionaryWithCapacity:2];
        [responseDict setObject:@"registered" forKey:@"type"];
	    NSMutableDictionary* regDict = [NSMutableDictionary dictionaryWithCapacity:1];
	    [regDict setObject:token forKey:@"deviceToken"];
        [responseDict setObject:regDict forKey:@"data"];
        [self sendSuccessCallback:responseDict];
    }
}

- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo{
    if (self.callbackId != nil) {
		if ( [UIApplication sharedApplication].applicationState == UIApplicationStateActive ) {
            // Send event of type "received" with the token
            NSMutableDictionary* responseDict = [NSMutableDictionary dictionaryWithCapacity:2];
            [responseDict setObject:@"received" forKey:@"type"];
            [responseDict setObject:userInfo forKey:@"data"];
            [self sendSuccessCallback:responseDict];
        }else{
            self.notificationPayload = userInfo;
            [self notificationOpened];
        }
    }
}

- (void)notificationOpened {
    NSLog(@"Notification opened");
    
    if (self.notificationPayload != nil)
    {
        // Send event of type "opened" with the token
        NSMutableDictionary* responseDict = [NSMutableDictionary dictionaryWithCapacity:2];
        [responseDict setObject:@"opened" forKey:@"type"];
        [responseDict setObject:self.notificationPayload forKey:@"data"];
        [self sendSuccessCallback:responseDict];
    }
}

- (void)sendSuccessCallback:(NSDictionary *)data {
    if (data != nil) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:data];
        [pluginResult setKeepCallbackAsBool:YES];
        if (self.callbackId != nil) {
		    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
        }
    }
}

- (void) updateAlias:(CDVInvokedUrlCommand *)command {
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
		NSString* alias = [command.arguments objectAtIndex:0];
	
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] sendAlias:alias];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void) tag:(CDVInvokedUrlCommand *)command {
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
		NSString* tag = [command.arguments objectAtIndex:0];
	
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] tag:tag];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void) untag:(CDVInvokedUrlCommand *)command {
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
		NSString* tag = [command.arguments objectAtIndex:0];
	
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] untag:tag];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void) debug:(CDVInvokedUrlCommand *)command {
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
		BOOL debug = [[command.arguments objectAtIndex:0]  isEqual:[NSNumber numberWithInt:1]];
	
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] debug:debug];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void) unregister:(CDVInvokedUrlCommand *)command {
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
	
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] unregister];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void) getRegistrationId:(CDVInvokedUrlCommand *)command {
	CDVPluginResult* pluginResult = nil;
	NSString* deviceId = [[Pushbots sharedInstance] getDeviceID];
	pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:deviceId];
	[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) clearBadgeCount:(CDVInvokedUrlCommand *)command {
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
	
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] clearBadgeCount];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

- (void) setBadge:(CDVInvokedUrlCommand *)command {
	[self.commandDelegate runInBackground:^{
		CDVPluginResult* pluginResult = nil;
		NSString* count = [command.arguments objectAtIndex:0];
		int badge = [count intValue];
		
		dispatch_async(dispatch_get_main_queue(), ^{
			[[Pushbots sharedInstance] setBadge:badge];
		});
		
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
	}];
}

@end

@implementation AppDelegate (PushbotsPlugin)

- (id) getCommandInstance:(NSString*)className{
    return [self.viewController getCommandInstance:className];
}

+ (void)load
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        Class class = [self class];
        
        SEL originalSelector = @selector(init);
        SEL swizzledSelector = @selector(swizzled_init);
        
        Method original = class_getInstanceMethod(class, originalSelector);
        Method swizzled = class_getInstanceMethod(class, swizzledSelector);
        
        BOOL didAddMethod =
        class_addMethod(class,
                        originalSelector,
                        method_getImplementation(swizzled),
                        method_getTypeEncoding(swizzled));
        
        if (didAddMethod) {
            class_replaceMethod(class,
                                swizzledSelector,
                                method_getImplementation(original),
                                method_getTypeEncoding(original));
        } else {
            method_exchangeImplementations(original, swizzled);
        }
    });
}


- (AppDelegate *)swizzled_init
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(createNotificationObserverOnLaunching:)
                                                 name:UIApplicationDidFinishLaunchingNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter]addObserver:self
                                            selector:@selector(applicationDidBecomeActive:)
                                                name:UIApplicationDidBecomeActiveNotification
                                              object:nil];
    return [self swizzled_init];
}

- (void)createNotificationObserverOnLaunching:(NSNotification *)notification
{
    if (notification)
    {
        NSDictionary *launchOptions = [notification userInfo];
        if (launchOptions) {
            self.launchNotification = [launchOptions objectForKey: @"UIApplicationLaunchOptionsRemoteNotificationKey"];
        }
    }
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    PushbotsPlugin *pushHandler = [self getCommandInstance:@"PushbotsPlugin"];
    [pushHandler didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
	PushbotsPlugin *pushHandler = [self getCommandInstance:@"PushbotsPlugin"];
    [pushHandler didReceiveRemoteNotification:userInfo];
}

- (void)applicationDidBecomeActive:(NSNotification *)notification {
    
    NSLog(@"active");
    
    PushbotsPlugin *pushHandler = [self getCommandInstance:@"PushbotsPlugin"];

    if (self.launchNotification) {
        pushHandler.notificationPayload = self.launchNotification;
        self.launchNotification = nil;
        [pushHandler performSelectorOnMainThread:@selector(notificationOpened) withObject:pushHandler waitUntilDone:NO];
    }
}

// The accessors use an Associative Reference since you can't define a iVar in a category
// http://developer.apple.com/library/ios/#documentation/cocoa/conceptual/objectivec/Chapters/ocAssociativeReferences.html
- (NSMutableArray *)launchNotification
{
    return objc_getAssociatedObject(self, &launchNotificationKey);
}

- (void)setLaunchNotification:(NSDictionary *)aDictionary
{
    objc_setAssociatedObject(self, &launchNotificationKey, aDictionary, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (void)dealloc
{
    self.launchNotification = nil; // clear the association and release the object
}

@end