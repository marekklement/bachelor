package cz.cvut.fel.adaptivestructure.states;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.Collection;

/**
 * Created by lunovana on 12-Mar-17.
 */

public interface CompositeState {
    void addState(State state);

    Collection<State> getStatesLikeCollection();

    void setDetector(CameraDetector detector);

    void setRate(Face.Emotions faceEmotions);

    State getState(String nameState);

    void deleteState(State state);
}
