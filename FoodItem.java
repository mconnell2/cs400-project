/**
 * Filename: FoodItem.java 
 * Project: Final Project - Food List 
 * Authors: Epic lecture 4 
 * Julie Book - jlsauer@wisc.edu 
 * David Billmire - dbillmire@wisc.edu
 * Mark Connell - mconnell2@wisc.edu
 * Michelle Lindblom - mlindblom@wisc.edu
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: 12/16/18 11:59 pm Version: 1.0
 * 
 * Credits: none
 * Bugs: no known bugs
 */
package application;

import java.util.HashMap;
import java.util.Random;

/**
 * This class represents a food item with all its properties.
 * 
 * @author Mark Connell
 */
public class FoodItem {
  private String name; // the name of the food item
  private String id; // the id of the food item
  private HashMap<String, Double> nutrients; // map of nutrients and value for food item

  /**
   * Constructor for name and ID
   * 
   * @param name name of the food item
   * @param id unique id of the food item
   */
  public FoodItem(String id, String name) {
    this.id = id;
    this.name = name;
    nutrients = new HashMap<String, Double>();
  }

  /**
   * Gets the name of the food item
   * 
   * @return name of the food item
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the unique id of the food item
   * 
   * @return id of the food item
   */
  public String getID() {
    return id;
  }

  /**
   * Gets the nutrients of the food item
   * 
   * @return nutrients of the food item
   */
  public HashMap<String, Double> getNutrients() {
    return nutrients;
  }

  /**
   * Adds a nutrient and its value to this food. If nutrient already exists, updates its value.
   */
  public void addNutrient(String name, double value) {
    nutrients.put(name, value);
  }

  /**
   * Returns the value of the given nutrient for this food item. If not present, then returns 0.
   */
  public double getNutrientValue(String name) {
    if (nutrients.containsKey(name) != true)
      return 0;
    return nutrients.get(name);
  }

  /**
   * Overriden to-string method for writing food item and related nutrients to file
   */
  @Override
  public String toString() {
    String value = String.join(";", this.id, this.name);
    value += " " + this.nutrients.toString();
    return "[ " + value + " ]";
  }

  // Testing out class remove this later, useful for testing/PQA for now
  public static void main(String[] args) {

    // create two food items
    FoodItem fi1 = new FoodItem("p1", "pasta");
    FoodItem fi2 = new FoodItem("p2", "pizza");

    // test name + ID functions
    System.out.println(fi1.getID());
    System.out.println(fi2.getName());

    // test adding + getting nutrient
    fi1.addNutrient("fat", 0.1);
    fi2.addNutrient("fat", 10.0);
    fi2.addNutrient("fat", 12.0);
    fi2.addNutrient("carbs", 16.0);

    System.out.println("fat in fi1:   " + fi2.getNutrientValue("fat"));
    System.out.println("fat in fi2:   " + fi2.getNutrientValue("fat"));
    System.out.println("carbs in fi2: " + fi2.getNutrientValue("carbs"));
    System.out.println("zinc in fi2:  " + fi2.getNutrientValue("zinc"));

    // test getNutrients
    HashMap<String, Double> nutrientHM = fi2.getNutrients();
    System.out.println(nutrientHM);
  }
}
