package cz.cvut.fel.adaptivestructure.adaptation;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import cz.cvut.fel.adaptivestructure.database.ASDatabase;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Node;
import cz.cvut.fel.adaptivestructure.properties.PropertyUtil;
import cz.cvut.fel.adaptivestructure.states.CompositeState;
import cz.cvut.fel.adaptivestructure.states.FabricState;
import cz.cvut.fel.adaptivestructure.states.State;
import cz.cvut.fel.adaptivestructure.utils.StateAdapter;

/**
 * This is preparation for adaptation. This class looks for changes in emotions and save everything to node table in database.
 * This class is based on Lunova's work, but was upgraded with room database and adapted to our use.
 *
 * @author Marek Klement
 */
public class AdaptationPrepare implements Detector.ImageListener {

    private static AdaptationPrepare instance = null;
    private final String NEUTRAL_STATE = "NEUTRAL";
    //
    private int detectorRate; //  number of video frames processed per second: the greater the rate, the more CPU intensive the processing is
    private CameraDetector detector;
    private SurfaceView cameraPreview;

    private State currentState = null;
    private CompositeState states;
    private Node currentNode;

    private ASDatabase db;
    private Context context;

    public static AdaptationPrepare getAdaptationMaker() {
        if (instance == null) instance = new AdaptationPrepare();
        return instance;
    }

    public void adapt(Context context, SurfaceView cameraPreview, View view, int id, String name) {
        this.start(context, cameraPreview);
    }

    /**
     * Intro point of this class. Everything is initialized here.
     *
     * @param context
     * @param cameraPreview
     */
    private void start(Context context, SurfaceView cameraPreview) {
        this.context = context;
        this.cameraPreview = cameraPreview;
        this.cameraPreview.setAlpha(0);
        this.states = FabricState.getAllStates();
        this.detectorRate = PropertyUtil.getStateChangingProperty(context);
        if (detector == null) {
            settingDetector();
        }
        if (currentState == null) currentState = states.getState(NEUTRAL_STATE);
    }

    /**
     * Each node is created here.
     *
     * @param view
     * @param name
     * @param buttons
     */
    void createApplicationStructure(View view, String name, List<String> buttons) {
        db = DatabaseInit.getASDatabase(context);
        int parent = -1;
        if (currentNode != null && currentNode.getUid() != view.getId()) {
            parent = currentNode.getUid();
        }
        currentNode = createNode(view.getId(), name, parent, buttons);
    }

    /**
     * Node is saved or updated in this method.
     *
     * @param uid
     * @param name
     * @param parent
     * @param buttons
     * @return
     */
    public Node createNode(int uid, String name, int parent, List<String> buttons, Context context) {
        ASDatabase db = DatabaseInit.getASDatabase(context);
        Node node = db.nodeDao().getById(uid);
        if (node == null) {
            node = new Node();
            node.setUid(uid);
            node.setName(name);
            node.setParent(parent);
            node.setButtons(buttons);
            node.setVersion(1);
            node.setStartVisit(LocalDateTime.now());
            //
            db.nodeDao().insert(node);
        } else {
            long visits = node.getVisits() + 1;
            node.setVisits(visits);
            node.setStartVisit(LocalDateTime.now());
            db.nodeDao().update(node);
        }
        return node;
    }

    /**
     * Node is saved or updated in this method.
     *
     * @param uid
     * @param name
     * @param parent
     * @param buttons
     * @return
     */
    private Node createNode(int uid, String name, int parent, List<String> buttons) {
        return createNode(uid, name, parent, buttons, context);
    }

    /**
     * This method is started when mood changes so we can save new emotions.
     *
     * @param faces
     * @param frame
     * @param v
     */
    @Override
    public void onImageResults(List<Face> faces, Frame frame, float v) {

        if (faces.size() == 0) {
            System.out.println("NO FACE");
        } else {
            Face face = faces.get(0);
            Face.Emotions faceEmotions = face.emotions;
            if (faceEmotions == null) {
                System.out.println("NO EMOTION");
            } else {
                states.setRate(faceEmotions);
                this.analiseEmotions();
            }
        }
    }


    /**
     * Sets detector of emotions
     */
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

    /**
     * This method analyse emotions and update node when emotion is changed
     */
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
            StateAdapter.adaptInterface(currentNode, currentState);
            update(currentNode);
        }
        System.out.println("Current state is " + currentState.getName());
    }

    /**
     * Method as holder for update in database
     *
     * @param node
     */
    private void update(Node node) {
        db.nodeDao().update(node);
    }
}
