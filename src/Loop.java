import java.util.ArrayList;

public class Loop {

    Loop (Arrow firstArrow) {

        loops.add(firstArrow);
        gain = firstArrow.getGain();
    }

    private ArrayList<Arrow> loops = new ArrayList<>();
    private int gain ;

    public void addNode(Arrow arrow){
        loops.add(arrow);
    }

    public void removeNode(Arrow arrow){
        loops.remove(arrow);
    }

    public void calculateGain (){
        for (int i = 0; i < loops.size(); i++) {
            gain = gain * loops.get(i).getGain();
        }
    }

    public int getGain() {
        return gain;
    }
}
