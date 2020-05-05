package cz.cvut.fel.adaptivestructure.utils;

/**
 * This state name builder is based on Lunovas's work.
 *
 * @author Marek Klement
 */
public abstract class StringUtils {

    public static String getStateName(String stateName) {
        StringBuilder sb = new StringBuilder();
        stateName = stateName.toUpperCase();
        sb.append(stateName.charAt(0));
        stateName = stateName.substring(1).toLowerCase();
        sb.append(stateName);
        return sb.toString();
    }

}