

// TODO add project header header

/*
 * This class stores and validates nutrient filter rules.
 */
public class Rule {

  String ruleNutrient; // stores name of nutrient TODO - use enum?
  String comparator; // stores comparator for nutrient comparison
  Double ruleValue; // stores value for nutrient comparison

  /*
   * constructor TODO don't know if we want to keep this
   */
  public Rule(String ruleNutrient, String comparator, Double ruleValue) {
    this.ruleNutrient = ruleNutrient;
    this.comparator = comparator;
    this.ruleValue = ruleValue;
  }

  /*
   * constructor parses a string; does no validation on parts of string, so string must be validated
   * first use isValidRuleString to validate the object before instantiating a rule TODO this is
   * going to throw exceptions if we have a bad string. Is there a better way to handle this?
   */
  public Rule(String ruleData) {
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
    // TODO add validation here - see if David has something in FoodData - enum?
    String ruleNutrient = ruleArray[0];
    if (ruleNutrient == null)
      return "enter a valid nutrient";

    // validate comparator
    String comparator = ruleArray[1];
    if (!comparator.equals(">=") && !comparator.equals("==") && !comparator.equals("<="))
      return "enter a valid comparator";

    // validate value
    try {
      Double ruleValue = Double.parseDouble(ruleArray[2]);
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

  /*
   * returns true of food item matches on the rule conditions
   * TODO do actually need this here? is this all handled in the B Tree? I presume so.
   */
  public boolean evalFoodItem(FoodItem foodItem) {

    // gather nutrient information
    Double nutrientValue = foodItem.getNutrientValue(ruleNutrient);

    // compare with comparator from rule and return true if foodItem satisfies the rule
    if (comparator.equals("==") && (nutrientValue.compareTo(ruleValue) == 0))
      return true;
    else if (comparator.equals(">=") && (nutrientValue.compareTo(ruleValue) >= 0))
      return true;
    else if (comparator.equals("<=") && (nutrientValue.compareTo(ruleValue) <= 0))
      return true;

    // otherwise, return false
    return false;
  }

  @Override
  public String toString() {
    return ruleNutrient + " " + comparator + " " + ruleValue;
  }

  // TODO Main method to delete later
  public static void main(String[] args) {

    // create two food items
    FoodItem fi1 = new FoodItem("p1", "pasta");
    FoodItem fi2 = new FoodItem("p2", "pizza");
    fi1.addNutrient("fat", 0.1);
    fi2.addNutrient("fat", 10.0);
    fi2.addNutrient("fat", 12.0);
    fi2.addNutrient("carbs", 16.0);


    // parse rule; maybe use this code later
    String ruleData = "fat == 1212.1adfa";

    // testing evaluation method
    System.out.println(Rule.isValidRuleString(ruleData));
    System.out.println("\nfinished validating.\n");

    // instantiate rule
    if (Rule.isValidRuleString(ruleData) == null) {
      Rule fatRule = new Rule(ruleData);

      // test toString
      System.out.println("\nTesting rule: " + fatRule);

      // test evaluating against food items
      System.out
          .println("Test evalFoodItem for " + fi1.getName() + ": " + fatRule.evalFoodItem(fi1));
      System.out
          .println("Test evalFoodItem for " + fi2.getName() + ": " + fatRule.evalFoodItem(fi2));
    }

  }

}
