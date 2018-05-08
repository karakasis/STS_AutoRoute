# STS_AutoRoute
*Assisting program written on Java for [Slay the Spire](http://store.steampowered.com/app/646570/Slay_the_Spire/).*

![alt text](https://raw.githubusercontent.com/karakasis/STS_AutoRoute/master-project/new_images/tutorial/STS%20Screenshots/2.png)
![alt text](https://raw.githubusercontent.com/karakasis/STS_AutoRoute/master-project/new_images/tutorial/STS%20Screenshots/1.png)
![alt text](https://raw.githubusercontent.com/karakasis/STS_AutoRoute/master-project/new_images/tutorial/STS%20Screenshots/3.png)



## Launch

  (No need for restart anymore just look at UPDATE#2 below.)
  
- Run STS_AutoRoute.exe or STS_AutoRoute.jar (Java required)
- Select this file : SlayTheSpire.log located within your Steam folder. (BETA: \sendToDevs\logs\SlayTheSpire.log)
- Open Slay the Spire and start a new run.
- From the moment you see the map of the run, close the game and a pop up will appear. 
- Load the new Layout on the app, and then open the game back either from Steam or from the top left Steam icon.
- Optimize preferences and find route.
- After every boss kill you will have to repeat steps 4-5. (Or wait until log is refreshed but that would take a while)
(If you delete log.txt created within the root folder of the .jar then step 2 will repeat in the next launch.)

## Support me

<form action="paypal.me/karakasisx" method="post" target="_top">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="RGQ8NSYPA59FL">
<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/pt_BR/i/scr/pixel.gif" width="1" height="1">
</form>

## Tutorial
  
  Algorithms : 
  - *Brute Force* :  
     Define priorities by setting the combo boxes on the right side of the icons on the Set Priorities Panel.
     Example -> - Rest 1
                - Elite 2
                - Merchant 4
                - Unknown 3
                - Enemy 5
     This will find the routes that have most Rest sites and exclude ALL other routes with less rest sites than the specified maximum.
     Then do the same thing with the remaining routes and keep only those with the most Elites and exclude ALL other routes.
     Repeat until all priorities have been consumed.
  - *Weighted Score* :  
     A more fluid aproach. After defining priorities the algorithm will assign weights to every category of rooms.
     Based on your priorities a score will be given to every route and the best scores will then be selected and previewed.
     
 (You can swap/toggle algorithms from the top banner or the Settings icon > Change Algorithm)
  
  - Skip Elite Box :  
  Will try to avoid Elites if possible (in some cases you can't skip an Elite fight)
  If it is possible, then it will find the optimal routes based on the Algorithm set and the priorities.  
  - Route from Current Node :  
  After finding a route, you can click on the rooms you have visited, effectivelly making a "path" of where you have been.  
  In any point you can choose to find a new route, suited on your current situation by going through the Settings icon to Route
  from Current Node. This will redirect you back to Set Priorities (explained above on Algorithms Section). By finding a new Route 
  your current path will not get erased.  
  - Change Route :  
  In some cases you will be given multiple (mostly 2 from what I have experienced) routes. When this is possible a visual icon will 
  pop-up on the left of your screen with the Slay the Spire map icon and 2 arrows. You can change the routes from the arrows or from the
  Settings icon > Change Route.  
  
  ## Big update

  UPDATE #2
  Everything runs smoothly! I can't beleive it works properly now. BETA or Regular branch just update the log file path to 
  
  
  **C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\sendToDevs\logs\SlayTheSpire.log**
  
  and no need for restart, everything is automatic now! Have fun.
  
  UPDATE #1
  Great news!!
  Looks like the issue with the log files is all gone now. If you are in the beta the working log SlayTheSpire.log
  is located in your root Steam folder 
  
  C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\sendToDevs\logs\SlayTheSpire.log
  
  Make sure to delete your old setting log.txt, and replace with the new path above!
  No restarts are needed! 
  
  *Some final words,*
  
    I am a post graduate Student of Computer Science and this project is far from being professional or completed. Just an attempt of
    mine, honing my skills in Java and having fun. I have been using this app for some days now and it can be relaxing in some
    situations when I am too lazy to plan my route. As far as future update of this, I am not aware at this point, since I am in 
    university and I might not find time to update. If anyone is interested in helping, improving or anything on this project let me
    know!
    
    Buggy approach : My knowledge goes as far as creating this and playing around with the log files to make it happen. If there is any
    other possible way to fix the open/close thing I would be more than happy to review .
   
