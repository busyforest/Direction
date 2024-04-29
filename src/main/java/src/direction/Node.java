package src.direction;

import java.util.ArrayList;
import java.util.List;

public class Node {
    int x;
    int y;
    Node f;
    boolean visited;
    double d;
    double[] distances;
    List<Edge> edges;
    String name;
    public void setXY(int x,int y) {
        this.x=x;
        this.y=y;
    }
    public void addAdj(Node v,double w) {
        v.f=this;
        Edge tempEdge = new Edge(v, w);
        edges.add(tempEdge);
    }

    public Node(String name) {
        this.name=name;
        edges = new ArrayList<Edge>();
        this.distances=new double[26];
    }

    @Override
    public String toString() {
        return this.name;
    }
}