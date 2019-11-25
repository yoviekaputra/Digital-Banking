package mlpt.siemo.digitalbanking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.UUID;

public class SimStateReceiver extends BroadcastReceiver {
    /**
     * @see BroadcastReceiver#onReceive(Context, Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.e(getClass().getSimpleName(), UUID.randomUUID().toString());
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
    }
}