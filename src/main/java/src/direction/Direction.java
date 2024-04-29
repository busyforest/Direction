package src.direction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Direction extends Application{
    TextField startField=new TextField();
    TextField endField =new TextField();
    TextArea msgArea=new TextArea();
    Node[] nodes=new Node[72];
    Graph graph = new Graph();
    Canvas canvas=new Canvas(1000,600);
    GraphicsContext gc=canvas.getGraphicsContext2D();
    @Override
    public 	void start(Stage primaryStage) throws Exception {

        //声明
        AnchorPane pane = new AnchorPane();

        Scene scene = new Scene(pane, 1300, 600);

        Image mapImage= new Image("file:src/images/tagged.png");
        ImageView imageView = new ImageView(mapImage);
        Label label = new Label("    Let's go"+'\n'+"travelling!");
        Label startLabel=new Label("start from:");
        Label endLabel=new Label("end with:");
        Button op1Button=new Button("operation1");
        Button op3Button=new Button("operation3");
        Button op2Button=new Button("operation2");
        Button op4Button=new Button("operation4");
        Font font =Font.font("Algerian", FontWeight.NORMAL, 30);
        Font font2=Font.font("Elephant", FontWeight.NORMAL,20);
        Font font3=Font.font("Comic Sans MS",FontWeight.NORMAL,15);
        System.setOut(new TextAreaPrintStream(msgArea));
        //绘制UI
        imageView.setFitHeight(600.0);
        imageView.setFitWidth(1000.0);
        pane.setLeftAnchor(label, 1060.0);
        pane.setTopAnchor(label, 18.0);
        pane.setLeftAnchor(startLabel, 1010.0);
        pane.setTopAnchor(startLabel, 100.0);
        pane.setLeftAnchor(endLabel, 1025.0);
        pane.setTopAnchor(endLabel, 140.0);
        pane.setLeftAnchor(startField, 1125.0);
        pane.setTopAnchor(startField, 103.0);
        pane.setLeftAnchor(endField, 1125.0);
        pane.setTopAnchor(endField, 143.0);
        pane.setLeftAnchor(op1Button, 1020.0);
        pane.setRightAnchor(op1Button, 168.0);
        pane.setTopAnchor(op1Button, 200.0);
        pane.setLeftAnchor(op3Button, 1020.0);
        pane.setRightAnchor(op3Button, 168.0);
        pane.setTopAnchor(op3Button, 250.0);
        pane.setLeftAnchor(op2Button, 1170.0);
        pane.setRightAnchor(op2Button, 20.0);
        pane.setTopAnchor(op2Button, 200.0);
        pane.setLeftAnchor(op4Button, 1170.0);
        pane.setTopAnchor(op4Button, 250.0);
        pane.setRightAnchor(op4Button, 20.0);
        pane.setLeftAnchor(msgArea, 1020.0);
        pane.setTopAnchor(msgArea, 300.0);
        pane.setBottomAnchor(msgArea, 20.0);
        pane.setRightAnchor(msgArea, 20.0);
        msgArea.setEditable(false);
        label.setFont(font);
        startLabel.setFont(font2);
        endLabel.setFont(font2);
        op1Button.setFont(font3);
        op3Button.setFont(font3);
        op2Button.setFont(font3);
        op4Button.setFont(font3);
        pane.getChildren().addAll(imageView,label,startLabel,endLabel,
                startField,endField,op1Button,op3Button,op2Button,
                msgArea,canvas,op4Button);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Positioning System");

        primaryStage.show();
        Init();
        op1Button.setOnAction(e->{
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            String startString=new String(startField.getText());
            String endString = new String(endField.getText());
            Node node1;
            Node node2;
            Node endNode ;
            gc.setLineWidth(5.00);
            gc.setStroke(Color.LIMEGREEN);
            node1=searchNode(startString);
            endNode=searchNode(endString);
            String deString=new String(graph.printShortestPath(searchNode(startString), searchNode(endString)));
            System.out.println(deString);
            String[] string=deString.replaceAll("[^a-zA-Z ]", "").split(" ");
            for(int i=4;i<string.length;i++) {
                for(int j = 0;j<graph.nodes.length;j++) {
                    if(string[i].equals(graph.nodes[j].name)) {
                        node2=graph.nodes[j];
                        gc.strokeLine(node1.x, node1.y, node2.x, node2.y);
                        node1=node2;
                        gc.moveTo((double)node1.x+10.00, (double)node1.y+10.00);
                    }
                }
            }

        });
        op2Button.setOnAction(e->{
            String endString=new String(endField.getText());
            graph.op2(searchNode(endString));
        });
        op3Button.setOnAction(e->{

            //graph.solveTSP(0);
            graph.prim(0);
            op4Draw();
        });
        op4Button.setOnAction(e->{
            String startString=new String(startField.getText());
            char start=startString.charAt(0);
            int index=(int)start;
            graph.prim(index-65);
            op4Draw();
        });
        canvas.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            if(startField.isFocused()) {
                for(Node node: graph.nodes) {
                    double distance = Math.sqrt(Math.pow(x - node.x, 2) + Math.pow(y- node.y, 2));
                    if(distance<=50) {
                        startField.clear();
                        startField.appendText(node.name);
                    }
                }
            }else if(endField.isFocused()) {
                for(Node node: graph.nodes) {
                    double distance = Math.sqrt(Math.pow(x - node.x, 2) + Math.pow(y- node.y, 2));
                    if(distance<=50) {
                        endField.clear();
                        endField.appendText(node.name);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    public Node searchNode(String nameString) {
        for(int i=0;i<26;i++) {
            if(graph.nodes[i].name.equals(nameString))
                return graph.nodes[i];
        }
        return graph.nodes[0];
    }
    public   void Init() {
        Node aNode = new Node("A");
        aNode.setXY(80,227);
        Node bNode = new Node("B");
        bNode.setXY(144, 540);
        Node cNode= new Node("C");
        cNode.setXY(187, 277);
        Node dNode = new Node("D");
        dNode.setXY(206, 397);
        Node eNode = new Node("E");
        eNode.setXY(247, 530);
        Node fNode = new Node("F");
        fNode.setXY(292, 143);
        Node gNode = new Node("G");
        gNode.setXY(318, 255);
        Node hNode = new Node("H");
        hNode.setXY(335, 364);
        Node iNode = new Node("I");
        iNode.setXY(460, 88);
        Node jNode = new Node("J");
        jNode.setXY(475, 220);
        Node kNode = new Node("K");
        kNode.setXY(485, 330);
        Node lNode= new Node("L");
        lNode.setXY(515, 470);
        Node mNode= new Node("M");
        mNode.setXY(540, 566);
        Node nNode = new Node("N");
        nNode.setXY(607, 200);
        Node oNode = new Node("O");
        oNode.setXY(630, 300);
        Node pNode = new Node("P");
        pNode.setXY(650, 420);
        Node qNode = new Node("Q");
        qNode.setXY(683, 540);
        Node rNode = new Node("R");
        rNode.setXY(720, 110);
        Node sNode = new Node("S");
        sNode.setXY(725, 183);
        Node tNode = new Node("T");
        tNode.setXY(733, 270);
        Node uNode = new Node("U");
        uNode.setXY(770, 410);
        Node vNode = new Node("V");
        vNode.setXY(835, 25);
        Node wNode = new Node("W");
        wNode.setXY(830, 167);
        Node xNode = new Node("X");
        xNode.setXY(850, 260);
        Node yNode = new Node("Y");
        yNode.setXY(856, 380);
        Node zNode = new Node("Z");
        zNode.setXY(860, 526	);
        // Set up your graph with nodes and edges
        graph.nodes = new Node[]{aNode,bNode,cNode,dNode,eNode,fNode,gNode,hNode,iNode,
                jNode,kNode,lNode,mNode,nNode,oNode,pNode,qNode,rNode,sNode,tNode,uNode,
                vNode,wNode,xNode,yNode,zNode};
        String filePath = "src/edge.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // 使用正则表达式匹配括号前的字符串和括号中的两个数字
            String pattern = "([A-Z]+)\\s+([A-Z]+)\\s+([0-9]+\\.[0-9]+)";
            Pattern r = Pattern.compile(pattern);

            while ((line = br.readLine()) != null) {
                Matcher m = r.matcher(line);
                if (m.find()) {
                    String node1 = m.group(1);
                    String node2 = m.group(2);
                    double weight = Double.parseDouble(m.group(3));
                    for(int i=0;i<26;i++) {
                        if(graph.nodes[i].name.equals(node1)) {
                            for(int j=0;j<26;j++) {
                                if (graph.nodes[j].name.equals(node2)) {
                                    graph.nodes[i].addAdj(graph.nodes[j], weight);
                                    graph.nodes[j].addAdj(graph.nodes[i], weight);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Call Dijkstra method with the source node
        // graph.Dijkstra(node1);
        // System.out.println( graph.printShortestPath(node1, node3));
    }
    public static class TextAreaPrintStream extends java.io.PrintStream {
        private TextArea textArea;

        public TextAreaPrintStream(TextArea textArea) {
            super(System.out);
            this.textArea = textArea;
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            final String message = new String(buf, off, len);
            javafx.application.Platform.runLater(() -> textArea.appendText(message));
        }
    }

    public void op4Draw() {
        String inputText= new String(msgArea.getText());
        String regex = "(\\d+\\.\\d+) -> ([A-Za-z])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputText);

        // 提取匹配项
        while (matcher.find()) {
            // 提取双精度数和字母
            String doubleValue = matcher.group(1);
            double val = Double.parseDouble(doubleValue);
            String letter = matcher.group(2);
            for(Node node:graph.nodes) {
                for(int j=0;j<node.edges.size();j++) {
                    if(node.edges.get(j).weight==val&&node.edges.get(j).destination.name.equals(letter)) {
                        gc.setLineWidth(5.00);
                        gc.setStroke(Color.LIMEGREEN);
                        gc.strokeLine(node.x, node.y,node.edges.get(j).destination.x ,node.edges.get(j).destination.y);
                    }
                }
            }
        }
    }
}
