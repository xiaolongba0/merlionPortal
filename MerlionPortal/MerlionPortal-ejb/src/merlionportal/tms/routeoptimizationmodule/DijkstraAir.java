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
        graph[5][4]=graph[4][5]=1000;
        graph[5][3]=graph[3][5]=3000;
        graph[3][2]=graph[2][3]=6000;
        graph[2][4]=graph[4][2]=5000;
        graph[2][5]=graph[5][2]=4000;
        graph[3][4]=graph[4][3]=1500;

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
