package cz.cvut.fel.adaptivestructure.states;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

/**
 * Created by lunovana on 06-May-17.
 */

public class DisgustState extends AbstractState {

    private String backgroundColor = "#FFC7C9";
    private String textColor = "#7A2834";
    private String accentColor = "#D88F6D";

    public DisgustState() {
        super("DISGUST");
        setStateOptions(backgroundColor, textColor, accentColor, true);
    }

    @Override
    public void setRate(Face.Emotions faceEmotions) {
        this.setRate(faceEmotions.getDisgust());
    }

    @Override
    public void setDetector(CameraDetector detector) {
        detector.setDetectDisgust(true);
    }
}
