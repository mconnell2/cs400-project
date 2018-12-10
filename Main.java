/**
 * Filename: Main.java Project: Final Project - Food List Authors: Epic lecture 4 Julie Book -
 * jlsauer@wisc.edu David Billmire Mark Connell Michelle Lindblom
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: 12/2/18 11:59 pm Version: 1.0
 * 
 * Credits: none
 * https://stackoverflow.com/questions/41319752/listview-setcellfactory-with-generic-label set lable
 * to generic lable
 * 
 * https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm - learning to do file chooser
 * 
 * Bugs: no known bugs
 */

package application;

import java.io.File;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Main class that will create a Food List UI where a user can add/remove food items from a meal
 * They can also load and save from a file and filter the food list
 */
public class Main extends Application {


  String nameFilter = null;
  Meal meal = new Meal();
  ObservableList<FoodItem> foodItemList;



  /*
   * (non-Javadoc)
   * 
   * @see javafx.application.Application#start(javafx.stage.Stage)
   */
  @Override
  public void start(Stage primaryStage) throws Exception {

    // Info about Stage
    int height = 500;
    String font = "Verdana";
    Color backgroundColor = Color.AZURE;
    
    // Variables used throughout method
    ListView<FoodItem> mealList = new ListView<FoodItem>();
    ListView<FoodItem> foodList = new ListView<FoodItem>();
    FoodData foodData = new FoodData();
    ObservableList<String> filterRules = FXCollections.observableArrayList();
    int buttonDefaultWidth = 125;
    
    // create horizontal box to add our grid elements
    primaryStage.setTitle("Food Project");
    HBox hbox = new HBox(10);
    hbox.setTranslateX(10);
    hbox.setTranslateY(10);
    Group root = new Group(hbox);
    Scene scene = new Scene(root, 1050, height, backgroundColor);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Column 1 - Food List and Filters
    ///////////////////////////////////////////////////////////////////////////////////////////////
    GridPane foodGrid = new GridPane();
    foodGrid.setHgap(10);
    foodGrid.setVgap(10);
    foodGrid.setPrefHeight(height * 0.9);

    // filter button
    Button filterButton = createButton("", 50);
    Image filterImage = new Image(getClass().getResourceAsStream("filter.png"));
    filterButton.setGraphic(new ImageView(filterImage));

    filterButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        ///////////////////////////////////////////////////////////////////////////////////////////
        // Filter Window
        ///////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////
        //// Prepare objects for VBox///
        ///////////////////////////////

        // Text objects
        Text nameFilterTitle = new Text("Name Filter");
        nameFilterTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
        nameFilterTitle.setFocusTraversable(true); // so that name field doesn't have focus
                                                   // immediately
        Text nutrientFilterTitle = new Text("Nutrient Filters");
        nutrientFilterTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
        Text ruleWarningText = new Text("Invalid rule."); // string will be updated later
        ruleWarningText.setFill(Color.RED);
        ruleWarningText.setVisible(false);

        // Label objects
        Label nameHelpLabel =
            new Label("Include foods with names that contain the following text:");
        Label nutrientHelpLabel =
            new Label("Include foods that pass nutrient rules (ex. fat >= 15)."
                + "\n  Supported nutrients are calories, carbohydrate, fat, fiber, and protein."
                + "\n  Supported comparators are <=, ==, >=.");
        nutrientHelpLabel.setWrapText(true);
        Label currentFiltersLabel = new Label("Current Filters");

        // TextField objects for input
        TextField nameFilterTextField = new TextField(nameFilter);
        nameFilterTextField.setPromptText("Enter text for name filter (ex. beans)");
        TextField nutrientFilterTextField = new TextField();
        nutrientFilterTextField.setPromptText("Enter rule (ex. fat > 100.0)");

        // ListView object for displaying rules
        ListView<String> ruleListView = new ListView<>();
        ruleListView.setItems(filterRules);
        ruleListView.setPrefHeight(150);

        // Buttons
        Button addFilterButton = createButton("_Add Filter", 150);
        Button removeFilterButton = createButton("_Remove Selected Filter", 150);
        Button acceptButton = createButton("_Save Filters", 150);

        /////////////////////////////////////////
        // put all controls into main VBox control
        // this uses some HBox controls as well
        /////////////////////////////////////////

        VBox filterVBox = new VBox(5);
        filterVBox.setPadding(new Insets(10));
        filterVBox.setBackground(null);

        // name filter controls and nutrient filter title and help text
        filterVBox.getChildren().addAll(nameFilterTitle, nameHelpLabel, nameFilterTextField);
        filterVBox.getChildren().addAll(nutrientFilterTitle, nutrientHelpLabel);

        // side by side nutrient filter text box, warning message, and add button
        HBox addNutrientFilterHBox = new HBox(10);
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        addNutrientFilterHBox.getChildren().addAll(nutrientFilterTextField, ruleWarningText,
            spacer1, addFilterButton);
        filterVBox.getChildren().add(addNutrientFilterHBox);

        // side by side filter header and remove button
        HBox currentFilterHBox = new HBox(10);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        currentFilterHBox.getChildren().addAll(currentFiltersLabel, spacer2, removeFilterButton);
        filterVBox.getChildren().add(currentFilterHBox);

        // rule list
        filterVBox.getChildren().add(ruleListView);

        // side by side spacer and accept button
        HBox acceptButtonHBox = new HBox(10);
        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        acceptButtonHBox.getChildren().addAll(spacer3, acceptButton);
        filterVBox.getChildren().add(acceptButtonHBox);

        // prepare and show stage
        // Group filterRoot = new Group(filterVBox);
        Scene scene = new Scene(filterVBox, 450, 450, Color.LIGHTCORAL);
        Stage ruleStage = new Stage();
        ruleStage.setTitle("Enter/Edit Filter(s)");
        ruleStage.setScene(scene);

        // handle closing window via Windows X - will save current name filter
        ruleStage.setOnCloseRequest(windowEvent -> {
          nameFilter = nameFilterTextField.getText();
        });
        ruleStage.show();


        ////////////////////////////////////////
        //// BUTTON ACTIONS FOR FILTER WINDOW////
        ////////////////////////////////////////

        // add filter button will save rule or will show error message
        addFilterButton.setOnAction(actionEvent -> {

          // validate entered rule string
          String errorMessage = Rule.isValidRuleString(nutrientFilterTextField.getText());

          // if no error message, save rule
          if (errorMessage == null) {
            filterRules.add(nutrientFilterTextField.getText());
            ruleListView.refresh();
            ruleWarningText.setVisible(false);
          }

          // otherwise, show inline warning message
          else {
            ruleWarningText.setText(errorMessage);
            ruleWarningText.setVisible(true);
          }
        });

        // remove filter button will remove current filter, if one is selected
        removeFilterButton.setOnAction(actionEvent -> {
          filterRules.remove(ruleListView.getSelectionModel().getSelectedItem());
          ruleListView.refresh();
        });

        // accept button will save name filter and close window
        acceptButton.setOnAction(actionEvent -> {
          nameFilter = nameFilterTextField.getText();
          ruleStage.close();
        });

      }
    });

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // END Filter Window
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // Title for food list
    Text foodTitle = new Text("Food List");
    foodTitle.setFont(Font.font(font, FontWeight.BOLD, 20));

    // Food list
    foodList.setPrefWidth(400);
    foodList.setCellFactory(new Callback<ListView<FoodItem>, ListCell<FoodItem>>() {
      @Override
      public ListCell<FoodItem> call(ListView<FoodItem> param) {
        ListCell<FoodItem> cell = new ListCell<FoodItem>() {
          @Override
          public void updateItem(FoodItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setText(null);
            } else {
              setText(item.getName());
            }
          }
        };
        return cell;
      }
    });
    
    // Food count
    Text foodCount = new Text("Food Count = 0");
    foodCount.setFont(Font.font(font, 12));

    // Add elements to the Grid
    foodGrid.add(foodTitle, 1, 1, 1, 1);
    GridPane.setHalignment(filterButton, HPos.RIGHT);
    foodGrid.add(filterButton, 2, 1, 1, 1);
    foodGrid.add(foodList, 1, 2, 2, 1);
    GridPane.setHalignment(foodCount, HPos.RIGHT);
    foodGrid.add(foodCount, 1, 3, 2, 1);
    hbox.getChildren().add(foodGrid);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Column 2 - Buttons
    ///////////////////////////////////////////////////////////////////////////////////////////////
    GridPane buttonGrid = new GridPane();
    buttonGrid.setPadding(new Insets(50, 0, 0, 0));
    buttonGrid.setHgap(10);
    buttonGrid.setVgap(10);

    // load Button
    Button loadButton = createButton("Load from File", buttonDefaultWidth);

    // save
    Button saveButton = createButton("Save to File", buttonDefaultWidth);

    // add new food item
    Button addFoodButton = createButton("Add New Food", buttonDefaultWidth);

    // clear Meal
    Button clearMealButton = createButton("Clear Meal", buttonDefaultWidth);

    // add food to meal
    Button addToMealButton = createButton("", buttonDefaultWidth);
    Image addImage = new Image(getClass().getResourceAsStream("ArrowRight.png"));
    addToMealButton.setGraphic(new ImageView(addImage));

    // remove food from meal
    Button removeButton = createButton("", buttonDefaultWidth);
    Image removeImage = new Image(getClass().getResourceAsStream("ArrowLeft.png"));
    removeButton.setGraphic(new ImageView(removeImage));

    // Add elements to button Grid
    buttonGrid.add(loadButton, 0, 0, 1, 1);
    buttonGrid.add(saveButton, 0, 1, 1, 1);
    buttonGrid.add(addFoodButton, 0, 2, 1, 1);
    buttonGrid.add(clearMealButton, 0, 3, 1, 1);
    buttonGrid.add(addToMealButton, 0, 10, 1, 1);
    buttonGrid.add(removeButton, 0, 11, 1, 1);
    hbox.getChildren().add(buttonGrid);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Column 3 - Meal and Nutrition info
    ///////////////////////////////////////////////////////////////////////////////////////////////
    GridPane mealGrid = new GridPane();
    mealGrid.setHgap(10);
    mealGrid.setVgap(10);
    mealGrid.setPrefHeight(height * 0.9);

    // Meal Title
    Text mealTitle = new Text("Meal");
    mealTitle.setFont(Font.font(font, FontWeight.BOLD, 20));

    // Meal List
    mealList.setItems(meal.getMeal());
    mealList.setPrefWidth(400);
    mealList.setCellFactory(new Callback<ListView<FoodItem>, ListCell<FoodItem>>() {
      @Override
      public ListCell<FoodItem> call(ListView<FoodItem> param) {
        ListCell<FoodItem> cell = new ListCell<FoodItem>() {
          @Override
          public void updateItem(FoodItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setText(null);
            } else {
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

    // Nutrition information - default is 0
    Text calories = new Text("Calories: 0");
    calories.setFont(Font.font(font, 16));

    Text fat = new Text("Fat: 0 g");
    fat.setFont(Font.font(font, 16));

    Text carbs = new Text("Carbohydrate: 0 g");
    carbs.setFont(Font.font(font, 16));

    Text fiber = new Text("Fiber: 0 g");
    fiber.setFont(Font.font(font, 16));

    Text protein = new Text("Protein: 0 g");
    protein.setFont(Font.font(font, 16));
    
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
        new PieChart.Data("Fat", 0),
        new PieChart.Data("Carbohydrate", 0),
        new PieChart.Data("Fiber", 0),
        new PieChart.Data("Protein", 0));
    PieChart pieChart = new PieChart(pieChartData);
    
    // Add info to Meal Grid
    mealGrid.add(mealTitle, 1, 1, 1, 1);
    mealGrid.add(mealList, 1, 2, 2, 1);
    mealGrid.add(nutritionTitle, 1, 3, 2, 1);
    mealGrid.add(calories, 1, 4, 1, 1);
    mealGrid.add(fat, 1, 5, 1, 1);
    mealGrid.add(carbs, 1, 6, 1, 1);
    mealGrid.add(fiber, 1, 7, 1, 1);
    mealGrid.add(protein, 1, 8, 1, 1);
    hbox.getChildren().add(mealGrid);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Column 4 - Help Icon
    ///////////////////////////////////////////////////////////////////////////////////////////////
    GridPane lastColumnGrid = new GridPane();
    lastColumnGrid.setHgap(10);
    lastColumnGrid.setVgap(10);

    // Help Button
    Button helpButton = createButton("?", 20);
    helpButton.setFont(Font.font(font, FontWeight.BOLD, 16));

    // Add to Grid/HBox
    lastColumnGrid.add(helpButton, 1, 1);
    hbox.getChildren().add(lastColumnGrid);



    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Button Actions
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    // Load Button Action
    loadButton.setOnAction(actionEvent -> {
       FileChooser fileChooser = new FileChooser();
       fileChooser.setTitle("Open Food Item File");
       
       File foodItemsFile = fileChooser.showOpenDialog(primaryStage);
       if (foodItemsFile != null) {
         foodData.loadFoodItems(foodItemsFile.getName());
         foodItemList = FXCollections.observableArrayList(foodData.getAllFoodItems());
         foodCount.setText("Food Count = " + foodItemList.size());
         foodList.setItems(foodItemList);
         foodList.refresh();
       }
    });
    
    // Save Button Action
    saveButton.setOnAction(actionEvent -> {
        // TODO Save Button NOT working
        System.out.println("Save Button pressed");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Food List");
        
        File saveFile = fileChooser.showSaveDialog(primaryStage);
        foodData.saveFoodItems(saveFile.getName());
    });
    
    // Add Food Button Action
    addFoodButton.setOnAction(actionEvent -> {
        // TODO Finish Add Food Button
        System.out.println("Add New Food Button pressed");
    });
    
    // Clear Meal Button Action
    clearMealButton.setOnAction(actionEvent -> {
        meal.clearMeal();
        mealList.refresh();
        updateNutrition(mealGrid, calories, fat, carbs, fiber, protein, pieChart);
    });
    
    // Add To Meal Button Action
    addToMealButton.setOnAction(actionEvent -> {
      FoodItem foodItem = foodList.getSelectionModel().getSelectedItem();
      meal.addFoodItem(foodItem);
      mealList.refresh();
      updateNutrition(mealGrid, calories, fat, carbs, fiber, protein, pieChart);
    });

    // Remove From Meal Button Action - needs to be after nutrition text created
    removeButton.setOnAction(actionEvent -> {
      meal.removeFoodItem(foodList.getSelectionModel().getSelectedIndex());
      mealList.refresh();
      updateNutrition(mealGrid, calories, fat, carbs, fiber, protein, pieChart);
    });

    // help button
    helpButton.setOnAction(actionEvent -> {
      // TODO Finish this
      System.out.println("Help button pressed");
    });

    // Show Stage
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /*
   * Creates a button with title that is passed in, will create it with a width that is specified
   * 
   * @param text that is displayed on button
   * 
   * @param button width
   * 
   * @return newly created button
   */
  private Button createButton(String name, int width) {
    Button button = new Button();
    button.setText(name);
    button.setPrefWidth(width);
    return button;
  }

  /*
   * Updates the nutrition info Grid This occurs upon adding new food items and clearing the meal
   * 
   * @param mealGrid that we are updating
   * 
   * @param calories text in the grid
   * 
   * @param fat text in the grid
   * 
   * @param carbohydrate text in the grid
   * 
   * @param fiber text in the grid
   * 
   * @param protein text in the grid
   */
  private void updateNutrition(GridPane mealGrid, Text calories, Text fat, Text carbs, Text fiber,
      Text protein, PieChart pieChart) {
    double caloriesInMeal = 0;
    double fatInMeal = 0;
    double carbsInMeal = 0;
    double fiberInMeal = 0;
    double proteinInMeal = 0;
    
    if (meal.analyzeMeal().get("calories") != null) {
      caloriesInMeal = meal.analyzeMeal().get("calories");
    }
    if (meal.analyzeMeal().get("fat") != null) {
      fatInMeal = meal.analyzeMeal().get("fat");
    }
    if (meal.analyzeMeal().get("carbohydrate") != null) {
      carbsInMeal = meal.analyzeMeal().get("carbohydrate");
    }
    if (meal.analyzeMeal().get("fiber") != null) {
      fiberInMeal = meal.analyzeMeal().get("fiber");
    }
    if (meal.analyzeMeal().get("protein") != null) {
      proteinInMeal = meal.analyzeMeal().get("protein");
    }

    calories.setText("Calories: " + caloriesInMeal);
    fat.setText("Fat: " + fatInMeal + " g");
    carbs.setText("Carbohydrate: " + carbsInMeal + " g");
    fiber.setText("Fiber: " + fiberInMeal + " g");
    protein.setText("Protein: " + proteinInMeal + " g");

    mealGrid.getChildren().removeAll(calories, fat, carbs, fiber, protein, pieChart);
    mealGrid.add(calories, 1, 4, 1, 1);
    mealGrid.add(fat, 1, 5, 1, 1);
    mealGrid.add(carbs, 1, 6, 1, 1);
    mealGrid.add(fiber, 1, 7, 1, 1);
    mealGrid.add(protein, 1, 8, 1, 1);
    
    if (fatInMeal == 0 & carbsInMeal == 0 & fiberInMeal == 0 & proteinInMeal == 0) {
      return;  //do not draw pie graph
    }
    
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
        new PieChart.Data("Fat", fatInMeal),
        new PieChart.Data("Carbohydrate", carbsInMeal),
        new PieChart.Data("Fiber", fiberInMeal),
        new PieChart.Data("Protein", proteinInMeal));
    pieChart.setData(pieChartData);
    mealGrid.add(pieChart, 2, 4, 1, 5);
  }


  /*
   * main method that will be run by default and will launch the UI
   */
  public static void main(String[] args) {
    launch(args);
  }
}
