import java.util.ArrayList;

public class Node {

    Node (String name){
        this.name = name;
    }

    private String name ;
    private ArrayList <Node> visitedNodes = new ArrayList<>();

    public void addVisitedNode (Node node) {
        visitedNodes.add(node);
    }
    public boolean visitedNodeContain (Node node) {
        return visitedNodes.contains(node);
    }
    public void removeVisitedNode (Node node) {
        visitedNodes.remove(node);
    }
    public void clearVisitedNodes () {
        visitedNodes.clear();
    }

    public String getName() {
        return name;
    }
}
