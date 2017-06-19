package kleyba.gameDev.evolution;

import java.util.*;

/**
 * Created by Kirtus on 4/21/2017.
 */
public class Creature
{

  private double x;
  private double y;
  //Enemy Mutes
  private double[] enemyNorthWestMutes;
  private double[] enemyNorthEastMutes;
  private double[] enemySouthEastMutes;
  private double[] enemySouthWestMutes;

  //Friend Mutes
  private double[] friendNorthWestMutes;
  private double[] friendNorthEastMutes;
  private double[] friendSouthEastMutes;
  private double[] friendSouthWestMutes;

  //Food Mutes
  private double[] foodNorthWestMutes;
  private double[] foodNorthEastMutes;
  private double[] foodSouthEastMutes;
  private double[] foodSouthWestMutes;

  private int speciesID;
  private Random random = new Random();
  private LinkedList<Creature> creatureList = new LinkedList<Creature>();
  private LinkedList<Food> foodList = new LinkedList<Food>();
  private int worldWidth;
  private int worldHeight;
  private int hp = 600;
  private int ap = 2;
  private int HP;


  public Creature(double x, double y, int speciesID, int worldWidth, int worldHeight,
                  double[] enemyNorthEastMutes, double[] enemyNorthWestMutes,
                  double[] enemySouthEastMutes, double[] enemySouthWestMutes,
                  double[] friendNorthEastMutes, double[] friendNorthWestMutes,
                  double[] friendSouthEastMutes, double[] friendSouthWestMutes,
                  double[] foodNorthWestMutes, double[] foodNorthEastMutes,
                  double[] foodSouthEastMutes, double[] foodSouthWestMutes)
  {
    this.x = x;
    this.y = y;
    this.speciesID = speciesID;
    this.worldHeight = worldHeight;
    this.worldWidth = worldWidth;
    this.enemyNorthEastMutes = enemyNorthEastMutes;
    this.enemyNorthWestMutes = enemyNorthWestMutes;
    this.enemySouthEastMutes = enemySouthEastMutes;
    this.enemySouthWestMutes = enemySouthWestMutes;
    this.friendNorthEastMutes = friendNorthEastMutes;
    this.friendNorthWestMutes = friendNorthWestMutes;
    this.friendSouthEastMutes = friendSouthEastMutes;
    this.friendSouthWestMutes = friendSouthWestMutes;
    this.foodNorthEastMutes = foodNorthEastMutes;
    this.foodNorthWestMutes = foodNorthWestMutes;
    this.foodSouthEastMutes = foodSouthEastMutes;
    this.foodSouthWestMutes = foodSouthWestMutes;

  }

  public Creature update()
  {
    //Population limiting
    int population = creatureList.size();
    if(population > 3500)
    {
      hp = hp - 10;
    }
    else if(population > 4500)
    {
      hp = hp - 20;
    }
    else if(population > 5000)
    {
      hp  = hp - 40;
    }
    else if(population > 5500)
    {
      hp  = hp - 600;
    }

    else
    {
      hp--;
    }
      double[] move = new double[9];
      for(int i = 0; i < 9; i++)
      {
        move[i] = random.nextDouble();
      }

      Creature nearestFriend = null;
      Creature nearestEnemy = null;
      Food nearestFood = null;

      if(population > 1 && hp > 0)
      {
        nearestFriend = findNearestFriend();
        nearestEnemy = findNearestEnemy();
        nearestFood = findNearestFood();
      }
      else
      {
        nearestFriend = null;
        nearestEnemy = null;
        nearestFood = null;
      }

      if(nearestEnemy != null)
      {
        int angleToEnemy = findAngleTo(nearestEnemy);
        if(getDist(nearestEnemy) < 400)
        {
          switch (angleToEnemy)
          {
            case 0:
              addMutations(move, enemyNorthEastMutes);
              break;
            case 1:
              addMutations(move, enemyNorthWestMutes);
              break;
            case 2:
              addMutations(move, enemySouthWestMutes);
              break;
            case 3:
              addMutations(move, enemySouthEastMutes);
              break;
            default:
              break;
          }
        }
      }
      if(nearestFriend != null)
      {
        int angleToFriend = findAngleTo(nearestFriend);
        if(getDist(nearestFriend) < 400)
        {
          switch (angleToFriend)
          {
            case 0:
              addMutations(move, friendNorthEastMutes);
              break;
            case 1:
              addMutations(move, friendNorthWestMutes);
              break;
            case 2:
              addMutations(move, friendSouthWestMutes);
              break;
            case 3:
              addMutations(move, friendSouthEastMutes);
              break;
            default:
              break;
          }
        }
      }

    if(nearestFood != null)
    {
      int angleToFood = findAngleToFood(nearestFood);
      if(getDistFood(nearestFood) < 400)
      {
        switch (angleToFood)
        {
          case 0:
            addMutations(move, foodNorthEastMutes);
            break;
          case 1:
            addMutations(move, foodNorthWestMutes);
            break;
          case 2:
            addMutations(move, foodSouthWestMutes);
            break;
          case 3:
            addMutations(move, foodSouthEastMutes);
            break;
          default:
            break;
        }
      }
    }


      int maxIndex = 0;
      double maxMove = move[0];
      for (int i = 0; i < 9; i++)
      {
        if (move[i] > maxMove)
        {
          maxIndex = i;
          maxMove = move[i];
        }
      }

      return moveTo(maxIndex);
  }

  private void addMutations(double[] move, double[] mutations)
  {
    for(int i = 0; i < 9; i++)
    {
      move[i] = move[i] + mutations[i];
    }
  }

  private int findAngleTo(Creature nearestFriend)
  {
    double angle = Math.atan2(nearestFriend.getY() - y, nearestFriend.getX() - x);
    return (int)(angle/(Math.PI/2));
  }

  /**
   * Returns the angle to the Food
   * @param f
   * @return
   */
  private int findAngleToFood(Food f)
  {
    double angle = Math.atan2(f.getY() - y, f.getX() - x);
    return (int)(angle/(Math.PI/2));
  }

  private Creature findNearestFriend()
  {
    Creature creatureNearest = creatureList.get(0);
    if(creatureNearest.equals(this))
    {
      creatureNearest = creatureList.get(1);
    }
    for(Creature c: creatureList)
    {
      if(!c.equals(this))
      {
        if(getDist(c) < getDist(creatureNearest) && c.getSpeciesID() == speciesID)
        {
          creatureNearest = c;
          if(getDist(c) < 100)
          {
            break;
          }
        }
      }
    }
    return creatureNearest;
  }


  private Creature findNearestEnemy()
  {
    Creature creatureNearest = creatureList.get(0);
    if(creatureNearest.equals(this))
    {
      creatureNearest = creatureList.get(1);
    }
    for(Creature c: creatureList)
    {
      if(!c.equals(this))
      {
        if(getDist(c) < getDist(creatureNearest) && c.getSpeciesID() != speciesID)
        {
          creatureNearest = c;
          if(getDist(c) < 100)
          {
            break;
          }
        }
      }
    }
    return creatureNearest;
  }

  /**
   * Returns the nearest food to this creature
   * @return
   */
  private Food findNearestFood()
  {
    Food nearest = foodList.get(0);
    for(Food f: foodList)
    {
      if(getDistFood(f) < getDistFood(nearest))
      {
        nearest = f;
      }
    }
    return nearest;
  }

  private Creature moveTo(int maxIndex)
  {
    double newx = x;
    double newy = y;
    switch (maxIndex)
    {
      case 0:
        break;
      case 1:
        newy = y - 1;
        break;
      case 2:
        newy = y - 1;
        newx = x + 1;
        break;
      case 3:
        newx = x + 1;
        break;
      case 4:
        newx = x + 1;
        newy = y + 1;
        break;
      case 5:
        newy = y + 1;
        break;
      case 6:
        newx = x - 1;
        newy = y + 1;
        break;
      case 7:
        newx = x - 1;
        break;
      case 8:
        newx = x - 1;
        newy = y - 1;
        break;
      default:
        break;
    }

      newx = adjustForBoundsX(newx);
      newy = adjustForBoundsY(newy);
      x = newx;
      y = newy;

      //Check if there is a creature where we are moving and if so interact
      Creature c = moveOnCreature(newx, newy);
      if (c != null)
      {
        if (c.getSpeciesID() == speciesID)
        {
          if(random.nextDouble() < 0.04285)
          {
            return reproduce();
          }
        }
        else if (c.getSpeciesID() != speciesID)
        {
          attack(c);
        }
      }

    //Check if there is a Food where we are moving and if so eat
    moveOnFood();

    return null;

  }

  private double adjustForBoundsX(double newx)
  {
    if(newx < 0)
    {
      return worldWidth;
    }
    if(newx > worldWidth)
    {
      return 0;
    }
    return newx;
  }
  private double adjustForBoundsY(double newy)
  {
    if(newy < 0)
    {
      return worldHeight;
    }
    if(newy > worldHeight)
    {
      return 0;
    }
    return newy;
  }

  private void attack(Creature c)
  {
    c.setHP(c.getHP() - c.getHP()/2);
    hp = hp + hp;
  }

  private Creature reproduce()
  {
    double[] newFriendNorthEastMutes = friendNorthEastMutes;
    double[] newFriendNorthWestMutes = friendNorthWestMutes;
    double[] newFriendSouthEastMutes = friendSouthEastMutes;
    double[] newFriendSouthWestMutes = friendSouthWestMutes;
    double[] newEnemyNorthEastMutes = enemyNorthEastMutes;
    double[] newEnemyNorthWestMutes = enemyNorthWestMutes;
    double[] newEnemySouthEastMutes = enemySouthEastMutes;
    double[] newEnemySouthWestMutes = enemySouthWestMutes;
    double[] newFoodNorthEastMutes = foodNorthEastMutes;
    double[] newFoodNorthWestMutes = foodNorthWestMutes;
    double[] newFoodSouthEastMutes = foodSouthEastMutes;
    double[] newFoodSouthWestMutes = foodSouthWestMutes;

    double mutSum = 0;
    for(int i = 0; i < 9; i++)
    {
      double mutationFactor = 5;
      double mutOne = random.nextDouble()/mutationFactor;
      double mutTwo = random.nextDouble()/mutationFactor;
      double mutThree = random.nextDouble()/mutationFactor;
      double mutFour = random.nextDouble()/mutationFactor;
      double mutFive = random.nextDouble()/mutationFactor;
      double mutSix = random.nextDouble()/mutationFactor;
      double mutSeven = random.nextDouble()/mutationFactor;
      double mutEight = random.nextDouble()/mutationFactor;
      double mutNine = random.nextDouble()/mutationFactor;
      double mutTen = random.nextDouble()/mutationFactor;
      double mutEleven = random.nextDouble()/mutationFactor;
      double mutTwelve = random.nextDouble()/mutationFactor;

      newFriendNorthEastMutes[i] = newFriendNorthEastMutes[i] + mutOne;
      newFriendNorthWestMutes[i] = newFriendNorthWestMutes[i] + mutTwo;
      newFriendSouthEastMutes[i] = newFriendSouthEastMutes[i] + mutThree;
      newFriendSouthWestMutes[i] = newFriendSouthWestMutes[i] + mutFour;
      newEnemyNorthEastMutes[i] = newEnemyNorthEastMutes[i] + mutFive;
      newEnemyNorthWestMutes[i] = newEnemyNorthWestMutes[i] + mutSix;
      newEnemySouthEastMutes[i] = newEnemySouthEastMutes[i] + mutSeven;
      newEnemySouthWestMutes[i] = newEnemySouthWestMutes[i] + mutEight;
      newFoodNorthEastMutes[i] = newFoodNorthEastMutes[i] + mutNine;
      newFoodNorthWestMutes[i] = newFoodNorthWestMutes[i] + mutTen;
      newFoodSouthEastMutes[i] = newFoodSouthEastMutes[i] + mutEleven;
      newFoodSouthWestMutes[i] = newFoodSouthWestMutes[i] + mutTwelve;

      mutSum = mutOne + mutTwo + mutThree + mutFour + mutFive + mutSix + mutSeven + mutEight + mutNine + mutTen + mutEleven + mutTwelve;
    }

    int newID = speciesID;
    if(mutSum > 1.15)
    {
      newID++;
    }
    double sign = random.nextDouble();
    if(sign < 0.5)
    {
      sign = -1;
    }
    else
    {
      sign = 1;
    }

    double cx = x + sign*1;
    double cy = y + sign*1;

    if(moveIsLegal(cx,cy))
    {
      Creature newCreature = new Creature(cx, cy, newID, worldWidth, worldHeight,
              newEnemyNorthEastMutes, newEnemyNorthWestMutes, newEnemySouthEastMutes, newEnemySouthWestMutes,
              newFriendNorthEastMutes, newFriendNorthWestMutes, newFriendSouthEastMutes, newFriendSouthWestMutes,
              newFoodNorthEastMutes, newFoodNorthWestMutes, newFoodSouthEastMutes, newFoodSouthWestMutes);
      newCreature.setFoodList(foodList);
      newCreature.setCreatureList(creatureList);
      return newCreature;
    }
    return null;

  }

  private Creature moveOnCreature(double newx, double newy)
  {
    for(Creature creature: creatureList)
    {
      if(creatureList.size() < 1000)
      {
        if(getDist(creature) < 30 && getDist(creature) > 10 && !creature.equals(this))
        {
          return creature;
        }
      }
      else if(creatureList.size() < 500)
      {
        if(getDist(creature) < 100 && getDist(creature) > 10 && !creature.equals(this))
        {
          return creature;
        }
      }
      else if(getDist(creature) < 25 && getDist(creature) > 19 && !creature.equals(this))
      {
        return creature;
      }
    }
    return null;
  }

  private Creature moveOnFood()
  {
    for(Food f: foodList)
    {
      if(getDistFood(f) < 25)
      {
        f.eat();
        hp = hp++;
      }
    }
    return null;
  }

  private double getDistFood(Food f)
  {
    return ((f.getX() - x)*(f.getX() - x) + (f.getY() - y)*(f.getY() - y));
  }

  private boolean moveOnEmpty(double newx, double newy)
  {
    for(Creature creature: creatureList)
    {
      if(creature.getX() == newx && creature.getY() == newy)
      {
        return false;
      }
    }
    return true;
  }

  private boolean moveIsLegal(double newx, double newy)
  {
    if(newx > 0 && newx < worldWidth)
    {
      if(newy > 0 && newy < worldHeight)
      {
        return true;
      }
    }
    return false;
  }


  public double getX()
  {
    return x;
  }
  public double getY()
  {
    return y;
  }

  public void setCreatureList(LinkedList<Creature> creatureList)
  {
    this.creatureList = creatureList;
  }

  public void setFoodList(LinkedList<Food> foodList)
  {
    this.foodList = foodList;
  }


  public int getSpeciesID()
  {
    return speciesID;
  }

  private double getDist(Creature c)
  {
    return ((c.getX() - x)*(c.getX() - x) + (c.getY() - y)*(c.getY() - y));
  }

  public int getHP()
  {
    return hp;
  }

  public void setHP(int HP)
  {
    this.hp = HP;
  }
}
