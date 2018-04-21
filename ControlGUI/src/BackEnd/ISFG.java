package BackEnd;

import java.util.ArrayList;

public interface ISFG {

    public Node addNode(String name) throws MyException;

    public Arrow addArrow(Node startNode, Node endNode, int gain) throws MyException;

    public void finish () throws MyException;

    public ArrayList<ForwardPath> getForwardPaths (Node sinkNode, Node sourceNode) throws MyException;

    public ArrayList<Loop> getLoops () throws MyException;

    public ArrayList<UntouchedLoop> getUnTouchedLoops () throws MyException;

    public float getOverAllTransferFunction (Node numerator, Node denominator) throws MyException;
}