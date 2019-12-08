
#dartApp
##JavaFX Application for dart players. The purpose of application is to count points. Players can define throws by writing in textFields or click od interactive dart board.
![board](https://user-images.githubusercontent.com/55205387/70396825-ba8db400-1a0c-11ea-9b30-c15f12476442.png)

##Application includes 3 types of games:
**Regular '01 game** which means players throw down points from setted amount eg 501 to 0. Player which do that first win. The             condition of win is that u have to have exacly 0 points (you cant overthrow, eg. throw 7 if u have 5 points) and if double out option is setted you have to finish with double field.( for example if u have 10 points u have to throw "double 5" field).

**Cricket**, Players are throwing to the defined field eg. "20" if player have 3 hits in this field, every another hit will add to the amount of points. If all players have at least 3 trows to the field, the field changes to the next one. If all of the fields (players choose 7 of them) are closed (at least 3 hits) by player and this player have the biggest amount of points, he wins the game. 
Player cant start throw to the next field if previous its not closed.

**Master Cricket** its similiar to cricket, with one difference. After 3 throws in a field, player adds points to the other players which didnt close the current field yet. The player who closed all the fields and have the least amount of point win the game.

**I prepared for you _2 compiled_ version of this app:**

**1. [jdk & jfx needed](https://drive.google.com/open?id=1Ub9V1EG1TcAqG3ePmD77lEgeMrtP3Yvx)  (jdk/jre and jfx needed)**
somehow, app dont see included sounds.

**2. [StandAloneVersion](https://drive.google.com/open?id=1EMD6uqESaXnmacwcTstW8WAxtyP3Zrii)  (standalone app- jdk and jfx included)**
still some problems with resources while deploying, sounds could not work in this version(on my PC it works fine)

**1.You need** have **jfx13** with "PATH_TO_FX" environment variable setted to "...\javafx-sdk-13\lib" directory and java_home and path to **jdk11**. 
On others versions of jdk and jfx it could not work.
After downloading, unzip files and run app through .bat file

**2. All you need is to download pack** and **run app** throung .bat shortcut in root dir or .../bin/launchApp.bat file.
All jdk and jfx files are included in pack. Thats why app has 40+ MB :D.


### My Conclusion

To be honest, this is my first "larger" project and **I wrote this much better than i expected**, **i've learned much more than i expected** and **it's  _possible to write_ this much better**
 than i did :D
