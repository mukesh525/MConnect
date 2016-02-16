package in.vmc.mcubeconnect.backgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import in.vmc.mcubeconnect.activity.ForgotPasword;
import in.vmc.mcubeconnect.activity.SignupActivity;

/**
 * Created by mukesh on 26/11/15.
 */
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private String sender;

    public void onReceive(Context context, Intent intent) {

        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");

            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = myBundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                sender = messages[i].getOriginatingAddress();
                if (sender.equals("TD-VMCIND")) {
                    strMessage = messages[i].getMessageBody();
                   /* String[] parts = strMessage.split(": ");
                    Log.d("OTP",strMessage.split("-")[1]);
                    String OTP = parts[1]; // 004*/
                    Log.d("OTP", strMessage);

                    try {
                        SignupActivity inst = SignupActivity.instance();
                        inst.updateList(strMessage);
                    } catch (Exception e) {
                        try {
                            ForgotPasword inst = ForgotPasword.instance();
                            inst.updateList(strMessage);
                           /* LoginActivity inst = LoginActivity.instance();
                            inst.updateList(strMessage);*/
                        } catch (Exception e1) {
                            Log.d("OTP", e1.toString());
                        }
                    }
                    try {
                        ForgotPasword inst = ForgotPasword.instance();
                        inst.updateList(strMessage);
                           /* LoginActivity inst = LoginActivity.instance();
                            inst.updateList(strMessage);*/
                    } catch (Exception e1) {
                        Log.d("OTP", e1.toString());
                    }

                    break;
                }
                // strMessage += "\n";
            }

            // Log.e("SMS", strMessage);
            // Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();

        }
    }
}

