package kz.beta.chatblock;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class QuickActions {
    private static final String PREF_NAME = "Preferences";
    private static final String KEY_AUTH = "auth";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public QuickActions(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveAuth(String authValue) {
        editor.putString(KEY_AUTH, authValue);
        editor.apply();
    }

    public String getAuth() {
        return sharedPreferences.getString(KEY_AUTH, null);
    }

    public void clearAuth() {
        editor.remove(KEY_AUTH);
        editor.apply();
    }

    public void saveString(String value, String key) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void saveId() {
        editor.putInt("id", IntId());
        editor.apply();
    }

    public int getId() {
        return sharedPreferences.getInt("id", 0);
    }

    public void clearObject(String key) {
        editor.remove(key);
        editor.apply();
    }
    public static String NewTimeMilliId(){
        long timeM = System.currentTimeMillis();
        return timeM + "id";
    }
    public static String CurrentDay(){
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return dateFormat.format(currentDate);
    }
    public static String CurrentHourMinute(){
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

        return dateFormat.format(currentDate);
    }

    public static int getVersionCode(ContextWrapper contextWrapper) {
        int versionCode = -1;
        try {
            // Get PackageInfo for the current app
            PackageInfo packageInfo = contextWrapper.getPackageManager().getPackageInfo(contextWrapper.getPackageName(), 0);

            // Retrieve versionCode (Deprecated in API 28, use longVersionCode instead)
            versionCode = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    public static int IntId(){
        Random random = new Random();
        return random.nextInt(9999) + 1;
    }


}

//  QuickActions storedPreferences = new QuickActions(this);

//  storedPreferences.saveAuth("your_auth_token_here");

// String authToken = storedPreferences.getAuth();