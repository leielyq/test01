package com.leielyq.test;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;


public class SystemUtils {
    public static final boolean SDK_VERSION_KITKAT_OR_LATER = (VERSION.SDK_INT >= 19);
    public static final boolean SDK_VERSION_KITKAT_WATCH_OR_LATER;
    public static final boolean SDK_VERSION_LOLLIPOP_MR1_OR_LATER;
    public static final boolean SDK_VERSION_LOLLIPOP_OR_LATER;
    public static final boolean SDK_VERSION_M_OR_LATER;
    public static final boolean SDK_VERSION_N_MR1_OR_LATER;
    public static final boolean SDK_VERSION_N_OR_LATER;
    public static final boolean SDK_VERSION_O_MR1_OR_LATER;
    public static final boolean SDK_VERSION_O_OR_LATER;
    private static Boolean sIsEMUI = null;
    private static Boolean sIsMIUI = null;

    static {
        boolean z;
        boolean z2 = true;
        if (VERSION.SDK_INT >= 20) {
            z = true;
        } else {
            z = false;
        }
        SDK_VERSION_KITKAT_WATCH_OR_LATER = z;
        if (VERSION.SDK_INT >= 21) {
            z = true;
        } else {
            z = false;
        }
        SDK_VERSION_LOLLIPOP_OR_LATER = z;
        if (VERSION.SDK_INT >= 22) {
            z = true;
        } else {
            z = false;
        }
        SDK_VERSION_LOLLIPOP_MR1_OR_LATER = z;
        if (VERSION.SDK_INT >= 23) {
            z = true;
        } else {
            z = false;
        }
        SDK_VERSION_M_OR_LATER = z;
        if (VERSION.SDK_INT >= 24) {
            z = true;
        } else {
            z = false;
        }
        SDK_VERSION_N_OR_LATER = z;
        if (VERSION.SDK_INT >= 25) {
            z = true;
        } else {
            z = false;
        }
        SDK_VERSION_N_MR1_OR_LATER = z;
        if (VERSION.SDK_INT >= 26) {
            z = true;
        } else {
            z = false;
        }
        SDK_VERSION_O_OR_LATER = z;
        if (VERSION.SDK_INT <= 26) {
            z2 = false;
        }
        SDK_VERSION_O_MR1_OR_LATER = z2;
    }

    public static int getPackageVersionCode(Context pContext) {
        return getPackageInfo(pContext).versionCode;
    }

    public static String getPackageVersionName(Context pContext) {
        return getPackageInfo(pContext).versionName;
    }

    public static String getPackageName(Context pContext) {
        return pContext.getPackageName();
    }

    private static PackageInfo getPackageInfo(Context pContext) {
        try {
            return pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getApplicationLabel(Context pContext, String pPackageName) {
        PackageManager packageManager = pContext.getPackageManager();
        try {
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(pPackageName, 0)).toString();
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static boolean hasSystemFeature(Context pContext, String pFeature) {
        try {
            Method PackageManager_hasSystemFeatures = PackageManager.class.getMethod("hasSystemFeature", new Class[]{String.class});
            if (PackageManager_hasSystemFeatures == null) {
                return false;
            }
            return ((Boolean) PackageManager_hasSystemFeatures.invoke(pContext.getPackageManager(), new Object[]{pFeature})).booleanValue();
        } catch (Throwable th) {
            return false;
        }
    }

    public static int currentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static boolean isEqualAndMoreThan(int version) {
        return VERSION.SDK_INT >= version;
    }

    private static boolean contains(String what, String... items) {
        for (String s : items) {
            if (s != null && s.toLowerCase(Locale.getDefault()).contains(what)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMIUI() {
        boolean z = true;
        if (sIsMIUI != null) {
            return sIsMIUI.booleanValue();
        }
        if (contains("xiaomi", Build.MANUFACTURER, Build.BRAND, Build.FINGERPRINT)) {
            sIsMIUI = Boolean.valueOf(true);
            return true;
        }
        try {
            BuildProperties prop = BuildProperties.newInstance();

            if (prop.getProperty("ro.miui.ui.version.code", null)
                    == null && prop.getProperty("ro.miui.ui.version.name", null)
                    == null && prop.getProperty("ro.miui.internal.storage", null)
                    == null) {

                z = false;
            }
            sIsMIUI = Boolean.valueOf(z);
        } catch (IOException e) {
            sIsMIUI = Boolean.valueOf(false);
        }
        return sIsMIUI.booleanValue();
    }

//    public static boolean isHuaweiEMUI() {
//        if (Objects.nonNull(sIsEMUI)) {
//            return sIsEMUI.booleanValue();
//        }
//        try {
//            boolean z;
//            if (Build.BRAND.equalsIgnoreCase("huawei") || Build.BRAND.equalsIgnoreCase("honor")) {
//                z = true;
//            } else {
//                z = false;
//            }
//            sIsEMUI = Boolean.valueOf(z);
//        } catch (Throwable th) {
//            sIsEMUI = Boolean.valueOf(false);
//        }
//        return sIsEMUI.booleanValue();
//    }
}
class BuildProperties {
    private final Properties properties = new Properties();

    private BuildProperties() throws IOException {
        this.properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
    }

    public static BuildProperties newInstance() throws IOException {
        return new BuildProperties();
    }

    public String getProperty(String name, String defaultValue) {
        return this.properties.getProperty(name, defaultValue);
    }
}
