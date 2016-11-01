#import <Foundation/Foundation.h>

@class Pushbots;

/*!
 @class
 PushBots SDK v2.0.4
 @abstract
 The primary interface for integrating PushBots with your app.
 
 <pre>
//in AppDelegate.h:
@property (strong, nonatomic) Pushbots *PushbotsClient;
//in AppDelegate.m
 NSString *appID =  @"56c3515f357e71aa0a0";
 self.PushbotsClient = [Pushbots sharedInstanceWithAppId:appID andLoggingEnabled:YES];
 </pre>
 
 For more advanced usage, please see the <a
 href="https://pushbots.com/developer/docs/ios-sdk-integration">PushBots iOS SDK integration</a>.
 */

@interface Pushbots : NSObject

/**
 *  Pushbots log levels.
 */
typedef NS_ENUM(NSUInteger, PBLogLevel) {
    PBLogLevelNoLogging, PBLogLevelError, PBLogLevelWarn, PBLogLevelInfo, PBLogLevelVerbose
};

@property (nonatomic, strong)NSString *deviceId;
@property (nonatomic, strong)NSString *objectId;
@property (nonatomic, strong) NSString *applicationId;
@property (nonatomic, assign) BOOL prompt;

/*!
 @method
 
 @abstract
 Initializes an instance of the API.
 
 @discussion
 
 @param appId        Pushbots Application ID.
 @param prompt

 */
- (id)initWithAppId:(NSString*)appId;
- (id)initWithAppId:(NSString*)appId prompt:(BOOL)prompt;

/*!
 @method
 
 @discussion
 Shared instance of PushBots automatically created by the library.
 */
+ (Pushbots*)sharedInstance;

/*!
 @method
 
 @discussion
 Set PuhsBots SDK log level
 <pre>[Pushbots setLogLevel:PBLogLevelVerbose];</pre>
 
 @param pbloglevel   PBLogLevelNoLogging, PBLogLevelVerbose, PBLogLevelInfo, PBLogLevelWarn, PBLogLevelError
 
 */
+ (void)setLogLevel:(PBLogLevel)pbloglevel;

/*!
 @method
 
 @discussion
Show prompt to register with remote notifications.
 */
- (void)registerForRemoteNotifications;


/*!
 @method
 
 @abstract
 Registers device with all its data on PushBots servers, and increment sessions count, updates last session date.
 
 @discussion
 This method should be called in <code>application:didRegisterForRemoteNotificationsWithDeviceToken:</code> to register the device on PushBots servers.
 
 @param deviceToken           Device token from Apple servers.
 */
- (void) registerOnPushbots:(NSData *)deviceToken;

/*!
 @method
 
 @abstract
 Get device data from Pushbots.
 
 @discussion
 This method will request device data from PushBots servers, should be used to sync tags/alias.
 
 @param callback         callback block to get device data as NSDictionary
 */
- (void) getDevice:(void (^)(NSDictionary *device, NSError *error))callback;

/*!
 @method
 
 @abstract
Update device data on Pushbots.
 
 @discussion
 This method will update device data on PushBots.
 Device data keys:
 tags: (array) set device tags.
 tags_add: (array) add tags.
 tags_remove: (array) remove tags.
 alias: (String) set device alias.
 badge: (Integer) set device badge.
 badge_p: (Integer) Increment device badge.
 badge_d: (Integet) decrement device badge.
 debug: (Boolean) Set device debug status for sandbox.
 subscribed: (Boolean) subscribe/unsubscribe from Push notifications.
 location: (array)[lat, lng] update device location.
 
 @param deviceObject    NSDictionary with device data to be updated.
 */
- (void) update:(NSDictionary *)deviceObject;

/*!
 @method
 
 @discussion
This method will toggle debug mode on the device, visit sandbox section in dashboard for more details.
 
 @param debug         Toggle debug mode for sandboxing.
 */
- (void) debug:(BOOL)debug;

/*!
 @method
 
 @discussion
 This method will update device alias on PushBots.
 
 @param alias         device alias.
 */
- (void) setAlias:(NSString *)alias;


/*!
 @method
 
 @discussion
 This method will remove device alias from PushBots.
 
 */
- (void) removeAlias;


/*!
 @method
 
 @discussion
 This method will add tags to the device info.
 
 @param tags         Array of device tags to be added to the device.
 */
- (void) tag:(NSArray *)tags;

/*!
 @method
 
 @discussion
 This method will remove tags from device info.
 
 @param tags         Array of device tags to be removed from the device.
 */
- (void) untag:(NSArray *)tags;

/*!
 @method
 
 @discussion
 This method will set badge count on Pushbots and in the app (applicationIconBadgeNumber).
 
 @param number         New badge count.
 */
- (void) setBadge: (int) number;

/*!
 @method
 
 @discussion
 This method will increment badge count on PushBots and in the app (applicationIconBadgeNumber).
 
 @param number         badge count to add.
 */
- (void) incrementBadgeCountBy: (int) number;

/*!
 @method
 
 @discussion
 This method will decrement badge count on PushBots and in the app (applicationIconBadgeNumber).
 
 @param number         badge count to substract.
 */
- (void) decrementBadgeCountBy: (int) number;

/*!
 @method
 
 @discussion
 This method will Clear badge count on PushBots and in the app (applicationIconBadgeNumber).
 
 */
- (void) clearBadgeCount;

+ (void)openURL:(NSDictionary *)userInfo;

/*!
 @method
 
 @discussion
 This method will toggle Push notification subscription status.
 @param subscribed        badge count to substract.

 */
- (void) toggleNotifications:(BOOL)subscribed;

/*!
 @method
 
 @discussion
 This method will toggle Push notification subscription status.
 @param subscribed        badge count to substract.
 
 */
- (void) trackPushNotificationOpenedWithLaunchOptions:(NSDictionary *) launchOptions;
- (void) trackPushNotificationOpenedWithPayload:(NSDictionary *) payload;

@end
