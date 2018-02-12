# STS_AutoRoute
Assisting program written on Java for Slay the Spire from Mega Crit Games. 

Launch : 
1. Run STS_AutoRoute.exe or STS_AutoRoute.jar (Java required)
2. Select this file : SlayTheSpire.log located within your Steam folder.
3. Open Slay the Spire and start a new run.
4. From the moment you see the map of the run, close the game and a pop up will appear.
5. Load the new Layout on the app, and then open the game back either from Steam or from the top left Steam icon.
6. Optimize preferences and find route.
7. After every boss kill you will have to repeat steps 4-5. (Or wait until log is refreshed but that would take a while)
(If you delete log.txt created within the root folder of the .jar then step 2 will repeat in the next launch.)

Tutorial : 
  Algorithms : 
  1. Brute Force :
     Define priorities by setting the combo boxes on the right side of the icons on the Set Priorities Panel.
     Example -> Rest 1
                Elite 2
                Merchant 4
                Unknown 3
                Enemy 5
     This will find the routes that have most Rest sites and exclude ALL other routes with less rest sites than the specified maximum.
     Then do the same thing with the remaining routes and keep only those with the most Elites and exclude ALL other routes.
     Repeat until all priorities have been consumed.
  2. Weighted Score :
     A more fluid aproach. After defining priorities the algorithm will assign weights to every category of rooms.
     Based on your priorities a score will be given to every route and the best scores will then be selected and previewed.
     
 (You can swap/toggle algorithms from the top banner or the Settings icon > Change Algorithm)
  
  Skip Elite Box : 
  Will try to avoid Elites if possible (in some cases you can't skip an Elite fight)
  If it is possible, then it will find the optimal routes based on the Algorithm set and the priorities.
  
  Route from Current Node :
  After finding a route, you can click on the rooms you have visited, effectivelly making a "path" of where you have been.
  In any point you can choose to find a new route, suited on your current situation by going through the Settings icon to Route 
  from Current Node. This will redirect you back to Set Priorities (explained above on Algorithms Section). By finding a new Route 
  your current path will not get erased.
  
  Change Route :
  In some cases you will be given multiple (mostly 2 from what I have experienced) routes. When this is possible a visual icon will 
  pop-up on the left of your screen with the Slay the Spire map icon and 2 arrows. You can change the routes from the arrows or from the
  Settings icon > Change Route.
  
  Some final words,
    I am a post graduate Student of Computer Science and this project is far from being professional or completed. Just an attempt of
    mine, honing my skills in Java and having fun. I have been using this app for some days now and it can be relaxing in some
    situations when I am too lazy to plan my route. As far as future update of this, I am not aware at this point, since I am in 
    university and I might not find time to update. If anyone is interested in helping, improving or anything on this project let me
    know!
    
    Buggy approach : My knowledge goes as far as creating this and playing around with the log files to make it happen. If there is any
    other possible way to fix the open/close thing I would be more than happy to review .
   
