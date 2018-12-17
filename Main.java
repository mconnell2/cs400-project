/**
 * Filename: Main.java 
 * Project: Final Project - Food List 
 * Authors: Epic lecture 4 
 * Julie Book - jlsauer@wisc.edu 
 * David Billmire - dbillmire@wisc.edu
 * Mark Connell - mconnell2@wisc.edu
 * Michelle Lindblom - mlindblom@wisc.edu
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: 12/2/18 11:59 pm Version: 1.0
 * 
 * Credits: 
 * https://stackoverflow.com/questions/41319752/listview-setcellfactory-with-generic-label set label
 * to generic label
 * 
 * https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm - learning to do file chooser
 * 
 * Bugs: no known bugs
 */

package application;

import java.io.File;
import java.util.Collections;
import java.util.List;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Main class that will create a Food List UI where a user can add/remove food items from a meal
 * They can also load and save from a file and filter the food list
 */
public class Main extends Application {

  // Class variables
  FoodData foodData = new FoodData();
  ObservableList<FoodItem> foodItemList = FXCollections.observableArrayList();;
  Meal meal = new Meal();
  String nameFilter = "";
  ObservableList<String> filterRules = FXCollections.observableArrayList();
  ListView<FoodItem> mealList = new ListView<FoodItem>();
  ListView<FoodItem> foodList = new ListView<FoodItem>();


  /**
   * javafx start method. Called to set up the stage
   * 
   * @see javafx.application.Application#start(javafx.stage.Stage)
   */
  @Override
  public void start(Stage primaryStage) throws Exception {

    // Info about Stage
    int height = 600;
    int width = 1250;
    String font = "Verdana";
    Color backgroundColor = Color.AZURE;

    // Variables used throughout method
    int buttonDefaultWidth = 125;

    // create horizontal box to add our grid elements
    primaryStage.setTitle("Food Project");
    HBox hbox = new HBox(10);
    hbox.setTranslateX(10);
    hbox.setTranslateY(10);
    Group root = new Group(hbox);
    Scene scene = new Scene(root, width + 50, height, backgroundColor);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Column 1 - Food List and Filters
    ///////////////////////////////////////////////////////////////////////////////////////////////
    GridPane foodGrid = new GridPane();
    foodGrid.setHgap(10);
    foodGrid.setVgap(10);
    foodGrid.setPrefSize(width * 0.425, height * 0.9);

    // Food count
    Text foodCount = new Text("Food Count = 0");
    foodCount.setFont(Font.font(font, 12));

    // filter button
    Button filterButton = createButton("", 50);
    Image filterImage = new Image(getClass().getClassLoader().getResourceAsStream("application/filter.png"));
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
        nutrientFilterTextField.setPromptText("Enter rule (ex. fat >= 15");

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
        Scene scene = new Scene(filterVBox, 450, 450, backgroundColor);
        Stage ruleStage = new Stage();
        ruleStage.setTitle("Enter/Edit Filter(s)");
        ruleStage.setScene(scene);

        // handle closing window via Windows X - will save current name filter, update button color
        ruleStage.setOnCloseRequest(windowEvent -> {
          
          //retrieve name filter, apply filters, update list
          nameFilter = nameFilterTextField.getText();
          boolean isListFiltered = closeFilterWindow();
          
          //update button highlighting and count
          if (isListFiltered) filterButton.setStyle("-fx-base: #00b8e6;");
          else filterButton.setStyle(null);
          foodCount.setText("Food Count = " + foodItemList.size());
        });
        
        ruleStage.show();

        ////////////////////////////////////////
        //// BUTTON ACTIONS FOR FILTER WINDOW////
        ////////////////////////////////////////

        // add filter button - will save rule or will show error message
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

        // remove filter button - will remove current filter, if one is selected
        removeFilterButton.setOnAction(actionEvent -> {
          filterRules.remove(ruleListView.getSelectionModel().getSelectedItem());
          ruleListView.refresh();
        });

        // accept (save) button - will save current name filter, update button color
        acceptButton.setOnAction(actionEvent -> {
          
          //retrieve name filter, apply filters, update list
          nameFilter = nameFilterTextField.getText();
          boolean isListFiltered = closeFilterWindow();
          
          //update button highlighting and count
          if (isListFiltered) filterButton.setStyle("-fx-base: #00b8e6;");
          else filterButton.setStyle(null);
          foodCount.setText("Food Count = " + foodItemList.size());
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
    foodList.setPrefSize(width * 0.425, height * 0.75);
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
    addFoodButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {

        ///////////////////////
        // ADD NEW FOOD WINDOW//
        ///////////////////////

        // Make the VBox
        VBox addNewFoodVBox = new VBox(5);
        addNewFoodVBox.setPadding(new Insets(10));
        addNewFoodVBox.setBackground(null);

        // set the Scene and stage
        Scene scene = new Scene(addNewFoodVBox, 450, 300, backgroundColor);
        Stage newFoodStage = new Stage();
        newFoodStage.setTitle("Add New Food Item");
        newFoodStage.setScene(scene);

        // set title text
        Text newFoodTitle = new Text("Add New Food Item");
        newFoodTitle.setFont(Font.font(font, FontWeight.BOLD, 20));
        addNewFoodVBox.getChildren().add(newFoodTitle);

        // ADD ALL FIELDS
        TextField nameField = addFoodItemField(addNewFoodVBox, "Name:", "e.g. cupcake");
        TextField calField = addFoodItemField(addNewFoodVBox, "Calories:", "e.g. 1000");
        TextField fatField = addFoodItemField(addNewFoodVBox, "Fat:", "e.g. 17");
        TextField carbField = addFoodItemField(addNewFoodVBox, "Carbohydrates:", "e.g. 124");
        TextField fiberField = addFoodItemField(addNewFoodVBox, "Fiber:", "e.g. 0");
        TextField proteinField = addFoodItemField(addNewFoodVBox, "Protein:", "e.g. 5");

        /// END FIELD CREATION

        // DRAW THE POPUP
        Region Vspacer = new Region();
        VBox.setVgrow(Vspacer, Priority.ALWAYS);

        HBox addFoodButtonsBox = new HBox(10);
        Region Hspacer = new Region();
        HBox.setHgrow(Hspacer, Priority.ALWAYS);

        Button addFoodButton = createButton("_Add", buttonDefaultWidth);
        addFoodButton.setPrefWidth(150);
        Button cancelAddButton = createButton("_Cancel", buttonDefaultWidth);

        cancelAddButton.setPrefWidth(150);
        addFoodButtonsBox.getChildren().addAll(Hspacer, addFoodButton, cancelAddButton);
        addNewFoodVBox.getChildren().addAll(Vspacer, addFoodButtonsBox);

        // HANDLE EVENTS
        cancelAddButton.setOnAction(e -> newFoodStage.close());
        addFoodButton.setOnAction(e -> {
          try {
            FoodItem newFood = new FoodItem(nameField.getText(),
                Double.parseDouble(calField.getText()), Double.parseDouble(fatField.getText()),
                Double.parseDouble(carbField.getText()), Double.parseDouble(fiberField.getText()),
                Double.parseDouble(proteinField.getText()));
            foodData.addFoodItem(newFood);
            foodItemList.add(newFood);
            foodItemList = FXCollections.observableArrayList(foodData.getAllFoodItems());
            foodItemList = foodItemList.sorted((a, b) -> a.getName().toUpperCase()
                .compareTo(b.getName().toUpperCase()));
            foodList.setItems(foodItemList);
            foodList.refresh();
            foodCount.setText("Food Count = " + foodItemList.size());

            newFoodStage.close();
          } catch (Exception ex) {
            Alert buttonAlert3 = new Alert(AlertType.WARNING, "Invalid entry");
            buttonAlert3.showAndWait().filter(response -> response == ButtonType.OK);
          }
        });

        newFoodStage.show();

        cancelAddButton.requestFocus();
        // Call the pop up

      }

      public TextField addFoodItemField(VBox addNewFoodVBox, String labelText, String ghostText) {
        HBox NameHBox = new HBox(10);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label newFoodName = new Label(labelText);
        TextField newFoodNameField = new TextField();
        newFoodNameField.setPromptText(ghostText);
        NameHBox.getChildren().addAll(newFoodName, spacer, newFoodNameField);
        addNewFoodVBox.getChildren().add(NameHBox);
        return newFoodNameField;
      }
    });
    // clear Meal
    Button clearMealButton = createButton("Clear Meal", buttonDefaultWidth);

    // add food to meal
    Button addToMealButton = createButton("", buttonDefaultWidth);
    Image addImage = new Image(getClass().getClassLoader().getResourceAsStream("application/ArrowRight.png"));
    addToMealButton.setGraphic(new ImageView(addImage));

    // remove food from meal
    Button removeButton = createButton("", buttonDefaultWidth);
    Image removeImage = new Image(getClass().getClassLoader().getResourceAsStream("application/ArrowLeft.png"));
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
    mealGrid.setPrefSize(width * 0.425, height * 0.9);

    // Meal Title
    Text mealTitle = new Text("Meal");
    mealTitle.setFont(Font.font(font, FontWeight.BOLD, 20));

    // Meal List
    mealList.setItems(meal.getMeal());
    mealList.setPrefSize(width * 0.425, height * 0.75);
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

    PieChart pieChart = new PieChart();

    // Add info to Meal Grid
    mealGrid.add(mealTitle, 1, 1, 2, 1);
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
    lastColumnGrid.setPrefWidth(width * 0.1);

    // Help Button
    Button helpButton = createButton("?", 20);
    helpButton.setFont(Font.font(font, FontWeight.BOLD, 16));

    // Add to Grid/HBox
    lastColumnGrid.add(helpButton, 1, 1);
    GridPane.setHalignment(helpButton, HPos.LEFT);
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
        foodData.loadFoodItems(foodItemsFile.getAbsolutePath());
        foodItemList = FXCollections.observableArrayList(foodData.getAllFoodItems());
        foodItemList = foodItemList.sorted((a, b) -> a.getName().toUpperCase()
            .compareTo(b.getName().toUpperCase()));
        foodList.setItems(foodItemList);
        foodList.refresh();
        foodCount.setText("Food Count = " + foodItemList.size());

      }
    });

    // Save Button Action
    saveButton.setOnAction(actionEvent -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Food List");

      File saveFile = fileChooser.showSaveDialog(primaryStage);
      foodData.saveFoodItems(saveFile.getAbsolutePath());
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
      if (foodItem != null) {
        meal.addFoodItem(foodItem);
        mealList.refresh();
        updateNutrition(mealGrid, calories, fat, carbs, fiber, protein, pieChart);
      }
    });

    // Remove From Meal Button Action - needs to be after nutrition text created
    removeButton.setOnAction(actionEvent -> {
      meal.removeFoodItem(mealList.getSelectionModel().getSelectedIndex());
      mealList.refresh();
      updateNutrition(mealGrid, calories, fat, carbs, fiber, protein, pieChart);
    });

    // help button
    helpButton.setOnAction(actionEvent -> {

      WebView helpWebView = new WebView();
      WebEngine helpWebEngine = helpWebView.getEngine();
      String helpFile = this.getClass().getClassLoader().getResource("application/help.html").toString();
      
      helpWebEngine.load(helpFile);
      StackPane helpPane = new StackPane();
      helpPane.getChildren().add(helpWebView);
      Scene helpScene = new Scene(helpPane, 640, 800, Color.ANTIQUEWHITE);

      Stage helpStage = new Stage();
      helpStage.setTitle("Help");
      helpStage.setScene(helpScene);
      helpStage.show();

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
  
 
  /**
   * helper method for saving/closing filter window and updating list with filters
   * TODO
   * @return true if list is filtered
   */
  private boolean closeFilterWindow() {
    boolean isListFiltered = false; 
    
    if (nameFilter.equals("") && filterRules.isEmpty()) {
      foodItemList = FXCollections.observableArrayList(foodData.getAllFoodItems());
    }

    // name filter, no rule filter
    else if (!nameFilter.equals("") && filterRules.isEmpty()) {
      isListFiltered = true;
      foodItemList = FXCollections.observableArrayList(foodData.filterByName(nameFilter));
    }

    // rule filter, no name filter
    else if (nameFilter.equals("") && !filterRules.isEmpty()) {
      isListFiltered = true;
      foodItemList =
          FXCollections.observableArrayList(foodData.filterByNutrients(filterRules));
    }

    // both name and nutrient filters
    else {
      isListFiltered = true;
      foodItemList = FXCollections.observableArrayList(foodData.filterByName(nameFilter));
      foodItemList.retainAll(foodData.filterByNutrients(filterRules));
    }
   
    //sort and refresh food list
    foodItemList = foodItemList.sorted((a, b) -> a.getName().toUpperCase()
        .compareTo(b.getName().toUpperCase()));
    foodList.setItems(foodItemList);
    foodList.refresh();
    
    return isListFiltered;
  }

  /**
   * Updates the nutrition info Grid This occurs upon adding new food items and clearing the meal
   * 
   * @param mealGrid that we are updating
   * @param calories text in the grid
   * @param fat text in the grid
   * @param carbohydrate text in the grid
   * @param fiber text in the grid
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
    mealGrid.add(calories, 1, 4);
    mealGrid.add(fat, 1, 5);
    mealGrid.add(carbs, 1, 6);
    mealGrid.add(fiber, 1, 7);
    mealGrid.add(protein, 1, 8);

    if (fatInMeal == 0 & carbsInMeal == 0 & fiberInMeal == 0 & proteinInMeal == 0) {
      return; // do not draw pie graph
    }

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        new PieChart.Data("Fat", fatInMeal), new PieChart.Data("Carbohydrate", carbsInMeal),
        new PieChart.Data("Fiber", fiberInMeal), new PieChart.Data("Protein", proteinInMeal));
    pieChart.setData(pieChartData);
    pieChart.setLabelsVisible(false);
    pieChart.setLegendSide(Side.LEFT);
    mealGrid.add(pieChart, 2, 5, 1, 4);
  }

  /*
   * main method that will be run by default and will launch the UI
   */
  public static void main(String[] args) {
    launch(args);
  }
}
