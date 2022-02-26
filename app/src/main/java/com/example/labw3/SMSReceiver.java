package com.example.labw3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    public static final String PC_PARTS_SMS_ACTION = "PC_PARTS_SMS_ACTION";
    public static final String PC_PARTS_SMS_KEY = "PC_PARTS_SMS_KEY";
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage message : messages) {
            String msg = message.getDisplayMessageBody();

            Intent msgIntent = new Intent();
            msgIntent.setAction(PC_PARTS_SMS_ACTION);
            msgIntent.putExtra(PC_PARTS_SMS_KEY, msg);
            context.sendBroadcast(msgIntent);
        }
    }
}