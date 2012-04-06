/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package com.hmkang.TiUA;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.KrollDict;

import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiProperties;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import com.hmkang.TiUA.IntentReceiver;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.CustomPushNotificationBuilder;
import com.urbanairship.push.PushManager;
import com.urbanairship.push.PushPreferences;

import android.os.Bundle;

@Kroll.module(name="Tiua", id="com.hmkang.TiUA")
public class TiuaModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "TiuaModule";
	private static final boolean DBG = TiConfig.LOGD;
    private static TiuaModule _THIS;
    
    private KrollFunction successCallback;
    private KrollFunction errorCallback;
    private KrollFunction messageCallback;

    private AirshipConfigOptions airshipConfig;
    
    private String mActivityName;
    private String mPackageName;
    private KrollDict mMessage;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;
	
	public TiuaModule(TiContext tiContext)
	{
		super(tiContext);
        _THIS = this;
        
        airshipConfig = null;
        mActivityName = null;
        mPackageName = null;
        
        successCallback = null;
        errorCallback = null;
        messageCallback = null;

        mMessage = new KrollDict();
	}

    static TiuaModule getInstance() {
        return _THIS;
    }

    public void onResume(Activity activity) {
    	Log.i(LCAT, "onResume: ");
    	
    	// Clear all of notification messages.
    	String ns = getTiContext().getTiApp().NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getTiContext().getTiApp().getSystemService(ns);
		mNotificationManager.cancelAll();
    }
	
	public void registerCallback(boolean valid, String apid) {
		KrollDict dict = new KrollDict();		
		dict.put("apid", apid);
		dict.put("success", valid);

    	Activity myAct = getTiContext().getTiApp().getCurrentActivity();
		if (valid) {
			if(myAct!=null) {
				successCallback.callAsync(getKrollObject(), dict);
			}
		} else {
			if(myAct!=null) {
				errorCallback.callAsync(getKrollObject(), dict);
			}
		}
	}
	
    private void pushMessage() {
    	Log.i(LCAT, "Push message: " + mMessage.size());
    	Activity myAct = getTiContext().getTiApp().getCurrentActivity();
    	if(myAct != null && mMessage.isEmpty()==false){
    		// Active state
    		messageCallback.callAsync(getKrollObject(), mMessage);
    		mMessage.clear();
    	}
    }
    
    public KrollDict getMessage() {
    	return mMessage;
    }
    
    public void sendMessage(int id, String alert) {
    	Log.i(LCAT, "Received from intent.");
    	Log.i(LCAT, "["+id+"] "+alert);
    	
    	// Put message
    	mMessage.put(Integer.toString(id), alert);
    	
    	// Push message
		pushMessage();
    }

    private AirshipConfigOptions getAirshipConfig() {
        return airshipConfig;
    }
    
    private void registerUA() {
		Log.d(LCAT, "inside registerUA()");

        UAirship.takeOff(getTiContext().getTiApp(), getAirshipConfig());

        PushManager.enablePush();

		PushPreferences prefs = PushManager.shared().getPreferences();
		Logger.info("My Application onCreate - App APID: " + prefs.getPushId());

        // Set intent receiver
        PushManager.shared().setIntentReceiver(IntentReceiver.class);
    }

    public String getPackageName(){
    	return mPackageName;
    }

    public String getActivityName(){
    	return mActivityName;
    }

    @Kroll.method
    public void setAirshipConfig(KrollDict param){
        AirshipConfigOptions options = new AirshipConfigOptions();

        options.inProduction = (Boolean)param.get("inProduction");
        options.pushServiceEnabled = (Boolean)param.get("pushServiceEnabled");
        options.c2dmSender = (String)param.get("c2dmSender");
        options.transport = (String)param.get("transport");
        options.productionAppKey = (String)param.get("productionAppKey");
        options.productionAppSecret = (String)param.get("productionAppSecret");
        options.developmentAppKey = (String)param.get("developmentAppKey");
        options.developmentAppSecret = (String)param.get("developmentAppSecret");

        airshipConfig = options;
    }

	@Kroll.method
	public String getApid() {
		return PushManager.shared().getAPID();
	}
	
	@Kroll.method
    public void registerForPushNotifications(KrollDict param) {
		Log.d(LCAT, "inside registerPush()");

        Activity myAct = getTiContext().getTiApp().getCurrentActivity();
        mPackageName = myAct.getComponentName().getPackageName();
        mActivityName = myAct.getComponentName().getClassName();

		successCallback = (KrollFunction) param.get("success");
        errorCallback = (KrollFunction) param.get("error");
        messageCallback = (KrollFunction) param.get("callback");
        
        Log.d(LCAT, "package name: " + mPackageName);
		Log.d(LCAT, "activity name: " + mActivityName);

        setAirshipConfig(param);

        registerUA();
    }
}

