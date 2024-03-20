import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class Pillar
{
   private double gapBetweenPillars;
   private double x = 500;
   private double y1 = 0;
   private double y2;
   private double height;
   private static double width = 60;
   private boolean isCounted = false;

   public Pillar()
   {
      Random rand = new Random();
      gapBetweenPillars = (500 - rand.nextInt(50) - 370);
      //gapBetweenPillars = 200;
      height = (500 - gapBetweenPillars) / 2; 
      y2 = height + gapBetweenPillars;
   }
   
   public double getGapBetweenPillars()
   {
      return gapBetweenPillars;
   }
   
   public void draw(GraphicsContext gc)
   {
      gc.clearRect(x, y1, width, 50);
      gc.setFill(Color.GREEN);
      gc.fillRect(x, y1, width, height);
      gc.fillRect(x, y2, width, height);
      x-=1;
   }
   
   public double getX()
   {
      return x;
   }
   
   public static double getWidth()
   {
      return width;
   }
   
   public void setIsCounted(boolean isCounted)
   {
      this.isCounted = isCounted;
   }
   
   public boolean getIsCounted()
   {
      return isCounted;
   }
   
   public boolean checkBoundaries(double xBird, double yBird)
   {
      if(xBird >= x && xBird <= x + width)
      {
         if(yBird >= y1 && yBird <= y1 + height)
         {
            return true;
         }
         else if(yBird+25 >= y1 && yBird+25 <= y1 + height)
         {
            return true;
         }
         else if(yBird >= y2 && yBird <= y2 + height)
         {
            return true;
         }
         else if(yBird+25 >= y2 && yBird+25 <= y2 + height)
         {
            return true;
         }
      }
      else if(xBird+25 >= x && xBird+25 <= x + width)
      {
         if(yBird >= y1 && yBird <= y1 + height)
         {
            return true;
         }
         else if(yBird+25 >= y1 && yBird+25 <= y1 + height)
         {
            return true;
         }
         else if(yBird >= y2 && yBird <= y2 + height)
         {
            return true;
         }
         else if(yBird+25 >= y2 && yBird+25 <= y2 + height)
         {
            return true;
         }
      }
      else if(yBird >= 475 || yBird <= 0)
      {
         return true;
      }
      return false;
   }
}