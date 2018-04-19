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

        ArrayList <Arrow> forwardPathArrows = forwardPath.getForwardPath();
        ArrayList <Loop> tempLoops = new ArrayList<>();
        ArrayList <UntouchedLoop> tempUnTouchedloops = new ArrayList<>();

        for (int i = 0  ; i < loops.size() ; i++) {
            ArrayList <Arrow> loopArrows = loops.get(i).getArrows();
            boolean flagAdd = true;
            for (int j = 0 ; j < loopArrows.size() ; j++) {
                boolean flagbreak = false;
                for (int k = 0; k < forwardPathArrows.size(); k++) {
                    if (compareArrows(loopArrows.get(j),forwardPathArrows.get(k))) {
                        flagbreak = true;
                        break;
                    }
                }
                if (flagbreak) {
                    flagAdd = false;
                    break;
                }
            }

            if (flagAdd)
                tempLoops.add(loops.get(i));
        }

        tempUnTouchedloops = findUnTouchedLoops(tempLoops);
        return calculateGeneralDelta(tempLoops,tempUnTouchedloops);

    }

    private ArrayList <UntouchedLoop>  findUnTouchedLoops (ArrayList <Loop> localLoops) {
        //TODO copy from Graph
        return null;
    }

    private boolean compareArrows (Arrow arrow1 , Arrow arrow2) {
        if (arrow1.getStartNode().getName().compareTo(arrow2.getStartNode().getName()) == 0)
            return true;
        else
            return false;
    }
}
