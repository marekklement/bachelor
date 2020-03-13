package cz.cvut.fel.adaptivestructure.adaptation;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.Collection;
import java.util.List;

import cz.cvut.fel.adaptivestructure.database.ASDatabase;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Node;
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

    // this node is root for all childes
    private Node root;
    // node for sub-nodes, starts with root
    private Node currentNode;

    private ASDatabase db;
    private Context context;

    public AdaptationMaker(Context context, SurfaceView cameraPreview) {
        this.context = context;
        this.cameraPreview = cameraPreview;
        this.cameraPreview.setAlpha(0);
        this.states = FabricState.getAllStates();
        settingDetector();
        if (currentState == null) currentState = states.getState(NEUTRAL_STATE);
    }

    public static AdaptationMaker getAdaptationMaker(Context context, SurfaceView cameraPreview) {
        if (instance == null) instance = new AdaptationMaker(context, cameraPreview);
        return instance;
    }

    public void createApplicationStructure(View view, int parent) {
        db = DatabaseInit.getASDatabase(context);
        root = createNode(view.getId(), view.getTransitionName(), parent);// todo right
        currentNode = root;
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

    public void onStop(){
        if(detector != null && detector.isRunning()) detector.stop();
        //if(db != null) db.close();
    }
}
