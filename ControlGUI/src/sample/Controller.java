package sample;

import BackEnd.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Controller {


    public Controller() {

    }


    @FXML
    private Canvas mainCanvas;
    @FXML
    private Canvas subCanvas;
    @FXML
    private StackPane stackPane;
    @FXML
    private Canvas solution;

    private double selfLoopRadius = 13;
    private double maxHeightOfCurve = 15;
    private boolean waitForAction = false;
    private ISFG backEnd;
    private Color chosenForNodes = Color.color(.212, .120, .99, .9);
    private Color selectionColor = Color.color(.912, .720, .19, .9);
    private Color chosenForText = Color.color(.23, .24, .43, .9);
    private Color arrowColor = Color.color(.43, .123, .23, .3);
    private double chosenRadiusForNodes = 15;
    private int totalTools = 3;
    private Icon node, connection, solver;
    private HashMap<String, Icon> icons;
    private LinkedList<GNode> gNodes;
    private LinkedList<GNode> selected;
    private LinkedList<GArrow> arrows;
    private boolean inSolution = false;
    private Icon current;
    private int solutionCounter = 0;
private  boolean clearing;
private boolean firstAndLastSelected;
    public void initialize() {

        arrows = new LinkedList<>();
        selected = new LinkedList<>();
        gNodes = new LinkedList<>();
        backEnd = new SFG();
        icons = new HashMap<>();
        mainCanvas.getGraphicsContext2D().setStroke(Color.GREEN);
        mainCanvas.getGraphicsContext2D().strokeRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        System.out.println("init");
        GraphicsContext context = subCanvas.getGraphicsContext2D();
        setSubCanvasMovement();
        setSubCanvasClickHandler();
        setSolnMovement();
        setLine(context);
        setIcons(context);

    }

    private void setSubCanvasMovement() {

        subCanvas.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                subCanvas.setTranslateY(subCanvas.getHeight() / 2);
            }
        });
        subCanvas.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                subCanvas.setTranslateY(5);
            }
        });
    }

    private void setSolnMovement() {
        solution.getGraphicsContext2D().setFill(Color.grayRgb(100, .7));
        solution.getGraphicsContext2D().fillRect(0, 0, solution.getWidth(), solution.getHeight());
        solution.setOnMouseEntered(e -> {
            solution.setTranslateX(solution.getWidth() / 2);
        });
        solution.setOnMouseExited(e -> {
            solution.setTranslateX(10 - solution.getWidth() / 2);
        });
        solution.setTranslateX(-solution.getWidth() / 2);


    }

    private void setLine(GraphicsContext context) {
        context.setFill(Color.rgb(138, 183, 23, .25));
        context.fillPolygon(new double[]{0, subCanvas.getWidth() / 16, subCanvas.getWidth() - subCanvas.getWidth() / 16, subCanvas.getWidth()}
                , new double[]{0, subCanvas.getHeight() * 3 / 4, subCanvas.getHeight() * 3 / 4, 0}, 4);

    }

    private void setIcons(GraphicsContext context) {
        Image connection = new Image(getClass().getResourceAsStream("connection.png"));
        Image node = new Image(getClass().getResourceAsStream("node.png"));
        Image solver = new Image(getClass().getResourceAsStream("solver.png"));
        Icon inode = new Icon(node, (subCanvas.getWidth() / (2 * totalTools)) - 15, 20, 30, 30, e -> {
            eventOfiNode(e);
        });
        Icon iconnection = new Icon(connection, (3 * subCanvas.getWidth() / (2 * totalTools)) - 15, 20, 30, 30, e -> {
            eventOfiConnection(e);

        });
        Icon isolver = new Icon(solver, (5 * subCanvas.getWidth() / (2 * totalTools)) - 15, 20, 30, 30, e -> {
            eventOfiSolver(e);
        });

        setIconToContext(context, iconnection, "connection");
        setIconToContext(context, inode, "node");
        setIconToContext(context, isolver, "solver");
    }

    private void setIconToContext(GraphicsContext context, Icon i, String name) {
        context.setStroke(Color.BLACK);
        context.setTextAlign(TextAlignment.CENTER);
        context.strokeText(name, i.getX() + i.getW() / 2, i.getY() - 5);
        context.drawImage(i.getImage(), i.getX(), i.getY(), i.getH(), i.getW());
        icons.put(name, i);
        setSquareOnIcon(i);

    }
private void setSquareOnIcon(Icon current){
    subCanvas.getGraphicsContext2D().setStroke(Color.grayRgb(100, .7));
    subCanvas.getGraphicsContext2D().strokeRect(current.getX(), current.getY(), current.getW(), current.getH());
}
    private void setSubCanvasClickHandler() {
        subCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!waitForAction) {
                    for (Map.Entry<String, Icon> entry : icons.entrySet()) {
                        if (entry.getValue().isInBoundaries(event.getX(), event.getY())) {
                            if (entry.getKey() == "solver") {
                                inSolution = true;
                                solution.setTranslateX(10 - solution.getWidth() / 2);
                                try {
                                    writeSolutionMessage("choose 2 nodes");

                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }

                            } else {
                                if(clearing){

                                    arrows = new LinkedList<>();
                                    selected = new LinkedList<>();
                                    gNodes = new LinkedList<>();
                                    backEnd = new SFG();
                                 mainCanvas.getGraphicsContext2D().clearRect(0,0,mainCanvas.getWidth(),mainCanvas.getHeight());
                                 writeSolutionMessage("");
                                 firstAndLastSelected=false;
                                 clearing=false;
                                }
                                solution.setTranslateX(-solution.getWidth() / 2);
                                inSolution = false;
                            }
                            if (current != null) {
                                subCanvas.getGraphicsContext2D().setStroke(Color.grayRgb(100, .7));
                                subCanvas.getGraphicsContext2D().strokeRect(current.getX(), current.getY(), current.getW(), current.getH());
                            }
                            current = entry.getValue();
                            subCanvas.getGraphicsContext2D().setStroke(selectionColor);
                            subCanvas.getGraphicsContext2D().strokeRect(entry.getValue().getX(), entry.getValue().getY(), entry.getValue().getW(), entry.getValue().getH());
                            mainCanvas.setOnMouseClicked(entry.getValue().getBehaviourOnMainCanvas());
                            break;
                        }
                    }
                }
            }
        });
    }

    private void eventOfiSolver(MouseEvent e) {
        if (((waitForAction && selected.size() == 0) || selected.size() == 2)&&!firstAndLastSelected) {
            return;
        }
        for (int i = 0; i < gNodes.size(); i++) {
            if (gNodes.get(i).isInBoundaries(e.getX(), e.getY())) {
                gNodes.get(i).setColor(selectionColor);
                drawGNode(gNodes.get(i), mainCanvas.getGraphicsContext2D());
                selected.add(gNodes.get(i));
                waitForAction = true;
                break;
            }
        }
        if (selected.size() == 2) {
            firstAndLastSelected=true;
            //TODO try {
            showSolution();
            for (int i = 0; i < selected.size(); i++) {
                selected.get(i).setColor(chosenForNodes);
                drawGNode(selected.get(i), mainCanvas.getGraphicsContext2D());
            }
            selected = new LinkedList<>();
            waitForAction = false;

        }

    }

    private void writeSolutionMessage(String s) {
        StringBuilder lenChecker = new StringBuilder(s);
        int counter=0;
        for(int i=0;i<lenChecker.length();i++){
            counter++;
            if(lenChecker.charAt(i)=='\n'){
                counter=0;
            }else if(counter==25){
                lenChecker.insert(i,'\n');
                counter=0;
            }
        }
        s=lenChecker.toString();
        solution.getGraphicsContext2D().clearRect(0, 0, solution.getWidth(), solution.getHeight());
        solution.getGraphicsContext2D().setFill(Color.grayRgb(100, .7));
        solution.getGraphicsContext2D().fillRect(0, 0, solution.getWidth(), solution.getHeight());
        solution.getGraphicsContext2D().setStroke(Color.LIGHTGREEN);
        double h = solution.getHeight();
        solution.getGraphicsContext2D().setTextAlign(TextAlignment.CENTER);
        solution.getGraphicsContext2D().strokeText(s, solution.getWidth() / 2, 20);
    }
    private void writeDownSolutionMessage(String s){
        StringBuilder lenChecker = new StringBuilder(s);
        int counter=0;
        for(int i=0;i<lenChecker.length();i++){
            counter++;
            if(lenChecker.charAt(i)=='\n'){
                counter=0;
            }else if(counter==25){
                lenChecker.insert(i,'\n');
                counter=0;
            }
        }
        s= lenChecker.toString();
        solution.getGraphicsContext2D().setStroke(Color.ORANGE);
        solution.getGraphicsContext2D().strokeText(s,solution.getWidth()/2,solution.getHeight()-40);
    }
    private void showSolution() {
        try {
            clearBackEnd();
            backEnd.finish();
            ArrayList<ForwardPath> forwardPaths = backEnd.getForwardPaths(selected.get(0).getNode(), selected.get(1).getNode());
            ArrayList<Loop> loops = backEnd.getLoops();
            ArrayList<ArrayList<Loop>> untouchedLoops = backEnd.getUnTouchedLoops();
            float tf = backEnd.getOverAllTransferFunction(selected.get(0).getNode(), selected.get(1).getNode());
            printForwardPathes(forwardPaths);
            writeDownSolutionMessage("CLICK ANY WHERE TO SHOW LOOPS");
            solutionCounter++;
            solution.setOnMouseClicked(e -> {
                switch (solutionCounter % 4) {
                    case 0:
                        printForwardPathes(forwardPaths);
                        writeDownSolutionMessage("CLICK ANY WHERE TO SHOW LOOPS");
                        break;
                    case 1:
                        printLoops(loops);
                        writeDownSolutionMessage("CLICK ANY WHERE TO SHOW UNTOUCHED LOOPS");

                        break;
                    case 2:
                        printUnTouchedLoops(untouchedLoops);
                        writeDownSolutionMessage("CLICK ANY WHERE TO SHOW TRANSFER FUNCTION");

                        break;
                    case 3:
                        printTF(tf);
                        writeDownSolutionMessage("CLICK ANY WHERE TO SHOW FORWARD PATHES");

                        break;
                    default:
                        writeSolutionMessage("Error functions not found");
                        break;
                }
                solutionCounter++;
            });


        } catch (Exception ee) {
            //throw new RuntimeException(ee);
            writeSolutionMessage("Error");
        }
    }

    private void clearBackEnd() {
        clearing=true;
    }

    private void printForwardPathes(ArrayList<ForwardPath> forwardPaths) {
        StringBuilder pathes = new StringBuilder();
        pathes.append("\"FORWARD PATHES\"\n\n");
        for (int i = 0; i < forwardPaths.size(); i++) {
            ArrayList<Arrow> arrows = forwardPaths.get(i).getArrows();
            StringBuilder path = new StringBuilder();
            for (int j = 0; j < arrows.size(); j++) {
                path.append(arrows.get(j).getStartNode().getName() + (j == arrows.size() - 1 ? ("->" + arrows.get(j).getEndNode().getName()) : "->"));
            }
            System.out.println();
            pathes.append(String.valueOf(i + 1) + ":" + path + "\n");
        }
        writeSolutionMessage(pathes.toString());

    }

    private void printLoops(ArrayList<Loop> loops) {
        StringBuilder loopsStr = new StringBuilder();
        loopsStr.append("\"LOOPS\"\n\n");
        for (int i = 0; i < loops.size(); i++) {
            ArrayList<Arrow> arrows = loops.get(i).getArrows();
            StringBuilder path = new StringBuilder();
            for (int j = 0; j < arrows.size(); j++) {
                path.append(arrows.get(j).getStartNode().getName() + (j == arrows.size() - 1 ? ("->" + arrows.get(j).getEndNode().getName()) : "->"));
            }
            loopsStr.append(i + ":" + path + "\n");
        }
        writeSolutionMessage(loopsStr.toString());

    }

    private String getLoopStr(Loop l) {
        ArrayList<Arrow> arrows = l.getArrows();
        StringBuilder path = new StringBuilder();
        for (int j = 0; j < arrows.size(); j++) {
            path.append(arrows.get(j).getStartNode().getName() + (j == arrows.size() - 1 ? ("->" + arrows.get(j).getEndNode().getName()) : "->"));
        }
        return path.toString();
    }

    private void printUnTouchedLoops(ArrayList<ArrayList<Loop>> untouchedLoops) {
        StringBuilder uTLS = new StringBuilder();
        uTLS.append("\"UNTOUCHED LOOPS\" \n\n");
        for (int i = 0; i < untouchedLoops.size(); i++) {
            for (int j = 0; j < untouchedLoops.get(i).size(); j++) {
                uTLS.append(getLoopStr(untouchedLoops.get(i).get(j)) + "\n");
            }
            uTLS.append("\n------------\n");

        }
        writeSolutionMessage(uTLS.toString());

    }

    private void printTF(float tf) {

        writeSolutionMessage("\"TRANSFERE FUNCTION\"\n\n" + String.valueOf(tf));
    }

    private void eventOfiConnection(MouseEvent e) {
        if ((waitForAction && selected.size() == 0) || selected.size() == 2) {
            return;
        }
        for (int i = 0; i < gNodes.size(); i++) {
            if (gNodes.get(i).isInBoundaries(e.getX(), e.getY())) {
                gNodes.get(i).setColor(selectionColor);
                drawGNode(gNodes.get(i), mainCanvas.getGraphicsContext2D());
                selected.add(gNodes.get(i));
                waitForAction = true;
                break;
            }
        }
        if (selected.size() == 2) {
            for (int i = 0; i < arrows.size(); i++) {
                if (selected.get(0).getNode() == arrows.get(i).getFirst().getNode() && selected.get(1).getNode() == arrows.get(i).getSecond().getNode()) {
                    for (int j = 0; j < selected.size(); j++) {
                        selected.get(j).setColor(chosenForNodes);
                        drawGNode(selected.get(j), mainCanvas.getGraphicsContext2D());
                    }
                    selected = new LinkedList<>();
                    waitForAction = false;
                    return;
                }
            }
            double midX, midY;
            GArrow gArrow = new GArrow();

            if (selected.get(1) == selected.get(0)) {
                double r = selfLoopRadius;
                midX = selected.get(0).getX();
                midY = selected.get(0).getY() - 2 * r - 10;
                gArrow.setMidx(selected.get(0).getX());
                gArrow.setMidy(selected.get(0).getY() - 2 * r);
                gArrow.setFirst(selected.get(0));
                gArrow.setSecond(selected.get(1));
                gArrow.setCurveHeight(0);
                mainCanvas.getGraphicsContext2D().strokeArc(selected.get(0).getX() - r, selected.get(0).getY() - 2 * r, 2 * r, 2 * r, 0, 360, ArcType.OPEN);
                drawArrow(gArrow, selected.get(0).getX()+r , selected.get(0).getY()-2*r-3, mainCanvas.getGraphicsContext2D());
            } else {
                midX = selected.get(0).getX() + ((selected.get(1).getX() - selected.get(0).getX()) / 2);
                midY = selected.get(0).getY() + ((selected.get(1).getY() - selected.get(0).getY()) / 2);

                boolean isCurve = false;
                isCurve = checkIfCurve();

                if (!isCurve) {
                    mainCanvas.getGraphicsContext2D().strokeLine(selected.get(0).getX(), selected.get(0).getY(), selected.get(1).getX(), selected.get(1).getY());
                    gArrow.setMidx(midX);
                    gArrow.setMidy(midY);
                    gArrow.setFirst(selected.get(0));
                    gArrow.setSecond(selected.get(1));
                    gArrow.setCurveHeight(0);
                    drawArrow(gArrow, selected.get(1).getX(), selected.get(1).getY(), mainCanvas.getGraphicsContext2D());

                } else {
                    PosXY arr = drawCurve(selected.get(0).getX(), selected.get(0).getY(), selected.get(1).getX(), selected.get(1).getY(), midX, midY, mainCanvas.getGraphicsContext2D());

                    gArrow.setMidx(arr.getX());
                    gArrow.setMidy(arr.getY());
                    gArrow.setFirst(selected.get(0));
                    gArrow.setSecond(selected.get(1));
                    gArrow.setCurveHeight((float) maxHeightOfCurve);
                    drawArrow(gArrow, selected.get(1).getX(), selected.get(1).getY() - maxHeightOfCurve, mainCanvas.getGraphicsContext2D());

                }
            }
            TextField textField = new TextField();
            textField.setMinWidth(100);
            textField.setTranslateX(gArrow.getMidx());
            textField.setTranslateY(gArrow.getMidy() - 25);
            textField.setAlignment(Pos.CENTER);
            textField.setOnKeyPressed(e1 -> {
                if (e1.getCode().isWhitespaceKey()) {
                    if (validGain(textField.getText())) {
                        try {

                            gArrow.setArrow(backEnd.addArrow(selected.get(0).node, selected.get(1).node, Integer.valueOf(textField.getText())));
                        } catch (Exception exe) {
                            //TODO
                        }
                        stackPane.getChildren().remove(textField);
                        System.out.println(gArrow.getSlope()+"--------------");
                        mainCanvas.getGraphicsContext2D().strokeText(textField.getText(), gArrow.getMidx()+Math.cos(90-gArrow.getSlope()%90)*20, gArrow.getMidy() + Math.sin(90-gArrow.getSlope()%90)*20);
                        arrows.add(gArrow);
                        for (int i = 0; i < selected.size(); i++) {
                            selected.get(i).setColor(chosenForNodes);
                            drawGNode(selected.get(i), mainCanvas.getGraphicsContext2D());
                        }
                        selected = new LinkedList<>();
                        waitForAction = false;
                    } else {
                        textField.setText("invalid");
                    }
                }
            });
            stackPane.getChildren().add(textField);
        }
    }

    private boolean checkIfCurve() {
        int counter = 0;

        for (int i = 0; i < gNodes.size(); i++) {
            double xi, xs1, xs2;
            xi = gNodes.get(i).getX();
            xs1 = selected.get(0).getX();
            xs2 = selected.get(1).getX();
            double y1 = selected.get(0).getY();
            double y2 = selected.get(1).getY();
            if (((xi > xs1 && xi < xs2) || (xi < xs1 && xi > xs2)) && Math.abs(gNodes.get(i).getY() - (y1 + y2) / 2) < maxHeightOfCurve + 10) {
                counter++;
            }

        }
        for (int i = 0; i < arrows.size(); i++) {
            if (arrows.get(i).getFirst() == selected.get(1) && arrows.get(i).getSecond() == selected.get(0)) {
                maxHeightOfCurve = arrows.get(i).getCurveHeight() + counter * 10 + 10;
                if (selected.get(0).getX() > selected.get(1).getX()) {
                    maxHeightOfCurve += 45;
                }
                return true;
            }
        }
        if (counter > 0) {
            maxHeightOfCurve = counter * 25;
            if (selected.get(0).getX() > selected.get(1).getX()) {
                maxHeightOfCurve += 20;
            }
            return true;
        }
        return false;
    }

    private boolean validGain(String text) {
        try {
            Integer.valueOf(text);
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    private void drawArrow(GArrow gArrow, double dirx, double diry, GraphicsContext gc) {
        gc.setFill(arrowColor);
        double dy = gArrow.getMidy() - diry;
        double dx = gArrow.getMidx() - dirx;
        double theta;

        theta = Math.atan(Math.abs(dy) / Math.abs(dx));
        theta = Math.toDegrees(theta);

        if (dy < 0 && dx < 0) {
            theta = 180 - theta;

        } else if (dx > 0 && dy > 0) {
            theta = 360 - theta;

        } else if (dy < 0 && dx > 0) {
            theta = theta;

        } else if (dx < 0 && dy > 0) {
            theta = 180 + theta;

        }
        dy = gArrow.getFirst().getY() - diry;
        dx = gArrow.getFirst().getX() - dirx;
        if (dx == 0) {
            theta = (dy > 0 ? 270 : 90);
        } else if (dy == 0) {
            theta = (dx > 0 ? 0 : 180);
        }
        gArrow.setSlope(theta);
        System.out.println(theta + "  ee");

        gc.setFill(chosenForText);
        gc.fillArc(gArrow.getMidx() - 10, gArrow.getMidy() - 10, 20, 20, theta - 20, 40, ArcType.ROUND);
    }

    private PosXY drawCurve(double x1, double y1, double x2, double y2, double xm, double ym, GraphicsContext context) {
        double maxtemp = maxHeightOfCurve;
        double cX, cY;
        double lineLen = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
        System.out.println("len" + lineLen);
        double slope = Math.toDegrees(Math.atan(Math.abs(y2 - y1) / Math.abs(x2 - x1)));
        double kklkl = (y2 - y1) * (x2 - x1);
        if (kklkl > 0) {
            slope = -slope;
            System.out.println(slope + "      <--------------------" + kklkl);
        }
        System.out.println("slop1             " + slope);
        double r = (maxHeightOfCurve * maxHeightOfCurve + lineLen * lineLen / 4) / (2 * maxHeightOfCurve);
        if (maxHeightOfCurve > r) {
            maxHeightOfCurve = lineLen / 4;
        }
        System.out.println("r        " + r);
        double slope2 = (90 - slope);
        if (kklkl > 0) {
            slope2 = 90 + slope;
            System.out.println(slope2 + "      <--------------------" + kklkl);
        }
        System.out.println("slop2    " + slope2);
        cY = ym + (r - maxHeightOfCurve) * Math.sin(Math.toRadians(slope2));
        cX = xm + (r - maxHeightOfCurve) * Math.cos(Math.toRadians(slope2));
        if (kklkl > 0) {
            cY = ym + (r - maxHeightOfCurve) * Math.sin(Math.toRadians(slope2));
            cX = xm - (r - maxHeightOfCurve) * Math.cos(Math.toRadians(slope2));
        }

        System.out.println(cX + " - - ----  -- " + cY);
        double ss = slope + Math.toDegrees(Math.atan(Math.abs(r - maxHeightOfCurve) / (lineLen / 2)));
        context.strokeArc(cX - r, cY - r, 2 * r, 2 * r, ss, 2 * (90 - ss + slope), ArcType.OPEN);

        double arrX, arrY, term, dD;
        if (slope2 == 90) {
            return new PosXY(xm, ym - maxHeightOfCurve)
                    ;
        }
        term = 2 * (-ym + cY);
        dD = (cY - ym) / (cX - xm);
        arrX = (-cX * cX + xm * xm - cY * cY + ym * ym - term * dD * cX + term * cY - maxHeightOfCurve * maxHeightOfCurve + r * r) / (2 * (-cX + xm) - term * dD);
        arrY = dD * (arrX - cX) + cY;

        maxHeightOfCurve = maxtemp;
        return new PosXY(arrX, arrY);
    }

    private void eventOfiNode(MouseEvent e) {
        if (waitForAction) {
            return;
        }
        waitForAction = true;//waiting to enter name
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        if (!validNodePos(e.getX(), e.getY())) {
            waitForAction = false;
            return;
        }
        GNode node1 = new GNode(chosenForNodes, e.getX(), e.getY(), chosenRadiusForNodes, new Node("temp"));
        drawGNode(node1, gc);
        TextField textField = new TextField();
        textField.setMinWidth(100);
        textField.setTranslateX(node1.getX());
        textField.setTranslateY(node1.getY() +
                (node1.getY() + node1.getR() > mainCanvas.getHeight() ? (-node1.getR() - 5) : (5 + node1.getR())));
        textField.setAlignment(Pos.CENTER);

        textField.setOnKeyPressed(e1 -> {
            if (e1.getCode().isWhitespaceKey()) {
                if (validNodeName(textField.getText())) {
                    waitForAction = false;
                    try {
                        Node bNode = new Node(textField.getText().trim());
                        backEnd.addNode(bNode);
                        node1.setNode(bNode);
                    } catch (Exception exe) {
                        //TODO
                    }
                    stackPane.getChildren().remove(textField);
                    writeText(node1, gc);
                    gNodes.add(node1);
                } else {
                    textField.setText("repeated");
                }
            }
        });
        stackPane.getChildren().add(textField);
        System.out.println("clicked from big behave node");
    }

    private void drawGNode(GNode n, GraphicsContext gc) {
        gc.setFill(n.getColor());
        gc.fillArc(n.getX() - n.getR() / 2, n.getY() - n.getR() / 2, n.getR(), n.getR(), 0, 360, ArcType.ROUND);

    }

    private void writeText(GNode n, GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setStroke(chosenForText);
        gc.strokeText(n.node.getName()
                , n.getX()
                , n.getY() +
                        (n.getY() + n.getR() > mainCanvas.getHeight() ? (-n.getR() - 5) : (5 + n.getR()))
        );
    }

    private boolean validNodePos(double x, double y) {
        for (int i = 0; i < gNodes.size(); i++) {
            if (gNodes.get(i).isInFarBoundaries(x, y)) {
                return false;
            }
        }
        return true;
    }

    private boolean validNodeName(String text) {
        for (int i = 0; i < gNodes.size(); i++) {
            System.out.println(gNodes.get(i).node.getName() + "  " + text);
            if (text.trim().compareToIgnoreCase(gNodes.get(i).node.getName()) == 0) {
                System.out.println("faaaaaaaaaaaalse");
                return false;
            }
        }
        return true;
    }

}
