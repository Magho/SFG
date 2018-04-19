import java.util.ArrayList;

public class SFG implements ISFG {

    boolean finished = false;
    private Graph graph = new Graph();
    private ArrayList <Loop> loops ;
    private ArrayList <UntouchedLoop> untouchedLoops ;
    private ArrayList <ForwardPath> forwardPaths ;

    @Override
    public void addNode(String name, String type) {
        Node node = new Node(name, type);
        graph.addNode(node);
    }

    @Override
    public void addArrow(Node startNode, Node endNode, int gain) {
        Arrow arrow = new Arrow(startNode, endNode, gain);
        graph.addArrow(arrow);
    }

    @Override
    public void finish() {

        graph.finish();

        this.loops = graph.getLoops();
        this.untouchedLoops = graph.getUntouchedLoops();
        this.forwardPaths = graph.getForwardPaths();
        this.finished = true;
    }

    @Override
    public ArrayList<ForwardPath> getForwardPathes() throws MyException {

        if (!finished) {
            throw new MyException("not finished entering info yet");
        } else {
            return forwardPaths;
        }
    }

    @Override
    public ArrayList<Loop> getLoops() throws MyException {

        if (!finished) {
            throw new MyException("not finished entering info yet");
        } else {
            return loops;
        }

    }

    @Override
    public ArrayList<UntouchedLoop> getUnTouchedLoops() throws MyException {

        if (!finished) {
            throw new MyException("not finished entering info yet");
        } else {
            return untouchedLoops;
        }
    }

    @Override
    public int getOverAllTransferFunction() throws MyException {

        if (!finished) {
            throw new MyException("not finished entering info yet");
        } else {
            
        }

        return 0;
    }

    private int  calculateGeneralDelta () {

        int gain = 1;

        for (int i = 0 ; i < loops.size(); i++) {
            gain = gain - loops.get(i).getGain();
        }

        for (int i = 0 ; i <untouchedLoops.size(); i++) {
            gain = gain + untouchedLoops.get(i).getGain();
        }

        return gain;
    }

    private int  calculateDeltaForSpecificForwardPath () {


        for () {

        }
        return 0;
    }
}
