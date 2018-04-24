package sample;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class Icon {
    private Image image;
    private double x , y , h , w;
    private EventHandler<MouseEvent> behaviourOnMainCanvas;



    public boolean isInBoundaries (double mX, double mY){
        if(mX-x>0&&mY-y>0&&mX-x<w&&mY-y<h){
            System.out.println("clicked on icon");
            return true;
        }else {
            return false;
        }
    }

    public Icon(Image image, double x, double y, double h, double w, EventHandler<MouseEvent> behaviourOnMainCanvas) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.behaviourOnMainCanvas = behaviourOnMainCanvas;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public EventHandler<MouseEvent> getBehaviourOnMainCanvas() {
        return behaviourOnMainCanvas;
    }

    public void setBehaviourOnMainCanvas(EventHandler<MouseEvent> behaviourOnMainCanvas) {
        this.behaviourOnMainCanvas = behaviourOnMainCanvas;
    }
}
