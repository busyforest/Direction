package src.direction;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

public class Graph {
    Node[] nodes;
    double[][][] a;
    public void op2(Node s) {
        // 将所有节点的距离初始化为无穷大，将源节点的距离初始化为0
        for (Node node : nodes) {
            node.d =2147483646.99;
            node.f = null;
        }
        s.d = 0;

        // 创建一个优先队列来存储节点，基于它们的距离
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.d));


        // 将源节点添加到优先队列
        priorityQueue.add(s);

        while (!priorityQueue.isEmpty()) {
            // 从优先队列中提取距离最小的节点
            Node u = priorityQueue.poll();

            // 遍历u的邻居
            for (Edge edge : u.edges) {
                Node v = edge.destination;
                double weight = edge.weight;

                // 松弛步骤：如果发现更短的路径，则更新邻居的距离
                if (u.d + weight < v.d) {
                    v.d = u.d + weight;
                    v.f = u;
                    // 将更新后的邻居添加到优先队列
                    priorityQueue.add(v);
                }
            }
        }

        // 输出最短路径和总距离
        for (Node node : nodes) {
            System.out.println("从节点 " + node + " 到此的最短路径: " + printPath(node));
            System.out.println("总距离: " + String.format("%.2f", node.d));
        }
    }


    public void Dijkstra(Node s) {
        // 将所有节点的距离初始化为无穷大，将源节点的距离初始化为0
        for (Node node : nodes) {
            node.d =2147483646.99;
            node.f = null;
        }
        s.d = 0;

        // 创建一个优先队列来存储节点，基于它们的距离
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.d));


        // 将源节点添加到优先队列
        priorityQueue.add(s);

        while (!priorityQueue.isEmpty()) {
            // 从优先队列中提取距离最小的节点
            Node u = priorityQueue.poll();

            // 遍历u的邻居
            for (Edge edge : u.edges) {
                Node v = edge.destination;
                double weight = edge.weight;

                // 松弛步骤：如果发现更短的路径，则更新邻居的距离
                if (u.d + weight < v.d) {
                    v.d = u.d + weight;
                    v.f = u;
                    // 将更新后的邻居添加到优先队列
                    priorityQueue.add(v);
                }
            }
        }

        // 输出最短路径和总距离
    }

    //辅助方法，用于打印从源节点到给定节点的路径
    private String printPath(Node destination) {
        Stack<Node> stack = new Stack<>();
        Node current = destination;
        while (current != null) {
            stack.push(current);
            current = current.f;
        }

        StringBuilder path = new StringBuilder();
        Stack<Node> reversedStack = new Stack<>();
        while (!stack.isEmpty()) {
            reversedStack.push(stack.pop());
        }

        while (!reversedStack.isEmpty()) {
            path.append(reversedStack.pop());
            if (!reversedStack.isEmpty()) {
                path.append(" -> ");
            }
        }

        return path.toString();
    }
    public List<Node> findShortestPath(Node start, Node end) {
        // Run Dijkstra's algorithm to compute shortest paths
        Dijkstra(start);

        List<Node> shortestPath = new ArrayList<>();

        // Backtrack from the destination node to the source node to reconstruct the path
        for (Node at = end; at != null; at = at.f) {
            shortestPath.add(at);
        }

        // Reverse the list to get the correct order from source to destination
        Collections.reverse(shortestPath);

        return shortestPath;
    }

    public String printShortestPath(Node start, Node end) {
        List<Node> shortestPath = findShortestPath(start, end);

        StringBuilder pathString = new StringBuilder();
        pathString.append("最短路径从节点 ").append(start.name).append(" 到节点 ").append(end.name).append(": ");

        // 输出路径节点和总距离
        double totalDistance = 0;
        for (Node node : shortestPath) {
            pathString.append(node).append(" -> ");
            totalDistance += node.d; // 累加每个节点的距离
        }

        // 移除最后的 " -> " 以得到最终字符串
        if (pathString.length() > 4) {
            pathString.setLength(pathString.length() - 4);
        }

        pathString.append("\n总距离: ").append(String.format("%.2f",totalDistance));

        return pathString.toString();
    }
    public void getDistances(Node s) {
        // 将所有节点的距离初始化为无穷大，将源节点的距离初始化为0
        for (Node node : nodes) {
            node.d =2147483646.99;
            node.f = null;
        }
        s.d = 0;

        // 创建一个优先队列来存储节点，基于它们的距离
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.d));


        // 将源节点添加到优先队列
        priorityQueue.add(s);

        while (!priorityQueue.isEmpty()) {
            // 从优先队列中提取距离最小的节点
            Node u = priorityQueue.poll();

            // 遍历u的邻居
            for (Edge edge : u.edges) {
                Node v = edge.destination;
                double weight = edge.weight;

                // 松弛步骤：如果发现更短的路径，则更新邻居的距离
                if (u.d + weight < v.d) {
                    v.d = u.d + weight;
                    v.f = u;
                    // 将更新后的邻居添加到优先队列
                    priorityQueue.add(v);
                }
            }
        }
        for(int i=0;i<nodes.length;i++) {
            if(nodes[i].d==0) {
                s.distances[i]=Double.MAX_VALUE;
            }else {
                s.distances[i]=nodes[i].d;
            }
        }
    }
    public void initDistances() {
        for(int i=0;i<nodes.length;i++) {
            getDistances(nodes[i]);
        }
    }
    public void solveTSP(int index) {
        initDistances();
        this.a=new double[26][26][2];
        for(int i=0;i<nodes.length;i++) {
            for(int j=0;j<nodes.length;j++) {
                a[i][j][0]=nodes[i].distances[j];
                a[i][j][1]=0;
            }
        }
        while(a[0][index][1]!=1) {
            char indexchar =(char) (index+65);
            System.out.print(indexchar+" -> ");
            for(int i =0;i<nodes.length;i++) {
                a[i][index][1]=1;
            }
            index=findMinIndexInRow(a, index);


        }
    }
    public static int findMinIndexInRow(double[][][] array, int rowIndex) {
        if (array == null || array.length == 0 || rowIndex < 0 || rowIndex >= array.length || array[rowIndex].length == 0) {
            throw new IllegalArgumentException("Invalid input");
        }

        int minIndex = 25;

        for (int i = 0; i < array[rowIndex].length; i++) {
            if (array[rowIndex][i][0] < array[rowIndex][minIndex][0]&&array[rowIndex][i][1]!=1) {
                minIndex = i;
            }
        }

        return minIndex;
    }
    public void prim(int index) {
        String[] info = new String[25];
        // 使用自定义比较器初始化优先级队列，以优先考虑权重最小的边
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
        // 选择一个起始节点（例如，数组中的第一个节点）
        Node startNode = nodes[index];
        startNode.visited = true;

        // 将起始节点的所有边添加到优先级队列中
        for (Edge edge : startNode.edges) {
            priorityQueue.add(edge);
        }

        // 列表用于存储最小生成树的边
        List<Edge> minimumSpanningTree = new ArrayList<>();

        while (!priorityQueue.isEmpty()) {
            // 从优先级队列中获取权重最小的边
            Edge minEdge= priorityQueue.poll();

            // 获取权重最小边的目标节点
            Node destinationNode = minEdge.destination;

            // 检查目标节点是否已访问
            if (!destinationNode.visited) {
                // 将目标节点标记为已访问
                destinationNode.visited = true;

                // 将权重最小的边添加到最小生成树中
                minimumSpanningTree.add(minEdge);

                // 将目标节点的所有边添加到优先级队列中
                for (Edge edge : destinationNode.edges) {
                    if (!edge.destination.visited) {
                        priorityQueue.add(edge);
                    }
                }
            }
        }
        int i=1;
        // 根据需要输出或使用最小生成树的边
        for (Edge edge : minimumSpanningTree) {
            System.out.println("边"+i+": "+edge.weight + " -> " + edge.destination);
            i++;
        }
    }

}
