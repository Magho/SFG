import java.util.ArrayList;

public class Loop {

    Loop (Arrow firstArrow) {

        arrows.add(firstArrow);
        gain = firstArrow.getGain();
    }

    private ArrayList<Arrow> arrows = new ArrayList<>();
    private int gain ;

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public void addNode(Arrow arrow){
        arrows.add(arrow);
    }

    public void removeNode(Arrow arrow){
        arrows.remove(arrow);
    }

    public void calculateGain (){
        for (int i = 0; i < arrows.size(); i++) {
            gain = gain * arrows.get(i).getGain();
        }
    }

    public int getGain() {
        return gain;
    }
}
