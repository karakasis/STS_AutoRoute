/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package STS;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Xrhstos
 */
public class Route {

    public static int[] weights;
    public static boolean skipElites = false;

    public static void weightSetter(String[] priorities) {
        Route.weights = new int[6];
        for (int i = 0; i < 6; i++) {
            if (priorities[i].startsWith("rest")) {
                Route.weights[i] = 0; // REST PRIORITY IS i 0,1,2,3,4,5
            } else if (priorities[i].startsWith("elite")) {
                Route.weights[i] = 1;
            } else if (priorities[i].startsWith("merchant")) {
                Route.weights[i] = 2;
            } else if (priorities[i].startsWith("unknown")) {
                Route.weights[i] = 3;
            } else if (priorities[i].startsWith("treasure")) {
                Route.weights[i] = 4;
            } else if (priorities[i].startsWith("enemy")) {
                Route.weights[i] = 5;
            }
        }
    }

    private ArrayList<Room> route;
    private int[] stats;
    private double score;
    private int id;

    public Route(ArrayList<Room> route, int id) {
        this.route = route;
        //Collections.reverse(this.route);
        stats = new int[]{0, 0, 0, 0, 0, 0};
        this.id = id;
        for (Room room : route) {
            switch (room.getType()) {
                case "Rest":
                    stats[0]++;
                    break;
                case "Elite":
                    stats[1]++;
                    break;
                case "Merchant":
                    stats[2]++;
                    break;
                case "Unknown":
                    stats[3]++;
                    break;
                case "Treasure":
                    stats[4]++;
                    break;
                case "Enemy":
                    stats[5]++;
                    break;
                default:
                    break;
            }
        }
    }

    public ArrayList<Room> getRoute() {
        return route;
    }

    public void evaluateRoute(int maxElites) {
        //LOG
        Main.log.add("Route ID: " + id + "\n");
        Main.log.add("Math: \n");
        //
        double[] weightValues = new double[]{2.5, 2.2, 1.9, 1.4, 0.8, 1};
        score = 0;
        
        
        if (skipElites) {
            //weightValues[5] = 0;
            if(stats[1] == maxElites){
               for (int i = 0; i < 5; i++) { // priorities
                //my top priority is :  //priority|
                score = score + stats[Route.weights[i]] * weightValues[i];
                } 
            }else{
                score = -1;
            }
        } else {
            for (int i = 0; i < 5; i++) { // priorities
                //my top priority is :  //priority|
                score = score + stats[Route.weights[i]] * weightValues[i];
                //LOG
                Main.log.add(score + " = " + stats[Route.weights[i]] + " * " + weightValues[i] +" ... ");
                //
            }
            Main.log.add("SCORE : "+ score);
            Main.log.add("\n");
        }
        
        //           Rest 1.8       Elite 1.6      Merchant 1.4      Unknown 1.2      Treasure 1      Enemy 0.8
            //score = stats[2] * 2.5 + stats[3] * 2.2 + stats[4] * 1.9 + stats[1] * 1.4 + stats[5] * 1 + stats[0] * 0.8;
        
    }

    public double getScore() {
        return score;
    }

    public int getID() {
        return id;
    }

    public String route2str() {
        String str = "";
        for (Room room : route) {
            str = str + (room.getType() + " ");
        }
        return str;
    }

    public void printRoute() {
        Collections.reverse(this.route);
        System.out.printf("Route ID: " + id + " Score: " + String.format("%.1f", score) + "\n");
        System.out.println(route2str());
        System.out.printf("Rest: " + String.format("%.1f", stats[0] * 100.0 / route.size()));
        System.out.println("%");
        System.out.printf("Elite: " + String.format("%.1f", stats[1] * 100.0 / route.size()));
        System.out.println("%");
        System.out.printf("Merchant: " + String.format("%.1f", stats[2] * 100.0 / route.size()));
        System.out.println("%");
        System.out.printf("Unknown: " + String.format("%.1f", stats[3] * 100.0 / route.size()));
        System.out.println("%");
        System.out.printf("Treasure: " + String.format("%.1f", stats[4] * 100.0 / route.size()));
        System.out.println("%");
        System.out.printf("Enemy: " + String.format("%.1f", stats[5] * 100.0 / route.size()));
        System.out.println("%");
        Collections.reverse(this.route);
    }

    public int[] getStats(){
        return stats;
    }
    
    public ArrayList<Character> getOrders() {
        ArrayList<Character> orders = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            if (i != route.size() - 1) {
                if (route.get(i + 1).getX() > route.get(i).getX()) {
                    orders.add('\\');
                } else if (route.get(i + 1).getX() < route.get(i).getX()) {
                    orders.add('/');
                } else {
                    orders.add('|');
                }
            }

        }
        return orders;
    }
}
