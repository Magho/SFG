import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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

    public void finish (){
        findForwardPathes();
        findLoops();
        findUnTouchedLoops();
    }

    //TODO revise
    private void findForwardPathes () {

        Node sourceNode = getSourceNode();
        Map <Node, Boolean> nodesVisited = new HashMap<>();

        for (int i = 0 ; i < nodes.size() ; i++) {
            nodesVisited.put(nodes.get(i), false);
        }

        Stack <Node> stackNode = new Stack<>();
        stackNode.add(sourceNode);
        Stack <Arrow> stackArrow = new Stack<>();
        ForwardPath forwardPath = new ForwardPath();

        while (!stackNode.empty()) {
            for (int i = 0 ; i < arrows.size() ; i++) {
                Node endNode = arrows.get(i).getEndNode();
                Node currentNode;
                Arrow currentArrow = stackArrow.peek();

                if (compareArrowStartNodeWithNode(arrows.get(i),sourceNode) && nodesVisited.get(endNode)) {

                    nodesVisited.put(endNode,true);
                    stackNode.add(endNode);
                    stackArrow.add(arrows.get(i));
                    currentNode = stackNode.peek();
                    forwardPath.addArrow(currentArrow);

                    if (compareTwoNodes(currentNode,getSinkNode())) {
                        forwardPaths.add(forwardPath);
                        forwardPath.removeArrow(currentArrow);
                    }
                } else {

                    forwardPath.removeArrow(currentArrow);

                }
                currentNode = stackNode.pop();
                stackArrow.pop();
                sourceNode = currentNode;
            }
        }
    }

    private Node getSourceNode () {

        Node node = null;

        for (int i = 0 ; i < nodes.size() ; i ++) {
            if (nodes.get(i).getType().compareTo("Source") == 0) {
                node = nodes.get(i);
            }
        }
        return node;
    }

    private Node getSinkNode () {

        Node node = null;

        for (int i = 0 ; i < nodes.size() ; i ++) {
            if (nodes.get(i).getType().compareTo("Sink") == 0) {
                node = nodes.get(i);
            }
        }
        return node;
    }

    //TODO revise
    private void findLoops () {

        for (int j = 0 ; j < nodes.size() ; j++ ) {
            Node sourceNode = nodes.get(j);
            Map<Node, Boolean> nodesVisited = new HashMap<>();

            for (int i = 0; i < nodes.size(); i++) {
                nodesVisited.put(nodes.get(i), false);
            }

            Stack<Node> stackNode = new Stack<>();
            stackNode.add(sourceNode);
            Stack<Arrow> stackArrow = new Stack<>();
            Loop loop = new Loop();

            while (!stackNode.empty()) {
                for (int i = 0; i < arrows.size(); i++) {
                    Node endNode = arrows.get(i).getEndNode();
                    Node currentNode;
                    Arrow currentArrow = stackArrow.peek();

                    if (compareArrowStartNodeWithNode(arrows.get(i), sourceNode) && nodesVisited.get(endNode)) {

                        nodesVisited.put(endNode, true);
                        stackNode.add(endNode);
                        stackArrow.add(arrows.get(i));
                        currentNode = stackNode.peek();
                        loop.addArrow(currentArrow);

                        if (compareTwoNodes(currentNode, nodes.get(j))) {
                            loops.add(loop);
                            loop.removeArrow(currentArrow);
                        }
                    } else {

                        loop.removeArrow(currentArrow);

                    }
                    currentNode = stackNode.pop();
                    stackArrow.pop();
                    sourceNode = currentNode;
                }
            }
        }
    }

    private void findUnTouchedLoops () {

        for (int i = 0  ; i < loops.size() ; i++) {
            ArrayList <Arrow> loop1Arrows = loops.get(i).getArrows();

            for (int l = i + 1  ; l < loops.size() ; l++) {
                ArrayList <Arrow> loop2Arrows = loops.get(i).getArrows();
                boolean flagAdd = true;

                for (int j = 0; j < loop1Arrows.size(); j++) {
                    boolean flagBreak = false;

                    for (int k = 0; k < loop2Arrows.size(); k++) {
                        if (compareArrowsStartNodes(loop1Arrows.get(j), loop2Arrows.get(k))) {
                            flagBreak = true;
                            break;
                        }
                    }
                    if (flagBreak) {
                        flagAdd = false;
                        break;
                    }
                }

                if (flagAdd) {
                    UntouchedLoop loop = new UntouchedLoop(loops.get(i), loops.get(l));
                    untouchedLoops.add(loop);
                }
            }
        }
    }

    private boolean compareArrowsStartNodes (Arrow arrow1 , Arrow arrow2) {
        if (arrow1.getStartNode().getName().compareTo(arrow2.getStartNode().getName()) == 0)
            return true;
        else
            return false;
    }

    private boolean compareArrowStartNodeWithNode (Arrow arrow1 , Node node) {
        if (arrow1.getStartNode().getName().compareTo(node.getName()) == 0)
            return true;
        else
            return false;
    }
    private boolean compareTwoNodes (Node node1 , Node node2) {
        if (node1.getName().compareTo(node2.getName()) == 0)
            return true;
        else
            return false;
    }

}
