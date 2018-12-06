package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Main extends Application {

  // this is where most of our code will go! Or all of it!

  @Override
  public void start(Stage primaryStage) {

    GridPane ruleGrid = new GridPane();
    ruleGrid.setHgap(10);
    ruleGrid.setVgap(10);
    ruleGrid.setPadding(new Insets(10, 10, 10, 10));
    //ruleGrid.setGridLinesVisible(true);
    //ruleGrid.setAlignment(Pos.CENTER);
    
    //titles
    Text nameTitle = new Text("Name Filter");
    Text foodTitle = new Text("Nutrient Filters");
    nameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    foodTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    
    //labels
    Label l1 = new Label("Name Filter");
    Label l2 = new Label("Include foods with names that contain the following text:");
    Label l3 = new Label("Nutrient Filters");
    Label nutrientInfoLabel = new Label("Include foods by nutrient (ex. protein >= 20), nutrients are "
        + "fat, carb, protein..");
    Label l5 = new Label("Current Filters");
   
    //input textFields
    TextField input = new TextField();
    input.setMaxHeight(20); input.setMaxWidth(200);
    input.setPromptText("Enter text for name filter");
    input.setFocusTraversable(false);
    TextField inputNutrientRule = new TextField();
    inputNutrientRule.setMaxHeight(20); inputNutrientRule.setMaxWidth(200);
    inputNutrientRule.setPromptText("Enter nutrient rules here (ex. fat > 100.0");
    inputNutrientRule.setFocusTraversable(false);
    
    // Nutrient rule list
    ListView<String> ruleListView = new ListView<>();
    ObservableList<String> rules = FXCollections.observableArrayList();
    String rule1 = "protein >= 20.0";
    String rule2 = "carbs >= 40.0";
    rules.add(rule1);
    rules.add(rule2);
    ruleListView.setItems(rules);
    ruleListView.setPrefWidth(300);
    
    //Buttons
    Button cancelButton = createButton("Cancel");
    cancelButton.setAlignment(Pos.CENTER_RIGHT);
    cancelButton.setStyle("-fx-font: 22 arial; -fx-base: #36e7c9;");
    cancelButton.setMinWidth(180d);
    Button acceptButton = createButton("Accept Filters");
    acceptButton.setAlignment(Pos.CENTER_RIGHT);
    acceptButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
    acceptButton.setMinWidth(180d);
    Button addFilterButton = createButton("Add to Rules");
    addFilterButton.setStyle("-fx-font: 12 arial; -fx-base: #f6e7c9;");
    Button removeFilterButton = createButton("Remove Selected");
    removeFilterButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
    
    //put all nodes into grid
    ruleGrid.add(nameTitle, 0, 0, 1, 1);
    ruleGrid.add(l2, 0, 1, 2, 1);
    ruleGrid.add(input, 0, 2, 1, 1);
    ruleGrid.add(foodTitle, 0, 3, 1, 1);
    ruleGrid.add(nutrientInfoLabel, 0, 4, 2, 1);
    ruleGrid.add(l5, 0, 5, 1, 1);
    ruleGrid.add(ruleListView, 0, 6, 1, 1);
    ruleGrid.add(removeFilterButton, 1 , 6, 2, 1);
    ruleGrid.add(inputNutrientRule, 0, 7, 1, 1);
    ruleGrid.add(addFilterButton, 1, 7, 1, 1);
    ruleGrid.add(cancelButton, 0, 8, 1, 1);
    ruleGrid.add(acceptButton, 1 , 8, 1, 1);
            
    //prepare and show stage
    Color backgroundColor = Color.AZURE;
    Scene scene = new Scene(ruleGrid, 500, 400, backgroundColor);
    Stage ruleStage = new Stage();
    ruleStage.setTitle("Enter Filter(s)");
    ruleStage.setScene(scene);
    ruleStage.show();
    
    //clicking add filter button
    addFilterButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String rule5 = inputNutrientRule.getText();
        
        //validate format
        if (rule5.contains("pizza")) {
          nutrientInfoLabel.setTextFill(Color.FIREBRICK);
          return;
        }
        else nutrientInfoLabel.setTextFill(Color.BLACK);
       
        rules.add(rule5);
        ruleListView.refresh();
        System.out.println("Add Button pressed");
      }
  });
  
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

  

  public static void main(String[] args) {
    launch(args);
  }
}
