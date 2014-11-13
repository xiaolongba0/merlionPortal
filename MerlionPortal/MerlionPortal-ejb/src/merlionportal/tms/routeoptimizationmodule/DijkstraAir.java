/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionportal.tms.routeoptimizationmodule;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import util.accessRightControl.Awesome;

/**
 *
 * @author Yuanbo
 */
@Stateless
@LocalBean
public class DijkstraAir {

    private  Awesome heap;
    private static int[][] graph = new int[10][10];

    public DijkstraAir() {
//        graph = new int[10][10];
//        /*
//         * The graph value assignment is just for checking the code. node A is
//         * referred as node 0, node B is referred as node 1 and so on. finally
//         * node F is referred as node 5.
//         */
////        graph[0][0] = graph[0][1] = graph[0][3] = graph[0][4] = graph[0][5] = graph[1][0] = graph[1][1] = graph[1][4] = graph[1][5] = graph[2][2] = graph[2][5] = graph[3][0] = graph[3][3] = graph[4][0] = graph[4][1] = graph[4][4] = graph[5][0] = graph[5][1] = graph[5][2] = graph[5][5] = 0;
////        graph[1][2] = graph[2][1] = graph[2][3] = graph[3][2] = graph[3][4] = graph[4][3] = graph[4][5] = graph[5][4] = 1;
////        graph[1][3] = graph[3][1] = 3;
////        graph[0][2] = graph[2][0] = 4;
////        graph[2][4] = graph[4][2] = 5;
////        graph[3][5] = graph[5][3] = 8;
    }

    public boolean createConnection(Integer startId, Integer endId, Integer dest) {
        System.out.println("Dij create Connection - Air");
        System.out.println("Start: " + startId + "End: " + endId + "Distance" + dest);
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
        heap = new Awesome();
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

   // Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Meth
