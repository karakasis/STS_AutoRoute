/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package STS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.Character.isDigit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xrhstos
 */
public class Map {

    private ArrayList<Route> Routes;
    private ArrayList<Route> routesFromCurrentNode;
    private ArrayList<Integer> best; //routes id
    private HashMap<Key, Room> gridMap;
    private ArrayList<Integer[]> steps;
    private ArrayList<Route> bestRoutes;
    private int maximumX;
    private ArrayList<Route> best2Routes;
    public int currentAlgorithm = 0;
    private Route currentRouteSelectedInGUI;

    //unused-old
    public Map(String path) {
        ArrayList<String> Map = new ArrayList<>();
        try {
            BufferedReader in = null;
            if (getClass().getResourceAsStream(path) == null) {
                System.out.println("The path is wrong.");
                System.exit(-1);
            }
            InputStream fileDir = getClass().getResourceAsStream(path);

            in = new BufferedReader(new InputStreamReader(fileDir, "ISO-8859-7"));

            String str;

            while ((str = in.readLine()) != null) {
                Map.add(str);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map(ArrayList<String> newMap) {
        Routes = new ArrayList<>();
        ArrayList<String> Map = new ArrayList<>(newMap);
        Map.remove(0);
        for (String ff : Map) {
            System.out.println(ff);
        }

        maximumX = Map.get(0).length();
        boolean bot;
        boolean top;
        int floor;
        gridMap = new HashMap<>();
        for (int y = 0; y < Map.size(); y = y + 2) {
            floor = Character.getNumericValue(Map.get(y).charAt(0));
            if (!Character.isWhitespace(Map.get(y).charAt(1))) {
                floor = floor * 10 + Character.getNumericValue(Map.get(y).charAt(1));
            }
            if (y == 0) {
                bot = false;
                top = true;
            } else if (y == Map.size() - 1) {
                bot = true;
                top = false;
            } else {
                bot = false;
                top = false;
            }
            for (int x = 0; x < Map.get(y).length(); x++) {

                if (!Character.isWhitespace(Map.get(y).charAt(x))) {
                    char current = Map.get(y).charAt(x);
                    switch (current) {
                        case 'M':
                            gridMap.put(new Key(x, y), new Room("Enemy", floor, top, bot, x, y));
                            break;
                        case '?':
                            gridMap.put(new Key(x, y), new Room("Unknown", floor, top, bot, x, y));
                            break;
                        case 'R':
                            gridMap.put(new Key(x, y), new Room("Rest", floor, top, bot, x, y));
                            break;
                        case 'E':
                            gridMap.put(new Key(x, y), new Room("Elite", floor, top, bot, x, y));
                            break;
                        case '$':
                            gridMap.put(new Key(x, y), new Room("Merchant", floor, top, bot, x, y));
                            break;
                        case 'T':
                            gridMap.put(new Key(x, y), new Room("Treasure", floor, top, bot, x, y));
                            break;
                        default:
                            break;
                    }

                }
            }
        }
        steps = new ArrayList<>();
        for (int j = 1; j < Map.size() - 1; j = j + 2) {

            for (int x = 0; x < Map.get(j).length(); x++) {

                if (!Character.isWhitespace(Map.get(j).charAt(x))) {
                    char current = Map.get(j).charAt(x);
                    switch (current) {
                        case '/':
                            // Parent x + 2 , j - 1 
                            //   /    x     , j
                            //Child   x - 1 , j + 1
                            gridMap.get(new Key(x + 2, j - 1)).addChildren(gridMap.get(new Key(x - 1, j + 1)));
                            //extra for gui:
                            steps.add(drawMapStepsGUI(current, x, j));

                            break;
                        case '|':
                            // Parent x , j - 1 
                            //   |    x , j
                            // Child  x , j + 1
                            gridMap.get(new Key(x, j - 1)).addChildren(gridMap.get(new Key(x, j + 1)));
                            //extra for gui:
                            steps.add(drawMapStepsGUI(current, x, j));
                            break;
                        case '\\':
                            // Parent x - 2 , j - 1 
                            //   \    x     , j
                            //  Child x + 1 , j + 1
                            gridMap.get(new Key(x - 2, j - 1)).addChildren(gridMap.get(new Key(x + 1, j + 1)));
                            //extra for gui:
                            steps.add(drawMapStepsGUI(current, x, j));
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        for (int i = 0; i < maximumX; i++) {
            if (gridMap.containsKey(new Key(i, 0))) {
                traverse(gridMap.get(new Key(i, 0)));
            }
        }
        //findBestRoutes();
    }

    private void traverse(Room root) {
        traverse(root, new ArrayList<>());
    }

    private void traverse(Room room, ArrayList<Room> path) {
        path.add(room);

        if (room.isBottom()) {
            Routes.add(new Route(path, Routes.size())); //route + id might crash
            //Routes.get(Routes.size() - 1).evaluateRoute();
        } else {
            for (Room node : room.getNodes()) {
                traverse(node, new ArrayList<>(path));
            }
        }
    }

    public ArrayList<Integer> findBestRoutes(String[] priorities, boolean skipElites) {
        if (currentAlgorithm == 0) {
            return f1(priorities, skipElites);
        } else {
            return f2(priorities, skipElites);
        }
    }

    private ArrayList<Integer> f1(String[] priorities, boolean skipElites) {
        //LOG
        Main.log.add("New route finds: " + "\n");
        Main.log.add("Priorities: " + "\n");
        Main.log.add(priorities[0] + ", ");
        Main.log.add(priorities[1] + ", ");
        Main.log.add(priorities[2] + ", ");
        Main.log.add(priorities[3] + ", ");
        Main.log.add(priorities[4] + ", ");
        Main.log.add(priorities[5] + "\n");
        //
        int max = 0;
        Route.skipElites = skipElites;
        Route.weightSetter(priorities);
        ArrayList<Route> copyRoutes;
        bestRoutes = new ArrayList<>();
        if (skipElites) {
            int min = 15;
            for (Route route : Routes) {
                if (route.getStats()[1] < min) {
                    bestRoutes.removeAll(bestRoutes);
                    bestRoutes.add(route);
                    min = route.getStats()[1];
                } else if (route.getStats()[1] == min) {
                    bestRoutes.add(route);
                }
            }
            copyRoutes = new ArrayList<>(bestRoutes);
            for (int i = 0; i < 4; i++) {
                max = 0;
                bestRoutes.removeAll(bestRoutes);
                for (Route route : copyRoutes) {
                    if (route.getStats()[Route.weights[i]] > max) {
                        bestRoutes.removeAll(bestRoutes);
                        bestRoutes.add(route);
                        max = route.getStats()[Route.weights[i]];
                    } else if (route.getStats()[Route.weights[i]] == max) {
                        bestRoutes.add(route);
                    }
                }
                copyRoutes = new ArrayList<>(bestRoutes);
            }
        } else {
            copyRoutes = new ArrayList<>(Routes);
            for (int i = 0; i < 6; i++) {
                max = 0;
                bestRoutes.removeAll(bestRoutes);
                for (Route route : copyRoutes) {
                    if (route.getStats()[Route.weights[i]] > max) {
                        bestRoutes.removeAll(bestRoutes);
                        bestRoutes.add(route);
                        max = route.getStats()[Route.weights[i]];
                    } else if (route.getStats()[Route.weights[i]] == max) {
                        bestRoutes.add(route);
                    }
                }
                copyRoutes = new ArrayList<>(bestRoutes);
            }
        }
        best2Routes = new ArrayList<>();
        for (Route route : bestRoutes) {
            if (!best2Routes.isEmpty()) {
                boolean alreadyExists = false;
                for (Route ro : best2Routes) {
                    if (route.route2str().equals(ro.route2str())) {
                        alreadyExists = true;
                        break;
                    }
                }
                if (!alreadyExists) {
                    best2Routes.add(route);
                    route.printRoute();
                }
            } else {
                best2Routes.add(route);
                route.printRoute();
            }

        }
        bestRoutes = new ArrayList<>(best2Routes);
        return best;
    }

    private ArrayList<Integer> f2(String[] priorities, boolean skipElites) {
        //LOG
        Main.log.add("New route finds: " + "\n");
        Main.log.add("Priorities: " + "\n");
        Main.log.add(priorities[0] + ", ");
        Main.log.add(priorities[1] + ", ");
        Main.log.add(priorities[2] + ", ");
        Main.log.add(priorities[3] + ", ");
        Main.log.add(priorities[4] + ", ");
        Main.log.add(priorities[5] + "\n");
        //
        double max = 0;
        best = new ArrayList<>(); //ids
        bestRoutes = new ArrayList<>();
        Route.skipElites = skipElites;
        Route.weightSetter(priorities);
        for (int i = 0; i < 5; i++) { // max 4 elites if skip elites is true only then it will iterate more than once
            for (Route route : Routes) {
                route.evaluateRoute(i);
                if (route.getScore() > max) {
                    best.removeAll(best);
                    best.add(route.getID());
                    max = route.getScore();
                } else if (route.getScore() == max) {
                    best.add(route.getID());
                }
            }
            if (!best.isEmpty()) {
                break;
            }else{
                System.out.println("Couldnt skip elite.");
            }
            
        }

        for (Integer id : best) {
            for (Route route : Routes) {
                if (route.getID() == id) {
                    if (!bestRoutes.isEmpty()) {
                        boolean alreadyExists = false;
                        for (Route ro : bestRoutes) {
                            if (route.route2str().equals(ro.route2str())) {
                                alreadyExists = true;
                                break;
                            }
                        }
                        if (!alreadyExists) {
                            bestRoutes.add(route);
                            route.printRoute();
                        }
                    } else {
                        bestRoutes.add(route);
                        route.printRoute();
                    }

                }
            }
        }

        return best;

    }

    public ArrayList<Route> getBestRoutes() {
        return bestRoutes;
    }

    public String drawMapNodesGUI(int x, int y) {
        if (gridMap.containsKey(new Key(x * 3 + 7, y / 2))) {
            return gridMap.get(new Key(x * 3 + 7, y / 2)).getType();
        } else {
            return null;
        }
    }

    public Integer[] drawMapStepsGUI(char s, int x, int y) {
        switch (s) {
            case '|':
                return new Integer[]{1, x - 7, (y * 2) - 1, (x - 7), y * 2, (x - 7), (y * 2) + 1};
            case '/':
                return new Integer[]{0, (x - 7) + 1, (y * 2) - 1, (x - 7), (y * 2), (x - 7) - 1, (y * 2) + 1};
            case '\\':
                return new Integer[]{2, (x - 7) - 1, (y * 2) - 1, (x - 7), (y * 2), (x - 7) + 1, (y * 2) + 1};
            default:
                break;
        }
        return null;
    }

    public ArrayList<Integer[]> requestSteps() {
        return steps;
    }

    private void traverse2(Room root, int floor, Room node) {
        traverse2(root, new ArrayList<>(), floor, node);
    }

    private void traverse2(Room room, ArrayList<Room> path, int floor, Room node) {
        path.add(room);

        if (room.getFloor() == floor && room.equals(node)) {

            routesFromCurrentNode.add(new Route(path, Routes.size() + routesFromCurrentNode.size())); //route + id might crash
            //Routes.get(Routes.size() - 1).evaluateRoute();
        } else {
            for (Room node2 : room.getNodes()) {
                traverse2(node2, new ArrayList<>(path), floor, node);
            }
        }
    }

    public void routeFromCurrentNode(Room node, int routeIndex) {
        routesFromCurrentNode = new ArrayList<>();
        currentRouteSelectedInGUI = bestRoutes.get(routeIndex);
        int floor = node.getFloor();
        for (int i = 0; i < maximumX; i++) {
            if (gridMap.containsKey(new Key(i, 0))) {
                traverse2(gridMap.get(new Key(i, 0)), floor, node);
            }
        }
    }

    public ArrayList<Integer> findBestRoutesFromCurrentNode(String[] priorities, boolean skipElites) {
        if (currentAlgorithm == 0) {
            return f1c(priorities, skipElites);
        } else {
            return f2c(priorities, skipElites);
        }
    }

    private ArrayList<Integer> f1c(String[] priorities, boolean skipElites) {
        //LOG
        Main.log.add("New route finds: " + "\n");
        Main.log.add("Priorities: " + "\n");
        Main.log.add(priorities[0] + ", ");
        Main.log.add(priorities[1] + ", ");
        Main.log.add(priorities[2] + ", ");
        Main.log.add(priorities[3] + ", ");
        Main.log.add(priorities[4] + ", ");
        Main.log.add(priorities[5] + "\n");
        //
        int max = 0;
        Route.skipElites = skipElites;
        Route.weightSetter(priorities);
        ArrayList<Route> copyRoutes;
        bestRoutes = new ArrayList<>();
        if (skipElites) {
            int min = 15;
            for (Route route : routesFromCurrentNode) {
                if (route.getStats()[1] < min) {
                    bestRoutes.removeAll(bestRoutes);
                    bestRoutes.add(route);
                    min = route.getStats()[1];
                } else if (route.getStats()[1] == min) {
                    bestRoutes.add(route);
                }
            }
            copyRoutes = new ArrayList<>(bestRoutes);
            for (int i = 0; i < 4; i++) {
                max = 0;
                bestRoutes.removeAll(bestRoutes);
                for (Route route : copyRoutes) {
                    if (route.getStats()[Route.weights[i]] > max) {
                        bestRoutes.removeAll(bestRoutes);
                        bestRoutes.add(route);
                        max = route.getStats()[Route.weights[i]];
                    } else if (route.getStats()[Route.weights[i]] == max) {
                        bestRoutes.add(route);
                    }
                }
                copyRoutes = new ArrayList<>(bestRoutes);
            }
        } else {
            copyRoutes = new ArrayList<>(routesFromCurrentNode);
            for (int i = 0; i < 6; i++) {
                max = 0;
                bestRoutes.removeAll(bestRoutes);
                for (Route route : copyRoutes) {
                    if (route.getStats()[Route.weights[i]] > max) {
                        bestRoutes.removeAll(bestRoutes);
                        bestRoutes.add(route);
                        max = route.getStats()[Route.weights[i]];
                    } else if (route.getStats()[Route.weights[i]] == max) {
                        bestRoutes.add(route);
                    }
                }
                copyRoutes = new ArrayList<>(bestRoutes);
            }
        }
        best2Routes = new ArrayList<>();
        for (Route route : bestRoutes) {
            if (!best2Routes.isEmpty()) {
                boolean alreadyExists = false;
                for (Route ro : best2Routes) {
                    if (route.route2str().equals(ro.route2str())) {
                        alreadyExists = true;
                        break;
                    }
                }
                if (!alreadyExists) {
                    best2Routes.add(route);
                    route.printRoute();
                }
            } else {
                best2Routes.add(route);
                route.printRoute();
            }

        }

        ArrayList<Route> concatedRoutes = new ArrayList<>();
        for (Route route : best2Routes) {
            concatedRoutes.add(concatRoutes(route, currentRouteSelectedInGUI));
        }
        int index = 0;
        for(Route route : concatedRoutes){
            for(Route main : Routes){
                if(route.getRoute().equals(main.getRoute())){
                    concatedRoutes.set(index,main);
                    break;
                }
            }
            index++;
        }
        
        bestRoutes = new ArrayList<>(concatedRoutes);
        return best;
    }

    private ArrayList<Integer> f2c(String[] priorities, boolean skipElites) {
        //LOG
        Main.log.add("New route finds: " + "\n");
        Main.log.add("Priorities: " + "\n");
        Main.log.add(priorities[0] + ", ");
        Main.log.add(priorities[1] + ", ");
        Main.log.add(priorities[2] + ", ");
        Main.log.add(priorities[3] + ", ");
        Main.log.add(priorities[4] + ", ");
        Main.log.add(priorities[5] + "\n");
        //
        double max = 0;
        best = new ArrayList<>(); //ids
        bestRoutes = new ArrayList<>();
        Route.skipElites = skipElites;
        Route.weightSetter(priorities);
        for (int i = 0; i < 5; i++) {
            for (Route route : routesFromCurrentNode) {
                route.evaluateRoute(i);
                if (route.getScore() > max) {
                    best.removeAll(best);
                    best.add(route.getID());
                    max = route.getScore();
                } else if (route.getScore() == max) {
                    best.add(route.getID());
                }
            }
            if(!best.isEmpty()){
                break;
            }else{
                System.out.println("Couldnt skip elite.");
            }
        }

        for (Integer id : best) {
            for (Route route : routesFromCurrentNode) {
                if (route.getID() == id) {
                    if (!bestRoutes.isEmpty()) {
                        boolean alreadyExists = false;
                        for (Route ro : bestRoutes) {
                            if (route.route2str().equals(ro.route2str())) {
                                alreadyExists = true;
                                break;
                            }
                        }
                        if (!alreadyExists) {
                            bestRoutes.add(route);
                            route.printRoute();
                        }
                    } else {
                        bestRoutes.add(route);
                        route.printRoute();
                    }

                }
            }
        }
        ArrayList<Route> concatedRoutes = new ArrayList<>();
        for (Route route : bestRoutes) {
            concatedRoutes.add(concatRoutes(route, currentRouteSelectedInGUI));
        }
        int index = 0;
        for(Route route : concatedRoutes){
            for(Route main : Routes){
                if(route.getRoute().equals(main.getRoute())){
                    concatedRoutes.set(index,main);
                    break;
                }
            }
            index++;
        }

        bestRoutes = new ArrayList<>(concatedRoutes);
        return best;
    }

    private Route concatRoutes(Route newRoute, Route oldRoute) {
        for(int i = newRoute.getRoute().size(); i<oldRoute.getRoute().size();i++){
            newRoute.getRoute().add(oldRoute.getRoute().get(i));
        }
        return newRoute;
    }
}
