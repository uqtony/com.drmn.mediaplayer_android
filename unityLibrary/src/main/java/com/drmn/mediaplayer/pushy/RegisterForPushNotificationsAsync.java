package com.drmn.mediaplayer.pushy;

import android.os.AsyncTask;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

import java.net.URL;

import me.pushy.sdk.Pushy;

public class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Object> {
    UnityPlayerActivity mActivity;

    public RegisterForPushNotificationsAsync(UnityPlayerActivity activity) {
        this.mActivity = activity;
    }

    protected Object doInBackground(Void... params) {
        try {
            // Register the device for notifications (replace MainActivity with your Activity class name)
            String deviceToken = Pushy.register(mActivity);

            // Registration succeeded, log token to logcat
            int pushy = Log.d("Pushy", "Pushy device token: " + deviceToken);

            // Send the token to your backend server via an HTTP GET request
            new URL("https://{YOUR_API_HOSTNAME}/register/device?token=" + deviceToken).openConnection();

            // Provide token to onPostExecute()
            return deviceToken;
        }
        catch (Exception exc) {
            // Registration failed, provide exception to onPostExecute()
            return exc;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        String message;

        // Registration failed?
        if (result instanceof Exception) {
            // Log to console
            Log.e("Pushy", result.toString());

            // Display error in alert
            message = ((Exception) result).getMessage();
        }
        else {
            message = "Pushy device token: " + result.toString() + "\n\n(copy from logcat)";
        }
    }
}
