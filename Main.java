package kleyba.gameDev.evolution;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application
{

  //Constants:
  private static final int FOOD_COUNT = 10;

  private int worldWidth = 400;
  private int worldHeight = 400;
  private Canvas canvas = new Canvas(2*worldWidth, 2*worldHeight);
  private GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
  private Group root = new Group();
  private LinkedList<Creature> creatureList;
  private LinkedList<Food> foodList = new LinkedList<Food>();;
  private Random random = new Random();
  private Color[] species;
  private int speciesCount;

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    root.getChildren().add(canvas);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();

    generateFood(FOOD_COUNT);
    generateCreatures();

    EvolveLoop evolveLoop = new EvolveLoop();
    evolveLoop.start();

  }

  private void generateFood(int spawnAmount)
  {
    for(int i = 0; i < spawnAmount; i++)
    {
      double rx = worldWidth*random.nextDouble();
      double ry = worldHeight*random.nextDouble();
      int rFoodAmount = random.nextInt(500);
      foodList.add(new Food(rx, ry, rFoodAmount));
    }
  }

  /**
   * This method builds all the creatures
   */
  private void generateCreatures()
  {
    creatureList = new LinkedList<Creature>();
    for(int i = 0; i < 100; i++)
    {
      double rx = 150 + (worldWidth - 300)*random.nextDouble();
      double ry = 150 + (worldHeight - 300)*random.nextDouble();
      double[] enemyNorthEastMutes = new double[9];
      double[] enemyNorthWestMutes = new double[9];
      double[] enemySouthEastMutes = new double[9];
      double[] enemySouthWestMutes = new double[9];
      double[] friendNorthEastMutes = new double[9];
      double[] friendNorthWestMutes = new double[9];
      double[] friendSouthEastMutes = new double[9];
      double[] friendSouthWestMutes = new double[9];
      double[] foodNorthEastMutes = new double[9];
      double[] foodNorthWestMutes = new double[9];
      double[] foodSouthEastMutes = new double[9];
      double[] foodSouthWestMutes = new double[9];
      for(int j = 0; j < 9; j++)
      {
        enemyNorthEastMutes[j] = 0;
        enemyNorthWestMutes[j] = 0;
        enemySouthEastMutes[j] = 0;
        enemySouthWestMutes[j] = 0;
        friendNorthEastMutes[j] = 0;
        friendNorthWestMutes[j] = 0;
        friendSouthEastMutes[j] = 0;
        friendSouthWestMutes[j] = 0;
        foodNorthEastMutes[j] = 0;
        foodNorthWestMutes[j] = 0;
        foodSouthEastMutes[j] = 0;
        foodSouthWestMutes[j] = 0;
      }

      Creature c = new Creature(rx,ry,0,worldWidth,worldHeight, enemyNorthEastMutes,
              enemyNorthWestMutes, enemySouthEastMutes, enemySouthWestMutes,
              friendNorthEastMutes, friendNorthWestMutes, friendSouthEastMutes,
              friendSouthWestMutes, foodNorthEastMutes, foodNorthWestMutes,
              foodSouthEastMutes, foodSouthWestMutes);
      creatureList.add(c);

      c.setCreatureList(creatureList);
      c.setFoodList(foodList);
    }

    species = new Color[100];

    for(int k = 0; k < 100; k++)
    {
      species[k] = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

  }

  private class EvolveLoop extends AnimationTimer
  {


    @Override
    public void handle(long now)
    {
      drawBackground();
      updateCreatures();
      updateFood();

      graphicsContext.setFill(Color.BLACK);
      graphicsContext.fillText("Population: " + creatureList.size(), 10, 10);
      graphicsContext.fillText("Number of Species: " + speciesCount, 10, 20);

    }

    private void updateFood()
    {
      ArrayList<Food> foodToRemove = new ArrayList<>();

      for(Food f: foodList)
      {
        //Drawing food
        graphicsContext.setFill(Color.LIME);
        graphicsContext.fillRect(2*f.getX(), 2*f.getY(), 10, 10);
        //Updating
        if(f.isEmpty())
        {
          foodToRemove.add(f);
        }
      }

      foodList.removeAll(foodToRemove);

      if(foodList.size() <= 4) //If there are less that 4 food sources, spawn more
      {
        generateFood(6);
      }

    }


    private void drawBackground()
    {
      graphicsContext.setFill(Color.ALICEBLUE);
      graphicsContext.fillRect(0,0,2*worldWidth,2*worldHeight);
    }

    private void updateCreatures()
    {
      ArrayList<Creature> creaturesToAdd = new ArrayList<>();
      ArrayList<Creature> creaturesToRemove = new ArrayList<>();
      speciesCount = 1;
      int speciesID = 0;
      int population = creatureList.size();
      if (population > 0)
      {
        Creature creatureToAdd;
        for (Creature c : creatureList)
        {
          //Species ID
          if (c.getSpeciesID() > speciesID)
          {
            speciesID++;
            speciesCount++;
          }

          //Drawing
          int speciesIndex = c.getSpeciesID();
          int timesBig = speciesIndex/100;
          graphicsContext.setFill(species[speciesIndex - 100*timesBig]);
          graphicsContext.fillRect(2*c.getX(), 2*c.getY(), 4, 4);

          //Updating
          if (c.getHP() < 0)
          {
            if (c!=null)
            {
              creaturesToRemove.add(c);
            }
          } else if(c!=null && population < 7000)
          {
            creatureToAdd = c.update();
            if (creatureToAdd != null)
            {
              creaturesToAdd.add(creatureToAdd);
            }
          }
          else if(c!=null && population > 7000)
          {
            creaturesToRemove.add(c);
            population = population - 1;
          }
        }
        creatureList.addAll(creaturesToAdd);
        creatureList.removeAll(creaturesToRemove);
      }
    }
  }

}
