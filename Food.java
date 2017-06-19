package kleyba.gameDev.evolution;

/**
 * Created by Kirtus on 5/9/2017.
 * The food class represents a single piece of food that the creatures can eat
 */
public class Food
{
  private double x;
  private double y;
  private int foodAmount;
  private boolean empty;

  /**
   * kleyba.gameDev.evolution.Food Constructor
   * @param x
   * x coordinate of food
   * @param y
   * y coordinate of food
   * @param foodAmount
   * The amount of food at this location
   */
  public Food(double x, double y, int foodAmount)
  {
    this.x = x;
    this.y = y;
    this.foodAmount = foodAmount;
    empty = false;
  }

  /**
   * The eat method removes 1 foodAmount from this food
   */
  public void eat()
  {
    foodAmount--;
    if(foodAmount == 0)
    {
      empty = true;
    }
  }

  //returns the x and y coordinates
  public double getX() {return x;}
  public double getY() {return y;}

  /**
   * Check if empty
   */
  public boolean isEmpty()
  {
    return empty;
  }

}
