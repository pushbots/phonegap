//
//  Pushbots.h
//  Pushbots framework 1.2.2
//
//  Created by Abdullah Diaa on 14/12/15.
//  Copyright (c) 2015 PushBots Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Pushbots;
@class CLLocation;

@interface Pushbots : NSObject

/*!
 @method
 
 @abstract
 Initializes a singleton instance of the API.
 
 @discussion
 
 @param appId        Pushbots Application ID.
 */
+ (Pushbots *)sharedInstanceWithAppId:(NSString *)appId;

/*!
 @method
 
 @abstract
 Returns the previously instantiated singleton instance of the API.
 
 @discussion
 The API must be initialized with <code>sharedInstanceWithAppId:</code> before
 calling this class method.
 */
+ (Pushbots *)sharedInstance;


/*!
 @method
 
 @abstract
 Registers device token on Pushbots servers.
 
 @discussion
 This method should be called in <code>application:didRegisterForRemoteNotificationsWithDeviceToken:</code> to register the device on pushbots servers.

 @param deviceToken           Device token from Apple servers.
 */
- (void) registerOnPushbots:(NSData *)deviceToken;


/*!
 @method
 
 @abstract
 Registers device token on Pushbots servers.
 
 @discussion
 This method should be called in <code>application:didReceiveRemoteNotification:</code> to handle the notification while the app is open.
 
 @param userInfo           A dictionary that contains information related to the remote notification, potentially including a badge number for the app icon, an alert sound, an alert message to display to the user, a notification identifier, and custom data.
 */
- (void) receivedPush:(NSDictionary *)userInfo;


/*!
 @method
 
 @abstract
 Returns Device token stored in "PushbotsTokenUDID/standardUserDefaults"
 
 @discussion
 This method will return device token stored in <code>[[NSUserDefaults standardUserDefaults] objectForKey:@"PushbotsTokenUDID"]</code>
 */
-(NSString *) getDeviceID;


/*!
 @method
 
 @abstract
 Toggles device debug mode.
 
 @discussion
 This method will toggle debug mode on the device, visit sandbox section in dashboard for more details.
 
 @param debug          Boolean value to toggle debug mode.
 */
- (void) debug:(BOOL)debug;


/*!
 @method
 
 @abstract
 Send device location [lat/lng] to Pushbots.
 
 @discussion
 This method will update device location on Pushbots servers.
 
 @param location          Device location
 */
-(void) setLocation: (CLLocation *) location;


/*!
 @method
 
 @abstract
 Send device location [lat/lng] to Pushbots.
 
 @discussion
 This method will update device location on Pushbots servers.
 
 @param lat          Location latitude
 @param lng          Location longitude
*/
-(void) setLocationLat: (NSString *) lat withLng:(NSString *) lng;


/*!
 @method
 
 @abstract
 Update device alias on Pushbots.
 
 @discussion
 This method will update device alias on Pushbots servers.
 
 @param alias          Device alias
 */
-(void) setAlias: (NSString *) alias;


/*!
 @method
 
 @abstract
 Tag the device on Pushbots.
 
 @discussion
 This method will update device tag on Pushbots servers.
 
 @param tag          Device tag
 */
-(void) tag:(NSString *)tag ;


/*!
 @method
 
 @abstract
 Remove tag from the device.
 
 @discussion
 This method will untag device from Pushbots servers.
 
 @param tag          Device tag
 */
-(void) untag:(NSString *)tag ;


/*!
 @method
 
 @abstract
 Remove the device from Pushbots.
 
 @discussion
 This method will remove stored device token from pushbots server.
 
 @param tag          Device tag
 */
-(void) unregister;


/*!
 @method

 @abstract
 Reset badge count on Pushbots server and applicationIconBadgeNumber.
  */
-(void) clearBadgeCount;

/*!
 @method
 
 @abstract
 Set badge count on Pushbots and applicationIconBadgeNumber.
 */
-(void) setBadge: (int) number;

/*!
 @method
 
 @abstract
 Decrement badge count on Pushbots and applicationIconBadgeNumber.
 */
-(void) decrementBadgeCountBy: (int) number;

/*!
 @method
 
 @abstract
 Record opened notification on Pushbots servers.
 */
-(void) OpenedNotification;

/*!
 @method
 
 @abstract
 Record opened notification on Pushbots servers.
 */
-(void) trackPushNotificationOpenedWithLaunchOptions:(NSDictionary *) launchOptions;
-(void) trackPushNotificationOpenedWithPayload:(NSDictionary *) payload;


/*! @deprecated Get Application ID and pushOptions from pushbots.xml <=1.0.5.5 */
+ (Pushbots *)getInstance;
/*! @deprecated <=1.0.6 */
-(void) badgeCount:(NSString *)count ;
/*! @deprecated <=1.0.6 */
-(void) setBadgeCount:(NSString *)count ;
/*! @deprecated <=1.0.6 */
-(void) decreaseBadgeCountBy:(NSString *)count ;
/*! @deprecated <=1.0.6 */
-(void) resetBadgeCount;
/*! @deprecated <=1.0.6 */
-(void) sendAlias: (NSString *) alias;
/*! @deprecated <=1.0.6 */
-(void) sendLocation: (CLLocation *) location;

@end
