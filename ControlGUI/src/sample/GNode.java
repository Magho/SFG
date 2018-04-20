package sample;


import javafx.scene.Node;
import javafx.scene.paint.Color;

public class GNode {
    private Color color;
    private double x;
    private double y;
    private double r;
    BackEnd.Node node;

    public GNode(Color color, double x, double y, double r, BackEnd.Node node) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.r = r;
        this.node = node;
    }
    public boolean isInBoundaries(double mx, double my){
        if(Math.abs(mx-x)<r&&Math.abs(my-y)<r){
            return true;
        }
        return false;
    }
    public boolean isInFarBoundaries(double mx, double my){
        if(Math.abs(mx-x)<r&&Math.abs(my-y)<r){
            return true;
        }
        return false;
    }
    public BackEnd.Node getNode() {
        return node;
    }

    public void setNode(BackEnd.Node node) {
        this.node = node;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

}
