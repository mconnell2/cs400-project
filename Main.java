/**
 * Filename: Main.java 
 * Project: Final Project - Food List
 * Authors: Epic lecture 4
 * Julie Book - jlsauer@wisc.edu
 * David Billmire
 * Mark Connell
 * Michelle Lindblom
 *
 * Semester: Fall 2018 
 * Course: CS400
 * 
 * Due Date: 12/2/18 11:59 pm 
 * Version: 1.0
 * 
 * Credits: none
 * 
 * Bugs: no known bugs
 */
package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Main class that will create a Food List UI where a user can add/remove food items from a meal
 * They can also load and save from a file and filter the food list 
 */
public class Main extends Application {

  // Names of foods that are available to display
  static ObservableList<FoodItem> foodItemList = FXCollections.observableArrayList();
  static ObservableList<FoodItem> mealFoodItems = FXCollections.observableArrayList();
  Meal meal = new Meal();

  
  

  /*
   * (non-Javadoc)
   * @see javafx.application.Application#start(javafx.stage.Stage)
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Food Project");
    
    // information about Stage
    int height = 500;
    String font = "Verdana";
    
    ListView<FoodItem> mealList = new ListView<FoodItem>();
    ListView<FoodItem> foodList = new ListView<FoodItem>();
    
    Meal meal = new Meal();
    
    

    // create horizontal box to add our 3 grid elements
    HBox hbox = new HBox(10);
    hbox.setTranslateX(10);
    hbox.setTranslateY(10);
    
    // Main root group 
    Group root = new Group(hbox); 
    Color backgroundColor = Color.AZURE;
    Scene scene = new Scene(root, 950, height, backgroundColor);

    //////////////////
    // Column 1
    /////////////////
    GridPane foodGrid = new GridPane();
    foodGrid.setHgap(10);
    foodGrid.setVgap(10);
    foodGrid.setPrefHeight(height * 0.9);
    
    // filter button
    Button filterButton = createButton("");
    filterButton.setPrefWidth(50);
    Image filterImage = new Image(getClass().getResourceAsStream("filter.png"));
    filterButton.setGraphic(new ImageView(filterImage));
    
    filterButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // TODO NEED to create a new window here
        System.out.println("Filter Button pressed");
      }
    });

    // Title for food list
    Text foodTitle = new Text("Food List");
    foodTitle.setFont(Font.font(font, FontWeight.BOLD, 20));

    // Food list
    foodList.setItems(foodItemList);
    foodList.setCellFactory(new Callback<ListView<FoodItem>,ListCell<FoodItem>>() {

      @Override
      public ListCell<FoodItem> call(ListView<FoodItem> param) {
        ListCell<FoodItem> cell = new ListCell<FoodItem>() {
          @Override
          public void updateItem(FoodItem item, boolean empty) {
              super.updateItem(item, empty);
              if (empty) {
                setText(null);
              }
              else {
              setText(item.getName());
              }
              
          }
      };

      return cell;
      }
    });

    foodList.setPrefWidth(350);
    
    // Food count
    Text foodCount = new Text("Food Count = " + foodItemList.size());
    foodCount.setFont(Font.font(font, 12));


    // Add elements to the Grid
    foodGrid.add(foodTitle, 1, 1, 1, 1);
    GridPane.setHalignment(filterButton, HPos.RIGHT);
    foodGrid.add(filterButton, 2, 1, 1, 1);
    foodGrid.add(foodList, 1, 2, 2, 1);
    GridPane.setHalignment(foodCount, HPos.RIGHT);
    foodGrid.add(foodCount, 1, 3, 2, 1);
    hbox.getChildren().add(foodGrid);

    ///////////////////
    // Column 2
    //////////////////
    GridPane buttonGrid = new GridPane();
    buttonGrid.setPadding(new Insets(45, 0, 0, 0));
    buttonGrid.setHgap(10);
    buttonGrid.setVgap(10);
    
    // load
    Button loadButton = createButton("Load from File");
    loadButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Load Button pressed");
        // get a file
        // call FoodData.load()
        // FoodList.addAll(FoodData.getAllFoodItems())
      }
    });

    // save
    Button saveButton = createButton("Save to File");
    saveButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Save Button pressed");
        // file picker
        // open a file
        // loop through all the food items in the database
        // write them to a file
        // close the file
      }
    });
    
 // add new food item
    Button addFoodButton = createButton("Add New Food");
    addFoodButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Add New Food Button pressed");
        
        // Call the pop up
        
      }
    });
 // clear Meal
    Button clearMealButton = createButton("Clear Meal");
    clearMealButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        meal.clearMeal();
        mealList.refresh();
      }
    });

    // add food to meal
    Button addToMealButton = createButton("");
    Image addImage = new Image(getClass().getResourceAsStream("ArrowRight.png"));
    addToMealButton.setGraphic(new ImageView(addImage));
    
    addToMealButton.setOnAction(actionEvent -> {
        FoodItem foodItem = foodList.getSelectionModel().getSelectedItem();
        meal.addFoodItem(foodItem);
        mealList.refresh();
        
    });

    // remove food from meal
    Button removeButton = createButton("");
    Image removeImage = new Image(getClass().getResourceAsStream("ArrowLeft.png"));
    removeButton.setGraphic(new ImageView(removeImage));
    removeButton.setOnAction(actionEvent -> {
      int foodIndex = foodList.getSelectionModel().getSelectedIndex();
      // TODO remove this
      System.out.println("removed:" + foodIndex + "-"
       + foodList.getSelectionModel().getSelectedItem().getName());
      meal.removeFoodItem(foodIndex);
      mealList.refresh();
    });
    

    // Add elements to button Grid
    buttonGrid.add(loadButton, 0, 0, 1, 1);
    buttonGrid.add(saveButton, 0, 1, 1, 1);
    buttonGrid.add(addFoodButton, 0, 2, 1, 1);
    buttonGrid.add(clearMealButton, 0, 3, 1, 1);
    buttonGrid.add(addToMealButton, 0, 10, 1, 1);
    buttonGrid.add(removeButton, 0, 11, 1, 1);
    hbox.getChildren().add(buttonGrid);

    ////////////////////
    // Column 3 - Meal and Nutrition info
    ////////////////////
    GridPane mealGrid = new GridPane();
    mealGrid.setHgap(10);
    mealGrid.setVgap(10);
    mealGrid.setPrefHeight(height * 0.9);


    // Meal Title
    Text mealTitle = new Text("Meal");
    mealTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
    
    
    
    // Meal List
    // mealList.setItems(mealFoodItems);
    mealList.setItems(meal.getMeal());
    mealList.setPrefWidth(350);
    mealList.setCellFactory(new Callback<ListView<FoodItem>,ListCell<FoodItem>>() {

      @Override
      public ListCell<FoodItem> call(ListView<FoodItem> param) {
        ListCell<FoodItem> cell = new ListCell<FoodItem>() {
          @Override
          public void updateItem(FoodItem item, boolean empty) {
              super.updateItem(item, empty);
              if (empty) {
                setText(null);
              }
              else {
              setText(item.getName());
              }
              
          }
      };

      return cell;
      }
    });
    
    // Nutrition title
    Text nutritionTitle = new Text("Meal Nutrition");
    nutritionTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
    
    // Nutrition information
    Text calories = new Text("Calories: 0"); // TODO get actual data
    calories.setFont(Font.font(font, 16));
    
    Text fat = new Text("Fat: 0 g"); // TODO get actual data
    fat.setFont(Font.font(font, 16));
    
    Text carbs = new Text("Carbohydrate: 0 g"); // TODO get actual data
    carbs.setFont(Font.font(font, 16));
    
    Text fiber = new Text("Fiber: 0 g"); // TODO get actual data
    fiber.setFont(Font.font(font, 16));
    
    Text protein = new Text("Protein: 0 g"); // TODO get actual data
    protein.setFont(Font.font(font, 16));
    

    
    // Add info to Grid
    mealGrid.add(mealTitle, 1, 1, 1, 1);
    mealGrid.add(mealList, 1, 2, 2, 1);
    mealGrid.add(nutritionTitle, 1, 3, 2, 1);
    mealGrid.add(calories, 1, 4, 1, 1);
    mealGrid.add(fat, 1, 5, 1, 1);
    mealGrid.add(carbs, 1, 6, 1, 1);
    mealGrid.add(fiber, 2, 4, 1, 1);
    mealGrid.add(protein, 2, 5, 1, 1);
    hbox.getChildren().add(mealGrid);
    
    ////////
    // Column 4
    //////
    GridPane lastColumnGrid = new GridPane();
    lastColumnGrid.setHgap(10);
    lastColumnGrid.setVgap(10);
    
    // Help Button
    Button helpButton = createButton("?");
    helpButton.setPrefWidth(20);
    helpButton.setFont(Font.font(font,FontWeight.BOLD,16));
    helpButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Finish this
        System.out.println("Help button pressed");
      }
    });
    lastColumnGrid.add(helpButton,1,1);
    hbox.getChildren().add(lastColumnGrid);
    
    // Show Stage
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /*
   * Creates a button with title that is passed in, will create it with a width of 125
   * @param text that is displayed on button
   * @return newly created button
   */
  private Button createButton(String name) {
    Button button = new Button();

    button.setText(name);
    button.setPrefWidth(125);  // TODO Make this width a parameter
    return button;
  }
  

  /*
   * main method that will be run by default and will launch the UI
   */
  public static void main(String[] args) {
    
    
    // TODO hard coded names for now - will be loading this from a list
    //names.add("Yoplait_GreekYogurtLemon");
    //names.add("Lancaster_SoftCremesButterscotchCaramel");
    //names.add("Kemps_FatFreeSkimMilk");
    foodItemList.add(new FoodItem("p1", "pasta"));
    foodItemList.add(new FoodItem("p2", "pizza"));
    foodItemList.add(new FoodItem("p3", "pickles"));
    foodItemList.add(new FoodItem("p4", "pizza"));
   
    
    launch(args);
  }


}
