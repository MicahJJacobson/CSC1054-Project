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
import javafx.scene.text.*;

public class Falling extends Application
{
   StackPane root;
   Canvas theCanvas;
   GraphicsContext gc;
   AnimationHandler ta;
   double velocity = 0;
   double x = 100;
   double y = 0;
   ArrayList<Pillar> pillars;
   int points = 0;
   Label score;
   int size = 25;
   FlowPane endScreen;
   Canvas endScreenCanvas;
   boolean gameEnded = false;
   public void start(Stage stage)
   {
      root = new StackPane();
      theCanvas = new Canvas();
      gc = theCanvas.getGraphicsContext2D();
      ta = new AnimationHandler();
      pillars = new ArrayList<Pillar>();
      score = new Label("0");
      endScreen = new FlowPane();
      endScreenCanvas = new Canvas();
      
      
      endScreen.setAlignment(Pos.CENTER);
      root.setAlignment(Pos.TOP_CENTER);
      
      theCanvas.setWidth(500);
      theCanvas.setHeight(500);
      endScreenCanvas.setWidth(200);
      endScreenCanvas.setHeight(100);
      
      root.setOnKeyPressed(new KeyPressedListener());
      
      root.getChildren().add(theCanvas);
      root.getChildren().add(score);
      
      endScreen.getChildren().add(endScreenCanvas);
      
      Scene scene = new Scene(root, 500, 500);
      stage.setScene(scene);
      stage.setTitle("Falling");
      stage.show();
      
      ta.start();
      root.requestFocus();
   }
   
   public static void main(String[] args)
   {
      launch(args);
   }
   
   public class KeyPressedListener implements EventHandler<KeyEvent>
   {
      public void handle(KeyEvent event)
      {
         /*
         if(event.getCode() == KeyCode.SPACE)
         {
            velocity = -2.5;
         }
         */
         if(gameEnded)
         {
            ta.start();
            root.getChildren().remove(endScreen);
            for(int i = 0; i < pillars.size(); i++)
            {
               pillars.remove(i);
               i--;
            }
            x = 100;
            y = 0;
            velocity = 0;
            points = 0;
            score.setText(""+points);
            gameEnded = false;
         }
         else
         {
            velocity = -2.5;
         }
      }
   }
   
   public class AnimationHandler extends AnimationTimer
   {
      int numTimesRun = 200;
      Pillar current;
      public void handle(long currentTimeInNanoSeconds)
      {
         //
         try
         {
            Thread.sleep(10);
         }
         catch(InterruptedException ie)
         {
            System.out.println("Interrupted");
         }
         catch(Exception e)
         {
            e.printStackTrace();
         }
         //
         gc.clearRect(0, 0, 500, 500);
         velocity+=0.06;
         y += 1 * velocity;
         gc.setFill(Color.PINK);
         gc.fillRect(x, y, size, size);
         if(numTimesRun > 250)
         {
            pillars.add(new Pillar());
            numTimesRun = 0;
         }
         for(int i = 0; i < pillars.size(); i++)
         {
            current = pillars.get(i);
            if(current.getX() + Pillar.getWidth() < 0)
            {
               pillars.remove(i);
               i--;
            }
            else
            {
               current.draw(gc);
               if((current.getX() + current.getWidth()) < x && !current.getIsCounted())
               {
                  points++;
                  System.out.println(points);
                  current.setIsCounted(true);
                  score.setText("" + points);
               }
            }
            if(current.checkBoundaries(x, y))
            {
               ta.stop();
               root.getChildren().add(endScreen);
               gameEnded = true;
               endScreenCanvas.getGraphicsContext2D().clearRect(0, 0, 200, 100);
               endScreenCanvas.getGraphicsContext2D().setFill(Color.PINK);
               endScreenCanvas.getGraphicsContext2D().fillRect(0, 0, 200, 100);
               endScreenCanvas.getGraphicsContext2D().setFill(Color.WHITE);
               endScreenCanvas.getGraphicsContext2D().setFont(Font.font("Verdana", 20));
               endScreenCanvas.getGraphicsContext2D().fillText("GAME OVER", 45, 50, 200);
               endScreenCanvas.getGraphicsContext2D().fillText("SCORE: " + score.getText(), 55, 80, 200);
            }
            //System.out.println(pillars.size());
         }
         numTimesRun++;
      }
   }
}
