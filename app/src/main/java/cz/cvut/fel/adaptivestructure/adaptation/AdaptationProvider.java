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
import cz.cvut.fel.adaptivestructure.properties.PropertyUtil;

/**
 * This class is used for creating new structure. Should be started each time an app is started. It checks for changes and change structure of the app.
 *
 * @author Marek Klement
 */
class AdaptationProvider {
    //
    private ASDatabase db;
    private Structure structure;
    private Structure newStructure;
    private boolean changeVersion;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    AdaptationProvider(Context context) {
        //
        this.context = context;
        this.db = DatabaseInit.getASDatabase(context);
        this.structure = db.structureDao().getHighestVersion();
        this.newStructure = structure;
        this.changeVersion = false;
    }

    /**
     * This method is start point of adaptation itself. It start depth first recursion.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    void changeStructure() {
        if (structure == null) {
            return;
        }
        String mainPageName = PropertyUtil.getMainPageName(context);
        List<String> mainPage = structure.getPages().get(mainPageName);
        if (mainPage == null) {
            throw new IllegalArgumentException("Main page should not be null!");
        }
        Pair<String, List<String>> pair = new Pair<>(mainPageName, mainPage);
        // calculate if problematic
        depthFirstChange(pair);
        if (changeVersion) {
            newStructure.setVersion(structure.getVersion() + 1);
            db.structureDao().insert(newStructure);
        }
    }

    /**
     * This method goes depth first into each page's buttons. Then checks for changes and change.
     *
     * @param parent
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void depthFirstChange(Pair<String, List<String>> parent) {
        List<String> buttons = parent.second;
        for (String button : buttons) {
            List<String> page = structure.getPages().get(button);
            if (page == null) {
                throw new IllegalArgumentException("Page should not be null!");
            }
            Pair<String, List<String>> pair = new Pair<>(button, page);
            depthFirstChange(pair);
            // get node with this name
            List<Node> nodes = db.nodeDao().getByName(button);
            if (nodes.size() != 1) {
                continue;
            }
            // calculate if problematic
            Node node = nodes.get(0);
            boolean shouldMove = shouldMove(node);
            if (shouldMove && node.getParent() != -1) {
                List<Node> parents = db.nodeDao().getByName(parent.first);
                if (parents.size() != 1) {
                    throw new IllegalArgumentException("There should not be " + parents.size() + " results!");
                }
                if (parents.get(0).getParent() != -1) {
                    moveToDestination(parents.get(0).getParent(), button);
                    removeFromNode(parents.get(0), button);
                    changeVersion = true;
                // todo why moving down is not working
                } else if (node.getParent() == 1) {
                    Node someNeighbour = getSomeNeighbour(node);
                    if (someNeighbour != null) {
                        moveToDestination(someNeighbour.getUid(), button);
                        removeFromNode(parents.get(0), button);
                        changeVersion = true;
                    }
                }
            }
        }
    }

    /**
     * This method is used for finding neighbour to which to move node. First found is used.
     *
     * @param node
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Node getSomeNeighbour(Node node) {
        Node parent = db.nodeDao().getById(node.getParent());
        List<String> buttons = parent.getButtons();
        List<String> filteredButtons = buttons.stream().filter(b -> !b.equals(node.getName())).collect(Collectors.toList());
        if (filteredButtons.size() == 0) {
            return null;
        }
        Node toReturn;
        List<Node> byName = db.nodeDao().getByName(filteredButtons.get(0));
        if(byName.size()==0){
            toReturn = AdaptationPrepare.getAdaptationMaker().createNode(db.nodeDao().findHighestId() + 1, filteredButtons.get(0), node.getParent(), structure.getPages().get(filteredButtons.get(0)), context);
        } else if(byName.size() > 1){
            throw new IllegalArgumentException("Size of 'byName' should be one and is: " + byName.size());
        } else {
            toReturn = byName.get(0);
        }
        return toReturn;
    }

    /**
     * Removing node from old parent.
     *
     * @param node
     * @param button
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void removeFromNode(Node node, String button) {
        List<String> buttons = node.getButtons();
        List<String> collect = buttons.stream().filter(b -> !b.equals(button)).collect(Collectors.toList());
        node.setButtons(collect);
        db.nodeDao().update(node);
        //
        List<String> butts = newStructure.getPages().get(node.getName());
        if (butts == null) {
            throw new IllegalArgumentException("Here buttons cannot be null!");
        }
        List<String> newButtons = butts.stream().filter(b -> !b.equals(button)).collect(Collectors.toList());
        HashMap<String, List<String>> pages = newStructure.getPages();
        pages.replace(node.getName(), newButtons);
        newStructure.setPages(pages);
    }

    /**
     * Moving node to the new parent
     *
     * @param destination
     * @param buttonToAdd
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void moveToDestination(int destination, String buttonToAdd) {
        Node parent = db.nodeDao().getById(destination);
        List<String> buttons = parent.getButtons();
        buttons.add(buttonToAdd);
        parent.setButtons(buttons);
        db.nodeDao().update(parent);
        List<String> collect = newStructure.getPages().get(parent.getName());
        if (collect == null) {
            throw new IllegalArgumentException("There should not be null!");
        }
        collect.add(buttonToAdd);
        newStructure.getPages().replace(parent.getName(), collect);
        List<Node> current = db.nodeDao().getByName(buttonToAdd);
        if (current.size() != 1) {
            throw new IllegalArgumentException("There should be just one!");
        }
        Node changeParentNode = current.get(0);
        changeParentNode.setParent(destination);
        db.nodeDao().update(changeParentNode);
    }

    /**
     * Core mothod. In this method we find out if some of the nodes should be moved somewhere.
     *
     * @param node
     * @return
     */
    private boolean shouldMove(Node node) {
        long visits = node.getVisits();
        // emotions - just for better visibility
        float anger = node.getAnger();
        float disgust = node.getDisgust();
        float joy = node.getJoy();
        float sadness = node.getSadness();
        float neutral = node.getNeutral();
        //
        int version = node.getVersion();
        int numberOfVisits = PropertyUtil.getNumberOfVisits(context);
        int changeValue = version * numberOfVisits;
        String mainPageName = PropertyUtil.getMainPageName(context);
        if (visits >= changeValue && !node.getName().equals(mainPageName)) {
            updateNodeToNextVersion(node);
            float badMood = anger + disgust + sadness;
            // todo see what here
            float goodMood = (neutral + joy)/** *2 **/;
            return badMood > goodMood;
        }
        return false;
    }

    private void updateNodeToNextVersion(Node node) {
        node.setVersion(node.getVersion() + 1);
        if(PropertyUtil.getChangeAfterProperty(context)){
            node.setNeutral(0);
            node.setSadness(0);
            node.setJoy(0);
            node.setDisgust(0);
            node.setAnger(0);
            node.setNeutralWeight(0);
            node.setSadnessWeight(0);
            node.setJoyWeight(0);
            node.setDisgustWeight(0);
            node.setAngerWeight(0);
        }
        db.nodeDao().update(node);
    }

}
