package BackEnd;

import java.util.ArrayList;

public interface ISFG {

    public Node addNode(String name, String type);

    public Arrow addArrow(Node startNode, Node endNode, int gain);

    public void finish();

    public ArrayList<ForwardPath> getForwardPathes() throws MyException;

    public ArrayList<Loop> getLoops() throws MyException;

    public ArrayList<UntouchedLoop> getUnTouchedLoops() throws MyException;

    public float getOverAllTransferFunction() throws MyException;
}
