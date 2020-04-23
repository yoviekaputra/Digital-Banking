package mlpt.siemo.digitalbanking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.UUID;

public class SimStateReceiver extends BroadcastReceiver {
    /**
     * @see BroadcastReceiver#onReceive(Context, Intent)
     */
    enum STATE {
        NOT_READY, CARD_IO_ERROR, LOADED
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (intent != null) {
                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    String state = bundle.getString("ss");
                    Log.e(getClass().getSimpleName(), "onReceived " + state);
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
    }
}