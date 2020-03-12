package cz.cvut.fel.adaptivestructure.states;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lunovana on 12-Mar-17.
 */

public class CompositeStateImpl implements CompositeState {

    private Map<String, State> allStates;

    public CompositeStateImpl() {
        super();
        allStates = new HashMap<>();
    }

    @Override
    public void addState(State state) {
        allStates.put(state.getName(), state);
    }

    @Override
    public State getState(String nameState) {
        if (!allStates.isEmpty()) return allStates.get(nameState.toUpperCase());
        return null;
    }

    @Override
    public Collection<State> getStatesLikeCollection() {
        return allStates.values();
    }

    @Override
    public void deleteState(State state) {
        if (!allStates.isEmpty() && allStates.containsKey(state.getName())) {
            allStates.remove(state);
        }
    }


    @Override
    public void setDetector(CameraDetector detector) {
        Collection<State> statesCollection = this.getStatesLikeCollection();
        for (State state :
                statesCollection) {
            state.setDetector(detector);
        }
    }

    @Override
    public void setRate(Face.Emotions faceEmotions) {
        Collection<State> statesCollection = this.getStatesLikeCollection();
        for (State state :
                statesCollection) {
            state.setRate(faceEmotions);
        }
    }

}
