package cz.cvut.fel.adaptivestructure.creation;

import android.content.Context;
import android.os.Build;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.RequiresApi;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Structure;

public class StructureCreation {

    private static HashMap<String, List<String>> createStructure(){
        HashMap<String, List<String>> pairs = new HashMap<>();
        List<String> buttons = new LinkedList<>();
        buttons.add("bla");
        buttons.add("bol");
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
        return pairs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Structure getOrMakeStructure(Context context){
        Structure highestVersion = DatabaseInit.getASDatabase(context).structureDao().getHighestVersion();
        if(highestVersion==null){
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
