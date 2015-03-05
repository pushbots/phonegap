//
//  Pushbots.h
//  Pushbots framework 1.0.6
//
//  Created by Abdullah Diaa on 01/03/15.
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

// @deprecated: Get Application ID and pushOptions from pushbots.xml <=1.0.5.5
+ (Pushbots *)getInstance;

/** @name PushBots Options */
- (void) RegisterDeviceToken:(NSData *)deviceToken;

-(NSString *) getDeviceID;
/*!
 Send device location to PushBots servers.
 @param location: Device location [CLLocation]
 */
-(void) sendLocation: (CLLocation *) location;
/*!
 Send device Lat/Lng to PushBots servers.
 @param lat: Device Latitude [NSString]
 @param lng: Device Longitude [NSString]
 */
-(void) sendLocationLat: (NSString *) lat withLng:(NSString *) lng;
/*!
 Send device Alias[username] to PushBots servers.
 @param alias: Device alias[username]
 */
-(void) sendAlias: (NSString *) alias;
/*!
 Tag/untag the device on PushBots servers.
 @param tag: Device tag
 */
-(void) tag:(NSString *)tag ;
-(void) untag:(NSString *)tag ;
-(void) unregister;

/*!
 Set device Badge count on servers.
 @param count: Custom badge count
 */
-(void) badgeCount:(NSString *)count ;
-(void) setBadgeCount:(NSString *)count ;
/*!
 decrease badge count.
 @param count: badge count to decrease
 */
-(void) decreaseBadgeCountBy:(NSString *)count ;
/*!
 Reset badge count to ZERO.
 */
-(void) resetBadgeCount;

/** @name PushBots Analytics */
/*!
 Record Notification opened on servers.
 */
-(void) OpenedNotification;
@end
