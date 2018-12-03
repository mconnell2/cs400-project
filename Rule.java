

//TODO add projcet header header


/*
 * This class stores and evaluates nutrient rules.
 */
public class Rule {
  
  String ruleNutrient;  //stores name of nutrient TODO - use enum?
  String operator;  //stores operator for nutrient comparison
  Double ruleValue;  //stores value for nutrient comparison

  /*
   * constructor
   */
  public Rule(String ruleNutrient, String operator, Double ruleValue) {
    this.ruleNutrient = ruleNutrient;
    this.operator = operator;
    this.ruleValue = ruleValue;
  }
  
  /*
   * returns true of food item matches on the rule conditions
   */
  public boolean evalFoodItem(FoodItem foodItem) {
    
    //gather nutrient information
    Double nutrientValue = foodItem.getNutrientValue(ruleNutrient);
    
    //compare with operator from rule and return true if foodItem satisfies the rule
    if (operator.equals("==") && (nutrientValue.compareTo(ruleValue) == 0)) return true;
    else if (operator.equals(">=") && (nutrientValue.compareTo(ruleValue) >= 0)) return true;
    else if (operator.equals("<=") && (nutrientValue.compareTo(ruleValue) <= 0)) return true;
    
    //otherwise, return false
    return false;
  }
  
  @Override
  public String toString() {
    return ruleNutrient + " " + operator + " " + ruleValue;
  }
  
  //TODO Main method to delete later
  public static void main(String[] args) {
        
    //create two food items
    FoodItem fi1 = new FoodItem("p1", "pasta");
    FoodItem fi2 = new FoodItem("p2", "pizza");
    fi1.addNutrient("fat", 0.1);
    fi2.addNutrient("fat", 10.0);
    fi2.addNutrient("fat", 12.0);
    fi2.addNutrient("carbs", 16.0);
    
    
    //parse rule; maybe use this code later
    String ruleData = "fat <= 12.00001";
    String[] ruleArray = ruleData.split(" ");
    String ruleNutrient = ruleArray[0];
    String operator = ruleArray[1];
    Double ruleDouble = Double.parseDouble(ruleArray[2]); 
    
    //instantiate rule
    Rule fatRule = new Rule(ruleNutrient,operator,ruleDouble);
    
    //test toString
    System.out.println(fatRule);
    
    //test evaluating against food items
    System.out.println("Test evalFoodItem for " + fi1.getName()+ ": " + fatRule.evalFoodItem(fi1));
    System.out.println("Test evalFoodItem for " + fi2.getName()+ ": " + fatRule.evalFoodItem(fi2));
    
    
  }

}
