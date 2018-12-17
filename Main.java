/**
 * Filename: Meal.java 
 * Project: Final Project - Food List 
 * Authors: Epic lecture 4 
 * Julie Book - jlsauer@wisc.edu 
 * David Billmire - dbillmire@wisc.edu
 * Mark Connell - mconnell2@wisc.edu
 * Michelle Lindblom - mlindblom@wisc.edu
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: 12/2/18 11:59 pm Version: 1.0
 * 
 * Credits: none
 * Bugs: no known bugs
 */
package application;

import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * This class stores and evaluates meals based on one or more food items.
 */
public class Meal {

  ObservableList<FoodItem> foodItemList; // stores food items that comprise the meal

  /*
   * constructor, empty meal
   */
  public Meal() {
    foodItemList = FXCollections.observableArrayList();
  }

  /*
   * retrieves sorted meal
   */
  public ObservableList<FoodItem> getMeal() {
    return foodItemList.sorted((a, b) -> a.getName().toUpperCase()
        .compareTo(b.getName().toUpperCase()));
  }

  /*
   * adds a food item to the meal and sorts foods in meal. Duplicate food items may be added
   */
  public void addFoodItem(FoodItem foodItem) {
    foodItemList.add(foodItem);
  }

  /*
   * removes a food item from the meal from the specific index. If the food item to remove does not
   * exist, the meal is not changed.
   */
  public void removeFoodItem(int index) {
    if ((index >= 0) && index < foodItemList.size())
      foodItemList.remove(index);
  }

  /*
   * removes all food items from the meal.
   */
  public void clearMeal() {
    foodItemList.clear();
  }

  /*
   * Returns a HashMap of nutrients (key, String) and grams (value, Double) for all food items in
   * the meal
   */
  /**
   * analyzes nutritional content of food items in the meal
   * 
   * @return HashMap of nutrients (key, String) and grams (value, Double)
   */
  public HashMap<String, Double> analyzeMeal() {

    // initialize HashMap for meal
    HashMap<String, Double> mealNutrients = new HashMap<String, Double>();

    // for each food item in the list
    for (FoodItem foodItem : foodItemList) {

      // retrieve nutrients
      HashMap<String, Double> foodNutrients = foodItem.getNutrients();

      // for each food item nutrient, add to meal nutrients
      for (String nutrient : foodNutrients.keySet()) {
        Double currentMealTotal = mealNutrients.get(nutrient);
        if (currentMealTotal == null)
          mealNutrients.put(nutrient, foodNutrients.get(nutrient));
        else
          mealNutrients.put(nutrient, currentMealTotal + foodNutrients.get(nutrient));
      }
    }

    return mealNutrients;
  }

  // Testing class
  public static void main(String[] args) {

    // create food items
    FoodItem fi1 = new FoodItem("p1", "pasta");
    FoodItem fi2 = new FoodItem("p2", "pizza");
    FoodItem fi3 = new FoodItem("p3", "pickles");
    FoodItem fi4 = new FoodItem("p4", "pizza");

    // test constructor
    Meal meal = new Meal();

    // test adding food items, including duplicates
    meal.addFoodItem(fi1);
    meal.addFoodItem(fi2);
    meal.addFoodItem(fi3);
    meal.addFoodItem(fi4);
    meal.addFoodItem(fi4); // test adding duplicate
    fi1.addNutrient("fat", 0.1);
    fi1.addNutrient("carbs", 0.1);
    fi3.addNutrient("fat", 1003.1);
    fi2.addNutrient("fat", 10.0);
    fi2.addNutrient("fat", 12.0);
    fi2.addNutrient("carbs", 16.0);
    fi4.addNutrient("zinc", 22.1);

    // test custom toString method to see if everything was added
    System.out.println(meal.toString());

    // test clearing menu
    // meal.clearMeal();
    // System.out.println(meal.toString());


    // test removing food item, including dups

    meal.removeFoodItem(0);
    System.out.println(meal.toString());

    // analyzing meal for totals
    HashMap<String, Double> mealNutrients = meal.analyzeMeal();

    System.out.println("Retrieving carbs: " + meal.analyzeMeal().get("carbs"));
    System.out.println(mealNutrients.toString());
  }
}
