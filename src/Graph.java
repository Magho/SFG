import java.util.ArrayList;

public class Graph {

    private ArrayList <Node> nodes = new ArrayList<>();
    private ArrayList <Arrow> arrows = new ArrayList<>();
    private ArrayList <ForwardPath> forwardPaths = new ArrayList<>();
    private ArrayList<Loop> loops = new ArrayList<>();
    private ArrayList<UntouchedLoop> untouchedLoops = new ArrayList<>();


    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public ArrayList<ForwardPath> getForwardPaths() {
        return forwardPaths;
    }

    public ArrayList<Loop> getLoops() {
        return loops;
    }

    public ArrayList<UntouchedLoop> getUntouchedLoops() {
        return untouchedLoops;
    }


    public void addForwardPath (ForwardPath forwardPath) {
        forwardPaths.add(forwardPath);
    }
    public void addLoops (Loop loop) {
        loops.add(loop);
    }
    public void addUnTouchedLoops (UntouchedLoop untouchedLoop) {
        untouchedLoops.add(untouchedLoop);
    }
    public void addNode (Node node) {
        nodes.add(node);
    }
    public void addArrow (Arrow arrow) {
        arrows.add(arrow);
    }

}
