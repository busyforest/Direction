package src.direction;

public class Edge {
    Node destination;
    Node start;
    double weight;

    public Edge(Node d,double w) {
        this.destination = d;
        this.weight = w;
    }
}
