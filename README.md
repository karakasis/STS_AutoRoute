# STS_AutoRoute
*Assisting program written on Java for [Slay the Spire](http://store.steampowered.com/app/646570/Slay_the_Spire/).*

*Get the [latest Version](https://github.com/karakasis/STS_AutoRoute/releases/tag/v1.2), updated May 9th 2018.*

![alt text](https://raw.githubusercontent.com/karakasis/STS_AutoRoute/master-project/new_images/tutorial/STS%20Screenshots/2.png)
![alt text](https://raw.githubusercontent.com/karakasis/STS_AutoRoute/master-project/new_images/tutorial/STS%20Screenshots/1.png)
![alt text](https://raw.githubusercontent.com/karakasis/STS_AutoRoute/master-project/new_images/tutorial/STS%20Screenshots/3.png)



## Launch

- Run STS_AutoRoute.exe or STS_AutoRoute.jar (Java required)
- Select this file : SlayTheSpire.log located within your Steam folder. 
**C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\sendToDevs\logs\SlayTheSpire.log**
- Open Slay the Spire and start a new run.
- A pop up will appear, asking to load the layout, click yes.
- Optimize preferences and find route.
- After you finish a run and start a new one, a new layout will ask to get loaded.

## Support me

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=XKQ7R4AWWVFR4)

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
  
  
  *Some final words,*
  
    I am a pre graduate Student of Computer Science and this project is far from being professional or completed.
    **I am no longer playing this game so if any you find any bug or any change in the game messes up with the code let me know and I
    will try to fix it. **
    As far as future update of this, I am not aware at this point, since I am in 
    university and I might not find time to update. If anyone is interested in helping, improving or anything on this project let me
    know!
   
