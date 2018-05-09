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
public class Controller {

    private GUI _gObj;
    private Tail _tObj;
    private Loading _load;

    public Controller(Tail t, GUI g) {
        _gObj = g;
        _tObj = t;

        _load = new Loading();
        _tObj = new Tail();
        _tObj.setUpTailer(_load.getLogFile(), this);
        
        _tObj.actionIsComing();
        //_load.startSlayTheSpireSTEAM();
    }
    
    public void killSteam(){
        _load.killSlayTheSpireSTEAM();
    }
    
    
    public void _logCheck(ArrayList<String> log) {
        if (checkUniqueMapEntry(log) && _load.parentCallback()) {
            //_tObj._stopTailing();
            if (_gObj != null) {
                _gObj.dispose();
            }
            _gObj = new GUI(log);
            _gObj.setupLayout();
            _gObj.passReference(this);
        }
        _tObj._startTailing();
    }
    
    private boolean checkUniqueMapEntry(ArrayList<String> log){
        if(_gObj == null){
            return true;
        }
        boolean mappedInput = false;
        boolean mapFound = false;
        ArrayList<String> mapList = new ArrayList<>();
        for (String str : log) {
            if (str.contains("Game Seed:")) {
                break;
            }
            if (mapFound && !mappedInput) {
                mapList.add(str);
            }

            if (str.contains("Generated the following dungeon map:")) {
                mapFound = true;
            }
        }
        mapList.remove(0);
        
        return !mapList.equals(_gObj.getCurrentLoadedMap());
    }

}
