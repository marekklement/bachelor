package cz.cvut.fel.adaptivestructure.creation;

import android.content.Context;
import android.os.Build;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.RequiresApi;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Structure;

/**
 * This method works for initialization of structure itself.
 *
 * @author Marek Klement
 */
public class StructureCreation {

    /**
     * This method is used to create application structure. As someone wants to test this app with its own structure, here is point where to initialize.
     * TODO: Follow steps bellow to create simply structure:
     * 1) Create new HashMap<String, List<String>> - first parameter will be name of the page and second its buttons.
     * 2) Create main page and its buttons - do not forget to put name of the main page to the config.properties
     * 3) Create other pages same way and them to HashMap. BEWARE! - all buttons have to have page with same name and name have to be unique
     * 4) Return HashMap
     *
     * @return
     */
    private static HashMap<String, List<String>> createStructure() {
        HashMap<String, List<String>> pairs = new HashMap<>();
        List<String> buttons = new LinkedList<>();
        buttons.add("bla");
        buttons.add("bol");
        buttons.add("testToGoDown");
        pairs.put("mainPage", buttons);
        //
        List<String> blaButtons = new LinkedList<>();
        blaButtons.add("next");
        blaButtons.add("newOne");
        pairs.put("bla", blaButtons);
        //
        List<String> bolButtons = new LinkedList<>();
        bolButtons.add("end");
        bolButtons.add("notEnd");
        pairs.put("bol", bolButtons);
        //
        List<String> notEndButtons = new LinkedList<>();
        notEndButtons.add("last");
        pairs.put("notEnd", notEndButtons);
        //
        pairs.put("end", new LinkedList<>());
        //
        pairs.put("next", new LinkedList<>());
        //
        pairs.put("newOne", new LinkedList<>());
        //
        pairs.put("last", new LinkedList<>());
        //
        pairs.put("testToGoDown", new LinkedList<>());
        //
        return pairs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Structure getOrMakeStructure(Context context) {
        Structure highestVersion = DatabaseInit.getASDatabase(context).structureDao().getHighestVersion();
        if (highestVersion == null) {
            HashMap<String, List<String>> structure = createStructure();
            Structure struc = new Structure();
            struc.setVersion(1);
            struc.setPages(structure);
            DatabaseInit.getASDatabase(context).structureDao().insert(struc);
            return struc;
        }
        return highestVersion;
    }
}
