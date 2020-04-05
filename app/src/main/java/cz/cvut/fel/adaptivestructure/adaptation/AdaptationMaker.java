package cz.cvut.fel.adaptivestructure.adaptation;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.fragment.app.Fragment;
import cz.cvut.fel.adaptivestructure.database.ASDatabase;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.MovedView;
import cz.cvut.fel.adaptivestructure.entity.Node;
import cz.cvut.fel.adaptivestructure.fragments.FinancesFragment;
import cz.cvut.fel.adaptivestructure.states.CompositeState;
import cz.cvut.fel.adaptivestructure.states.FabricState;
import cz.cvut.fel.adaptivestructure.states.State;
import cz.cvut.fel.adaptivestructure.utils.StateAdapter;

public class AdaptationMaker implements Detector.ImageListener {

    private static AdaptationMaker instance = null;
    private final int detectorRate = 10; //  number of video frames processed per second: the greater the rate, the more CPU intensive the processing is
    private final String NEUTRAL_STATE = "NEUTRAL";

    private CameraDetector detector;
    private SurfaceView cameraPreview;

    private State currentState = null;
    private CompositeState states;

    private ViewGroup currentLayout;
    private int wayHere;

    // this node is root for all childes
    private Node root;
    // node for sub-nodes, starts with root
    private Node currentNode;

    private ASDatabase db;
    private Context context;

    public void adapt(Context context, SurfaceView cameraPreview, View view, int id){
        this.wayHere = id;
        saveCurrentView(view);
        this.start(context, cameraPreview);
        this.createApplicationStructure(view);
        //db.nodeDao().deleteAll();
        createNewViews(view);
        move(view);
    }

    private void saveCurrentView(View view){
        if(view instanceof ViewGroup){
            currentLayout = (ViewGroup) view;
        } else {
            throw new IllegalArgumentException("View is not type layout");
        }
    }

    private void start(Context context, SurfaceView cameraPreview){
        this.context = context;
        this.cameraPreview = cameraPreview;
        this.cameraPreview.setAlpha(0);
        this.states = FabricState.getAllStates();
        settingDetector();
        if(currentState == null) currentState = states.getState(NEUTRAL_STATE);
    }

    public static AdaptationMaker getAdaptationMaker() {
        if (instance == null) instance = new AdaptationMaker();
        return instance;
    }

    private void createApplicationStructure(View view) {
        db = DatabaseInit.getASDatabase(context);
        long parent = -1;
        if(currentNode!=null && currentNode.getUid() != view.getId()){
            parent = currentNode.getUid();
        }
        currentNode = createNode(view.getId(), view.getResources().getResourceEntryName(view.getId()), parent);// todo right
        if(currentNode.getParent() == -1){
            root = currentNode;
        }
    }

    private void removeOldViews(View view){
        ViewGroup layoutFromView = getLayoutFromView(view);
        Node byId = db.nodeDao().getById(view.getId());
        if(byId == null){
            return;
        }
        List<MovedView> toBeDeleted = byId.getToBeDeleted();
        if(toBeDeleted != null && toBeDeleted.size()!=0) {
            for (MovedView removeView : toBeDeleted) {

            }
        }
    }

    private void createNewViews(View view){
        ViewGroup layoutFromView = getLayoutFromView(view);
        Node byId = db.nodeDao().getById(view.getId());
        if(byId == null){
            return;
        }
        List<MovedView> toCreate = byId.getToBeCreated();
        if(toCreate != null && toCreate.size()!=0) {
            for (MovedView newView : toCreate) {
                layoutFromView.addView(createNewView(newView));
            }
        }
    }

    private View createNewView(MovedView movedView){
        Button component = new Button(context);
        component.setId(movedView.getUid());
        component.setText(movedView.getName());
        component.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==component.getId()){

                }
            }
        });

        return component;
    }

    private ViewGroup getLayoutFromView(View view){
        if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i<viewGroup.getChildCount();i++) {
                View childAt = viewGroup.getChildAt(i);
                if(childAt instanceof ViewGroup){
                    ViewGroup bottom = (ViewGroup) childAt;
                    if(bottom.getChildCount()!=0){
                        return bottom;
                    }
                }
            }
            return viewGroup;
        }
        throw new IllegalArgumentException("No ViewGroup found!");
    }

    private void addToList(View view, Node node){
        List<MovedView> toMove = node.getToBeCreated();
        if(toMove==null){
            toMove = new LinkedList<>();
        }
        Button but;
        if(view instanceof Button){
            but = (Button) view;
        } else {
            throw new IllegalArgumentException("View is not a button!");
        }
        MovedView movedView = new MovedView();
        movedView.setType(view.getClass().getName());
        movedView.setUid(view.getId());
        movedView.setLayoutId(currentLayout.getId());
        movedView.setName(but.getText().toString());
        toMove.add(movedView);
        node.setToBeCreated(toMove);
        db.nodeDao().update(node);
    }

    private void removeFromList(long id, List<MovedView> movedViews){
        for(MovedView movedView : movedViews){
            if(movedView.getUid()==id){
                movedViews.remove(movedView);
                break;
            }
        }
    }

    private void moveToParent(List<View> views){
        Node byId = db.nodeDao().getById(currentNode.getParent());
        if(byId!=null) {
            addToRemove(byId);
            for (View view : views) {
                addToList(view, byId);
            }
        }
    }

    private void addToRemove(Node node) {
        List<MovedView> toMove = node.getToBeDeleted();
        if(toMove==null){
            toMove = new LinkedList<>();
        }
        MovedView movedView = new MovedView();
        movedView.setUid(wayHere);
        node.setToBeDeleted(toMove);
        db.nodeDao().update(node);
    }

    private void move(View view){
        //
        int id = view.getId();
        if(id==2131230725) { // todo
            this.moveToParent(getAllViews(view));
        }
    }

    private Node createNode(long uid, String name, long parent) {
        Node node = db.nodeDao().getById(uid);
        if (node == null) {
            node = new Node();
            node.setUid(uid);
            node.setName(name);
            node.setParent(parent);
            // init all emotions todo
            //
            db.nodeDao().insert(node);
        } else {
            long visits = node.getVisits() + 1;
            node.setVisits(visits);
            db.nodeDao().update(node);
        }
        return node;
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {

        if (faces.size() == 0) {
            //((TextView)(activity.findViewById(R.id.textView))).setText("NO FACE");
            System.out.println("NO FACE");
        } else {
            Face face = faces.get(0);
            Face.Emotions faceEmotions = face.emotions;
            if (faceEmotions == null) {
                //((TextView) (activity.findViewById(R.id.textView))).setText("NO EMOTION");
                System.out.println("NO EMOTION");
            } else {
                states.setRate(faceEmotions);
                this.analiseEmotions();
            }
        }
    }

    private void settingDetector() {
        detector = new CameraDetector(context, CameraDetector.CameraType.CAMERA_FRONT, cameraPreview);
        if (detector == null) {
            System.out.println("Error in creating Detector");
            return;
        }
        detector.setImageListener(this);
        states.setDetector(detector);
        detector.setMaxProcessRate(detectorRate); //The maximum processing rate to operate in [FPS]
        detector.start();
    }

    private void analiseEmotions() {
        Collection<State> stateCollection = states.getStatesLikeCollection();
        State oldCurrentState = currentState;
        currentState = states.getState(NEUTRAL_STATE);
        if (states != null) {
            String neutralString = currentState.getName();
            for (State state : stateCollection) {
                if (!state.getName().equalsIgnoreCase(neutralString) && !state.isNeutral()) {
                    currentState = state;
                    break;
                }
            }
        }

        if (!oldCurrentState.equals(currentState)) {
            currentState.setChanging(0);
        } else if (currentState.ifChange()) {
            // todo update node
            StateAdapter.adaptInterface(currentNode, currentState);
            update(currentNode);
            Node byId = db.nodeDao().getById(currentNode.getUid());
            long ii = byId.getStateChanging();


        }

        System.out.println("Current state is " + currentState.getName());
    }

    private void update(Node node) {
        db.nodeDao().update(node);
    }

    public void stop(){
        if(detector != null && detector.isRunning()) {
            detector.stop();
        }
    }

    private List<View> getAllViews(View view){
        if(view instanceof ViewGroup) {
            ViewGroup layout = (ViewGroup) view;
            List<View> views = new ArrayList<>();
            for (int i = 0; i < layout.getChildCount(); i++) {
                View childAt = layout.getChildAt(i);
                List<View> allViews = getAllViews(childAt);
                if(allViews == null || allViews.size()==0){
                    if(childAt.getTag()!=null) {
                        String redirect = (String) childAt.getTag();
                        if(redirect.equals("redirect")) {
                            views.add(childAt);
                        }
                    }
                } else {
                    views.addAll(allViews);
                }
            }
            return views;
        }
        return null;
    }
}
