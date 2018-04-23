package sample;

import BackEnd.Arrow;
import javafx.scene.paint.Color;


public class GArrow {
    private Color lineColor=Color.SILVER;
    private Color arrowColor=Color.BLACK;
    private Arrow arrow;
    private double slope;
    private double midx;
    private double midy;

    public GArrow() {

    }

    public GArrow(Arrow arrow, double slope, double midx, double midy) {
        this.arrow = arrow;
        this.slope = slope;
        this.midx = midx;
        this.midy = midy;
    }


    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(Color arrowColor) {
        this.arrowColor = arrowColor;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public void setArrow(Arrow arrow) {
        this.arrow = arrow;
    }

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public double getMidx() {
        return midx;
    }

    public void setMidx(double midx) {
        this.midx = midx;
    }

    public double getMidy() {
        return midy;
    }

    public void setMidy(double midy) {
        this.midy = midy;
    }
}
