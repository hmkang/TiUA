/*
Copyright 2009-2011 Urban Airship Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE URBAN AIRSHIP INC ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL URBAN AIRSHIP INC OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.hmkang.TiUA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class IntentReceiver extends BroadcastReceiver {

	private static final String logTag = "TiUA";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(logTag, "Received intent: " + intent.toString());
		String action = intent.getAction();

		if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {

		    int id = intent.getIntExtra(PushManager.EXTRA_NOTIFICATION_ID, 0);
            String msg = intent.getStringExtra(PushManager.EXTRA_ALERT);
		    Log.i(logTag, "Received push notification. Alert: " 
		            + msg
		            + " [NotificationID="+id+"]");

            TiuaModule.getInstance().fireEvent("tiuapush", msg);

		} else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {

            String msg = intent.getStringExtra(PushManager.EXTRA_ALERT);
			Log.i(logTag, "User clicked notification. Message: " + msg);

            Intent launch = new Intent(Intent.ACTION_MAIN);
            //launch.setClass(UAirship.shared().getApplicationContext(), getTiContext().getActivity().class);
            launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            UAirship.shared().getApplicationContext().startActivity(launch);

		} else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
            String apid = intent.getStringExtra(PushManager.EXTRA_APID);
            Boolean valid = intent.getBooleanExtra(PushManager.EXTRA_REGISTRATION_VALID, false) ;
            Log.i(logTag, "Registration complete. APID:" + apid
                    + ". Valid: " + valid);
            if(!valid){
                apid = "";
            }
            TiuaModule.getInstance().fireEvent("tiuaregister", apid);
		}
	}
}
