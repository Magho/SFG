package BackEnd;
import java.util.*;

public class Graph {

    private ArrayList <Node> nodes = new ArrayList<>();
    private ArrayList <Arrow> arrows = new ArrayList<>();
    private ArrayList <ForwardPath> forwardPaths = new ArrayList<>();
    private ArrayList<Loop> loops = new ArrayList<>();
    public ArrayList <ArrayList <Loop> > AllUnTouchedLoops = new ArrayList<>();

    /*
    private ArrayList<UntouchedLoop> untouchedLoops = new ArrayList<>();
*/
    private Node sinkNode = null;
    private Node sourceNode = null;

    public void setSinkNode(Node sinkNode) {
        this.sinkNode = sinkNode;
    }

    public void setSourceNode(Node sourceNode) {
        this.sourceNode = sourceNode;
    }

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

    public ArrayList <ArrayList <Loop> > getUntouchedLoops() {
        return AllUnTouchedLoops;
    }

    public void addNode (Node node) throws MyException {

        if (!nodes.contains(node)) {
            nodes.add(node);
        } else
            throw new MyException("add repeated node");
    }

    public void addArrow (Arrow arrow) {
        if (!arrows.contains(arrow)) {
            arrows.add(arrow);
        } else {
            for (int i = 0 ; i < arrows.size() ; i++) {
                if (compareArrows(arrow, arrows.get(i))) {
                    arrows.get(i).setGain(arrows.get(i).getGain() + arrow.getGain());
                }
            }
        }
    }

    public void removeNode (Node node) throws MyException {
        if (!nodes.contains(node)) {
            throw new MyException("add repeated node");
        } else {
            nodes.remove(node);
        }
    }

    public void removeArrow (Arrow arrow) throws MyException {
        if (!arrows.contains(arrow)) {
            throw new MyException("remove unInserted arrow");
        } else {
            arrows.remove(arrow);
        }
    }
        private boolean compareArrows (Arrow arrow1, Arrow arrow2) {
            return compareTwoNodes(arrow1.getStartNode(),arrow2.getStartNode()) &&
                    compareTwoNodes(arrow1.getEndNode(), arrow2.getEndNode());
        }



    public void finish () throws MyException {

        findLoops();
        FindUntouchedLoops1(loops, AllUnTouchedLoops);
    }

    public void findForwardPaths () throws MyException {

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

                if (sourceNode.visitedNodeContain(endNode)) {
                    continue;
                }

                if (compareArrowStartNodeWithNode(arrows.get(i),sourceNode) &&
                        !nodesVisited.get(endNode) &&
                        !compareTwoNodes(sourceNode, endNode)) {

                    nodesVisited.put(endNode,true);
                    stackNode.add(endNode);
                    sourceNode.addVisitedNode(endNode);
                    stackArrow.add(arrows.get(i));
                    forwardPath.addArrow(arrows.get(i));
                    sourceNode = stackNode.peek();
                    nodesVisited.put(sourceNode, true);

                    if (compareTwoNodes(sourceNode, getSinkNode())) {

                        ForwardPath real = new ForwardPath(forwardPath);
                        forwardPaths.add(real);
                        nodesVisited.put(sourceNode, false);
                        stackNode.pop();
                        forwardPath.removeArrow(stackArrow.pop());
                        sourceNode = stackNode.peek();
                        sourceNode.removeVisitedNode(getSinkNode());
                        nodesVisited.put(sourceNode, true);

                    } else {
                        i = -1;
                    }

                } else {
                    nodesVisited.put(endNode, false);
                }
            }
            stackNode.pop();
            if (!stackArrow.empty()) {
                forwardPath.removeArrow(stackArrow.pop());
            }
            if (stackNode.empty()) {
                break;
            }
            sourceNode = stackNode.peek();
            nodesVisited.put(sourceNode,true);
        }
        removeRepeatedForwardPaths();
    }

        private void removeRepeatedForwardPaths () {
            for (int i = 0 ; i < forwardPaths.size() ; i++) {
                ForwardPath forwardPath1 = forwardPaths.get(i);
                for (int j = i+1 ; j < forwardPaths.size() ; j++) {
                    ForwardPath forwardPath2 = forwardPaths.get(j);
                    if (compareTwoForwardPaths (forwardPath1, forwardPath2) ) {
                        forwardPaths.remove(forwardPath2);
                        j--;
                    }
                }
            }
        }
        private boolean compareTwoForwardPaths (ForwardPath forwardPath1 , ForwardPath forwardPath2) {
        boolean repeated = true;
        if (forwardPath1.getArrows().size() != forwardPath2.getArrows().size()) {
            repeated  = false;
        } else {
            for (int i = 0 ; i < forwardPath1.getNodes().size() ; i++) {
                if (!forwardPath2.getNodes().contains(forwardPath1.getNodes().get(i))) {
                    repeated =  false;
                    break;
                }
            } if (repeated) {
                for (int i = 0; i < forwardPath2.getNodes().size(); i++) {
                    if (!forwardPath1.getNodes().contains(forwardPath2.getNodes().get(i))) {
                        repeated =  false;
                        break;
                    }
                }
            }
        }

        return repeated;
    }
        private Node getSourceNode () {
            return sourceNode;
        }
        private Node getSinkNode () {
            return sinkNode;
        }

    private void findLoops () throws MyException {

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

                    if ((stackNode.search(endNode) != -1 && !compareTwoNodes(endNode,nodes.get(j))))
                        continue;

                    if (sourceNode.visitedNodeContain(endNode))
                        continue;

                    if (compareArrowStartNodeWithNode(arrows.get(i), sourceNode) &&
                            !nodesVisited.get(endNode) &&
                            !compareTwoNodes(sourceNode, endNode)) {

                        nodesVisited.put(endNode, true);
                        stackNode.add(endNode);
                        sourceNode.addVisitedNode(endNode);
                        stackArrow.add(arrows.get(i));
                        loop.addArrow(arrows.get(i));
                        sourceNode = stackNode.peek();
                        nodesVisited.put(sourceNode, true);

                        if (compareTwoNodes(sourceNode, nodes.get(j))) {

                            Loop real = new Loop(loop);
                            loops.add(real);
                            nodesVisited.put(sourceNode, false);
                            stackNode.pop();
                            loop.removeArrow(stackArrow.pop());
                            sourceNode = stackNode.peek();
                            sourceNode.removeVisitedNode(nodes.get(j));
                            nodesVisited.put(sourceNode, true);

                        } else
                            i = -1;
                    } else
                        nodesVisited.put(endNode, false);
                }

                stackNode.pop().clearVisitedNodes();
                if (!stackArrow.empty())
                    loop.removeArrow(stackArrow.pop());

                if (stackNode.empty())
                    break;

                sourceNode = stackNode.peek();
                nodesVisited.put(sourceNode,true);
            }
            clearVisitedNodes ();
        }
        removeRepeatedLoops ();
        findSelfLoops();
    }


        private void clearVisitedNodes () {
            for (int i = 0 ; i < nodes.size() ; i++) {
                nodes.get(i).clearVisitedNodes();
            }
        }
        private void removeRepeatedLoops () {
            for (int i = 0 ; i < loops.size() ; i++) {
                Loop loop1 = loops.get(i);
                for (int j = i + 1 ; j < loops.size() ; j++) {
                    Loop loop2 = loops.get(j);
                    if (compareTwoLoops (loop1, loop2) ) {
                        loops.remove(loop2);
                        j--;
                    }
                }
            }
        }
        private boolean compareTwoLoops (Loop loop1 , Loop loop2) {
            boolean repeated = true;
                if (loop1.getArrows().size() != loop2.getArrows().size()) {
                    repeated  = false;
                } else {
                    for (int i = 0 ; i < loop1.getNodes().size() ; i++) {
                        if (!loop2.getNodes().contains(loop1.getNodes().get(i))) {
                            repeated =  false;
                            break;
                        }
                    } if (repeated) {
                        for (int i = 0; i < loop2.getNodes().size(); i++) {
                            if (!loop1.getNodes().contains(loop2.getNodes().get(i))) {
                                repeated =  false;
                                break;
                            }
                        }
                    }
                }

            return repeated;
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

    public void FindUntouchedLoops1 (ArrayList <Loop> loops, ArrayList <ArrayList <Loop> > allUnTouchedLoops) {

        int count = 0 ;
        for (int i = 0  ; i < loops.size() - 1 ; i++) {


            ArrayList <Loop> arr1 = new ArrayList<>();
            arr1.add(loops.get(i));
            arr1.add(loops.get(i+1));
            allUnTouchedLoops.add(arr1);

            for (int j = i + 2  ; j < loops.size() ; j++) {

                ArrayList <Loop> arr2 = new ArrayList<>();
                arr2.add(loops.get(i));
                arr2.add(loops.get(j));
                allUnTouchedLoops.add(arr2);

                int Size = allUnTouchedLoops.size() - 1;

                for (int k = count ; k < Size ; k++) {
                    ArrayList <Loop> looplist = new ArrayList<>();
                    for (int l = 0 ; l < allUnTouchedLoops.get(k).size() ; l++) {
                        looplist.add(allUnTouchedLoops.get(k).get(l));
                    }
                    looplist.add(loops.get(j));
                    allUnTouchedLoops.add(looplist);
                }
            }
            count =allUnTouchedLoops.size();
        }

        //remove touched loops from each arrayList
        FindUntouchedLoops2(allUnTouchedLoops);

        removeRepeattedUntouchedLoops(allUnTouchedLoops);

    }

    private void FindUntouchedLoops2 ( ArrayList <ArrayList <Loop> > allUnTouchedLoops) {

        for (int l = 0 ; l < allUnTouchedLoops.size() ; l++) {
            ArrayList <Loop> loops = allUnTouchedLoops.get(l);
            for (int i = 0; i < loops.size(); i++) {
                Loop loop1 = loops.get(i);
                for (int j = i + 1; j < loops.size(); j++) {
                    Loop loop2 = loops.get(j);
                    for (int k = 0; k < loop2.getNodes().size(); k++) {
                        Node node = loop2.getNodes().get(k);
                        if (loop1.getNodes().contains(node)) {
                            loops.remove(j);
                            j--;
                            break;
                        }
                    }
                }
            }

            //remove arrayLists with one loop
            if (allUnTouchedLoops.get(l).size() <= 1) {
                allUnTouchedLoops.remove(l);
                l--;
            }
        }
    }

    private void removeRepeattedUntouchedLoops (ArrayList <ArrayList <Loop> > allUnTouchedLoops) {

        for (int i = 0  ; i < allUnTouchedLoops.size() ; i++) {
            ArrayList <Loop> loop1 = allUnTouchedLoops.get(i);
            for (int j = i + 1  ; j < allUnTouchedLoops.size() ; j++) {
                ArrayList <Loop> loop2 = allUnTouchedLoops.get(j);
                boolean flag1 = true;
                boolean flag2 = true;
                for (int k = 0 ; k < loop2.size(); k++) {
                    Loop loop = loop2.get(k);
                    if (!loop1.contains(loop)) {
                        flag1 = false;
                        break;
                    }
                }
                for (int k = 0 ; k < loop1.size(); k++) {
                    Loop loop = loop1.get(k);
                    if (!loop2.contains(loop)) {
                        flag2 = false;
                        break;
                    }
                }
                if (flag1 && flag2) {
                    allUnTouchedLoops.remove(j);
                    j--;
                }
            }
        }
    }

    public void print (ArrayList <ArrayList <Loop> > allUnTouchedLoops) {

        System.out.println();
        for (int i = 0  ; i < allUnTouchedLoops.size() ; i++) {
            ArrayList <Loop> arrayListOfloops = allUnTouchedLoops.get(i);
            for (int j = 0  ; j < arrayListOfloops.size() ; j++) {
                arrayListOfloops.get(j).print();
            }
            System.out.println();
        }
    }

}
