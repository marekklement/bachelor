package cz.cvut.fel.adaptivestructure.states;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

/**
 * Created by lunovana on 12-Mar-17.
 */

public class SadnessState extends AbstractState {

    private final String backgroundColor = "#F27C38";
    private final String textColor = "#F8F8F8";
    private final String accentColor = "#F21D1D";

    public SadnessState() {
        super("SADNESS");
        setStateOptions(backgroundColor, textColor, accentColor, false);
    }

    @Override
    public void setDetector(CameraDetector detector) {
        detector.setDetectSadness(true);
    }


    @Override
    public void setRate(Face.Emotions faceEmotions) {
        this.setRate(faceEmotions.getSadness());
    }

}
