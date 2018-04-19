import java.util.ArrayList;

public interface ISFG {

    public void addNode(String name, String type);

    public void addArrow(Node startNode, Node endNode, int gain);

    public void finish ();

    public ArrayList<ForwardPath> getForwardPathes () throws MyException;

    public ArrayList<Loop> getLoops () throws MyException;

    public ArrayList<UntouchedLoop> getUnTouchedLoops () throws MyException;

    public int getOverAllTransferFunction () throws MyException;
}
