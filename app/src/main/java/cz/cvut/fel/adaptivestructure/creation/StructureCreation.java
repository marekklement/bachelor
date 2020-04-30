package cz.cvut.fel.adaptivestructure.creation;

import android.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class StructureCreation {

    public List<Pair<String, List<String>>> createStructure(){
        List<Pair<String, List<String>>> pairs = new LinkedList<>();
        List<String> buttons = new LinkedList<>();
        buttons.add("bla");
        buttons.add("bol");
        Pair<String, List<String>> mainPage = new Pair<>("mainPage", buttons);
        List<String> blaButtons = new LinkedList<>();
        blaButtons.add("next");
        blaButtons.add("newOne");
        Pair<String, List<String>> blaPage = new Pair<>("bla", blaButtons);
        List<String> bolButtons = new LinkedList<>();
        bolButtons.add("end");
        bolButtons.add("notEnd");
        Pair<String, List<String>> bolPage = new Pair<>("bol", bolButtons);
        List<String> notEndButtons = new LinkedList<>();
        notEndButtons.add("last");
        Pair<String, List<String>> notEndPage = new Pair<>("notEnd", notEndButtons);
        pairs.add(mainPage);
        pairs.add(blaPage);
        pairs.add(bolPage);
        pairs.add(notEndPage);
        return pairs;
    }
}
