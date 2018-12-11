package application;

//TODO add project header here
public enum Nutrient {
    calories, carbohydrate, fat, fiber, protein;  //TODO is it OK to be lower case?
  
  //TODO keeping this in case I need to switch those to lower case above...
  public String getString(Nutrient n) {
    return n.toString().toLowerCase();
  }
  
  /*
   * check to see if enum contains particular string
   */
  public static boolean contains(String stringToCheck) {
    for(Nutrient n : values()) {
      if (n.name().equals(stringToCheck)) return true;
    }
    return false;
      
  }
}
