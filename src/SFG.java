import java.util.ArrayList;

public class SFG implements ISFG {

    boolean finished = false;
    private Graph graph = new Graph();
    private ArrayList <Loop> loops ;
    private ArrayList <UntouchedLoop> untouchedLoops ;
    private ArrayList <ForwardPath> forwardPaths ;

    @Override
    public void addNode(String name, String type) throws MyException {
        if (!finished) {
            Node node = new Node(name, type);
            graph.addNode(node);
        } else
            throw new MyException("add node after finish");
    }

    @Override
    public void addArrow(Node startNode, Node endNode, int gain) throws MyException {
        if (!finished) {
            if (graph.getNodes().contains(startNode) && graph.getNodes().contains(endNode)) {
                Arrow arrow = new Arrow(startNode, endNode, gain);
                graph.addArrow(arrow);
            } else
                throw new MyException("add arrow between two non added nodes");
        } else
            throw new MyException("add arrow after finish");
    }

    @Override
    public void finish() throws MyException {

        if (graph.getNodes().size() != 0) {

            if (!checkDisconnectedGraph())
                throw new MyException("the graph is disconnected");

            graph.finish();

            this.loops = graph.getLoops();
            this.untouchedLoops = graph.getUntouchedLoops();
            this.forwardPaths = graph.getForwardPaths();
            this.finished = true;
        } else
            throw new MyException("call finish without adding any node");
    }

        private boolean checkDisconnectedGraph () {
            for (int i = 0 ; i < graph.getNodes().size() ; i++) {
                Node node = graph.getNodes().get(i);
                boolean found = false;
                for (int j = 0 ; j < graph.getArrows().size() ; j++) {
                    Arrow arrow = graph.getArrows().get(j);
                    if (compareTwoNodes(node, arrow.getStartNode()) | compareTwoNodes(node, arrow.getEndNode())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        }

            private boolean compareTwoNodes (Node node1 , Node node2) {
        if (node1.getName().compareTo(node2.getName()) == 0)
            return true;
        else
            return false;
    }

    @Override
    public ArrayList<ForwardPath> getForwardPaths() throws MyException {

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
    public float getOverAllTransferFunction() throws MyException {

        float overAllGain = 0;
        if (!finished) {
            throw new MyException("not finished entering info yet");
        } else {

            for (int i = 0 ; i < forwardPaths.size() ; i++) {
                float temp = forwardPaths.get(i).getGain() * calculateDeltaForSpecificForwardPath(forwardPaths.get(i))
                        / calculateGeneralDelta(loops, untouchedLoops);
                overAllGain = overAllGain + temp ;
            }
        }

        return overAllGain;
    }

        private float  calculateGeneralDelta (ArrayList<Loop> localLoops, ArrayList <UntouchedLoop> localUnTouchedLoops) {

            float gain = 1;

            for (int i = 0 ; i < localLoops.size(); i++) {
                gain = gain - localLoops.get(i).getGain();
            }

            for (int i = 0 ; i < localUnTouchedLoops.size(); i++) {
                gain = gain + localUnTouchedLoops.get(i).getGain();
            }

            return gain;
        }

        private float  calculateDeltaForSpecificForwardPath (ForwardPath forwardPath) {

            ArrayList <Arrow> forwardPathArrows = forwardPath.getArrows();
            ArrayList <Loop> tempLoops = new ArrayList<>();
            ArrayList <UntouchedLoop> tempUnTouchedLoops;

            // get loops unTouched with the forwardPath
            for (int i = 0  ; i < loops.size() ; i++) {
                ArrayList <Arrow> loopArrows = loops.get(i).getArrows();
                boolean flagAdd = true;
                for (int j = 0 ; j < loopArrows.size() ; j++) {
                    boolean flagBreak = false;
                    for (int k = 0; k < forwardPathArrows.size(); k++) {
                        if (compareArrows(loopArrows.get(j),forwardPathArrows.get(k))) {
                            flagBreak = true;
                            break;
                        }
                    }
                    if (flagBreak) {
                        flagAdd = false;
                        break;
                    }
                }

                if (flagAdd)
                    tempLoops.add(loops.get(i));
            }
            //get unTouchedLoops from the loops unTouched with the forwardPath
            tempUnTouchedLoops = findUnTouchedLoops(tempLoops);
            return calculateGeneralDelta(tempLoops, tempUnTouchedLoops);

        }

        private ArrayList <UntouchedLoop>  findUnTouchedLoops (ArrayList <Loop> localLoops) {

            ArrayList <UntouchedLoop> tempUnTouchedLoops = new ArrayList<>();

            for (int i = 0  ; i < localLoops.size() ; i++) {
                ArrayList <Arrow> loop1Arrows = localLoops.get(i).getArrows();

                for (int l = i + 1  ; l < localLoops.size() ; l++) {
                    ArrayList <Arrow> loop2Arrows = localLoops.get(i).getArrows();
                    boolean flagAdd = true;

                    for (int j = 0; j < loop1Arrows.size(); j++) {
                        boolean flagBreak = false;

                        for (int k = 0; k < loop2Arrows.size(); k++) {
                            if (compareArrows(loop1Arrows.get(j), loop2Arrows.get(k))) {
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
                        UntouchedLoop loop = new UntouchedLoop(localLoops.get(i), localLoops.get(l));
                        tempUnTouchedLoops.add(loop);
                    }
                }
            }

        return tempUnTouchedLoops;
    }

    private boolean compareArrows (Arrow arrow1 , Arrow arrow2) {
        if (arrow1.getStartNode().getName().compareTo(arrow2.getStartNode().getName()) == 0)
            return true;
        else
            return false;
    }
}
