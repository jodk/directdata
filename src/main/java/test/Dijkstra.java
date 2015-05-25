package test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdekun on 15-5-15-下午2:43.
 */
public class Dijkstra {
    private int[] dist = null;
    private int[] path;
    private boolean[] visit;
    private Graph graph;

    public int[] getDist() {
        return dist;
    }

    public void setDist(int[] dist) {
        this.dist = dist;
    }

    public int[] getPath() {
        return path;
    }

    public void setPath(int[] path) {
        this.path = path;
    }

    public boolean[] getVisit() {
        return visit;
    }

    public void setVisit(boolean[] visit) {
        this.visit = visit;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Dijkstra(Graph graph) {
        this.graph = graph;
        // init();
    }

    private void init(int start) {
        int size = this.graph.getNodeList().size();
        this.path = new int[size];//节点路径的上一个节点是什么
        this.dist = new int[size];
        for (int i = 0; i < size; i++) {
            this.path[i] = start;
            this.dist[i] = this.graph.getMetrix()[start][i];
        }
        this.visit = new boolean[size];
        dist[start] = 0;
        visit[start] = true;
    }

    public Dijkstra dijkstra(int start) {
        init(start);
        int size = graph.getNodeList().size();
        for (int m = 1; m < size; m++) {
            int min = 201;
            int k = start;//选出离start最近的node
            for (int i = 0; i < size; i++) {
                if (!visit[i] && graph.getMetrix()[start][i] < min) {
                    min = graph.getMetrix()[start][i];
                    k = i;
                }
            }
            visit[k] = true;
            dist[k] = dist[k] > min ? min : dist[k];
            if (dist[k] > min) {
                path[k] = start;
            }
            //更新dist
            for (int j = 0; j < size; j++) {
                if (graph.getMetrix()[k][j] < 200) {
                    int newdist = dist[k] + graph.getMetrix()[k][j];
                    if (dist[j] > newdist) {
                        dist[j] = newdist;
                        path[j] = k;
                    }
                }
            }
        }
        return this;
    }

    public static void main(String[] args) {
        List<Node> nodeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Node node = new Node();
            node.setName("name" + i);
            node.setUuid(i + 1);
            nodeList.add(node);
        }
        Graph graph1 = new Graph(nodeList);
        // test
        int[][] met = graph1.getMetrix();
        met[0][1] = 10;
        met[0][4] = 100;
        met[0][3] = 30;
        met[1][2] = 50;
        met[2][4] = 10;
        met[3][2] = 20;
        met[3][4] = 60;
       /*-------------------------------------*/
        for (int m = 0; m < graph1.getMetrix().length; m++) {
            int[] temp = graph1.getMetrix()[m];
            for (int n = 0; n < temp.length; n++) {
                System.out.print(temp[n] + "  ");
            }
            System.out.println();
        }
        Dijkstra dijkstra = new Dijkstra(graph1);
        dijkstra.dijkstra(0);
        int[] result = dijkstra.getDist();
        int[] path = dijkstra.getPath();
        System.out.println("path is ......");
        for (int m = 0; m < path.length; m++) {
            int index = path[m];
            if (index != -1) {
                System.out.println(1 + index + "---->" + (m + 1));
            }
        }
        System.out.println("result is ................");
        for (int k = 0; k < result.length; k++) {
            System.out.print(result[k] + "  ");
        }

    }
}

class Node {
    private String name;
    private int uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}

class Graph {
    private List<Node> nodeList = null;
    private int[][] metrix;

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public int[][] getMetrix() {
        return metrix;
    }

    public void setMetrix(int[][] metrix) {
        this.metrix = metrix;
    }

    public Graph(List<Node> nodeList) {
        this.nodeList = nodeList;
        if (this.nodeList != null && !this.nodeList.isEmpty()) {
            int size = this.nodeList.size();
            this.metrix = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    this.metrix[i][j] = getWeight(this.nodeList.get(i), this.nodeList.get(j));
                }
            }
        }
    }

    /**
     * 权重 可重写
     *
     * @return
     */
    public int getWeight(Node startNode, Node endNode) {
        return 200;
        /**
         if(startNode.getUuid()==endNode.getUuid()){
         return 0;
         }
         if(endNode.getUuid()%3==1 || endNode.getUuid()<startNode.getUuid()){
         return 200;//不通
         }
         return (int)(5*Math.random())+1;
         */
    }
}