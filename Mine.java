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

import java.security.SecureRandom;

public class Mine extends DrawableObject
{
   //the variable which holds the color
   private Color redColor;
   //the variable used to interpolate
   private float interpolationVariable;
   //the value of green and blue which will be calculated using the interpolationVariable
   private float colorValue;
   //which way it is interpolating, -1 for going to red and 1 for going to white
   private int way = 1;
   //holds whether or not the mine has been hit by the player
   private boolean wasHit = false;
   static SecureRandom rand2 = new SecureRandom();
   public Mine(float x, float y, Random rand)
   {
      super(x, y);
      //setting the interpolation variable to a random value between 0 and 1

      float interpolationVariable = rand2.nextFloat();
      //initializing colorValue
      colorValue = 1 * interpolationVariable + 0 * (1 - interpolationVariable);
      //initializing redColor
      redColor = new Color(1, colorValue, colorValue, 1);
   }
   
   //draws the mine
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      if(!wasHit)
      {
         updateColor();
         gc.setFill(redColor);
         gc.fillOval(x-6, y-6, 12, 12);
      }
   }
   
   //increments the interpolationVariable and changes the color based on the value
   public void updateColor()
   {
      interpolationVariable += 0.05 * way;
      
      if(interpolationVariable > 1)
      {
         interpolationVariable = 1;
         way = -1;
      }
      else if(interpolationVariable < 0)
      {
         interpolationVariable = 0;
         way = 1;
      }
      colorValue = 1 * interpolationVariable + 0 * (1 - interpolationVariable);
      redColor = new Color(1, colorValue, colorValue, 1);
   }
   
   public void setWasHit(boolean wasHit)
   {
      this.wasHit = wasHit;
   }
   
   public boolean getWasHit()
   {
      return wasHit;
   }
}