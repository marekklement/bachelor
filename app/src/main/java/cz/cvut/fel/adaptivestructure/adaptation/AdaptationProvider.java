package cz.cvut.fel.adaptivestructure.adaptation;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;
import cz.cvut.fel.adaptivestructure.database.ASDatabase;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Node;
import cz.cvut.fel.adaptivestructure.entity.Structure;

public class AdaptationProvider {

    // properties
    private int MIN_VISITS = 5;
    //
    private Context context;
    private ASDatabase db;
    private Structure structure;
    private Structure newStructure;
    private boolean changeVersion;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AdaptationProvider(Context context){
        this.context = context;
        db = DatabaseInit.getASDatabase(this.context);
        structure = db.structureDao().getHighestVersion();
        newStructure = initNewEmptyStructure();
        changeVersion = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Structure initNewEmptyStructure() {
        return structure;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeStructure(){
        if(structure == null){
            return;
        }
        List<String> mainPage = structure.getPages().get("mainPage");
        if(mainPage == null){
            throw new IllegalArgumentException("Main page should not be null!");
        }
        Pair<String, List<String>> pair = new Pair<>("mainPage", mainPage);
        // calculate if problematic
        depthFirstChange(pair);
        if(changeVersion){
            newStructure.setVersion(structure.getVersion() + 1);
            db.structureDao().insert(newStructure);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void depthFirstChange(Pair<String, List<String>> parent){
        List<String> buttons = parent.second;
        for (String button : buttons) {
            List<String> page = structure.getPages().get(button);
            if(page == null){
                throw new IllegalArgumentException("Page should not be null!");
            }
            Pair<String, List<String>> pair = new Pair<>(button, page);
            depthFirstChange(pair);
            // get node with this name
            List<Node> nodes = db.nodeDao().getByName(button);
            if(nodes.size()!=1){
                continue;
            }
            // calculate if problematic
            Node node = nodes.get(0);
            if(shouldMoveUp(node) && node.getParent() != -1){
                List<Node> parents = db.nodeDao().getByName(parent.first);
                if(parents.size()!=1){
                    throw new IllegalArgumentException("There should not be " + parents.size() + " results!");
                }
                if(parents.get(0).getParent() != -1){
                    moveToDestination(parents.get(0).getParent(), button);
                    removeFromNode(parents.get(0), button);
                    changeVersion = true;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void removeFromNode(Node node, String button) {
        List<String> buttons = node.getButtons();
        List<String> collect = buttons.stream().filter(b -> !b.equals(button)).collect(Collectors.toList());
        node.setButtons(collect);
        db.nodeDao().update(node);
        //
        List<String> butts = newStructure.getPages().get(node.getName());
        if(butts == null){
            throw new IllegalArgumentException("Here buttons cannot be null!");
        }
        List<String> newButtons = butts.stream().filter(b -> !b.equals(button)).collect(Collectors.toList());
        HashMap<String, List<String>> pages = newStructure.getPages();
        pages.replace(node.getName(), newButtons);
        newStructure.setPages(pages);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void moveToDestination(int destination, String buttonToAdd) {
        Node parent = db.nodeDao().getById(destination);
        List<String> buttons = parent.getButtons();
        buttons.add(buttonToAdd);
        parent.setButtons(buttons);
        db.nodeDao().update(parent);
        List<String> collect = newStructure.getPages().get(parent.getName());
        if(collect == null){
            throw new IllegalArgumentException("There should not be null!");
        }
        collect.add(buttonToAdd);
        newStructure.getPages().replace(parent.getName(), collect);
        List<Node> current = db.nodeDao().getByName(buttonToAdd);
        if(current.size() != 1){
            throw new IllegalArgumentException("There should be just one!");
        }
        Node changeParentNode = current.get(0);
        changeParentNode.setParent(destination);
        db.nodeDao().update(changeParentNode);
    }

    private boolean shouldMoveUp(Node node){
        long visits = node.getVisits();
        // emotions
        float anger = node.getAnger();
        float disgust = node.getDisgust();
        float joy = node.getJoy();
        float sadness = node.getSadness();
        float neutral = node.getNeutral();
        //
        int version = node.getVersion();
        int changeValue = version * MIN_VISITS;
        if(visits >= changeValue && !node.getName().equals("mainPage")){
            node.setVersion(node.getVersion() + 1);
            db.nodeDao().update(node);
            float badMood = anger + disgust + sadness;
            float goodMood = (neutral + joy)*2;
            return badMood > goodMood;
        }
        return false;
    }

    private boolean shouldMoveDown(Node node){
        // todo
        throw new UnsupportedOperationException("Not supported yet!");
    }

}
