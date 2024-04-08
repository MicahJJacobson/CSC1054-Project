import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;

//I know it's not really drawable but this makes things easier
public class Grid extends DrawableObject
{
   //will hold all of the mines
   ArrayList<Mine> minesInGrid;
   //will hold the grid positions (aka the x / 100 and y / 100)
   int gridX;
   int gridY;
   
   static Random rand = new Random();

   public Grid(float x, float y)
   {
      super(x, y);
      //used to randomize the 30% chance of mine
            minesInGrid = new ArrayList<>();
      //just so that I can use it as the origin
      Player startingPoint = new Player(300, 300);
      int n = ((int)this.distance(startingPoint))/1000;
      //System.out.println(n);
      gridX = ((int)getX()) / 100;
      gridY = ((int)getY()) / 100;
      for(int i = 0; i < n; i++)
      {
         if((rand.nextInt(100) + 1) <= 30)
         {
            minesInGrid.add(new Mine(getX() + (rand.nextFloat() * 100),getY() + (rand.nextFloat() * 100), rand));
         }
      }
   }
   
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      float currentX;
      float currentY;
      for(int i = 0; i < minesInGrid.size(); i++)
      {
         currentX = minesInGrid.get(i).getX();
         currentY = minesInGrid.get(i).getY();
         minesInGrid.get(i).drawMe(x + currentX, y + currentY, gc);
      }
   }
   
   public ArrayList<Mine> getMines()
   {
      return minesInGrid;
   }
}