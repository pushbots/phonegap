#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

//Use UserNotifications with iOS 10+
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 100000
#define IOS10PLUS 1
#import <UserNotifications/UserNotifications.h>
#endif

@class Pushbots;

/*!
 @class
 PushBots SDK v2.5.0
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
+ (NSDictionary*) currentlyShowingNotification;
+ (NSString*)applicationId;
+ (NSString*)deviceId;
+ (BOOL) prompt;
+ (BOOL) receivedCallback;

typedef void (^PushBotsReceivedNotification)(NSDictionary * result);
typedef void (^PushBotsOpenedNotification)(NSDictionary * result);
typedef void (^PushBotsRegistered)(NSString * userid);

+ (void) onRegistered:(PushBotsRegistered)rCallback;


/*!
 @method
 
 @abstract
 Initializes an instance of the API.
 
 @discussion
 Initializes an instance of the PushBots.

 @param appId        Pushbots Application ID.
 @param launchOptions   launchOptions
 */
+(id)initWithAppId:(NSString*)appId withLaunchOptions:(NSDictionary *)launchOptions;

/*!
 @method
 
 @abstract
 Initializes an instance of the API.
 
 @discussion
 Initializes an instance of the PushBots.
 
 @param appId        Pushbots Application ID.
 @param launchOptions   launchOptions
 @param prompt      Push notification prompt on first app open.
 
 */
+ (id)initWithAppId:(NSString*)appId withLaunchOptions:(NSDictionary *)launchOptions prompt:(BOOL)prompt;
+ (id)initWithAppId:(NSString*)appId withLaunchOptions:(NSDictionary *)launchOptions prompt:(BOOL)p receivedNotification:(PushBotsReceivedNotification)rCallback openedNotification:(PushBotsOpenedNotification)oCallback;

/*!
 @method
 
 @abstract
 Initializes an instance of the API.
 
 @discussion
 Initializes an instance of the PushBots.
 
 @param appId        Pushbots Application ID.
 @param launchOptions   launchOptions
 @param p      Push notification prompt on first app open.
 @param rCallback    block to access notification data on received.
 */
+ (id)initWithAppId:(NSString*)appId withLaunchOptions:(NSDictionary *)launchOptions prompt:(BOOL)p receivedNotification:(PushBotsReceivedNotification)rCallback;


+ (void)notificationReceived:(NSDictionary*)messageDict;
/*!
 @method
 
 @discussion
 Set PuhsBots SDK log level
 <pre>[Pushbots setLogLevel:PBLogLevelVerbose];</pre>
 
 @param pbloglevel   PBLogLevelNoLogging, PBLogLevelVerbose, PBLogLevelInfo, PBLogLevelWarn, PBLogLevelError
 
 */
+ (void)setLogLevel:(PBLogLevel)pbloglevel;
+ (void)setLogLevel:(PBLogLevel)pbloglevel isUILog:(BOOL)uiLog;
/*!
 @method
 
 @discussion
Show prompt to register with remote notifications.
 */
+ (void)registerForRemoteNotifications;


/*!
 @method
 
 @abstract
 Registers device with all its data on PushBots servers, and increment sessions count, updates last session date.
 
 @discussion
 This method should be called in <code>application:didRegisterForRemoteNotificationsWithDeviceToken:</code> to register the device on PushBots servers.
 
 @param deviceToken           Device token from Apple servers.
 */
+ (void) registerOnPushbots:(NSData *)deviceToken;

/*!
 @method
 
 @abstract
 Get device data from Pushbots.
 
 @discussion
 This method will request device data from PushBots servers, should be used to sync tags/alias.
 
 @param callback         callback block to get device data as NSDictionary
 */
+ (void) getDevice:(void (^)(NSDictionary *device, NSError *error))callback;
+ (void) checkInApp:(void (^)(NSArray *inappmessages, NSError *error))callback;
+(void) inAppNotificationOpenedWithId:(NSString *) inapp_id;

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
+ (void) update:(NSDictionary *)deviceObject;

/*!
 @method
 
 @discussion
This method will toggle debug mode on the device, visit sandbox section in dashboard for more details.
 
 @param debug         Toggle debug mode for sandboxing.
 */
+ (void) debug:(BOOL)debug;

/*!
 @method
 
 @discussion
 This method will update device alias on PushBots.
 
 @param alias         device alias.
 */
+ (void) setAlias:(NSString *)alias;

+ (void) setName:(NSString *)name;
+ (void) setFirstName:(NSString *)f_name;
+ (void) setLastName:(NSString *)l_name;
+ (void) setEmail:(NSString *)email;
+ (void) setGender:(NSString *)gender;
+ (void) setPhone:(NSString *)phone;



/*!
 @method
 
 @discussion
 This method will remove device alias from PushBots.
 
 */
+ (void) removeAlias;


/*!
 @method
 
 @discussion
 This method will add tags to the device info.
 
 @param tags         Array of device tags to be added to the device.
 */
+ (void) tag:(NSArray *)tags;

/*!
 @method
 
 @discussion
 This method will remove tags from device info.
 
 @param tags         Array of device tags to be removed from the device.
 */
+ (void) untag:(NSArray *)tags;

/*!
 @method
 
 @discussion
 This method will set badge count on Pushbots and in the app (applicationIconBadgeNumber).
 
 @param number         New badge count.
 */
+ (void) setBadge: (int) number;

/*!
 @method
 
 @discussion
 This method will increment badge count on PushBots and in the app (applicationIconBadgeNumber).
 
 @param number         badge count to add.
 */
+ (void) incrementBadgeCountBy: (int) number;

/*!
 @method
 
 @discussion
 This method will decrement badge count on PushBots and in the app (applicationIconBadgeNumber).
 
 @param number         badge count to substract.
 */
+ (void) decrementBadgeCountBy: (int) number;

/*!
 @method
 
 @discussion
 This method will Clear badge count on PushBots and in the app (applicationIconBadgeNumber).
 
 */
+ (void) clearBadgeCount;

+ (void)openURL:(NSDictionary *)userInfo;

/*!
 @method
 
 @discussion
 This method will toggle Push notification subscription status.
 @param subscribed        badge count to substract.

 */
+ (void) toggleNotifications:(BOOL)subscribed;
+ (BOOL)showTakeoverNotificationWithObject:(NSDictionary *) notification;
+ (void)showTakeoverNotificationWith:(NSDictionary *) notification;
+ (BOOL) validateInAppMessage:(NSDictionary *) message;

+ (void) trackPushNotificationOpenedWithLaunchOptions:(NSDictionary *) launchOptions;
+ (void) trackPushNotificationOpenedWithPayload:(NSDictionary *) payload;
+ (void) trackPushNotificationOpenedWithPoll:(NSDictionary *) payload andAnswerId:(NSString *)ansewerID sync:(BOOL) sync;

+(void) trackEvent:(NSString *)event;
+(void) trackEvent:(NSString *)event withValue:(NSString *)value;
+(void) shareLocation:(BOOL)isSharingEnabled;
+(void) shareLocationPrompt:(BOOL)isPrompt;
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunguarded-availability"
#ifdef IOS10PLUS
// iOS 10 only
// Notification Service Extension
+ (UNMutableNotificationContent*)didReceiveNotificationExtensionRequest:(UNNotificationRequest*)request withContent:(UNMutableNotificationContent*)replacementContent;
+ (UNMutableNotificationContent*)serviceExtensionTimeWillExpireRequest:(UNNotificationRequest*)request withContent:(UNMutableNotificationContent*)replacementContent;

#endif
#pragma clang diagnostic pop

@end
