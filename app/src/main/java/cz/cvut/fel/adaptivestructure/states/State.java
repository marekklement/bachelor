package cz.cvut.fel.adaptivestructure.states;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

/**
 * Created by lunovana on 10-Mar-17.
 */

public interface State {

    String getName();

    void setName(String name);

    void setRate(Face.Emotions faceEmotions);

    void setChanging(long changing);

    boolean ifChange();

    boolean isNeutral();

    void setDetector(CameraDetector detector);

    int getStateBackgroundColor();

    int getStateTextColor();

    int getStateAccentColor();

    boolean ifBorderRoundOff();

}
