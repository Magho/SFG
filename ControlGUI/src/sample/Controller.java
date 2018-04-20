package sample;

import BackEnd.ISFG;
import BackEnd.Node;
import BackEnd.SFG;
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
    private boolean waitForAction=false;
    private ISFG backEnd;
    private Color selectionColor = Color.RED;
    private Color chosenForNodes = Color.BLUE;
    private Color arrowColor = Color.GREEN;

    private double chosenRadiusForNodes = 30;
    private int totalTools =3;
    private Icon node,connection,solver;
    private HashMap<String,Icon> icons ;
    private LinkedList<GNode> gNodes;
    private LinkedList<GNode> selected;
    private LinkedList<GArrow> arrows;
    private void setIconToContext(GraphicsContext context , Icon i ,String name){
        context.drawImage(i.getImage(),i.getX(),i.getY(),i.getH(),i.getW());
        icons.put(name,i);
    }


    private void setLine(GraphicsContext context){
        context.setFill(Color.rgb(138,183,23,.25));
        context.fillPolygon(new double[]{0,subCanvas.getWidth()/16,subCanvas.getWidth()-subCanvas.getWidth()/16,subCanvas.getWidth()}
                ,new double[]{0,subCanvas.getHeight()*3/4,subCanvas.getHeight()*3/4,0},4);

    }



    private void setSubCanvasClickHandler(){
        subCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!waitForAction) {
                    for (Map.Entry<String, Icon> entry : icons.entrySet()) {
                        if (entry.getValue().isInBoundaries(event.getX(), event.getY())) {
                            mainCanvas.setOnMouseClicked(entry.getValue().getBehaviourOnMainCanvas());
                            break;
                        }
                    }
                }
            }
        });
    }

    private void drawGNode(GNode n,GraphicsContext gc){
gc.setFill(n.getColor());
        gc.fillArc(n.getX()-n.getR()/2,n.getY()-n.getR()/2,n.getR(),n.getR(),0,360,ArcType.ROUND);

    }


    private void writeText(GNode n,GraphicsContext gc){
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setStroke(Color.RED);
        gc.strokeText(n.node.getName()
                 ,n.getX()
                ,n.getY()+
                        (n.getY()+n.getR()>mainCanvas.getHeight()     ?(-n.getR()-5):(5+n.getR()))
        );
    }


    private void setIcons(GraphicsContext context){
        Image connection = new Image(getClass().getResourceAsStream("connection.png"));
        Image node = new Image(getClass().getResourceAsStream("node.png"));
        Image solver = new Image(getClass().getResourceAsStream("solver.png"));
        Icon inode = new Icon(node,(subCanvas.getWidth()/(2*totalTools))-25,10,50,50,e->{
            eventOfiNode(e);
        });
        Icon iconnection = new Icon(connection,(3*subCanvas.getWidth()/(2*totalTools))-25,10,50,50,e->{
            eventOfiConnection(e);

        });
        Icon isolver = new Icon(solver,(5*subCanvas.getWidth()/(2*totalTools))-25,10,50,50,e->{
            if(waitForAction){return;}
            System.out.println("clicked from big behave solver");

        });

        setIconToContext(context,iconnection,"connection");
        setIconToContext(context,inode,"node");
        setIconToContext(context,isolver,"solver");
    }


    private void eventOfiConnection(MouseEvent e) {
        if((waitForAction&&selected.size()==0)||selected.size()==2){return;}
        for(int i=0;i<gNodes.size();i++){
            if(gNodes.get(i).isInBoundaries(e.getX(),e.getY())){
                gNodes.get(i).setColor(selectionColor);
                drawGNode(gNodes.get(i),mainCanvas.getGraphicsContext2D());
                selected.add(gNodes.get(i));
                waitForAction=true;
                break;
            }
        }
        if(selected.size()==2){

            mainCanvas.getGraphicsContext2D().strokeLine(selected.get(0).getX(),selected.get(0).getY(),selected.get(1).getX(),selected.get(1).getY());
            double midX = selected.get(0).getX()+((selected.get(1).getX()-selected.get(0).getX())/2);
            double midY = selected.get(0).getY()+((selected.get(1).getY()-selected.get(0).getY())/2);
            GArrow gArrow = new GArrow();
            gArrow.setMidx(midX);
            gArrow.setMidy(midY);
            drawArrow(gArrow,selected.get(1).getX(),selected.get(1).getY() ,mainCanvas.getGraphicsContext2D());
            TextField textField = new TextField();
            textField.setMinWidth(100);
            textField.setTranslateX(midX);
            textField.setTranslateY(midY-25);
            textField.setAlignment(Pos.CENTER);
            textField.setOnKeyPressed(e1->{
                if(e1.getCode().isWhitespaceKey()) {
                    if(validGain(textField.getText())) {
                        gArrow.setArrow(backEnd.addArrow(selected.get(0).node,selected.get(1).node,Integer.valueOf(textField.getText())));
                        stackPane.getChildren().remove(textField);
                        mainCanvas.getGraphicsContext2D().strokeText(textField.getText(),midX,midY-25);
                        selected = new LinkedList<>();
                        waitForAction=false;
                        arrows.add(gArrow);
                    }else{
                        textField.setText("unvalid");
                    }
                }});
            stackPane.getChildren().add(textField);
        }
        }

    private boolean validGain(String text) {
        try {
            Integer.valueOf(text);
            return true;
        }catch (Exception e){
            return false;
        }


}

    private void drawArrow(GArrow gArrow,double dirx,double diry ,GraphicsContext gc) {
        gc.setFill(arrowColor);
        double dy = gArrow.getMidy()-diry;
        double dx =gArrow.getMidx()-dirx;
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
            theta = 180+theta ;

        } else if (dx == 0) {
            theta = (dy>0? 270:90);
        } else if (dy == 0) {
            theta = (dx>0 ? 0:180);
        }
        gArrow.setSlope(theta);
        System.out.println(theta+" ");
        gc.fillArc(gArrow.getMidx()-20,gArrow.getMidy()-20,40,40,theta-20,40,ArcType.ROUND);
    }

    private void eventOfiNode(MouseEvent e) {
        if(waitForAction){return;}
        waitForAction=true;//waiting to enter name
        GraphicsContext gc=mainCanvas.getGraphicsContext2D();
        if(!validNodePos(e.getX(),e.getY())){
            waitForAction=false;
            return;
        }
        GNode node1 = new GNode(chosenForNodes,e.getX(),e.getY(),chosenRadiusForNodes,new Node("temp","temp"));
        drawGNode(node1,gc);
        TextField textField = new TextField();
        textField.setMinWidth(100);
        textField.setTranslateX(node1.getX());
        textField.setTranslateY(node1.getY()+
                (node1.getY()+node1.getR()>mainCanvas.getHeight()     ?(-node1.getR()-5):(5+node1.getR())));
        textField.setAlignment(Pos.CENTER);

        textField.setOnKeyPressed(e1->{
            if(e1.getCode().isWhitespaceKey()) {
                if(validNodeName(textField.getText())) {
                    waitForAction=false;
                    Node bNode = backEnd.addNode(textField.getText().trim(), "normal");
                    node1.setNode(bNode);
                    stackPane.getChildren().remove(textField);
                    writeText(node1, gc);
                    gNodes.add(node1);
                }else{
                    textField.setText("repeated");
                }
            }});
        stackPane.getChildren().add(textField);
        System.out.println("clicked from big behave node");
    }

    private boolean validNodePos(double x, double y) {
        for(int i =0;i<gNodes.size();i++){
            if(gNodes.get(i).isInFarBoundaries(x,y)){
                return false;
            }
        }
        return true;
    }

    private boolean validNodeName(String text) {
        for(int i =0 ; i< gNodes.size();i++){
            System.out.println(gNodes.get(i).node.getName()+"  "+text);
            if(text.trim().compareToIgnoreCase(gNodes.get(i).node.getName())==0){
                System.out.println("faaaaaaaaaaaalse");
                return false;
            }
        }
        return true;
    }
    private void setSubCanvasMovement(){
        subCanvas.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stackPane.setTranslateY(0);

            }
        });
        subCanvas.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stackPane.setTranslateY(-subCanvas.getHeight()+10);
            }
        });
    }
    public void initialize(){
        arrows = new LinkedList<>();
        selected = new LinkedList<>();
        gNodes = new LinkedList<>();
        backEnd = new SFG();
        icons = new HashMap<>();
        System.out.println("init");
        GraphicsContext context =subCanvas.getGraphicsContext2D();
        setSubCanvasMovement();
        setSubCanvasClickHandler();
        setLine(context);
        setIcons(context);

    }

}
