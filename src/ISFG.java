import java.util.ArrayList;

public interface ISFG {

    public void addNode(String name, String type);

    public void addArrow(Node startNode, Node endNode, int gain);

    public void finish ();

    public ArrayList<ForwardPath> getForwardPathes ();

    public ArrayList<Loop> getLoops ();

    public ArrayList<UntouchedLoop> getUnTouchedLoops ();

    public int getOverAllTransferFunction ();
}
