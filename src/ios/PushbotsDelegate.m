#import "PushbotsDelegate.h"
#import <Pushbots/Pushbots.h>

@implementation AppDelegate (notification)

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    // This method will be called everytime you open the app
    // Register the deviceToken on Pushbots
    [[Pushbots sharedInstance] registerOnPushbots:deviceToken];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    //Handle notification when the user click it while app is running in background or foreground.
    [[Pushbots sharedInstance] receivedPush:userInfo];
}

@end