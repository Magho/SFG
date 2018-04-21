import java.util.ArrayList;

public interface ISFG {

    public void addNode(String name, String type) throws MyException;

    public void addArrow(Node startNode, Node endNode, int gain) throws MyException;

    public void finish ();

    public ArrayList<ForwardPath> getForwardPaths () throws MyException;

    public ArrayList<Loop> getLoops () throws MyException;

    public ArrayList<UntouchedLoop> getUnTouchedLoops () throws MyException;

    public float getOverAllTransferFunction () throws MyException;
}
