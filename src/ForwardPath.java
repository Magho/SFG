import java.util.ArrayList;

public class ForwardPath {

    ForwardPath (Arrow firstArrow) {

        forwardPath.add(firstArrow);
        gain = firstArrow.getGain();
    }

    private ArrayList <Arrow> forwardPath = new ArrayList<>();
    private int gain ;

    public void addNode(Arrow arrow){
        forwardPath.add(arrow);
    }

    public void removeNode(Arrow arrow){
        forwardPath.remove(arrow);
    }

    public void calculateGain (){
        for (int i = 0 ; i < forwardPath.size(); i++) {
            gain = gain * forwardPath.get(i).getGain();
        }
    }

    public int getGain() {
        return gain;
    }
}
