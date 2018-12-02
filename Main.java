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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

/**
 * Main class that will create a Food List UI where a user can add/remove food items from a menu
 * They can also load and save from a file and filter the food list 
 */
public class Main extends Application {

  // Names of foods that are available to display
  static ObservableList<String> names = FXCollections.observableArrayList();

  /*
   * (non-Javadoc)
   * @see javafx.application.Application#start(javafx.stage.Stage)
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Food Project");
    
    // information about Stage
    int width = 900;
    int height = 500;
    String font = "Verdana";

    // create horizontal box to add our 3 grid elements
    HBox hbox = new HBox(10);
    hbox.setTranslateX(10);
    hbox.setTranslateY(10);

    // Main root group 
    Group root = new Group(hbox); 
    Color backgroundColor = Color.AZURE;
    Scene scene = new Scene(root, width + 50, height, backgroundColor);


    // Column 1
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
    ListView<String> foodList = new ListView<>();
    foodList.setItems(names);
    foodList.setPrefWidth(350);

    // Add elements to the Grid
    foodGrid.add(foodTitle, 1, 1, 1, 1);
    GridPane.setHalignment(filterButton, HPos.RIGHT);
    foodGrid.add(filterButton, 2, 1, 1, 1);
    foodGrid.add(foodList, 1, 2, 2, 1);
    hbox.getChildren().add(foodGrid);


    // Column 2
    GridPane buttonGrid = new GridPane();
    buttonGrid.setHgap(10);
    buttonGrid.setVgap(10);
    
    // load
    Button loadButton = createButton("Load from File");
    loadButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Load Button pressed");
      }
    });

    // save
    Button saveButton = createButton("Save to File");
    saveButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Save Button pressed");
      }
    });
    
 // add new food item
    Button addFoodButton = createButton("Add New Food");
    addFoodButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Add New Food Button pressed");
      }
    });

    // add food to menu
    Button addToMenuButton = createButton("");
    Image addImage = new Image(getClass().getResourceAsStream("ArrowRight.png"));
    addToMenuButton.setGraphic(new ImageView(addImage));
    
    addToMenuButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Finish this
        System.out.println(
            "Add Button pressed on: " + foodList.getSelectionModel().selectedItemProperty());
      }
    });

    // remove food from menu 
    Button removeButton = createButton("");
    Image removeImage = new Image(getClass().getResourceAsStream("ArrowLeft.png"));
    removeButton.setGraphic(new ImageView(removeImage));
    
    removeButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Remove Button pressed");
      }
    });

    // Add elements to button Grid
    buttonGrid.add(loadButton, 1, 1, 1, 1);
    buttonGrid.add(saveButton, 1, 2, 1, 1);
    buttonGrid.add(addFoodButton, 1, 3, 1, 1);
    buttonGrid.add(addToMenuButton, 1, 15, 1, 1);
    buttonGrid.add(removeButton, 1, 16, 1, 1);
    hbox.getChildren().add(buttonGrid);


    // Column 3 - Meal and Nutrition info
    GridPane menuGrid = new GridPane();
    menuGrid.setHgap(10);
    menuGrid.setVgap(10);
    menuGrid.setPrefHeight(height * 0.9);


    // Meal Title
    Text menuTitle = new Text("Menu");
    menuTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
    
    // Meal List
    ListView<String> menuList = new ListView<>();
    menuList.setItems(names);
    menuList.setPrefWidth(350);
    
    // Nutrition title
    Text nutritionTitle = new Text("Meal Nutrition");
    nutritionTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
    menuGrid.add(nutritionTitle, 1, 3, 2, 1);

    // Nutrition information
    Text calories = new Text("Calories: 2000"); // TODO get actual data
    calories.setFont(Font.font(font, 16));
    
    Text fat = new Text("Fat: 10 g"); // TODO get actual data
    fat.setFont(Font.font(font, 16));
    
    Text carbs = new Text("Carbohydrate: 50 g"); // TODO get actual data
    carbs.setFont(Font.font(font, 16));
    
    Text fiber = new Text("Fiber: 12 g"); // TODO get actual data
    fiber.setFont(Font.font(font, 16));
    
    Text protein = new Text("Protein: 20 g"); // TODO get actual data
    protein.setFont(Font.font(font, 16));
    
    // Add info to Grid
    menuGrid.add(menuTitle, 1, 1, 2, 1);
    menuGrid.add(menuList, 1, 2, 2, 1);
    menuGrid.add(calories, 1, 4, 1, 1);
    menuGrid.add(fat, 1, 5, 1, 1);
    menuGrid.add(carbs, 1, 6, 1, 1);
    menuGrid.add(fiber, 2, 4, 1, 1);
    menuGrid.add(protein, 2, 5, 1, 1);
    hbox.getChildren().add(menuGrid);
    
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
    button.setPrefWidth(125);
    return button;
  }

  /*
   * main method that will be run by default and will launch the UI
   */
  public static void main(String[] args) {
    
    
    // TODO hard coded names for now - will be loading this from a list
    names.add("Yoplait_GreekYogurtLemon");
    names.add("Lancaster_SoftCremesButterscotchCaramel");
    names.add("Kemps_FatFreeSkimMilk");

    launch(args);
  }


}
