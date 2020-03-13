package cz.cvut.fel.adaptivestructure.states;

import android.graphics.Color;

/**
 * Created by lunovana on 09-Mar-17.
 */

public abstract class AbstractState implements State {

    private final short changingFactor = 10;
    private final short isNeutralFactor = 1;
    private final int defaultBackgroundColor = Color.parseColor("#333333");
    private final int defaultTextColor = Color.parseColor("#000000");
    private final int defaultAccentColor = Color.parseColor("#FFFFFF");
    private String name;
    private float rate;
    private long changing;
    private int stateBackgroundColor;
    private int stateTextColor;
    private int stateAccentColor;

    private boolean ifBorderRoundOff;

    public AbstractState() {
        this.rate = 0;
    }

    public AbstractState(String name) {
        this.name = name.toUpperCase();
        this.rate = 0;
    }


    public AbstractState(String name, String backgroundColor, String textColor, String accentColor, boolean ifBorderRoundOff) {
        this.name = name.toUpperCase();
        this.rate = 0;
        setStateOptions(backgroundColor, textColor, accentColor, ifBorderRoundOff);
    }


    protected void setStateOptions(String backgroundColor, String textColor, String accentColor, boolean ifBorderRoundOff) {
        this.stateBackgroundColor = (backgroundColor == null) ? defaultBackgroundColor : Color.parseColor(backgroundColor);
        this.stateTextColor = (textColor == null) ? defaultTextColor : Color.parseColor(textColor);
        this.stateAccentColor = (accentColor == null) ? defaultAccentColor : Color.parseColor(accentColor);
        this.ifBorderRoundOff = ifBorderRoundOff;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    protected void setRate(float rate) {
        if (this.rate != rate) incrementChanging();
        this.rate = rate;
    }

    protected void incrementChanging() {
        this.changing++;
    }

    @Override
    public void setChanging(long changing) {
        this.changing = changing;
    }

    @Override
    public boolean ifChange() {
        return (changing == changingFactor);
    }

    @Override
    public boolean isNeutral() {
        return rate < isNeutralFactor;
    }

    @Override
    public int getStateBackgroundColor() {
        return stateBackgroundColor;
    }

    @Override
    public int getStateTextColor() {
        return stateTextColor;
    }

    @Override
    public int getStateAccentColor() {
        return stateAccentColor;
    }

    @Override
    public boolean ifBorderRoundOff() {
        return ifBorderRoundOff;
    }
}
