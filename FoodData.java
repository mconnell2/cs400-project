import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * JLB edited, and again
 *MHC was here too.
 *MAL test
 * 
 * @author sapan (sapan@cs.wisc.edu)
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
        indexes      = new HashMap<String, BPTree<Double,FoodItem>>();
    }
    
    
    public void saveFoodItems(String filename) {
      // TODO: Complete
    }
    
    /**
     * Loads food items from a file into the database
     * @param filePath - path to a file to load
     */
    @Override
    public void loadFoodItems(String filePath) {
        try {
			Files.lines(Paths.get(filePath))
				.map(line -> { 
					String[] parts = line.split(",");
					FoodItem newItem = null;
					if (parts.length > 1) {
						newItem = new FoodItem(parts[0], parts[1]);
						for (int i = 2; i < parts.length; i += 2) {
							newItem.addNutrient(parts[i], Double.parseDouble(parts[i+1]));
						}
					}
					return newItem;
				})
				.filter(a -> a != null)
				.forEach(item -> this.addFoodItem(item));
		} catch (IOException e) {
			System.out.println("Failed to parse file: " + filePath);
			e.printStackTrace();
		}
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
    	return foodItemList.stream()
    		.filter(item -> item.getName().contains(substring))
    		.collect(Collectors.toList());
    }

    /**
     * Filters the food item database given a set of nutrient filter rules
     * Illegal or unparsable rules are ignored.
     * Rules are combined with AND logic; the resulting set of food items satisfy all specified rules
     * @param rules - list of rule strings
     * @return list of every food item which satisfies every rule
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
    	
    	Set<FoodItem> results = new HashSet<FoodItem>();
    	boolean isFirstRule = true;
        for (String ruleString : rules) {
        	Rule rule = new Rule(ruleString);
        	//TODO: handle exceptions
        	
        	//Get all food items matching this rule
        	HashSet<FoodItem> filterResults = new HashSet<FoodItem>(indexes.get(rule.ruleNutrient).rangeSearch(rule.ruleValue, rule.getcomparator()));

        	if (isFirstRule) {						//First time through - initialize the result set to the first filtered result set
        		results.addAll(filterResults);
        		isFirstRule = false;
        	}
        	else {									//Take the intersection of the running results and the current filtered set
        		results.retainAll(filterResults);
        	}
        }
        
        return results.stream().collect(Collectors.toList());
    }

    /**
     * Adds a new Food Item to the database.
     * Updates internal nutrient indicies.
     * @param foodItem - new food item to insert
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	this.foodItemList.add(foodItem);
    	
    	//Index by each nutrient
    	for ( Map.Entry<String, Double> pair : foodItem.getNutrients().entrySet()) {
    		this.indexes.get(pair.getKey()).insert(pair.getValue(), foodItem);
    	}
    }

    /**
     * Accessor returning all food items in the database.
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return this.foodItemList;
    }

}
