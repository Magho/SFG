import java.util.ArrayList;

public class ForwardPath {


    ForwardPath (){

    }

    ForwardPath (ForwardPath forwardPath) throws MyException {
        for (int i = 0; i < forwardPath.getArrows().size() ; i++) {
            this.addArrow(forwardPath.getArrows().get(i));
        }
    }

    private ArrayList <Arrow> arrows = new ArrayList<>();
    private int gain = 1;

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public void addArrow(Arrow arrow) throws MyException {
        if (!arrows.contains(arrow)) {
            arrows.add(arrow);
        } else
            throw new MyException("add repeated arrow into forwardPath");
    }

    public void removeArrow(Arrow arrow) throws MyException {
        if (arrows.contains(arrow)) {
            arrows.remove(arrow);
        } else
            throw new MyException("remove unexcited arrow from forwardPath");

    }

    public void calculateGain (){
        for (int i = 0 ; i < arrows.size(); i++) {
            gain = gain * arrows.get(i).getGain();
        }
    }

    public int getGain() {
        calculateGain();
        return gain;
    }
}
