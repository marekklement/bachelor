package cz.cvut.fel.adaptivestructure.utils;

import cz.cvut.fel.adaptivestructure.entity.Node;
import cz.cvut.fel.adaptivestructure.states.State;

public class StateAdapter {

    public static void adaptInterface(Node currentNode, State currentState) {
        updateStateWeight(currentNode, currentState);
        updateStatesRate(currentNode);
    }

    private static void updateStateWeight(Node currentNode, State currentState) {
        long weight;
        switch (currentState.getName()) {
            case "JOY":
                weight = currentNode.getJoyWeight() + 1;
                currentNode.setJoyWeight(weight);
                break;
            case "NEUTRAL":
                weight = currentNode.getNeutralWeight() + 1;
                currentNode.setNeutralWeight(weight);
                break;
            case "SADNESS":
                weight = currentNode.getSadnessWeight() + 1;
                currentNode.setSadnessWeight(weight);
                break;
            case "DISGUST":
                weight = currentNode.getDisgustWeight() + 1;
                currentNode.setDisgustWeight(weight);
                break;
            case "ANGER":
                weight = currentNode.getAngerWeight() + 1;
                currentNode.setAngerWeight(weight);
                break;
            default:
                throw new IllegalArgumentException("No other states defined yet");
        }
        currentNode.setStateChanging(currentNode.getStateChanging() + 1);
    }

    private static void updateStatesRate(Node currentNode) {
        updateJoy(currentNode);
        updateNeutral(currentNode);
        updateSadness(currentNode);
        updateDisgust(currentNode);
        updateAnger(currentNode);
    }

    private static void updateAnger(Node currentNode) {
        currentNode.setAnger(getPercent(currentNode.getStateChanging(), currentNode.getAngerWeight()));
    }

    private static void updateDisgust(Node currentNode) {
        currentNode.setDisgust(getPercent(currentNode.getStateChanging(), currentNode.getDisgustWeight()));
    }

    private static void updateSadness(Node currentNode) {
        currentNode.setSadness(getPercent(currentNode.getStateChanging(), currentNode.getSadnessWeight()));
    }

    private static void updateJoy(Node currentNode) {
        currentNode.setJoy(getPercent(currentNode.getStateChanging(), currentNode.getJoyWeight()));
    }

    private static void updateNeutral(Node currentNode) {
        currentNode.setNeutral(getPercent(currentNode.getStateChanging(), currentNode.getNeutralWeight()));
    }


    private static float getPercent(long stateChanging, long weight) {
        if (stateChanging == 0) return 0;
        return (weight * 100) / (float) (stateChanging);
    }
}
