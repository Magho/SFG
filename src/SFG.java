import java.util.ArrayList;

public class SFG implements ISFG {

    boolean finished = false;
    private Graph graph = new Graph();

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
        this.finished = true;
    }

    @Override
    public ArrayList<ForwardPath> getForwardPathes() {

        if (!finished) {
            //TODO not finished yet
        } else {

        }

        return null;
    }

    @Override
    public ArrayList<Loop> getLoops() {

        if (!finished) {
            //TODO not finished yet
        } else {

        }

        return null;
    }

    @Override
    public ArrayList<UntouchedLoop> getUnTouchedLoops() {

        if (!finished) {
            //TODO not finished yet
        } else {

        }

        return null;
    }

    @Override
    public int getOverAllTransferFunction() {

        if (!finished) {
            //TODO not finished yet
        } else {

        }
        
        return 0;
    }

    private int  calculateGeneralDelta () {
        return 0;
    }
    private int  calculateDeltaForSpecificForwardPath () {
        return 0;
    }
}
