package cz.cvut.fel.adaptivestructure.adaptation;

public class DoubleRange {

    private double top;
    private double bottom;

    public DoubleRange(double bottom, double top) {
        this.top = top;
        this.bottom = bottom;
    }

    public boolean isInside(double number) {
        return number <= top && number >= bottom;
    }
}
