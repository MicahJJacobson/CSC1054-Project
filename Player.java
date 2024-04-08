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

public class Player extends DrawableObject
{
   //w, a, s, and d hold whether or not the corresponding key is being pressed
   private boolean w = false;
   private boolean a = false;
   private boolean s = false;
   private boolean d = false;
   //the velocities of the player
   private float xVelocity = 0;
   private float yVelocity = 0;
   //holds whether or not the mine has been hit by the player
   private boolean wasHit = false;
   
   public Player(float x, float y)
   {
      super(x, y);
   }
  
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      if(!wasHit)
      {
         //black outline of the player
         gc.setFill(Color.BLACK);
         gc.fillOval(x-14,y-14,27,27);
         //inner part of the player
         gc.setFill(Color.GRAY);
         gc.fillOval(x-13,y-13,25,25);
      }
   }
   
   public void act()
   {
      //if w is being pressed
      if(w)
      {
         //if the yVelocity is greater than or equal to -4.9, decrese yVelocity by 0.1
         //otherwise, set yVelocity to -5 if s is not being pressed
         if(yVelocity >= -4.9)
         {
            yVelocity -= 0.1;
         }
         else if(!s)
         {
            yVelocity = -5;
         }
      }
      //if a is being pressed
      if(a)
      {
         //if the xVelocity is greater than or equal to -4.9, decrese xVelocity by 0.1
         //otherwise, set xVelocity to -5 if d is not being pressed
         if(xVelocity >= -4.9)
         {
            xVelocity -= 0.1;
         }
         else if(!d)
         {
            xVelocity = -5;
         }
      }
      //if s is being pressed
      if(s)
      {
         //if the yVelocity is less than or equal to 4.9, increase yVelocity by 0.1
         //otherwise, set yVelocity to 5 if s is not being pressed
         if(yVelocity <= 4.9)
         {
            yVelocity += 0.1;
         }
         else if(!w)
         {
            yVelocity = 5;
         }
      }
      //if d is being pressed
      if(d)
      {
         //if the xVelocity is less than or equal to 4.9, increase xVelocity by 0.1
         //otherwise, set xVelocity to 5 if a is not being pressed
         if(xVelocity <= 4.9)
         {
            xVelocity += 0.1;
         }
         else if(!a)
         {
            xVelocity = 5;
         }
      }
      //if neither w or s are being pressed
      if(!(w || s))
      {
         //if yVelocity is greater than 0.25, decrease yVelocity by 0.025
         if(yVelocity > 0.25)
         {
            yVelocity -= 0.025;
         }
         //if yVelocity is less than -0.25, increase yVelocity by 0.025
         else if(yVelocity < -0.25)
         {
            yVelocity += 0.025;
         }
         //otherwise, set yVelocity to 0
         else
         {
            yVelocity = 0;
         }
      }
      //if neither a or d are being pressed
      if(!(a || d))
      {
         //if xVelocity is greater than 0.25, decrease xVelocity by 0.025
         if(xVelocity > 0.25)
         {
            xVelocity -= 0.025;
         }
         //if xVelocity is less than -0.25, increase xVelocity by 0.025
         else if(xVelocity < -0.25)
         {
            xVelocity += 0.025;
         }
         //otherwise, set xVelocity to 0
         else
         {
            xVelocity = 0;
         }
      }
      
      setX(getX() + xVelocity);
      setY(getY() + yVelocity);
      
      //System.out.println(score);
   }
   
   //used to change the value of whether or not the w key is being pressed
   public void up(boolean w)
   {
      this.w = w;
   }
   
   //used to change the value of whether or not the a key is being pressed
   public void left(boolean a)
   {
      this.a = a;
   }
   
   //used to change the value of whether or not the s key is being pressed
   public void down(boolean s)
   {
      this.s = s;
   }
   
   //used to change the value of whether or not the d key is being pressed
   public void right(boolean d)
   {
      this.d = d;
   }
   
   public boolean getW()
   {
      return w;
   }
   
   public boolean getA()
   {
      return a;
   }
   
   public boolean getS()
   {
      return s;
   }
   
   public boolean getD()
   {
      return d;
   }
   
   public void addToXVelocity(double numAdding)
   {
      xVelocity += numAdding;
   }
   
   public void addToYVelocity(double numAdding)
   {
      xVelocity += numAdding;
   }
   
   public float getXVelocity()
   {
      return xVelocity;
   }
   
   public float getYVelocity()
   {
      return yVelocity;
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