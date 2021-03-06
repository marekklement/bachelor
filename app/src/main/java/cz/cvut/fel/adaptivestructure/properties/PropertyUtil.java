package cz.cvut.fel.adaptivestructure.properties;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cz.cvut.fel.adaptivestructure.R;

/**
 * This is util for getting properties from config.properties file
 *
 * @author Marek Klement
 */
public class PropertyUtil {

    private static final String TAG = "PropertyUtil";

    //
    private final static String DIRECTORY_NAME_PROPERTY = "directory_of_pages";
    private final static String MAIN_PAGE_NAME = "main_page_name";
    private final static String NUMBER_OF_VISITS_PROPERTY_NAME = "minimal_visits_for_change";
    private final static String STATE_CHANGING_PROPERTY_NAME = "detector_rate";
    private final static String CHANGE_AFTER_PROPERTY_NAME = "change_after_each_version_update";
    private final static String THRESHOLD_NAME = "threshold";

    private static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            int id = context.getResources().getIdentifier("config", "raw",
                    context.getPackageName());
            System.out.println("This is id: " + id);
            InputStream rawResource = context.getResources().openRawResource(id);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file.");
        }

        return null;
    }

    /**
     * Gets directory from properties.
     *
     * @return
     */
    public static String getDirectoryName(Context context) {
        String directoryName = PropertyUtil.getConfigValue(context, DIRECTORY_NAME_PROPERTY);
        if (directoryName == null || directoryName.equals("")) {
            throw new IllegalArgumentException("Please set property 'main_page_name' in config file!");
        }
        return directoryName;
    }

    /**
     * Gets name of main page from properties
     *
     * @param context
     * @return
     */
    public static String getMainPageName(Context context) {
        String mainPageName = PropertyUtil.getConfigValue(context, MAIN_PAGE_NAME);
        if (mainPageName == null || mainPageName.equals("")) {
            throw new IllegalArgumentException("Please set property 'main_page_name' in config file!");
        }
        return mainPageName;
    }

    /**
     * Gets number of visits from properties
     *
     * @param context
     * @return
     */
    public static int getNumberOfVisits(Context context) {
        String minVisits = PropertyUtil.getConfigValue(context, NUMBER_OF_VISITS_PROPERTY_NAME);
        if (minVisits == null || minVisits.equals("")) {
            throw new IllegalArgumentException("Please set property 'minimal_visits_for_change' in config file!");
        }
        return Integer.parseInt(minVisits);
    }

    /**
     * Gets treshold value from properties
     *
     * @param context
     * @return
     */
    public static int getThreshold(Context context) {
        String threshold = PropertyUtil.getConfigValue(context, THRESHOLD_NAME);
        if (threshold == null || threshold.equals("")) {
            throw new IllegalArgumentException("Please set property 'threshold' in config file!");
        }
        return Integer.parseInt(threshold);
    }

    /**
     * Find value of property NUMBER_OF_VISITS_PROPERTY_NAME.
     *
     * @param context
     * @return
     */
    public static int getStateChangingProperty(Context context) {
        String numberOfStateChanging = PropertyUtil.getConfigValue(context, STATE_CHANGING_PROPERTY_NAME);
        if (numberOfStateChanging == null || numberOfStateChanging.equals("")) {
            throw new IllegalArgumentException("Please set property 'main_page_name' in config file!");
        }
        return Integer.parseInt(numberOfStateChanging);
    }

    /**
     * Find value of property change after.
     *
     * @param context
     * @return
     */
    public static boolean getChangeAfterProperty(Context context) {
        String changeAfter = PropertyUtil.getConfigValue(context, CHANGE_AFTER_PROPERTY_NAME);
        if (changeAfter == null || changeAfter.equals("")) {
            throw new IllegalArgumentException("Please set property 'main_page_name' in config file!");
        }
        return Boolean.getBoolean(changeAfter);
    }
}
