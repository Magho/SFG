package BackEnd;

import java.util.ArrayList;

public class Loop {

    Loop (){

    }

    Loop (Loop loop) throws MyException {
        for (int i = 0 ; i < loop.getArrows().size() ; i++) {
            this.addArrow(loop.getArrows().get(i));
        }
    }

    private ArrayList<Arrow> arrows = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();
    private int gain = 1;

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void addArrow(Arrow arrow) throws MyException {
        if (!arrows.contains(arrow)) {
            arrows.add(arrow);
            if (!nodes.contains(arrow.getStartNode())) {
                nodes.add(arrow.getStartNode());
            }
            if (!nodes.contains(arrow.getEndNode())) {
                nodes.add(arrow.getEndNode());
            }
        } else
            throw new MyException("add repeated arrow into loop");
    }

    public void removeArrow(Arrow arrow) throws MyException {
        if (arrows.contains(arrow)) {
            arrows.remove(arrow);
        } else
            throw new MyException("remove unexcited arrow from loop");

    }

    public void calculateGain (){
        for (int i = 0; i < arrows.size(); i++) {
            gain = gain * arrows.get(i).getGain();
        }
    }

    public int getGain() {
        calculateGain();
        return gain;
    }

    public void print () {
        for (int i = 0 ; i < nodes.size() ; i ++) {
            System.out.print(nodes.get(i).getName());
        }
        System.out.print(nodes.get(0).getName());
        System.out.println();
    }
}
