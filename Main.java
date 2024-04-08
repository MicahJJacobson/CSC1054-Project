import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;

import java.io.*;

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


public class Main extends Application
{
   FlowPane root;
   //BorderPane root;
   Canvas theCanvas = new Canvas(800,800);
   AnimationHandler ta;
   Player thePlayer;
   double score;
   //Mine m;
   Grid grid;
   int cgridx;
   int cgridy;
   ArrayList<ArrayList<Grid>> grids;
   //hold the grid value of the top left grid
   int topLeftX;
   int topLeftY;
   //keeps track of whether or not a collision has occurred
   boolean collisionOccurred;
   //high score
   int highscore;
   //the GraphicsContext that will draw everything
   GraphicsContext gc;
   
   
   
   //ArrayList<Projectile> projectiles;

   public void start(Stage stage)
   {
      root = new FlowPane();
      root.getChildren().add(theCanvas);
      
      //projectiles = new ArrayList<Projectile>();
      
      collisionOccurred = false;
      
      //setting highscore to the value held in highscore.txt
      try
      {
         Scanner fileReader = new Scanner(new File("highscore.txt"));
         highscore = fileReader.nextInt();
      }
      catch(FileNotFoundException fnfe)
      {
         System.out.println("File not Found");
      }
      catch(NoSuchElementException nsee)
      {
         System.out.println("There is no high score");
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
      
      gc = theCanvas.getGraphicsContext2D();
      //initializing the player
      thePlayer = new Player(300, 300);
      //setting the inital grid values of the player
      cgridx = ((int)thePlayer.getX())/100;
      cgridy = ((int)thePlayer.getY())/100;
      //setting the score to zero
      score = 0;
      //these are loop variables which allow me to make 8 out of the 10 rows and columns since 2 rows and 2 columns will be in the negative
      int numRows = 8;
      int numColumns = 8;
      //this will hold all of the Grids
      grids = new ArrayList<ArrayList<Grid>>();
      
      //this will instantiate all of the Grids in the positive axes
      for(int i = 0; i < numRows; i++)
      {
         grids.add(new ArrayList<Grid>());
         for(int j = 0; j < numColumns; j++)
         {
            //will add a grid with its corresponding x value
            grids.get(i).add(new Grid(i * 100, j * 100));
         }
         grids.get(i).add(0, new Grid(i * 100, -100));
         grids.get(i).add(0, new Grid(i * 100, -200));
      }
      
      //adding two new ArrayLists of Grids at the beginning because they will have grids in the negative axes
      grids.add(0, new ArrayList<Grid>());
      grids.add(0, new ArrayList<Grid>());
      //this will add all of the Grids in the negative axes
      //this looks uncecessarily complicated but every other method I tried did not work for some reason
      for(int i = 1; i <= 2; i++)
      {
         //this will add all of the positive columns in the negative rows
         for(int j = 0; j < numColumns; j++)
         {
            if(i == 1)
            {
               grids.get(i-1).add(new Grid(2 * -100, j * 100));
            }
            else
            {
               grids.get(i-1).add(new Grid(1 * -100, j * 100));
            }
            
         }
         //this if-else will add the negative columns
         if(i == 1)
         {
            grids.get(i-1).add(0, new Grid(2 * -100, -100));
            grids.get(i-1).add(0, new Grid(2 * -100, -200));
         }
         else
         {
            grids.get(i-1).add(0, new Grid(1 * -100, -100));
            grids.get(i-1).add(0, new Grid(1 * -100, -200));
         }
      }
      
      //sets the x and y positions of the top left Grid in the grids 2D ArrayList
      topLeftX = (int)grids.get(0).get(0).getX()/100;
      topLeftY = (int)grids.get(0).get(0).getY()/100;
      
      //setting up the listeners
      root.setOnKeyPressed(new KeyPressedListener());
      root.setOnKeyReleased(new KeyReleasedListener());
      //root.setOnMousePressed(new MousePressedListener());
      
      Scene scene = new Scene(root, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :)");
      stage.show();
      
      ta = new AnimationHandler();
      ta.start();
      root.requestFocus();
   }
   
   
   
   
   
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
	  //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
	//figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
	  //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
	  //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }
   
   
   
   
   
   //the grid currently being handled when looping through grids
   Grid currentGrid;
   //the current mine being handled when looping through the mines in a grid
   Mine currentMine;
   //holds the value of whether or not the file has been written in or not
   boolean isWrittenIn;
   public class AnimationHandler extends AnimationTimer
   {
      //this is a reference player in order to check the distance from the starting point
      Player startingPoint = new Player(300, 300);
      public void handle(long currentTimeInNanoSeconds) 
      {
         gc.clearRect(0,0,600,600);
         
         //USE THIS CALL ONCE YOU HAVE A PLAYER
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 

         
	      //example calls of draw - this should be the player's call for draw
         thePlayer.draw(300,300,gc,true); //all other objects will use false in the parameter.
         
         //changes the values of velocity and changes the player's position depending on that
         thePlayer.act();
         
         //this will check if the player has moved from one grid to another and will shift the arraylist grids positions depending on which
         //direction the player moves
         if(((int)thePlayer.getX())/100 != cgridx || ((int)thePlayer.getY())/100 != cgridy)
         {
            //this will run if the player moves to a grid to the left
            if(((int)thePlayer.getX())/100 < cgridx)
            {
               topLeftX--;
               for(int i = 0; i < 10; i++)
               {
                  for(int j = 9; j > 0; j--)
                  {
                     grids.get(i).set(j, grids.get(i).get(j-1));
                  }
               }
            }
            //this will run if the player moves to a grid to the right
            if(((int)thePlayer.getX())/100 > cgridx)
            {
               topLeftX++;
               for(int i = 0; i < 10; i++)
               {
                  for(int j = 0; j < 9; j++)
                  {
                     grids.get(i).set(j, grids.get(i).get(j+1));
                  }
               }
            }
            //this will run if the player moves to a grid to the top
            if(((int)thePlayer.getY())/100 < cgridy)
            {
               topLeftY--;
               for(int i = 9; i > 0; i--)
               {
                  for(int j = 0; j < 10; j++)
                  {
                     grids.get(i).set(j, grids.get(i-1).get(j));
                  }
               }
            }
            //this will run if the player moves to a grid to the bottom
            if(((int)thePlayer.getY())/100 > cgridy)
            {
               topLeftY++;
               for(int i = 0; i < 9; i++)
               {
                  for(int j = 0; j < 10; j++)
                  {
                     grids.get(i).set(j, grids.get(i+1).get(j));
                  }
               }
            }
            //the next four for loops will create new Grids on each edge of the 2D arraylist grids every single time the player moves
            //to another grid
            for(int i = 0; i < 10; i++)
            {
               grids.get(0).set(i, new Grid((topLeftX + i) * 100, topLeftY * 100));
            }
            for(int i = 0; i < 10; i++)
            {
               grids.get(9).set(i, new Grid((topLeftX + i) * 100, (topLeftY + 9) * 100));
            }            
            for(int i = 1; i < 9; i++)
            {
               grids.get(i).set(0, new Grid(topLeftX * 100, (topLeftY + i) * 100));
            }
            for(int i = 1; i < 9; i++)
            {
               grids.get(i).set(9, new Grid((topLeftX + 9) * 100, (topLeftY +  i) * 100));
            }
            
         }
         
         //sets the score to the distance from the player to the origin
         score = thePlayer.distance(startingPoint);
         gc.setFill(Color.WHITE);
         //displays the score and highscore in the top left corner of the screen
         gc.fillText("Score: " + (int)score, 50, 50);
         if(collisionOccurred)
         {
            highscore = (int)score;
         }
         gc.fillText("Highscore: " + (int)highscore, 50, 80);
         
         //draws every single grid in the grids array which will call the draw method for each of their mines
         for(int i = 0; i < grids.size(); i++)
         {
            for(int j = 0; j < grids.get(i).size(); j++)
            {
               grids.get(i).get(j).draw(thePlayer.getX(), thePlayer.getY(), gc, false);
            }
         }
         
         //setting the cgridx and cgridy values to a new value
         cgridx = ((int)thePlayer.getX())/100;
         cgridy = ((int)thePlayer.getY())/100;
         
         //if a collision has occurred, stop the AnimationHandler, and if there is a new high score, write down the high score
         //in the file
         if(collisionOccurred)
         {
            ta.stop();
            try
            {
               Scanner fileReader = new Scanner(new File("highscore.txt"));
               //if thhe file has text in it, ,set isWrittenIn to true
               if(fileReader.hasNextInt())
               {
                  isWrittenIn = true;
               }
               else
               {
                  isWrittenIn = false;
               }
               fileReader.close();
               //creating a FileOutputStream which is overwriting, not appending
               FileOutputStream fos = new FileOutputStream("highscore.txt", false);
               //creating a PrintWriter which will autoflush
               PrintWriter pw = new PrintWriter(fos, true);
               //if isWrittenIn is true
               if(isWrittenIn)
               {
                  //if the current score is greater than the high score, write the current score into the highscore.txt file
                  if(score > highscore)
                  {
                     pw.println((int)score);
                  }
                  //if the current score is not greater than the high score, write the highscore back into the file (it was deleted when
                  //the FileOutputStream was created)
                  else
                  {
                     pw.println(highscore);
                  }
               }
               //if isWrittenIn is false, write the score in highscore.txt
               else
               {
                  pw.println((int)score);
               }
               
            }
            catch(FileNotFoundException fnfe)
            {
               System.out.println("Could not find file");
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
         }
         
         
         //this will check every Mine object in every Grid object from the grids arraylist to see if any of them have collided with
         //the player. If any have, then set collisionOccurred to true, and set wasHit for the mine hit and the player hit to true
         for(int i = 0; i < grids.size(); i++)
         {
            for(int j = 0; j < grids.get(i).size(); j++)
            {
               currentGrid = grids.get(i).get(j);
               for(int k = 0; k < currentGrid.getMines().size(); k++)
               {
                  currentMine = currentGrid.getMines().get(k);
                  if(currentMine.distance(thePlayer) < 20)
                  {
                     collisionOccurred = true;
                     currentMine.setWasHit(true);
                     thePlayer.setWasHit(true);
                  }
               }
            }
         }
         
         /*
         for(int i = 0; i < projectiles.size(); i++)
         {
            projectiles.get(i).draw(gc);
         }
         */
      }     
   }
   
   public class KeyPressedListener implements EventHandler<KeyEvent>
   {
      //will set the values of up, left, down, and right to true when w, a, s, and d are pushed respectively
      public void handle(KeyEvent e)
      {
         if(e.getCode() == KeyCode.W)
         {
            thePlayer.up(true);
         }
         if(e.getCode() == KeyCode.A)
         {
            thePlayer.left(true);
         }
         if(e.getCode() == KeyCode.S)
         {
            thePlayer.down(true);
         }
         if(e.getCode() == KeyCode.D)
         {
            thePlayer.right(true);
         }
      }
   }
   
   public class KeyReleasedListener implements EventHandler<KeyEvent>
   {
      //will set the values of up, left, down, and right to false when w, a, s, and d are released respectively
      public void handle(KeyEvent e)
      {
         if(e.getCode() == KeyCode.W)
         {
            thePlayer.up(false);
         }
         if(e.getCode() == KeyCode.A)
         {
            thePlayer.left(false);
         }
         if(e.getCode() == KeyCode.S)
         {
            thePlayer.down(false);
         }
         if(e.getCode() == KeyCode.D)
         {
            thePlayer.right(false);
         }
      }
   }
   
   /*
   Grid gridBeingOperatedOn;
   public class MousePressedListener implements EventHandler<MouseEvent>
   {
      public void handle(MouseEvent me)
      {
         
         //projectiles.add(new Projectile(thePlayer.getX(), thePlayer.getY(), (float)me.getX() + (cgridx * 100), (float)me.getY() + (cgridy * 100)));
         gridBeingOperatedOn = grids.get((int)(me.getX() / 100) + 2).get((int)(me.getY() / 100) + 2);
         projectiles.add(new Projectile(thePlayer.getX(), thePlayer.getY(), gridBeingOperatedOn.getX(),gridBeingOperatedOn.getY()));
         System.out.println(me.getX());
      }
   }
   */
   

   public static void main(String[] args)
   {
      launch(args);
   }
}