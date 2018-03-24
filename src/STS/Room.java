/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package STS;

import java.util.ArrayList;

/**
 *
 * @author Xrhstos
 */
public class Room {

    private String type;
    private int floor;
    private boolean isTop;
    private boolean isBottom;
    private ArrayList<Room> children;
    private ArrayList<Room> parent;
    private int internalIndexer;
    private int x;
    private int y;
    private boolean hasLeft, hasRight, hasBottom;
    private int[] xyInMap;

    public Room(String type, int floor, boolean top, boolean bot, int x, int y) {
        this.type = type;
        this.floor = floor;
        isTop = top;
        isBottom = bot;
        children = new ArrayList<>();
        parent = new ArrayList<>();
        internalIndexer = 0;
        this.x = x;
        this.y = y;
        hasLeft= false; hasRight = false; hasBottom = false;
        xyInMap = new int[]{-1,-1};
    }

    public boolean isBottom() {
        return isBottom;
    }

    public void addChildren(Room room) {
        if(room.getX()>this.x){
            hasRight = true;
        }else if(room.getX()<this.x){
            hasLeft = true;
        }else{
            hasBottom = true;
        }
        
        children.add(room);
        room.addParent(this);
    }

    public void addParent(Room room) {
        parent.add(room);
    }

    public ArrayList<Room> getNodes() {
        return children;
    }
    
    public ArrayList<Room> getParents() {
        return parent;
    }
    
    public String getType(){
        return type;
    }
    
    public Integer getX(){
        return x;
    }
    
    public Integer getY(){
        return y;
    }
    
    public boolean hasLeft(){
        return hasLeft;
    }
    
    public boolean hasRight(){
        return hasRight;
    }
    
    public boolean hasBottom(){
        return hasBottom;
    }
    
    public void setXYInMap(int[] xy){
        xyInMap = xy;
    }
    
    public int[] getXYInMap(){
        return xyInMap;
    }
    
    public int getFloor(){
        return floor;
    }
}
