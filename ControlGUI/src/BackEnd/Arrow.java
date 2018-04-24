package BackEnd;

public class Arrow {

    Arrow (Node startNode, Node endNode, int gain){

        this.startNode = startNode;
        this.endNode = endNode;
        this.gain = gain;
    }

    private Node startNode;
    private Node endNode;
    private int gain;

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public int getGain() {
        return gain;
    }
    public void setGain(int gain) {
        this.gain = gain;
    }
}
