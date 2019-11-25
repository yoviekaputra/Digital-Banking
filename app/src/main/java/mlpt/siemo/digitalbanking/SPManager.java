package mlpt.siemo.digitalbanking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by briliant.yudistira on 21/06/2017.
 */

public class SPManager {
    public static void saveString(Context activity, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context activity, String key, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(activity);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static String getString(Context activity, String key) {
        return getString(activity, key, "");
    }
}
