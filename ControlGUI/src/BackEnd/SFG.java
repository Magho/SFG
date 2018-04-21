package BackEnd;

import java.util.ArrayList;

public class SFG implements ISFG {

    boolean finished = false;
    private Graph graph = new Graph();
    private ArrayList <Loop> loops ;
    private ArrayList <UntouchedLoop> untouchedLoops ;
    private ArrayList <ForwardPath> forwardPaths ;

    @Override
    public Node addNode(String name) throws MyException {
        System.out.println("BACKEND:: node Added ->"+name);
        if (!finished) {
            Node node = new Node(name);
            graph.addNode(node);
            return node;
        } else
            throw new MyException("add node after finish");
    }

    @Override
    public Arrow addArrow(Node startNode, Node endNode, int gain) throws MyException {
        System.out.println("BACKEND:: arrow Added ->"+startNode.getName()+" -> "+endNode.getName()+"  g: "+gain);

        if (!finished) {
            if (graph.getNodes().contains(startNode) && graph.getNodes().contains(endNode)) {
                Arrow arrow = new Arrow(startNode, endNode, gain);
                graph.addArrow(arrow);
                return arrow;
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
    public ArrayList<ForwardPath> getForwardPaths(Node sinkNode, Node sourceNode) throws MyException {

        if (!finished) {
            throw new MyException("not finished entering info yet");
        } else if (!checkIfSink(sinkNode) | !checkIfSource(sourceNode)) {
            throw new MyException("can't find forward path between non sink and non source node");
        } else {

            graph.setSinkNode(sinkNode);
            graph.setSourceNode(sourceNode);
            graph.findForwardPaths();
            forwardPaths = graph.getForwardPaths();
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
    public float getOverAllTransferFunction(Node numerator, Node denominator) throws MyException {

        if (!finished) {
            throw new MyException("not finished entering info yet");
        } else {
            return decideTheCaseOfTheTF(numerator,denominator);
        }
    }

    private float decideTheCaseOfTheTF (Node numerator, Node denominator) throws MyException {

        float overAllGain ;

        if (checkIfSource(denominator) && checkIfSink(numerator)) {
            graph.setSinkNode(numerator);
            graph.setSourceNode(denominator);

            graph.findForwardPaths();
            forwardPaths = graph.getForwardPaths();
            overAllGain = calcOverAllTransferFunction();

        } else if (checkIfSource(denominator) && !checkIfSink(numerator)) {

            //handel numerator
            Node node = new Node((numerator.getName() + "'"));
            graph.addNode(node);
            Arrow arrow = new Arrow(numerator, node,1);
            graph.addArrow(arrow);
            graph.setSinkNode(node);
            graph.setSourceNode(denominator);

            //calculate T.F
            graph.findForwardPaths();
            forwardPaths = graph.getForwardPaths();
            overAllGain = calcOverAllTransferFunction();

            //reset the graph
            graph.removeArrow(arrow);
            graph.removeNode(node);

        } else if (!checkIfSource(denominator) && checkIfSink(numerator)) {

            float overAllGain1 = 0;
            float overAllGain2 = 0;

            //handel denominator
            Node nodeSink = new Node ((denominator.getName() + "\'"));
            Node nodeSource = new Node ((denominator.getName() + "\'\'"));
            graph.addNode(nodeSink);
            graph.addNode(nodeSource);

            Arrow arrowSink = new Arrow(denominator, nodeSink,1);
            Arrow arrowSource = new Arrow(nodeSource, denominator,1);
            graph.addArrow(arrowSink);
            graph.addArrow(arrowSource);

            graph.setSinkNode(numerator);
            graph.setSourceNode(nodeSource);

            //calc T.F1
            graph.findForwardPaths();
            forwardPaths = graph.getForwardPaths();
            overAllGain1 = calcOverAllTransferFunction();

            graph.setSinkNode(nodeSink);
            graph.setSourceNode(nodeSource);

            //calc T.F2
            graph.findForwardPaths();
            forwardPaths = graph.getForwardPaths();
            overAllGain2 = calcOverAllTransferFunction();

            //calc over all gain equal T.F1  / T.F2
            overAllGain = overAllGain1 / overAllGain2;

            //reset graph
            graph.removeNode(nodeSink);
            graph.removeNode(nodeSource);
            graph.removeArrow(arrowSink);
            graph.removeArrow(arrowSource);

        } else {

            float overAllGain1 = 0;
            float overAllGain2 = 0;

            //handel numerator
            Node node = new Node((numerator.getName() + "'"));
            graph.addNode(node);
            Arrow arrow = new Arrow(numerator, node,1);
            graph.addArrow(arrow);

            //handel denominator
            Node nodeSink = new Node ((denominator.getName() + "\'"));
            Node nodeSource = new Node ((denominator.getName() + "\'\'"));
            graph.addNode(nodeSink);
            graph.addNode(nodeSource);

            Arrow arrowSink = new Arrow(denominator, nodeSink,1);
            Arrow arrowSource = new Arrow(nodeSource, denominator,1);
            graph.addArrow(arrowSink);
            graph.addArrow(arrowSource);

            graph.setSinkNode(node);
            graph.setSourceNode(nodeSource);

            //calc T.F1
            graph.findForwardPaths();
            forwardPaths = graph.getForwardPaths();
            overAllGain1 = calcOverAllTransferFunction();

            graph.setSinkNode(nodeSink);
            graph.setSourceNode(nodeSource);

            //calc T.F2
            graph.findForwardPaths();
            forwardPaths = graph.getForwardPaths();
            overAllGain2 = calcOverAllTransferFunction();

            //over all gain equal T.F1 / T.F2
            overAllGain = overAllGain1 / overAllGain2;

            //reset graph
            graph.removeNode(nodeSink);
            graph.removeNode(nodeSource);
            graph.removeArrow(arrowSink);
            graph.removeArrow(arrowSource);
            graph.removeArrow(arrow);
            graph.removeNode(node);
        }
        return overAllGain;
    }

        private boolean checkIfSink (Node numerator) {
        for (int i = 0 ; i < graph.getArrows().size() ; i ++) {
            if (compareTwoNodes(numerator, graph.getArrows().get(i).getStartNode())) {
                return false;
            }
        }
        return true;
    }

        private boolean checkIfSource (Node denominator) {
        for (int i = 0 ; i < graph.getArrows().size() ; i ++) {
            if (compareTwoNodes(denominator, graph.getArrows().get(i).getEndNode())) {
                return false;
            }
        }
        return true;
    }

        private float calcOverAllTransferFunction () {

            float overAllGain = 0;

            for (int i = 0 ; i < forwardPaths.size() ; i++) {
                float temp = forwardPaths.get(i).getGain() * calculateDeltaForSpecificForwardPath(forwardPaths.get(i))
                        / calculateGeneralDelta(loops, untouchedLoops);
                overAllGain = overAllGain + temp ;
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
