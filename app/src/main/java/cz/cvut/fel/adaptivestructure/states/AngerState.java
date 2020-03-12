package cz.cvut.fel.adaptivestructure.states;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

/**
 * Created by lunovana on 06-May-17.
 */

public class AngerState extends AbstractState {

    private String backgroundColor = "#72BDE8";
    private String textColor = "#4753B5";
    private String accentColor = "#747582";

    public AngerState() {
        super("ANGER");
        setStateOptions(backgroundColor, textColor, accentColor, true);
    }

    @Override
    public void setRate(Face.Emotions faceEmotions) {
        this.setRate(faceEmotions.getAnger());
    }

    @Override
    public void setDetector(CameraDetector detector) {
        detector.setDetectAnger(true);
    }
}
