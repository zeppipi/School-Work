package com.example.movieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_FILTER = "Filter";
    public static final String SMS_KEY = "key";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("debug", "SMS");
        //Im going to be honest I have no idea how this telephony class works bruh
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++){
            //extract the messages to broadcast
            SmsMessage currentMsg = messages[i];
            String msg = currentMsg.getDisplayMessageBody();

            //time to sent the messages through the broadcast
            Intent msgIntent = new Intent();
            msgIntent.setAction(SMS_FILTER);
            msgIntent.putExtra(SMS_KEY, msg);
            context.sendBroadcast(msgIntent);
        }
    }
}
