import java.util.*;

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

    public void addNode (Node node) throws MyException {

        if (!nodes.contains(node)) {
            nodes.add(node);
        } else
            throw new MyException("add repeated node");
    }

    public void addArrow (Arrow arrow) throws MyException {
        if (!arrows.contains(arrow)) {
            arrows.add(arrow);
        } else
            throw new MyException("add repeated arrow");
    }

    public void finish () throws MyException {
        findForwardPaths();
        findLoops();
        findUnTouchedLoops();
    }

    private void findForwardPaths () throws MyException {

        Node sourceNode = getSourceNode();
        Map <Node, Boolean> nodesVisited = new HashMap<>();
        for (int i = 0 ; i < nodes.size() ; i++) {
             nodesVisited.put(nodes.get(i), false);
        }

        Stack <Node> stackNode = new Stack<>();
        stackNode.add(sourceNode);
        nodesVisited.put(sourceNode,true);
        Stack <Arrow> stackArrow = new Stack<>();
        ForwardPath forwardPath = new ForwardPath();

        while (!stackNode.empty()) {

            for (int i = 0 ; i < arrows.size() ; i++) {

                Node endNode = arrows.get(i).getEndNode();

                if (compareArrowStartNodeWithNode(arrows.get(i),sourceNode) &&
                        !nodesVisited.get(endNode) &&
                        !compareTwoNodes(sourceNode, endNode)) {

                    nodesVisited.put(endNode,true);
                    stackNode.add(endNode);
                    stackArrow.add(arrows.get(i));
                    forwardPath.addArrow(arrows.get(i));
                    sourceNode = stackNode.peek();
                    nodesVisited.put(sourceNode,true);

                    if (compareTwoNodes(sourceNode,getSinkNode())) {

                        ForwardPath real = new ForwardPath(forwardPath);
                        forwardPaths.add(real);
                        nodesVisited.put(sourceNode, false);
                        stackNode.pop();
                        forwardPath.removeArrow(stackArrow.pop());
                        stackNode.pop();
                        forwardPath.removeArrow(stackArrow.pop());
                        sourceNode = stackNode.peek();
                        nodesVisited.put(sourceNode,true);

                    } else {
                        i = 0;
                    }

                } else {
                    //just case node was visited before and all other conditions satisfied
                    if (!compareTwoNodes(sourceNode, endNode) &&
                            compareArrowStartNodeWithNode(arrows.get(i),sourceNode)) {
                        nodesVisited.put(endNode, false);
                    }
                }
            }

            stackNode.pop();
            forwardPath.removeArrow(stackArrow.pop());
            sourceNode = stackNode.peek();
            nodesVisited.put(sourceNode,true);
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

    private void findLoops () throws MyException {

        findSelfLoops();

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

                for (int i = 0 ; i < arrows.size() ; i++) {

                    Node endNode = arrows.get(i).getEndNode();

                    if (compareArrowStartNodeWithNode(arrows.get(i),sourceNode) &&
                            !nodesVisited.get(endNode) &&
                            !compareTwoNodes(sourceNode, endNode)) {

                        nodesVisited.put(endNode,true);
                        stackNode.add(endNode);
                        stackArrow.add(arrows.get(i));
                        loop.addArrow(arrows.get(i));
                        sourceNode = stackNode.peek();
                        nodesVisited.put(sourceNode,true);

                        if (compareTwoNodes(sourceNode, nodes.get(j))) {

                            Loop real = new Loop(loop);
                            loops.add(real);
                            nodesVisited.put(sourceNode, false);
                            stackNode.pop();
                            loop.removeArrow(stackArrow.pop());
                            stackNode.pop();
                            loop.removeArrow(stackArrow.pop());
                            sourceNode = stackNode.peek();
                            nodesVisited.put(sourceNode,true);

                        } else {
                            i = 0;
                        }

                    } else {
                        //just case node was visited before and all other conditions satisfied
                        if (!compareTwoNodes(sourceNode, endNode) &&
                                compareArrowStartNodeWithNode(arrows.get(i),sourceNode)) {
                            nodesVisited.put(endNode, false);
                        }
                    }
                }

                stackNode.pop();
                loop.removeArrow(stackArrow.pop());
                sourceNode = stackNode.peek();
                nodesVisited.put(sourceNode,true);
            }
        }


    }

        private void findSelfLoops() throws MyException {
        for (int i = 0 ; i < arrows.size() ; i++) {
            if (compareTwoNodes(arrows.get(i).getStartNode(), arrows.get(i).getEndNode())) {
                Loop real = new Loop();
                real.addArrow(arrows.get(i));
                loops.add(real);
            }
        }
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

}
