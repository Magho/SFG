import java.util.ArrayList;

public class Loop {


    private ArrayList<Arrow> arrows = new ArrayList<>();
    private int gain = 1;

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public void addArrow(Arrow arrow){
        arrows.add(arrow);
    }

    public void removeArrow(Arrow arrow){
        arrows.remove(arrow);
    }

    public void calculateGain (){
        for (int i = 0; i < arrows.size(); i++) {
            gain = gain * arrows.get(i).getGain();
        }
    }

    public int getGain() {
        calculateGain();
        return gain;
    }
}
