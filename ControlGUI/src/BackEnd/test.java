package BackEnd;

import java.util.ArrayList;

public class test {

    public static void main(String[] args) throws MyException {

        SFG sfg = new SFG();


//test 1
        Node node1 = new Node ("1");
        Node node2 = new Node ("2");
        Node node3 = new Node ("3");
        Node node4 = new Node ("4");
        Node node5 = new Node ("5");
        Node node6 = new Node ("6");
        Node node7 = new Node ("7");
        Node node8 = new Node ("8");


        sfg.addNode(node1);
        sfg.addNode(node2);
        sfg.addNode(node3);
        sfg.addNode(node4);
        sfg.addNode(node5);
        sfg.addNode(node6);
        sfg.addNode(node7);
        sfg.addNode(node8);

        sfg.addArrow(node1,node2,1);
        sfg.addArrow(node2,node3,1);
        sfg.addArrow(node3,node4,1);
        sfg.addArrow(node4,node5,1);
        sfg.addArrow(node5,node6,1);
        sfg.addArrow(node6,node7,1);
        sfg.addArrow(node7,node8,1);
        sfg.addArrow(node6,node5,1);
        sfg.addArrow(node4,node7,1);
        sfg.addArrow(node8,node6,1);
        sfg.addArrow(node6,node8,1);
        sfg.addArrow(node7,node3,1);
        sfg.addArrow(node8,node2,1);

        sfg.finish();


        //test getLoops
        ArrayList <Loop> loops = sfg.getLoops();

        System.out.println("loops");
        System.out.println();
        for (int i = 0 ; i < loops.size() ; i++ ) {
            loops.get(i).print();
            System.out.println();
        }


        //test get unTouchedloops
        ArrayList <ArrayList <Loop> > AllUnTouchedLoops = sfg.getUnTouchedLoops();

        System.out.println("untouched loops");
        Graph graph = new Graph();
        graph.print(AllUnTouchedLoops);


        //test get forwardPaths
        ArrayList <ForwardPath> forwardPaths = sfg.getForwardPaths(node1, node8);

        System.out.println("forwardPaths");
        System.out.println();
        for (int i = 0 ; i < forwardPaths.size() ; i++ ) {
            forwardPaths.get(i).print();
            System.out.println();
        }


        // test getOverAllTransferFunction
        System.out.println(sfg.getOverAllTransferFunction(node1,node8));


//test 2
/*
        Node node1 = new Node ("1");
        Node node2 = new Node ("2");
        Node node3 = new Node ("3");
        Node node4 = new Node ("4");
        Node node5 = new Node ("5");
        Node node6 = new Node ("6");
        Node node7 = new Node ("7");
        Node node8 = new Node ("8");
        Node node9 = new Node ("9");
        Node node10 = new Node ("10");


        sfg.addNode(node1);
        sfg.addNode(node2);
        sfg.addNode(node3);
        sfg.addNode(node4);
        sfg.addNode(node5);
        sfg.addNode(node6);
        sfg.addNode(node7);
        sfg.addNode(node8);
        sfg.addNode(node9);
        sfg.addNode(node10);

        sfg.addArrow(node1,node2,1);
        sfg.addArrow(node2,node3,1);
        sfg.addArrow(node3,node4,1);
        sfg.addArrow(node4,node5,1);
        sfg.addArrow(node5,node6,1);
        sfg.addArrow(node6,node7,1);
        sfg.addArrow(node2,node8,1);
        sfg.addArrow(node8,node9,1);
        sfg.addArrow(node9,node10,1);
        sfg.addArrow(node10,node6,1);
        sfg.addArrow(node9,node8,1);
        sfg.addArrow(node10,node9,1);
        sfg.addArrow(node4,node3,1);
        sfg.addArrow(node5,node4,1);

        sfg.finish();


        //test getLoops
        ArrayList <Loop> loops = sfg.getLoops();

        System.out.println("loops");
        System.out.println();
        for (int i = 0 ; i < loops.size() ; i++ ) {
            loops.get(i).print();
            System.out.println();
        }


        //test get unTouchedloops
        ArrayList <ArrayList <Loop> > AllUnTouchedLoops = sfg.getUnTouchedLoops();

        System.out.println("untouched loops");
        Graph graph = new Graph();
        graph.print(AllUnTouchedLoops);


        //test get forwardPaths
        ArrayList <ForwardPath> forwardPaths = sfg.getForwardPaths(node1, node7);

        System.out.println("forwardPaths");
        System.out.println();
        for (int i = 0 ; i < forwardPaths.size() ; i++ ) {
            forwardPaths.get(i).print();
            System.out.println();
        }


        // test getOverAllTransferFunction
        System.out.println(sfg.getOverAllTransferFunction(node1,node7));
*/

        // test 3
/*
        Node node1 = new Node ("1");
        Node node2 = new Node ("2");
        Node node3 = new Node ("3");
        Node node4 = new Node ("4");
        Node node5 = new Node ("5");
        Node node6 = new Node ("6");
        Node node7 = new Node ("7");
        Node node8 = new Node ("8");


        sfg.addNode(node1);
        sfg.addNode(node2);
        sfg.addNode(node3);
        sfg.addNode(node4);
        sfg.addNode(node5);
        sfg.addNode(node6);
        sfg.addNode(node7);
        sfg.addNode(node8);

        sfg.addArrow(node1,node2,1);
        sfg.addArrow(node2,node3,1);
        sfg.addArrow(node3,node4,1);
        sfg.addArrow(node4,node5,1);
        sfg.addArrow(node5,node6,1);
        sfg.addArrow(node6,node7,1);
        sfg.addArrow(node7,node8,1);
        sfg.addArrow(node6,node3,1);
        sfg.addArrow(node8,node2,1);
        //sfg.addArrow(node8,node5,1);
        sfg.addArrow(node5,node8,1);

        sfg.finish();


        //test getLoops
        ArrayList <Loop> loops = sfg.getLoops();

        System.out.println("loops");
        System.out.println();
        for (int i = 0 ; i < loops.size() ; i++ ) {
            loops.get(i).print();
            System.out.println();
        }


        //test get unTouchedloops
        ArrayList <ArrayList <Loop> > AllUnTouchedLoops = sfg.getUnTouchedLoops();

        System.out.println("untouched loops");
        Graph graph = new Graph();
        graph.print(AllUnTouchedLoops);


        //test get forwardPaths
        ArrayList <ForwardPath> forwardPaths = sfg.getForwardPaths(node1, node8);

        System.out.println("forwardPaths");
        System.out.println();
        for (int i = 0 ; i < forwardPaths.size() ; i++ ) {
            forwardPaths.get(i).print();
            System.out.println();
        }


        // test getOverAllTransferFunction
        System.out.println(sfg.getOverAllTransferFunction(node1,node8));

*/
    }
}
