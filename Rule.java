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
 * Due Date: 12/16/18 11:59 pm Version: 1.0
 * 
 * Credits: none
 * Bugs: no known bugs
 */
package application;

/*
 * This class stores and validates nutrient filter rules.
 */
public class Rule {

  String ruleNutrient; // stores name of nutrient
  String comparator; // stores comparator for nutrient comparison
  Double ruleValue; // stores value for nutrient comparison

  /*
   * constructor takes in valid strings for nutrients, comparators and values
   */
  public Rule(String ruleNutrient, String comparator, Double ruleValue) {
    this.ruleNutrient = ruleNutrient;
    this.comparator = comparator;
    this.ruleValue = ruleValue;
  }

  /*
   * constructor parses a string; if string is invalid, will throw IllegalArgumentException use
   * isValidRuleString to validate the object before instantiating a rule
   */
  public Rule(String ruleData) throws IllegalArgumentException {

    // throw exception if string isn't properly for
    if (isValidRuleString(ruleData) != null)
      throw new IllegalArgumentException();

    // parse and store string
    String[] ruleArray = ruleData.split(" ");
    this.ruleNutrient = ruleArray[0];
    this.comparator = ruleArray[1];
    this.ruleValue = Double.parseDouble(ruleArray[2]);
  }

  /*
   * static method to call on possible rule strings to determine if the string is valid returns null
   * if rule is valid, a message if rule is invalid this should be called prior to instantiating a
   * Rule object
   */
  public static String isValidRuleString(String ruleData) {

    // parse rule and validate each piece of data
    String[] ruleArray = ruleData.split(" ");
    if (ruleArray.length < 3)
      return "enter a complete rule";
    if (ruleArray.length > 3)
      return "enter a rule with fewer spaces";

    // validate string
    String ruleNutrient = ruleArray[0];
    if ((ruleNutrient == null) || 
        (!ruleNutrient.equals("calories") && !ruleNutrient.equals("fat") &&
        !ruleNutrient.equals("carbohydrate") && !ruleNutrient.equals("fiber") 
        && !ruleNutrient.equals("protein"))) return "enter a valid nutrient";

    // validate comparator
    String comparator = ruleArray[1];
    if (!comparator.equals(">=") && !comparator.equals("==") && !comparator.equals("<="))
      return "enter a valid comparator";

    // validate value
    try {
      String doubleString = ruleArray[2];

      // disallow users or file entering a double like "14d"
      if (doubleString.substring(doubleString.length() - 1, doubleString.length()).matches("\\D"))
        return "enter a valid nutrient amount";

      // parse double - will throw exception if not double
      Double.parseDouble(doubleString);

    } catch (Exception e) {
      return "enter a valid nutrient amount";
    }

    return null;
  }

  /*
   * accessor for nutrient from rule
   */
  public String getNutrient() {
    return ruleNutrient;
  }

  /*
   * accessor for comparator from rule
   */
  public String getcomparator() {
    return comparator;
  }

  /**
   * accessor for value from rule
   */
  public Double getValue() {
    return ruleValue;
  }


  @Override
  public String toString() {
    return ruleNutrient + " " + comparator + " " + ruleValue;
  }

  // Testing Class
  public static void main(String[] args) {

    // create two food items
    FoodItem fi1 = new FoodItem("p1", "pasta");
    FoodItem fi2 = new FoodItem("p2", "pizza");
    fi1.addNutrient("fat", 12.0);
    fi2.addNutrient("carbs", 16.0);

    // parse rule; maybe use this code later
    String ruleData = "fat >= 14d";

    // testing evaluation method
    System.out.println(Rule.isValidRuleString(ruleData));
    System.out.println("\nfinished validating.\n");

    // instantiate rule
    if (Rule.isValidRuleString(ruleData) == null) {
      Rule fatRule = new Rule(ruleData);

      // test toString
      System.out.println("\nTesting rule: " + fatRule);

    }

  }

}
