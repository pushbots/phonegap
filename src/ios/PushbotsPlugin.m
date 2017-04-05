#import "PushbotsPlugin.h"
#import <Cordova/CDV.h>
#import <objc/runtime.h>
#import <objc/message.h>

static char launchNotificationKey;

@implementation PushbotsPlugin

@synthesize notificationPayload;
@synthesize silentHandler;
@synthesize callbackId;

- (void) initialize:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        
        //Pushbots Application Id
        NSString* appId = [command.arguments objectAtIndex:0];
        
        self.callbackId = command.callbackId;
        dispatch_async(dispatch_get_main_queue(), ^{
            //Ask for Push permission && create Pushbots sharedInstance
            self.PushbotsClient = [[Pushbots alloc] initWithAppId:appId prompt:YES];
        });
        
        CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [result setKeepCallbackAsBool:YES];
        [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
        
        if (self.notificationPayload){
            [self notificationOpened:self.notificationPayload];
            self.notificationPayload = nil;
        }
        
    }];
}

- (void)didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    if (self.callbackId != nil) {
        NSString *objectId = [[NSUserDefaults standardUserDefaults] valueForKey:@"com.pushbots.api.object_id"];
        // Send the event
        NSString *token = [[[[deviceToken description] stringByReplacingOccurrencesOfString:@"<"withString:@""]
                            stringByReplacingOccurrencesOfString:@">" withString:@""]
                           stringByReplacingOccurrencesOfString: @" " withString: @""];
        
        
        if(objectId == nil){
            // Register the token on Pushbots
            [self.PushbotsClient registerOnPushbots:deviceToken];
        }else{
            [self.PushbotsClient update:@{@"token" : token}];
        }
        
        
        // Send event of type "registered" with the token
        NSMutableDictionary* responseDict = [NSMutableDictionary dictionaryWithCapacity:2];
        [responseDict setObject:@"registered" forKey:@"type"];
        NSMutableDictionary* regDict = [NSMutableDictionary dictionaryWithCapacity:1];
        [regDict setObject:token forKey:@"token"];
        [responseDict setObject:regDict forKey:@"data"];
        [self sendSuccessCallback:responseDict];
        
        
        if(objectId != nil && token != nil){
            // Send event of type "user" with the token
            NSMutableDictionary* responseDict = [NSMutableDictionary dictionaryWithCapacity:2];
            [responseDict setObject:@"user" forKey:@"type"];
            NSMutableDictionary* regDict = [NSMutableDictionary dictionaryWithCapacity:1];
            [regDict setObject:objectId forKey:@"userId"];
            [regDict setObject:token forKey:@"token"];
            [responseDict setObject:regDict forKey:@"data"];
            [self sendSuccessCallback:responseDict];
        }else{
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(registeredWithPushbots:) name:NSUserDefaultsDidChangeNotification object:nil];
        }
        
    }
}

- (void)registeredWithPushbots:(NSNotification *)notification {
    
    // Get the user defaults
    NSUserDefaults *defaults = (NSUserDefaults *)[notification object];
    
    NSString *objectId = [defaults stringForKey:@"com.pushbots.api.object_id"];
    NSString *token = [defaults stringForKey:@"com.pushbots.api.deviceID"];
    
    if(objectId != nil && token != nil){
        NSMutableDictionary* responseDict = [NSMutableDictionary dictionaryWithCapacity:2];
        [responseDict setObject:@"user" forKey:@"type"];
        NSMutableDictionary* regDict = [NSMutableDictionary dictionaryWithCapacity:1];
        [regDict setObject:objectId forKey:@"userId"];
        [regDict setObject:token forKey:@"token"];
        [responseDict setObject:regDict forKey:@"data"];
        [self sendSuccessCallback:responseDict];
    }
}

- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo from:(NSString *)from{
    if (self.callbackId != nil) {
        if ( [UIApplication sharedApplication].applicationState != UIApplicationStateActive ) {
            self.notificationPayload = userInfo;
        }
        
        [Pushbots openURL:userInfo];
        
        // Send event of type "received" with the token
        NSMutableDictionary* responseDict = [NSMutableDictionary dictionaryWithCapacity:2];
        [responseDict setObject:from forKey:@"source"];
        [responseDict setObject:@"received" forKey:@"type"];
        [responseDict setObject:userInfo forKey:@"data"];
        self.notificationPayload = nil;
        [self sendSuccessCallback:responseDict];
    }
}

- (void)notificationOpened:(NSDictionary *)userInfo {
    
    dispatch_async(dispatch_get_main_queue(), ^{
        //Track opened notifications
        [self.PushbotsClient trackPushNotificationOpenedWithPayload:userInfo];
    });
    
    NSLog(@"Notification opened");
    // Send event of type "opened" with the token
    NSMutableDictionary* responseDict = [NSMutableDictionary dictionaryWithCapacity:2];
    [responseDict setObject:@"opened" forKey:@"type"];
    [responseDict setObject:userInfo forKey:@"data"];
    
    [self sendSuccessCallback:responseDict];
    
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
            [self.PushbotsClient setAlias:alias];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}


- (void) removeAlias:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient removeAlias];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void) update:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        NSDictionary* update_obj = [command.arguments objectAtIndex:0];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient update:update_obj];
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
            [self.PushbotsClient tag:@[tag]];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void) setTags:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        NSArray* tags = [command.arguments objectAtIndex:0];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient update:@{@"tags_add" : tags}];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void) removeTags:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        NSArray* tags = [command.arguments objectAtIndex:0];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient update:@{@"tags_remove" : tags}];
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
            [self.PushbotsClient untag:@[tag]];
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
            [self.PushbotsClient debug:debug];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void) toggleNotifications:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        BOOL notifications_sub = [[command.arguments objectAtIndex:0]  isEqual:[NSNumber numberWithInt:1]];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient toggleNotifications:notifications_sub];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void) unregister:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        //Unsubscribe the user
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient toggleNotifications:false];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void) getRegistrationId:(CDVInvokedUrlCommand *)command {
    CDVPluginResult* pluginResult = nil;
    
    //Retrieve token from NSUserDefaults "com.pushbots.api.deviceID"
    NSString *token = [[NSUserDefaults standardUserDefaults] objectForKey:@"com.pushbots.api.deviceID"];
    
    if (!token || [token isEqualToString:@""])
    return;
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:token];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


- (void) clearBadgeCount:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient clearBadgeCount];
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
            [self.PushbotsClient setBadge:badge];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}


- (void) incrementBadgeCountBy:(CDVInvokedUrlCommand*)command{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        NSString* count = [command.arguments objectAtIndex:0];
        int badge = [count intValue];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient incrementBadgeCountBy:badge];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void) decrementBadgeCountBy:(CDVInvokedUrlCommand*)command{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        NSString* count = [command.arguments objectAtIndex:0];
        int badge = [count intValue];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.PushbotsClient decrementBadgeCountBy:badge];
        });
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}



//Call this function to set the completion handler of silent notifications
-(void) done:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^ {
        //PushBots notificationId in payload, it has the key "pb_n_id"
        NSString* notification_id = [command.arguments objectAtIndex:0];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            //Setting Interval of 0.1 seconds to call the completionHandler with PushBots notification Id
            [NSTimer scheduledTimerWithTimeInterval:0.1
                                             target:self
                                           selector:@selector(silentCompletionHandler:)
                                           userInfo:notification_id
                                            repeats:NO];
        });
        
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

//Send CompletionHandler Signal
-(void)silentCompletionHandler:(NSTimer*)timer
{
    
    if (silentHandler) {
        UIApplication *application = [UIApplication sharedApplication];
        //Let's get the completionHandler from silentHandler Dict
        completionHandler = [silentHandler[[timer userInfo]] copy];
        if (completionHandler) {
            NSLog(@"Background time remaining: %f seconds (%d mins)", [application backgroundTimeRemaining], (int)([application backgroundTimeRemaining] / 60));
            completionHandler(UIBackgroundFetchResultNewData);
            completionHandler = nil;
        }
    }
}

@end

@implementation AppDelegate (PushbotsPlugin)

- (id) getCommandInstance:(NSString*)className{
    return [self.viewController getCommandInstance:className];
}

+ (void)load{
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

- (AppDelegate *)swizzled_init{
    
    
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

- (void)createNotificationObserverOnLaunching:(NSNotification *)notification {
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

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:( void (^)(UIBackgroundFetchResult))completionHandler;{
    if (application.applicationState == UIApplicationStateActive) {
        PushbotsPlugin *pushHandler = [self getCommandInstance:@"PushbotsPlugin"];
        [pushHandler didReceiveRemoteNotification:userInfo from:@"foreground"];
        completionHandler(UIBackgroundFetchResultNewData);
    }else{
        
        id not_aps = [userInfo objectForKey:@"aps"];
        id contentAvailable = [not_aps objectForKey:@"content-available"];
        
        //Check for contentAvailable to know if the notification should be handled in background
        if ([contentAvailable intValue] == 1) {
            NSLog(@"Silent notification detected!");
            void (^silentCompletionHandler)(UIBackgroundFetchResult) = ^(UIBackgroundFetchResult result){
                dispatch_async(dispatch_get_main_queue(), ^{
                    completionHandler(result);
                });
            };
            PushbotsPlugin *pushHandler = [self getCommandInstance:@"PushbotsPlugin"];
            
            if (pushHandler.silentHandler == nil) {
                pushHandler.silentHandler = [NSMutableDictionary dictionaryWithCapacity:1];
            }
            
            id notification_id = [userInfo objectForKey:@"pb_n_id"];
            
            if (notification_id != nil) {
                NSLog(@"silentCompletionHandler");
                [pushHandler.silentHandler setObject:silentCompletionHandler forKey:notification_id];
            }
            [pushHandler didReceiveRemoteNotification:userInfo from:@"hidden"];
        }else {
            //The application is brought from background to foreground
            PushbotsPlugin *pushHandler = [self getCommandInstance:@"PushbotsPlugin"];
            [pushHandler didReceiveRemoteNotification:userInfo from:@"background"];
            completionHandler(UIBackgroundFetchResultNewData);
        }
    }
}

- (void)applicationDidBecomeActive:(NSNotification *)notification {
    PushbotsPlugin *pushHandler = [self getCommandInstance:@"PushbotsPlugin"];
    
    if (self.launchNotification) {
        pushHandler.notificationPayload = [self.launchNotification copy];
        self.launchNotification = nil;
    }
}

// The accessors use an Associative Reference since you can't define a iVar in a category
// http://developer.apple.com/library/ios/#documentation/cocoa/conceptual/objectivec/Chapters/ocAssociativeReferences.html
- (NSMutableArray *)launchNotification
{
    return objc_getAssociatedObject(self, &launchNotificationKey);
}

- (void)setLaunchNotification:(NSDictionary *)aDictionary {
    objc_setAssociatedObject(self, &launchNotificationKey, aDictionary, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (void)dealloc {
    self.launchNotification = nil; // clear the association and release the object
    if([[NSUserDefaults standardUserDefaults] valueForKey:@"com.pushbots.api.object_id"] == nil){
        [[NSNotificationCenter defaultCenter] removeObserver:self name:NSUserDefaultsDidChangeNotification object:nil];
    }
}

@end