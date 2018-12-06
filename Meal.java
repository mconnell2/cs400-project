//TODO add project header here

import java.util.ArrayList;
import java.util.HashMap;
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
  
  /*
   * Returns a string summarizing the nutritional information of all food items in the meal.
   * 
   * TODO - above is the class diagram info; I think we should return a hashmap
   * as that will help with displaying later.
   * TODO - we could get rid of this tag and instead update totals on every remove/add of an ingredient
   */
  public HashMap<String, Double> analyzeMeal() {
    
    //intialize HashMap for meal
    HashMap<String, Double> mealNutrients = new HashMap<String, Double>();
    
    //for each food item in the list
    for (FoodItem foodItem: foodItemList) {
      
      //retrieve nutrients
      HashMap<String, Double> nutrients = foodItem.getNutrients();
      
      //for each nutrient, add to meal nutrients
      for (String nutrient: nutrients.keySet()) {
        Double currentMealTotal = mealNutrients.get(nutrient);
        if (currentMealTotal == null) mealNutrients.put(nutrient, nutrients.get(nutrient));
        else mealNutrients.put(nutrient, currentMealTotal + nutrients.get(nutrient));
      }
    }
    
    return mealNutrients;
    
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
    fi1.addNutrient("fat", 0.1);
    fi2.addNutrient("fat", 0.1);
    fi3.addNutrient("fat", 1003.1);
    fi2.addNutrient("fat", 10.0);
    fi2.addNutrient("fat", 12.0);
    fi2.addNutrient("carbs", 16.0);
    fi4.addNutrient("zinc", 22.1);
    
    //test custom toString method to see if everything was added
    System.out.println(meal.toString());
    
    //test clearing menu
    /*
    meal.clearMeal();
    System.out.println(meal.toString());
    */
    
    //test removing food item, including dups
    /*
    meal.removeFoodItem(fi4);
    System.out.println(meal.toString());
    */
    
    //analyzing meal for totals
    HashMap<String, Double> mealNutrients = meal.analyzeMeal();
    System.out.println(mealNutrients.toString());       
  }
}
