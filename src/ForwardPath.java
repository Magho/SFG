import java.util.ArrayList;

public class ForwardPath {

    private ArrayList <Arrow> forwardPath = new ArrayList<>();
    private int gain = 1;

    public ArrayList<Arrow> getForwardPath() {
        return forwardPath;
    }

    public void addArrow(Arrow arrow){
        forwardPath.add(arrow);
    }

    public void removeArrow(Arrow arrow){
        forwardPath.remove(arrow);
    }

    public void calculateGain (){
        for (int i = 0 ; i < forwardPath.size(); i++) {
            gain = gain * forwardPath.get(i).getGain();
        }
    }

    public int getGain() {
        calculateGain();
        return gain;
    }
}
