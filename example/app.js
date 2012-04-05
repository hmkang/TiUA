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

	ua.addEventListener('tiuapush', function(msg){
		Ti.API.log('TiUA push received: ' + msg);
	    Ti.UI.createAlertDialog({
	      title:'TiUA push received',
	      message: "Message: "+msg
	    }).show();
	});
	ua.addEventListener('tiuaregister', function(apid){
		Ti.API.log('TiUA Registered: ' + valid);
	    Ti.UI.createAlertDialog({
	      title:'TiUA registered',
	      message: "APID: "+apid
	    }).show();
	});
}

