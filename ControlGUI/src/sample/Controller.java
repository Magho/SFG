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

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Controller {
    @FXML
    private Canvas mainCanvas;
    @FXML
    private Canvas subCanvas;
    @FXML
    private StackPane stackPane;
    @FXML
    private Canvas solution;
    private double maxHeightOfCurve = 60;
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
        Icon inode = new Icon(node, (subCanvas.getWidth() / (2 * totalTools)) - 25, 10, 50, 50, e -> {
            eventOfiNode(e);
        });
        Icon iconnection = new Icon(connection, (3 * subCanvas.getWidth() / (2 * totalTools)) - 25, 10, 50, 50, e -> {
            eventOfiConnection(e);

        });
        Icon isolver = new Icon(solver, (5 * subCanvas.getWidth() / (2 * totalTools)) - 25, 10, 50, 50, e -> {
            eventOfiSolver(e);
        });

        setIconToContext(context, iconnection, "connection");
        setIconToContext(context, inode, "node");
        setIconToContext(context, isolver, "solver");
    }

    private void setIconToContext(GraphicsContext context, Icon i, String name) {
        context.drawImage(i.getImage(), i.getX(), i.getY(), i.getH(), i.getW());
        icons.put(name, i);
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
                                    solution.getGraphicsContext2D().strokeText("choose 2 nodes", solution.getWidth() / 2, solution.getHeight() / 2);

                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }

                            } else {
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
            showSolution();

            for (int i = 0; i < selected.size(); i++) {
                selected.get(i).setColor(chosenForNodes);
                drawGNode(selected.get(i), mainCanvas.getGraphicsContext2D());
            }
            selected = new LinkedList<>();
            waitForAction = false;

        }

    }

    private void showSolution() {
        try {
            System.out.println(selected.get(0).getNode().getName() + "   " + selected.get(1).getNode().getName());
            backEnd.finish();
            ArrayList<ForwardPath> forwardPaths = backEnd.getForwardPaths(selected.get(0).getNode(), selected.get(1).getNode());
            ArrayList<Loop> loops = backEnd.getLoops();
            ArrayList<UntouchedLoop> untouchedLoops = backEnd.getUnTouchedLoops();
            float tf = backEnd.getOverAllTransferFunction(selected.get(0).getNode(), selected.get(1).getNode());

            solution.getGraphicsContext2D().clearRect(0, 0, solution.getWidth(), solution.getHeight());
            solution.getGraphicsContext2D().setFill(Color.grayRgb(100, .7));
            solution.getGraphicsContext2D().fillRect(0, 0, solution.getWidth(), solution.getHeight());
            solution.getGraphicsContext2D().setStroke(Color.RED);
            double h = solution.getHeight();
            solution.getGraphicsContext2D().setTextAlign(TextAlignment.CENTER);
            printForwardPathes(forwardPaths);
            printLoops(loops);
            printUnTouchedLoops(untouchedLoops);
            printTF(tf);

        } catch (Exception ee) {
            throw new RuntimeException(ee);
            // System.out.println(ee.toString()+"\n"+ee.fillInStackTrace());
            //TODO
        }
    }

    private void printForwardPathes(ArrayList<ForwardPath> forwardPaths) {
        solution.getGraphicsContext2D().strokeText("Forward Pathes", solution.getWidth() / 2, 20);
        for (int i = 0; i < forwardPaths.size(); i++) {
            ArrayList<Arrow> arrows = forwardPaths.get(i).getArrows();
            StringBuilder path = new StringBuilder();
            for (int j = 0; j < arrows.size(); j++) {
                System.out.print(arrows.get(i).getStartNode().getName()+"->");
                path.append(arrows.get(j).getStartNode().getName() + (j == arrows.size() ? ("->"+arrows.get(j).getEndNode().getName()) : "->"));

            }
            System.out.println();

            solution.getGraphicsContext2D().strokeText(path.toString(), solution.getWidth() / 2, 40 + i * 150 / forwardPaths.size());
        }


    }
private void printLoops(ArrayList<Loop> loops){

    solution.getGraphicsContext2D().strokeText("Loops", solution.getWidth() / 2, 200);
    for (int i = 0; i < loops.size(); i++) {
        ArrayList<Arrow> arrows = loops.get(i).getArrows();
        StringBuilder path = new StringBuilder();
        for (int j = 0; j < arrows.size(); j++) {
            System.out.print(arrows.get(j).getStartNode().getName()+"->");
            path.append(arrows.get(j).getStartNode().getName() + (j == arrows.size() ? ("->"+arrows.get(j).getEndNode().getName()) : "->"));
        }
        solution.getGraphicsContext2D().strokeText(path.toString(), solution.getWidth() / 2, 220 + i * 150 / loops.size());
        System.out.println();
    }

}
private void printUnTouchedLoops(ArrayList<UntouchedLoop> untouchedLoops){

    solution.getGraphicsContext2D().strokeText("UnTouched loops", solution.getWidth() / 2, 370);
    for (int i = 0; i < untouchedLoops.size(); i++) {
        Loop l1 = untouchedLoops.get(i).getLoop1();
        ArrayList<Arrow> arrows = l1.getArrows();
        StringBuilder path = new StringBuilder();
        for (int j = 0; j < arrows.size(); j++) {
            path.append(arrows.get(j).getStartNode().getName() + (j == arrows.size() ? ("->"+arrows.get(j).getEndNode().getName()) : "->"));
        }
        path.append("||");
        solution.getGraphicsContext2D().strokeText(path.toString(), solution.getWidth() / 2, 380 + (i / 2) * 100 / untouchedLoops.size());

        Loop l2 = untouchedLoops.get(i).getLoop2();
        arrows = l2.getArrows();
        path = new StringBuilder();

        for (int j = 0; j < arrows.size(); j++) {
            path.append(arrows.get(j).getStartNode().getName() + (j == arrows.size() ? ("->"+arrows.get(j).getEndNode().getName()) : "->"));
        }
        path.append("\n---------------------");
        solution.getGraphicsContext2D().strokeText(path.toString(), solution.getWidth() / 2, 380 + (i / 2) * 100 / untouchedLoops.size());

    }

}
private void printTF(float tf){
    solution.getGraphicsContext2D().strokeText("OverAll T.F", solution.getWidth() / 2, 490);
    solution.getGraphicsContext2D().strokeText(String.valueOf(tf), solution.getWidth() / 2, 400);
    System.out.println(tf);

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
            double midX, midY;
            GArrow gArrow = new GArrow();

            if (selected.get(1) == selected.get(0)) {
                double r = 40;
                midX = selected.get(0).getX();
                midY = selected.get(0).getY() - 2 * r - 10;
                gArrow.setMidx(selected.get(0).getX());
                gArrow.setMidy(selected.get(0).getY() - 2 * r);
                mainCanvas.getGraphicsContext2D().strokeArc(selected.get(0).getX() - r, selected.get(0).getY() - 2 * r, 2 * r, 2 * r, 0, 360, ArcType.OPEN);
                drawArrow(gArrow, gArrow.getMidx() + 10, gArrow.getMidy(), mainCanvas.getGraphicsContext2D());
            } else {
                midX = selected.get(0).getX() + ((selected.get(1).getX() - selected.get(0).getX()) / 2);
                midY = selected.get(0).getY() + ((selected.get(1).getY() - selected.get(0).getY()) / 2);

                boolean isCurve = false;
                if (selected.get(0).getX() > selected.get(1).getX()) {
                    isCurve = true;
                }
                if (!isCurve) {
                    mainCanvas.getGraphicsContext2D().strokeLine(selected.get(0).getX(), selected.get(0).getY(), selected.get(1).getX(), selected.get(1).getY());
                    gArrow.setMidx(midX);
                    gArrow.setMidy(midY);
                    drawArrow(gArrow, selected.get(1).getX(), selected.get(1).getY(), mainCanvas.getGraphicsContext2D());

                } else {
                    PosXY arr = drawCurve(selected.get(0).getX(), selected.get(0).getY(), selected.get(1).getX(), selected.get(1).getY(), midX, midY, mainCanvas.getGraphicsContext2D());

                    gArrow.setMidx(arr.getX());
                    gArrow.setMidy(arr.getY());

                    drawArrow(gArrow, selected.get(1).getX(), selected.get(1).getY(), mainCanvas.getGraphicsContext2D());

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
                        mainCanvas.getGraphicsContext2D().strokeText(textField.getText(), gArrow.getMidx(), gArrow.getMidy() - 10);
                        arrows.add(gArrow);
                        for (int i = 0; i < selected.size(); i++) {
                            selected.get(i).setColor(chosenForNodes);
                            drawGNode(selected.get(i), mainCanvas.getGraphicsContext2D());
                        }
                        selected = new LinkedList<>();
                        waitForAction = false;
                    } else {
                        textField.setText("unvalid");
                    }
                }
            });
            stackPane.getChildren().add(textField);
        }
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

        } else if (dx == 0) {
            theta = (dy > 0 ? 270 : 90);
        } else if (dy == 0) {
            theta = (dx > 0 ? 0 : 180);
        }
        gArrow.setSlope(theta);
        System.out.println(theta + " ");
        gc.setFill(chosenForText);
        gc.fillArc(gArrow.getMidx() - 20, gArrow.getMidy() - 20, 40, 40, theta - 20, 40, ArcType.ROUND);
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
                        Node bNode = backEnd.addNode(textField.getText().trim());
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
