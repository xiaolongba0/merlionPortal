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
public class DijkstraLand {

    private  Awesome heap;
    private static int[][] graph = new int[10][10];

    public DijkstraLand() {
        graph[0][1]=graph[1][0]=50;
        graph[1][2]=graph[2][1]=50;
        graph[0][6]=graph[6][0]=30;
        graph[1][6]=graph[6][1]=60;
        graph[2][6]=graph[6][2]=20;
        
        
        graph[3][4]=graph[4][3]=1000;
        graph[3][5]=graph[5][3]=3000;
        graph[4][5]=graph[5][4]=1000;
        graph[3][7]=graph[7][3]=2000;
        graph[4][7]=graph[7][4]=2000;
        graph[5][7]=graph[7][5]=2000;
    }

    public Boolean createConnection(Integer startId, Integer endId, Integer dest) {
        System.out.println("Dij create Connection - Land");
        System.out.println("Start: "+startId+"End: " +endId + "Distance" +dest);
        if (dest > 0) {
            graph[startId][endId] = graph[endId][startId] = dest;
            System.out.println(startId +" "+endId+" "+graph[startId][endId]+" " + " "+graph[endId][startId]);
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

    public Integer solve(Integer numOfNodes, Integer source, Integer dest) {
        System.out.println("Solve = ===== > "+"NON  "+ numOfNodes+"  Source  "+source+"  dest  "+dest);
        heap = new Awesome();
        for(int i=0;i<10;i++){
            for(int k = 0;k<10;k++){
                System.out.println(graph[i][k]);
            }
        }
        
        heap.push(source, 0);
        
        while (!heap.isEmpty()) {
            System.out.println("passed ");
            int u = heap.pop();
            if (u == dest) {
                Integer result = heap.cost[dest];
                System.out.println("===========this is the result??????" + result);
                
                return result;
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

