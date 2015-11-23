package edm_opensource.niagarabooker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Created by mattiaspernhult on 2015-11-19.
 */
public class SharedPref {

    private final static String APP = "edm.opensource.NIAGARABOOKER";
    private final static String USER = APP + ".user";
    private final static String CREDENTIALS = APP + ".credentials";

    public static void setUser(Context context, String user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER, user);
        editor.commit();
    }

    public static String getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        if (sharedPreferences.contains(USER)) {
            String userReturn = sharedPreferences.getString(USER, null);
            return userReturn;
        }
        return null;
    }

    public static void setUserCredentials(Context context, String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CREDENTIALS, username + ":" + password);
        editor.commit();
    }

    public static String getUserCredentials(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        if (sharedPreferences.contains(CREDENTIALS)) {
            String credentials = sharedPreferences.getString(CREDENTIALS, null);
            return credentials;
        }
        return null;
    }
}
