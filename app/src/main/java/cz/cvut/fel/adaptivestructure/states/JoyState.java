package cz.cvut.fel.adaptivestructure.states;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

/**
 * Created by lunovana on 12-Mar-17.
 */

public class JoyState extends AbstractState {

    private String backgroundColor = "#E3E059";
    private String textColor = "#273772";
    private String accentColor = "#96942C";

    public JoyState() {
        super("JOY");
        setStateOptions(backgroundColor, textColor, accentColor, false);
    }

    @Override
    public void setDetector(CameraDetector detector) {
        detector.setDetectJoy(true);
    }

    @Override
    public void setRate(Face.Emotions faceEmotions) {
        this.setRate(faceEmotions.getJoy());
    }

}
