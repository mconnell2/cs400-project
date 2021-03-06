/**
 * Filename: FoodData.java 
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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents the backend for managing all the operations associated with FoodItems
 * 
 */
public class FoodData implements FoodDataADT<FoodItem> {

  // List of all the food items.
  private List<FoodItem> foodItemList;

  // Map of nutrients and their corresponding index
  private HashMap<String, BPTree<Double, FoodItem>> indexes;

  /**
   * Public constructor
   */
  public FoodData() {
    foodItemList = new ArrayList<FoodItem>();
    indexes = new HashMap<String, BPTree<Double, FoodItem>>();
  }

  @Override
  public String toString() {
    return foodItemList.toString();
  }

  /**
   * Saves current food items to a file in .csv format.
   * 
   * @param filename - name of the file to save the food items to
   */
  public void saveFoodItems(String filename) {
    // Set up print writer
    FileWriter fwOutput = null;
    try {
      fwOutput = new FileWriter(filename);
    } catch (IOException e) {
      System.out.println("Failed to open save file: " + e.getMessage());
    }

    for (FoodItem food : foodItemList) {
      String line = getDataLine(food);
      if (line.length() > 0) {
        writeDataLine(fwOutput, line);
      }
    }

    if (fwOutput != null) {
      try {
        fwOutput.close();
      } catch (IOException e) {
        System.out.println("Failed to close save file: " + e.getMessage());
      }
    }
  }

  /**
   * Loads food items from a file into the database
   * 
   * @param filePath - path to a file to load
   */
  @Override
  public void loadFoodItems(String filePath) {
    try {
      Files.lines(Paths.get(filePath)).map(line -> parseLine(line)).filter(a -> a != null)
          .forEach(item -> this.addFoodItem(item));
    } catch (IOException e) {
      System.out.println("Failed to parse file: " + e.getMessage());
    }
  }

  /**
   * Returns a subset of food items whose name contains a specified substring.
   * 
   * @param substring - string filter for item names
   * @return list of filtered food items
   */
  @Override
  public List<FoodItem> filterByName(String substring) {
    return foodItemList.stream()
        .filter(item -> item.getName().toUpperCase().contains(substring.toUpperCase()))
        .collect(Collectors.toList());
  }

  /**
   * Filters the food item database given a set of nutrient filter rules Illegal or unparsable rules
   * are ignored. Rules are combined with AND logic; the resulting set of food items satisfy all
   * specified rules
   * 
   * @param rules - list of rule strings
   * @return list of every food item which satisfies every rule
   */
  @Override
  public List<FoodItem> filterByNutrients(List<String> rules) {

    Set<FoodItem> results = new HashSet<FoodItem>();
    boolean isFirstRule = true;
    Rule rule = null;
    for (String ruleString : rules) {
      try {
        rule = new Rule(ruleString);
      } catch (IllegalArgumentException e) {
        // Skip this rule
      }

      if (rule != null) {
        // Get all food items matching this rule
        HashSet<FoodItem> filterResults = new HashSet<FoodItem>(
            indexes.get(rule.ruleNutrient).rangeSearch(rule.ruleValue, rule.getcomparator()));

        if (isFirstRule) { // First time through - initialize the result set to the first filtered
                           // result set
          results.addAll(filterResults);
          isFirstRule = false;
        } else { // Take the intersection of the running results and the current filtered set
          results.retainAll(filterResults);
        }
      }
    }

    return results.stream().collect(Collectors.toList());
  }

  /**
   * Adds a new Food Item to the database. Updates internal nutrient indexes. Does nothing if an
   * invalid foodItem is provided
   * 
   * @param foodItem - new food item to insert
   */
  @Override
  public void addFoodItem(FoodItem foodItem) {
    if (foodItem != null) {
      this.foodItemList.add(foodItem);

      // Index by each nutrient
      for (Map.Entry<String, Double> pair : foodItem.getNutrients().entrySet()) {

        if (indexes.containsKey(pair.getKey())) {
          indexes.get(pair.getKey()).insert(pair.getValue(), foodItem);
        } else {
          indexes.put(pair.getKey(), new BPTree<Double, FoodItem>(10));
          indexes.get(pair.getKey()).insert(pair.getValue(), foodItem);
        }

        BPTree<Double, FoodItem> index = this.indexes.get(pair.getKey());

        // Initialize index if not yet created
        if (index == null) {
          index = new BPTree<Double, FoodItem>(3);
          this.indexes.put(pair.getKey(), index);
        }

        // Update the index
        index.insert(pair.getValue(), foodItem);

      }
    }
  }

  /**
   * Accessor returning all food items in the database.
   */
  @Override
  public List<FoodItem> getAllFoodItems() {
    return this.foodItemList;
  }


  /**
   * Parses a food data line. Each food data line is in the following csv format:
   * id,name,<nutrient1>,<nutrient1_value>,...,<nutrientN>,<nutrientN_value>
   * 
   * @param line - line to parse
   * @return new food item if successfully parsed; otherwise null
   */
  private static FoodItem parseLine(String line) {
    FoodItem food = null;
    String[] parts = line.split(",");
    if (parts.length > 1 && parts[0].length() > 0 && parts[1].length() > 0) {
      food = new FoodItem(parts[0], parts[1]);
      for (int i = 2; i < parts.length; i += 2) {
        if (parts[i].length() > 0 && parts[i + 1].length() > 0) {
          food.addNutrient(parts[i], Double.parseDouble(parts[i + 1]));
        }
      }
    }
    return food;
  }

  /**
   * Creates a food data line from a food item. Each food data line is in the folloiwng csv format:
   * id,name,<nutrient1>,<nutrient1_value>,...,<nutrientN>,<nutrientN_value>;
   * 
   * @param food - food data item to build a string for
   * @return food data line, or empty string if the food data item is invalid
   */
  private static String getDataLine(FoodItem food) {
    String line = "";
    if (food != null) {
      line = String.join(",", food.getID(), food.getName());

      for (Map.Entry<String, Double> pair : food.getNutrients().entrySet()) {
        line = String.join(",", line, pair.getKey().toLowerCase(), pair.getValue().toString());
      }
    }
    return line;
  }

  /**
   * Writes a line of fooditem data to a provided file writer
   * 
   * @param writer - file writer object to write to
   * @param line - string to write
   */
  private static void writeDataLine(FileWriter writer, String line) {
    if (writer != null && line.length() > 0) {
      try {
        writer.write(line + "\n");
      } catch (IOException e) {
        System.out.println("Failed to write line: " + e.getMessage());
      }
    }
  }

  // Testing main method
  public static void main(String[] args) {
    FoodData data = new FoodData();

    // Print empty data
    System.out.println("Empty data: " + data.toString());

    // Create new food item
    FoodItem food = new FoodItem("12345", "Smashed_giblets");
    food.addNutrient("calories", 120);
    food.addNutrient("fat", 23);
    food.addNutrient("carbohydrates", 0);
    food.addNutrient("fiber", 0.0);
    food.addNutrient("protein", 12);
    data.addFoodItem(food);
    System.out.println("Added one food " + data.toString());


    // Load more food
    data.loadFoodItems("foodItems.txt");
    System.out.println("Added many foods " + data.toString());

  }

}
