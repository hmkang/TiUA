// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var window = Ti.UI.createWindow({
	backgroundColor:'white'
});
var label = Ti.UI.createLabel({
	text: 'Titanium Urban Airship Module for Android'
});
window.add(label);
window.open();

// TODO: write your module tests here
if (Ti.Platform.name == "android") {
	var tiua = require('com.hmkang.TiUA');
	Ti.API.info("module is => " + tiua);

	tiua.registerForPushNotifications({
		developmentAppKey: '<Your developmentAppKey>',
		developmentAppSecret: '<Your developmentAppSecret>',
		transport: 'c2dm',
		inProduction: false,
		c2dmSender: '<Your email as c2dmSender>',
		pushServiceEnabled: true,
		
		success: function(e) {
			Ti.API.log('TiUA Register succeed: ' + e.apid);
		    Ti.UI.createAlertDialog({
		      title:'TiUA Registered',
		      message: e.apid
		    }).show();
		},
		error: function(e) {
			Ti.API.log("TiUA Register failed: ");
		},
		callback: function(e) {
			Ti.API.log('TiUA push received:');
			var message = "";
			for(var key in e){
				Ti.API.log("Pushed: "+key+", "+e[key]);
				message += key+":"+e[key]+"\n";
			}
		    Ti.UI.createAlertDialog({
		      title:'TiUA push received',
		      message: message
		    }).show();
		}
	});

}

