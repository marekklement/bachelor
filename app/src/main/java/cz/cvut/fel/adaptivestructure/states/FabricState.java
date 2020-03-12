package cz.cvut.fel.adaptivestructure.states;

import android.util.Log;

import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cz.cvut.fel.adaptivestructure.Utils.StringUtils;

/**
 * Created by lunovana on 12-Mar-17.
 */

public abstract class FabricState {

    private static CompositeState states = null;

    public static CompositeState getAllStates(){

        if(states == null) {
            states = new CompositeStateImpl();

            states.addState(new NeutralState());
            states.addState(new JoyState());
            states.addState(new SadnessState());
            states.addState(new DisgustState());
            states.addState(new AngerState());
        }

        return states;
    }

    public static boolean addNewState(String stateName, String backgroundColor, String textColor, String accentColor, boolean changeShape, CameraDetector detector){

        stateName = StringUtils.getStateName(stateName);
        Method setRateMethod = null;

        final Method[] faceEmotionsMethods = Face.Emotions.class.getMethods();
        for(int i = 0; i < faceEmotionsMethods.length; i++){
            if(faceEmotionsMethods[i].getName().contains("get" + stateName)){
                setRateMethod = faceEmotionsMethods[i];
            }
        }

        if(setRateMethod == null) {
            Log.d("ADD_STATE", "detector can't detect required emotion (" + stateName + ")");
            return false;
        }

        final Method finalsetRateMethod = setRateMethod;

        Method setDetectorMethod = null;

        final Method[] cameraDetectorMethods = CameraDetector.class.getMethods();
        for(int i = 0; i < cameraDetectorMethods.length; i++){
            if(cameraDetectorMethods[i].getName().contains("setDetect" + stateName)){
                setDetectorMethod = cameraDetectorMethods[i];
            }
        }

        if(setDetectorMethod == null) {
            Log.d("ADD_STATE", "detector can't get result from required emotion (" + stateName + ")");
            return false;
        }

        final Method finalSetDetectorMethod = setDetectorMethod;

        State newState = new AbstractState(stateName, backgroundColor, textColor, accentColor, changeShape) {
            @Override
            public void setRate(Face.Emotions faceEmotions) {
                try {
                    this.setRate((Float) finalsetRateMethod.invoke(faceEmotions));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setDetector(CameraDetector detector) {
                if(finalSetDetectorMethod !=null) try {
                    finalSetDetectorMethod.invoke(detector, true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        };

        newState.setDetector(detector);

        if(states == null) states = getAllStates();
        if(states.getState(stateName) == null) states.addState(newState);

        return true;
    }

    public static void deleteState(String stateName){
        if(states != null){
            State state = states.getState(stateName);
            if(state != null) states.deleteState(state);
        }
    }
}
