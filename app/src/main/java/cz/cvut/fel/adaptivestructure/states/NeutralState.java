package cz.cvut.fel.adaptivestructure.states;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

/**
 * Created by lunovana on 12-Mar-17.
 */

public class NeutralState extends AbstractState {

    private final String backgroundColor = "#EEC9B1";
    private final String textColor = "#47576F";
    private final String accentColor = "#A27F68";

    public NeutralState() {
        super("NEUTRAL");
        setStateOptions(backgroundColor, textColor, accentColor, false);
    }

    @Override
    public void setDetector(CameraDetector detector) {

    }

    @Override
    public void setRate(Face.Emotions faceEmotions) {
        //this.setChanging(this.getChanging()+1);
        incrementChanging();
    }
}
