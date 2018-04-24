package BackEnd;
import java.util.ArrayList;

public interface ISFG {

    public void addNode(Node node) throws MyException;

    public Arrow addArrow(Node startNode, Node endNode, int gain) throws MyException;

    public void finish () throws MyException;

    public ArrayList<ForwardPath> getForwardPaths (Node sourceNode, Node sinkNode) throws MyException;

    public ArrayList<Loop> getLoops () throws MyException;

    public ArrayList <ArrayList <Loop> >  getUnTouchedLoops () throws MyException;

    public float getOverAllTransferFunction (Node numerator, Node denominator) throws MyException;
}
