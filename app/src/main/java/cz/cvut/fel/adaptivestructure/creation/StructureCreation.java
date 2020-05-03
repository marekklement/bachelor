package cz.cvut.fel.adaptivestructure.creation;

import android.content.Context;
import android.util.Pair;

import java.util.LinkedList;
import java.util.List;

import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Structure;

public class StructureCreation {

    private static List<Pair<String, List<String>>> createStructure(){
        List<Pair<String, List<String>>> pairs = new LinkedList<>();
        List<String> buttons = new LinkedList<>();
        buttons.add("bla");
        buttons.add("bol");
        Pair<String, List<String>> mainPage = new Pair<>("mainPage", buttons);
        pairs.add(mainPage);
        //
        List<String> blaButtons = new LinkedList<>();
        blaButtons.add("next");
        blaButtons.add("newOne");
        Pair<String, List<String>> blaPage = new Pair<>("bla", blaButtons);
        pairs.add(blaPage);
        //
        List<String> bolButtons = new LinkedList<>();
        bolButtons.add("end");
        bolButtons.add("notEnd");
        Pair<String, List<String>> bolPage = new Pair<>("bol", bolButtons);
        pairs.add(bolPage);
        //
        List<String> notEndButtons = new LinkedList<>();
        notEndButtons.add("last");
        Pair<String, List<String>> notEndPage = new Pair<>("notEnd", notEndButtons);
        pairs.add(notEndPage);
        //
        Pair<String, List<String>> endPage = new Pair<>("end", new LinkedList<>());
        pairs.add(endPage);
        //
        Pair<String, List<String>> nextPage = new Pair<>("next", new LinkedList<>());
        pairs.add(nextPage);
        //
        Pair<String, List<String>> newOnePage = new Pair<>("newOne", new LinkedList<>());
        pairs.add(newOnePage);
        //
        Pair<String, List<String>> lastPage = new Pair<>("last", new LinkedList<>());
        pairs.add(lastPage);
        //
        return pairs;
    }

    public static Structure getOrMakeStructure(Context context){
        Structure highestVersion = DatabaseInit.getASDatabase(context).structureDao().getHighestVersion();
        if(highestVersion==null){
            List<Pair<String, List<String>>> structure = createStructure();
            Structure struc = new Structure();
            struc.setVersion(1);
            struc.setPages(structure);
            DatabaseInit.getASDatabase(context).structureDao().insert(struc);
            return struc;
        }
        return highestVersion;
    }
}
