package com.bitjunkie.smartdialer;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by Patrick on 4/23/2017.
 */

public class ReverseLookup extends Service {
    BroadcastReceiver br;
    private static boolean ringing = false;
    private static boolean incoming = false;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
            Toast.makeText(context,"Testing: " + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER), Toast.LENGTH_LONG).show();
            Lookup(context, intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
        }
        public void Lookup(Context context, String phonenumber){
            if(phonenumber !=null)
                Toast.makeText(context,"Testing: " + phonenumber, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"Testing: null", Toast.LENGTH_LONG).show();

        }
    };
    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        filter.addAction("your_action_strings"); //further more
        filter.addAction("your_action_strings"); //further more

        registerReceiver(receiver, filter);
        //Toast.makeText(getApplicationContext(),"Service started", Toast.LENGTH_LONG).show();
    }
    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }
}
