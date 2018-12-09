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
import java.util.ArrayList;
import java.util.List;
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
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

  
  String nameFilter = null;
  Meal meal = new Meal();
  
  

  /*
   * (non-Javadoc)
   * @see javafx.application.Application#start(javafx.stage.Stage)
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
   // Names of foods that are available to display
    FoodData foodData = new FoodData();
    foodData.loadFoodItems("foodItems.txt");
    List<FoodItem> loadFoodList = foodData.getAllFoodItems();  
    ObservableList<FoodItem> foodItemList = FXCollections.observableArrayList(loadFoodList);
    ObservableList<String> filterRules = FXCollections.observableArrayList();
    
    primaryStage.setTitle("Food Project");
    

    // information about Stage
    int height = 500;
    String font = "Verdana";
    
    ListView<FoodItem> mealList = new ListView<FoodItem>();
    ListView<FoodItem> foodList = new ListView<FoodItem>();

    


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
        
        /////////////////
        //FILTER WINDOW//
        /////////////////
        
        ///////////////////////////////
        ////Prepare objects for VBox///
        ///////////////////////////////
                
        //Text objects
        Text nameFilterTitle = new Text("Name Filter");
        nameFilterTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
        nameFilterTitle.setFocusTraversable(true);  //so that name field doesn't have focus immediately
        Text nutrientFilterTitle = new Text("Nutrient Filters");
        nutrientFilterTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
        Text ruleWarningText = new Text("Invalid rule."); //string will be updated later
        ruleWarningText.setFill(Color.RED);
        ruleWarningText.setVisible(false);

        //Label objects
        Label nameHelpLabel = new Label("Include foods with names that contain the following text:");
        Label nutrientHelpLabel = new Label("Include foods that pass nutrient rules (ex. fat >= 15)."
            + "\n  Supported nutrients are calories, carbohydrate, fat, fiber, and protein."
            + "\n  Supported comparators are <=, ==, >=.");
        nutrientHelpLabel.setWrapText(true);
        Label currentFiltersLabel = new Label("Current Filters");
       
        //TextField objects for input
        TextField nameFilterTextField = new TextField(nameFilter);
        nameFilterTextField.setPromptText("Enter text for name filter (ex. beans)");
        TextField nutrientFilterTextField = new TextField();
        nutrientFilterTextField.setPromptText("Enter rule (ex. fat > 100.0)");
        
        //ListView object for displaying rules
        ListView<String> ruleListView = new ListView<>();
        ruleListView.setItems(filterRules);
        ruleListView.setPrefHeight(150);
        
        //Buttons
        Button addFilterButton = createButton("_Add Filter");
        addFilterButton.setPrefWidth(150);
        Button removeFilterButton = createButton("_Remove Selected Filter");
        removeFilterButton.setPrefWidth(150);
        Button acceptButton = createButton("_Save Filters");
        acceptButton.setPrefWidth(150);
        
        /////////////////////////////////////////
        //put all controls into main VBox control
        //this uses some HBox controls as well
        /////////////////////////////////////////

        VBox filterVBox = new VBox(5);
        filterVBox.setPadding(new Insets(10));
        filterVBox.setBackground(null);

        //name filter controls and nutrient filter title and help text
        filterVBox.getChildren().addAll(nameFilterTitle,nameHelpLabel,nameFilterTextField);
        filterVBox.getChildren().addAll(nutrientFilterTitle,nutrientHelpLabel);
        
        //side by side nutrient filter text box, warning message, and add button
        HBox addNutrientFilterHBox = new HBox(10);
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        addNutrientFilterHBox.getChildren().addAll(nutrientFilterTextField,ruleWarningText,spacer1,addFilterButton);
        filterVBox.getChildren().add(addNutrientFilterHBox);

        //side by side filter header and remove button
        HBox currentFilterHBox = new HBox(10);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        currentFilterHBox.getChildren().addAll(currentFiltersLabel,spacer2,removeFilterButton);
        filterVBox.getChildren().add(currentFilterHBox);

        //rule list
        filterVBox.getChildren().add(ruleListView);

        //side by side spacer and accept button
        HBox acceptButtonHBox = new HBox(10);
        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        acceptButtonHBox.getChildren().addAll(spacer3,acceptButton);
        filterVBox.getChildren().add(acceptButtonHBox);
                        
        //prepare and show stage
       // Group filterRoot = new Group(filterVBox); 
        Scene scene = new Scene(filterVBox, 450, 450, Color.LIGHTCORAL);
        Stage ruleStage = new Stage();
        ruleStage.setTitle("Enter/Edit Filter(s)");
        ruleStage.setScene(scene);
        
        //handle closing window via Windows X - will save current name filter
        ruleStage.setOnCloseRequest(windowEvent ->  {
          nameFilter = nameFilterTextField.getText();
        });
        ruleStage.show();
        
        
        ////////////////////////////////////////
        ////BUTTON ACTIONS FOR FILTER WINDOW////
        ////////////////////////////////////////

        //add filter button will save rule or will show error message
        addFilterButton.setOnAction(actionEvent ->  {
  
            //validate entered rule string
            String errorMessage = Rule.isValidRuleString(nutrientFilterTextField.getText());
          
            //if no error message, save rule
            if (errorMessage == null) {
              filterRules.add(nutrientFilterTextField.getText());
              ruleListView.refresh();
              ruleWarningText.setVisible(false);
            }
          
           //otherwise, show inline warning message
            else {
              ruleWarningText.setText(errorMessage);
              ruleWarningText.setVisible(true);
            }           
        });
        
        //remove filter button will remove current filter, if one is selected
        removeFilterButton.setOnAction(actionEvent -> {
          filterRules.remove(ruleListView.getSelectionModel().getSelectedItem());
          ruleListView.refresh();
        });

        //accept button will save name filter and close window
        acceptButton.setOnAction(actionEvent -> {
          nameFilter = nameFilterTextField.getText();
          ruleStage.close();
        });
            
      }
    });

    /////////////////////////
    ////END FILTER WINDOW////
    /////////////////////////

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
    

    // ADD TO MEAL BUTTON
    addToMealButton.setOnAction(actionEvent -> {
      FoodItem foodItem = foodList.getSelectionModel().getSelectedItem();
      meal.addFoodItem(foodItem);
      mealList.refresh();
      calories.setText("Calories: " + meal.analyzeMeal().get("calories"));
      fat.setText("Fat: " + meal.analyzeMeal().get("fat"));
      carbs.setText("Carbohydrate: " + meal.analyzeMeal().get("carbs"));
      fiber.setText("Fiber: " + meal.analyzeMeal().get("fiber"));
      protein.setText("Protein: " + meal.analyzeMeal().get("protein"));
      
      mealGrid.getChildren().removeAll(calories, fat, carbs, fiber, protein);
      mealGrid.add(calories, 1, 4, 1, 1);
      mealGrid.add(fat, 1, 5, 1, 1);
      mealGrid.add(carbs, 1, 6, 1, 1);
      mealGrid.add(fiber, 2, 4, 1, 1);
      mealGrid.add(protein, 2, 5, 1, 1);
      
  });
    
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
        
    
    launch(args);
  }


}
