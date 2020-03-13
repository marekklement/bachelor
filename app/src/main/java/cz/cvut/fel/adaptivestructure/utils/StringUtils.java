package cz.cvut.fel.adaptivestructure.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by lunovana on 03-Apr-17.
 */

public abstract class StringUtils {

    private static final String className = "com.project.lunovana.affectivatest.states.";

    public static String getStateClassName(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(className);
        sb.append(name.charAt(0));
        name = name.substring(1).toLowerCase();
        sb.append(name).append("State");
        return sb.toString();
    }

    public static String getExportFileName(String exportFileName, Context context) {
        if (exportFileName == null) {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
            } catch (final PackageManager.NameNotFoundException e) {
            }
            exportFileName = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
        }

        System.out.println("exportFileName = " + exportFileName);

        return exportFileName;
    }

    public static String getStateName(String stateName) {
        StringBuilder sb = new StringBuilder();
        stateName = stateName.toUpperCase();
        sb.append(stateName.charAt(0));
        stateName = stateName.substring(1).toLowerCase();
        sb.append(stateName);
        return sb.toString();
    }

}