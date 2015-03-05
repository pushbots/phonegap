var PushbotsPlugin = {
    initialize: function(appId) {
        cordova.exec(
            this.success,
            this.fail,
            'PushbotsPlugin',
            'initializeWithAppId',
            [appId]
        );
    },
    setAlias: function(alias) {
        cordova.exec(
            this.success,
            this.fail,
            'PushbotsPlugin',
            'setAlias',
            [alias]
        );
    },
    tag: function(tag) {
        cordova.exec(
            this.success,
            this.fail,
            'PushbotsPlugin',
            'tag',
            [tag]
        );
    },
    untag: function(untag) {
        cordova.exec(
            this.success,
            this.fail,
            'PushbotsPlugin',
            'untag',
            [untag]
        );
    },
    resetBadge: function() {
        cordova.exec(
            this.success,
            this.fail,
            'PushbotsPlugin',
            'resetBadge',
            []
        );
    },
    setBadge: function(count) {
        cordova.exec(
            this.success,
            this.fail,
            'PushbotsPlugin',
            'setBadge',
            [count]
        );
    },
	success: function(){
		console.log("Success");
	},
	fail: function(error){
		console.log("Error", error);
	}
};
module.exports = PushbotsPlugin;