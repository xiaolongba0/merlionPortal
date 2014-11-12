/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.tms.routeoptimizationmodule;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class DijkstraLand {

    private static Heap heap = new Heap();
    private static int[][] graph;

    public DijkstraLand() {
        graph = new int[10][10];
        /*
         * The graph value assignment is just for checking the code. node A is
         * referred as node 0, node B is referred as node 1 and so on. finally
         * node F is referred as node 5.
         */
//        graph[0][0] = graph[0][1] = graph[0][3] = graph[0][4] = graph[0][5] = graph[1][0] = graph[1][1] = graph[1][4] = graph[1][5] = graph[2][2] = graph[2][5] = graph[3][0] = graph[3][3] = graph[4][0] = graph[4][1] = graph[4][4] = graph[5][0] = graph[5][1] = graph[5][2] = graph[5][5] = 0;
//        graph[1][2] = graph[2][1] = graph[2][3] = graph[3][2] = graph[3][4] = graph[4][3] = graph[4][5] = graph[5][4] = 1;
//        graph[1][3] = graph[3][1] = 3;
//        graph[0][2] = graph[2][0] = 4;
//        graph[2][4] = graph[4][2] = 5;
//        graph[3][5] = graph[5][3] = 8;
    }

    public boolean createConnection(Integer startId, Integer endId, Integer dest) {
        System.out.println("Dij create Connection - Land");
        System.out.println("Start: "+startId+"End: " +endId + "Distance" +dest);
        if (dest > 0) {
            graph[startId][endId] = graph[endId][startId] = dest;
            return true;
        } else {
            return false;
        }
    }

//    public static void main(String[] args) {
//        DijkstraLand dij = new DijkstraLand();
//        // Source is node A (node 0) and destination is node F (node 5)
//        System.out.println(dij.solve(6, 0, 5));
//    }

    public int solve(Integer numOfNodes, Integer source, Integer dest) {
        heap.push(source, 0);
        while (!heap.isEmpty()) {
            int u = heap.pop();
            if (u == dest) {
                return heap.cost[dest];
            }
            for (int i = 0; i < numOfNodes; i++) {
                if (graph[u][i] > 0) {
                    heap.push(i, heap.cost[u] + graph[u][i]);
                }
            }
        }
        return -1;
    }
}


class Heap {

    private int[] data;
    private int[] index;
    public int[] cost;
    private int size;

    public Heap() {
        data = new int[10];
        index = new int[10];
        cost = new int[10];

        for (int i = 0; i < 10; i++) {
            index[i] = -1;
            cost[i] = -1;
        }

        size = 0;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    private void shiftUp(int i) {
        int j;
        while (i > 0) {
            j = (i - 1) / 2;
            if (cost[data[i]] < cost[data[j]]) {
                // swap here
                int temp = index[data[i]];
                index[data[i]] = index[data[j]];
                index[data[j]] = temp;
                // swap here
                temp = data[i];
                data[i] = data[j];
                data[j] = temp;
                i = j;
            } else {
                break;
            }
        }
    }

    private void shiftDown(int i) {
        int j, k;
        while (2 * i + 1 < size) {
            j = 2 * i + 1;
            k = j + 1;
            if (k < size && cost[data[k]] < cost[data[j]]
                    && cost[data[k]] < cost[data[i]]) {
                // swap here
                int temp = index[data[k]];
                index[data[k]] = index[data[i]];
                index[data[i]] = temp;
                // swap here
                temp = data[k];
                data[k] = data[i];
                data[i] = temp;

                i = k;
            } else if (cost[data[j]] < cost[data[i]]) {
                // swap here
                int temp = index[data[j]];
                index[data[j]] = index[data[i]];
                index[data[i]] = temp;
                // swap here
                temp = data[j];
                data[j] = data[i];
                data[i] = temp;

                i = j;
            } else {
                break;
            }
        }
    }

    public int pop() {
        int res = data[0];
        data[0] = data[size - 1];
        index[data[0]] = 0;
        size--;
        shiftDown(0);
        return res;
    }

    public void push(int x, int c) {
        if (index[x] == -1) {
            cost[x] = c;
            data[size] = x;
            index[x] = size;
            size++;
            shiftUp(index[x]);
        } else {
            if (c < cost[x]) {
                cost[x] = c;
                shiftUp(index[x]);
                shiftDown(index[x]);
            }
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
