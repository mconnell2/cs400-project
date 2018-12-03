//TODO add project header here

import java.util.ArrayList;
import java.util.List;

/*
 * This class stores and evaluates meals based on one or more food items.
 */
public class Meal {
  
  List<FoodItem> foodItemList; //stores food items that comprise the meal

  /*
   * constructor, empty meal
   */
  public Meal() {
    foodItemList = new ArrayList<FoodItem>();
  }
  
  /*
   * adds a food item to the meal.
   * Duplicate food items may be added
   */
  public void addFoodItem(FoodItem foodItem) {
    foodItemList.add(foodItem);
  }
  
  /*
   * removes a food item from the meal.
   * If the food item to remove does not exist, the meal is not changed.
   */
  public void removeFoodItem(FoodItem foodItem) {

    //iterate backwards through list; compare IDs
    for (int i = foodItemList.size()-1; i >= 0; i--) {
      if (foodItemList.get(i).getID().equals(foodItem.getID())) foodItemList.remove(i);
    }
  }
  
  /*
   * removes all food items from the meal.
   */
  public void clearMeal() {
    foodItemList.clear();
  }
  
  //TODO - not sure we need this for anything? It's not in our spec.
  //I also needed to add a toString method in foodItem.
  @Override
  public String toString() {
    return foodItemList.toString();
  }
  
  //TODO remove main method after done with testing
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    
    FoodItem fi1 = new FoodItem("p1", "pasta");
    FoodItem fi2 = new FoodItem("p2", "pizza");
    FoodItem fi3 = new FoodItem("p3", "pickles");
    FoodItem fi4 = new FoodItem("p4", "pizza");
    
    Meal meal = new Meal();
    meal.addFoodItem(fi1);
    meal.addFoodItem(fi2);
    meal.addFoodItem(fi3);
    meal.addFoodItem(fi4);
    meal.addFoodItem(fi4); //test adding duplicate

    //test custom toString method to see if everything was added
    System.out.println(meal.toString());
    
    //test clearing menu
    /*
    meal.clearMeal();
    System.out.println(meal.toString());
    */
    
    //test removing food item, including dups
    meal.removeFoodItem(fi4);
    System.out.println(meal.toString());
        
  }

}
